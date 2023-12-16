package pl.ligatenisaziemnego.server.tournament;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserPermission;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserRepository;
import pl.ligatenisaziemnego.server.controlleradvice.ApiError;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;
import pl.ligatenisaziemnego.server.knockoutbracket.KnockoutBracket;
import pl.ligatenisaziemnego.server.knockoutbracket.MatchInKnockoutBracket;
import pl.ligatenisaziemnego.server.match.Match;
import pl.ligatenisaziemnego.server.security.SecurityService;
import pl.ligatenisaziemnego.server.tournament.group.TournamentGroup;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentService {
    private final TournamentMapper tournamentMapper;
    private final TournamentRepository tournamentRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final SecurityService securityService;

    public TournamentService(TournamentMapper tournamentMapper, TournamentRepository tournamentRepository,
            ApplicationUserRepository applicationUserRepository, SecurityService securityService) {
        this.tournamentMapper = tournamentMapper;
        this.tournamentRepository = tournamentRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.securityService = securityService;
    }


    public Tournament get(Long id) throws ExceptionWithResponseEntity {
        return tournamentRepository.findById(id).orElseThrow(() -> ApiError.NOT_FOUND_ID(Tournament.class, id));
    }

    public List<Tournament> getAllByEndDate() {
        return tournamentRepository.findByOrderByEndDateAsc();
    }

    public List<Tournament> getAllByName(String name) {
        return tournamentRepository.findAllByNameContainsIgnoreCase(name);
    }

    @Transactional(rollbackFor = Exception.class)
    public Tournament create(TournamentCreateDto tournamentCreateDto) throws ExceptionWithResponseEntity {
        var tournament = tournamentMapper.toEntity(tournamentCreateDto);
        tournament.setPlayers(applicationUserRepository.findAllById(tournamentCreateDto.getPlayerIds()));
        tournament.getGroups().forEach(tournamentGroup -> {
            var matches = new ArrayList<Match>();
            var playerIds = tournamentGroup.getPlayerIds();
            for (int i = 0; i < playerIds.size(); i++) {
                for (int j = i + 1; j < playerIds.size(); j++) {
                    var match = new Match();
                    match.setFirstPlayerId(playerIds.get(i));
                    match.setSecondPlayerId(playerIds.get(j));
                    matches.add(match);
                }
            }
            tournamentGroup.setMatches(matches);
        });
        if (tournament.getPlayers().size() != tournamentCreateDto.getPlayerIds().size()) throw ApiError.BAD_REQUEST(
                Map.of("playerIds", "playerIds contains at least one entry that doesn't correspond to applicationUserId"));

        tournament.setOrganizerId(securityService.getApplicationUserFromAuthentication().getId());

        validate(-1L, tournament);
        var tournamentGroups = tournament.getGroups();
        tournament.setGroups(null);
        tournamentRepository.save(tournament);
        var tournamentId = tournament.getId();
        tournamentGroups.forEach(tournamentGroup -> tournamentGroup.getMatches().forEach(match -> match.setTournamentId(tournamentId)));
        tournament.setGroups(new ArrayList<>(tournamentGroups));
        tournamentRepository.save(tournament);

        return tournament;
    }

    private void validate(Long id, Tournament tournament) throws ExceptionWithResponseEntity {
        if (tournamentRepository.existsByNameIgnoreCaseAndIdNot(tournament.getName(), id))
            throw ApiError.ANOTHER_WITH(Tournament.class, "name", tournament.getName());
    }

    public List<ApplicationUser> getContact(Long tournamentId, Long groupId) throws ExceptionWithResponseEntity {
        var tournament = get(tournamentId);
        var group = tournament.getGroups().stream().filter(g -> g.getId().equals(groupId)).findFirst()
                              .orElseThrow(() -> ApiError.NOT_FOUND_ID(TournamentGroup.class, groupId));
        var applicationUser = securityService.getApplicationUserFromAuthentication();
        if (group.getPlayerIds().stream().noneMatch(pId -> pId.equals(applicationUser.getId())))
            throw ApiError.FORBIDDEN("You are not a member of this group");
        return group.getPlayers();
    }

    public void createKnockoutBracket(Long id) throws ExceptionWithResponseEntity {
        @AllArgsConstructor
        final class PlayerResult implements Comparable<PlayerResult> {
            private Long id;
            private Long matchesWon;
            private Long matchesLost;
            private Long setsWon;
            private Long setsLost;
            private Long gamesWon;
            private Long gamesLost;

            @Override
            public int compareTo(@Nonnull PlayerResult other) {
                return Comparator.<PlayerResult>comparingLong(pr -> (pr.matchesLost - pr.matchesWon))
                                 .thenComparing(pr -> pr.setsLost - pr.setsWon).thenComparing(pr -> pr.gamesLost - pr.gamesWon)
                                 .compare(this, other);
            }
        }

        @AllArgsConstructor
        final class GroupResult {
            private List<PlayerResult> playersResults;
        }


        var tournament = get(id);

        var applicationUser = securityService.getApplicationUserFromAuthentication();
        if (!tournament.getOrganizerId().equals(applicationUser.getId()) &&
            !applicationUser.getPermissions().contains(ApplicationUserPermission.TOURNAMENT__UPDATE_ANY))
            throw ApiError.FORBIDDEN("You have to be organizer or have permission to edit any tournament to create knockout bracket");


        if (tournament.getKnockoutBracket() != null) {
            throw ApiError.BAD_REQUEST(Map.of("knockoutBracket", "numberOfPlayers knockoutBracket has already been created"));
        }


        var groupsResults = new ArrayList<GroupResult>();
        for (var group : tournament.getGroups()) {
            var playersResults = group.getPlayerIds().stream()
                                      .collect(Collectors.toMap(pId -> pId, pId -> new PlayerResult(pId, 0L, 0L, 0L, 0L, 0L, 0L)));
            for (var match : group.getMatches()) {
                if (match.getResult() == null) {
                    throw ApiError.BAD_REQUEST(
                            Map.of("groupMatches", "Match in groupMatches with id " + match.getId() + " doesn't have a result"));
                }


                var firstPlayerResults = playersResults.get(match.getFirstPlayerId());
                var secondPlayerResults = playersResults.get(match.getSecondPlayerId());

                firstPlayerResults.setsWon += match.getResult().getFirstPlayerScore();
                firstPlayerResults.setsLost += match.getResult().getSecondPlayerScore();
                secondPlayerResults.setsWon += match.getResult().getSecondPlayerScore();
                secondPlayerResults.setsLost += match.getResult().getFirstPlayerScore();

                var winForFirst = match.getResult().getFirstPlayerScore() > match.getResult().getSecondPlayerScore() ? 1 : 0;
                firstPlayerResults.matchesWon += winForFirst;
                firstPlayerResults.matchesLost += 1 - winForFirst;
                secondPlayerResults.matchesWon += 1 - winForFirst;
                secondPlayerResults.matchesLost += winForFirst;
                for (var setResults : match.getResult().getSetResults()) {
                    firstPlayerResults.gamesWon += setResults.getFirstPlayerScore();
                    firstPlayerResults.gamesLost += setResults.getSecondPlayerScore();

                    secondPlayerResults.gamesWon += setResults.getSecondPlayerScore();
                    secondPlayerResults.gamesLost += setResults.getFirstPlayerScore();
                }
            }
            groupsResults.add(new GroupResult(new ArrayList<>(playersResults.values())));
            Collections.sort(groupsResults.getLast().playersResults);
        }
        int numberOfPlayersLeft = tournament.getNumberOfPlayersInKnockoutBracket();

        var playerSeeds = new ArrayList<PlayerResult>();

        for (int place = 0; numberOfPlayersLeft > 0; place++) {
            int finalPlace = place;
            // TODO decide how to handle equal results between players
            var l = groupsResults.stream().map(gr -> gr.playersResults.size() > finalPlace ? gr.playersResults.get(finalPlace) : null)
                                 .filter(Objects::nonNull).sorted().limit(Math.min(numberOfPlayersLeft, groupsResults.size())).toList();
            numberOfPlayersLeft -= l.size();
            playerSeeds.addAll(l);
        }
        var seedPositions = Arrays.asList(0, 1);
        while (seedPositions.size() < tournament.getNumberOfPlayersInKnockoutBracket()) seedPositions = doubleSeedPositions(seedPositions);
        var result = new ArrayList<PlayerResult>(tournament.getNumberOfPlayersInKnockoutBracket().intValue());
        for (var sp : seedPositions) {
            result.add(playerSeeds.get(sp));
        }
        var knockoutBracket = new KnockoutBracket();
        knockoutBracket.setMatches(new ArrayList<>());
        knockoutBracket.setNumberOfPlayers(tournament.getNumberOfPlayersInKnockoutBracket().longValue());
        for (int i = 0; i < result.size(); i += 2) {
            var match = new Match();
            match.setTournamentId(tournament.getId());
            match.setFirstPlayerId(result.get(i).id);
            match.setSecondPlayerId(result.get(i + 1).id);

            var knockoutBracketMatch = new MatchInKnockoutBracket();
            knockoutBracketMatch.setMatch(match);
            knockoutBracketMatch.setStage(0L);
            knockoutBracket.getMatches().add(knockoutBracketMatch);
        }
        int position = 0;
        long stage = 1;
        for (int prvStageSize = knockoutBracket.getMatches().size(); prvStageSize > 1; prvStageSize /= 2, stage++) {
            for (int i = 0; i < prvStageSize; i += 2) {
                var match = new Match();
                match.setTournamentId(tournament.getId());

                var knockoutBracketMatch = new MatchInKnockoutBracket();
                knockoutBracketMatch.setMatch(match);
                knockoutBracketMatch.setStage(stage);

                knockoutBracket.getMatches().get(position++).setNextMatchInKnockoutBracket(knockoutBracketMatch);
                knockoutBracket.getMatches().get(position++).setNextMatchInKnockoutBracket(knockoutBracketMatch);
                knockoutBracket.getMatches().add(knockoutBracketMatch);
            }
        }
        tournament.setKnockoutBracket(knockoutBracket);
        tournamentRepository.save(tournament);
    }

    private List<Integer> doubleSeedPositions(List<Integer> seedPositions) {
        var length = seedPositions.size();
        var result = new ArrayList<Integer>(length * 2);
        for (var sp : seedPositions) {
            result.add(sp);
            result.add(2 * length - 1 - sp);
        }
        return result;
    }

    public void deleteKnockoutBracket(Long id) throws ExceptionWithResponseEntity {
        var tournament = get(id);

        var applicationUser = securityService.getApplicationUserFromAuthentication();
        if (!tournament.getOrganizerId().equals(applicationUser.getId()) &&
            !applicationUser.getPermissions().contains(ApplicationUserPermission.TOURNAMENT__UPDATE_ANY))
            throw ApiError.FORBIDDEN("You have to be organizer or have permission to edit any tournament to delete knockout bracket");

        tournament.setKnockoutBracket(null);
        tournamentRepository.save(tournament);
    }
}
