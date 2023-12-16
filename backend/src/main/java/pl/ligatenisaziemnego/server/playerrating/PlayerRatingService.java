package pl.ligatenisaziemnego.server.playerrating;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserRepository;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserService;
import pl.ligatenisaziemnego.server.match.Match;
import pl.ligatenisaziemnego.server.tournament.TournamentRepository;
import pl.ligatenisaziemnego.server.tournament.TournamentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiConsumer;

@Service
public class PlayerRatingService {
    private final TournamentService tournamentService;
    private final TournamentRepository tournamentRepository;
    private final ApplicationUserService applicationUserService;
    private final ApplicationUserRepository applicationUserRepository;

    public PlayerRatingService(TournamentService tournamentService,
            TournamentRepository tournamentRepository, ApplicationUserService applicationUserService,
            ApplicationUserRepository applicationUserRepository) {
        this.tournamentService = tournamentService;
        this.tournamentRepository = tournamentRepository;
        this.applicationUserService = applicationUserService;
        this.applicationUserRepository = applicationUserRepository;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 10) // 10 min
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updatePlayersRatings() {
        var tournaments = tournamentService.getAllByEndDate();
        tournaments = tournaments.stream().filter(tournament -> {
            var knockoutBracket = tournament.getKnockoutBracket();
            if (knockoutBracket == null) return false;
            var finalMatch = knockoutBracket.getMatches().getLast().getMatch();
            return finalMatch.getResult() != null;
        }).toList();
        var prvRating = new HashMap<Long, Long>();
        var rating = new HashMap<Long, Long>();
        final MutableBoolean islast = new MutableBoolean(false);
        BiConsumer<Long, Long> updateRating = (Long playerId, Long ratingToAdd) -> {
            rating.merge(playerId, ratingToAdd, (oldValue, _v) -> oldValue + ratingToAdd);
            if (!islast.booleanValue()) prvRating.merge(playerId, ratingToAdd, (oldValue, _v) -> oldValue + ratingToAdd);
        };
        for (var tournament : tournaments) {
            if (tournament == tournaments.getLast()) islast.setTrue();

            tournament = tournamentRepository.findById(tournament.getId()).orElseThrow();
            var scoring = tournament.getScoring();
            var matchesInTournament = new ArrayList<Match>();

            // Participation
            for (var player : tournament.getPlayers()) {
                updateRating.accept(player.getId(), scoring.getRatingForTournamentParticipation().longValue());
            }

            // Group stage
            for (var group : tournament.getGroups()) {
                matchesInTournament.addAll(group.getMatches());
            }

            // Knockout stage
            var lastStageIndex = Integer.numberOfTrailingZeros(tournament.getNumberOfPlayersInKnockoutBracket()) - 1;
            if (tournament.getKnockoutBracket() != null) {
                for (var kbMatch : tournament.getKnockoutBracket().getMatches()) {
                    var match = kbMatch.getMatch();
                    var stage = kbMatch.getStage().intValue();

                    var ratingToAdd = scoring.getRatingForKnockoutStageParticipation().get(stage).longValue();

                    matchesInTournament.add(match);

                    if (match.getFirstPlayerId() != null) updateRating.accept(match.getFirstPlayerId(), ratingToAdd);
                    if (match.getSecondPlayerId() != null) updateRating.accept(match.getSecondPlayerId(), ratingToAdd);


                    if (stage == lastStageIndex && match.getResult() != null)
                        updateRating.accept(match.getResult().getWinnerId(), scoring.getRatingForTournamentWin().longValue());


                }
            }
            // (Group stage + Knockout stage) matches
            for (Match match : matchesInTournament) {
                var result = match.getResult();
                if (result == null) continue;

                var loserId = Objects.equals(result.getWinnerId(),
                        match.getFirstPlayerId()) ? match.getSecondPlayerId() : match.getFirstPlayerId();

                updateRating.accept(result.getWinnerId(), scoring.getRatingForMatchWin().longValue());

                if (result.getWalkover()) {
                    updateRating.accept(loserId, scoring.getRatingForMatchWalkover().longValue());
                } else {
                    updateRating.accept(loserId, scoring.getRatingForMatchLoss().longValue());
                }
            }
        }
        var applicationUsers = applicationUserRepository.findAll();
        for (var applicationUser : applicationUsers) {
            var newRating = rating.get(applicationUser.getId());
            applicationUser.setRating(newRating == null ? 0 : newRating);

            var newPrvRating = prvRating.get(applicationUser.getId());
            applicationUser.setPrvRating(newPrvRating == null ? 0 : newPrvRating);
            applicationUserRepository.save(applicationUser);

        }
        applicationUsers.sort((ApplicationUser au1, ApplicationUser au2) -> au2.getRating().compareTo(au1.getRating()));
        applicationUsers.sort((ApplicationUser au1, ApplicationUser au2) -> au2.getPrvRating().compareTo(au1.getPrvRating()));
        int lastTheSame = -1;
        for (int i = 0; i < applicationUsers.size(); i++) {
            if (i > 0 && applicationUsers.get(i - 1).getPrvRating().equals(applicationUsers.get(i).getPrvRating())) {
                applicationUsers.get(i).setPrvRanking(lastTheSame);
            } else {
                lastTheSame = i;
                applicationUsers.get(i).setPrvRanking(i);
            }
        }
        lastTheSame = -1;
        applicationUsers.sort((ApplicationUser au1, ApplicationUser au2) -> au2.getRating().compareTo(au1.getRating()));
        for (int i = 0; i < applicationUsers.size(); i++) {
            if (i > 0 && applicationUsers.get(i - 1).getRating().equals(applicationUsers.get(i).getRating())) {
                applicationUsers.get(i).setRanking(lastTheSame);
            } else {
                lastTheSame = i;
                applicationUsers.get(i).setRanking(i);
            }

        }
    }
}
