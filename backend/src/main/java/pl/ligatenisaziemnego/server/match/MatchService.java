package pl.ligatenisaziemnego.server.match;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;
import pl.ligatenisaziemnego.server.controlleradvice.ApiError;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;
import pl.ligatenisaziemnego.server.email.EmailService;
import pl.ligatenisaziemnego.server.playerrating.PlayerRatingService;
import pl.ligatenisaziemnego.server.security.SecurityService;
import pl.ligatenisaziemnego.server.tournament.TournamentRepository;
import pl.ligatenisaziemnego.server.tournament.TournamentService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final SecurityService securityService;
    private final TournamentService tournamentService;
    private final PlayerRatingService playerRatingService;
    private final EmailService emailService;

    public MatchService(MatchRepository matchRepository, MatchMapper matchMapper, SecurityService securityService,
            TournamentService tournamentService, PlayerRatingService playerRatingService, EmailService emailService) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
        this.securityService = securityService;
        this.tournamentService = tournamentService;
        this.playerRatingService = playerRatingService;
        this.emailService = emailService;
    }

    public Match get(Long id) throws ExceptionWithResponseEntity {
        return matchRepository.findById(id).orElseThrow(() -> ApiError.NOT_FOUND_ID(Match.class, id));
    }

    @Value("${rozgrywkitenisa.origin}")
    private String origin;

    public MatchDto update(Long id, MatchUpdateDto matchUpdateDto) throws ExceptionWithResponseEntity {
        var match = matchMapper.update(matchUpdateDto, get(id));
        validate(id, match);

        var applicationUser = securityService.getApplicationUserFromAuthentication();
        var tournament = tournamentService.get(match.getTournamentId());
        if (!applicationUser.getId().equals(tournament.getOrganizer().getId())) {
            if (!applicationUser.getId().equals(match.getFirstPlayerId()) && !applicationUser.getId().equals(match.getSecondPlayerId()))
                throw ApiError.FORBIDDEN("You can't change match that you don't participate in or organize");
        }
        var mailReceivers = new ArrayList<ApplicationUser>();
        if (tournament.getKnockoutBracket() != null) {
            var matchInKnockoutBracket = tournament.getKnockoutBracket().getMatches().stream()
                                                   .filter(mInKB -> mInKB.getMatch().getId().equals(id)).findAny().orElse(null);
            if (matchInKnockoutBracket == null)
                throw ApiError.BAD_REQUEST(Map.of("knockoutBracket", "You can't change group match when knockout bracket is created"));

            var nextMatchInKnockoutBracket = matchInKnockoutBracket.getNextMatchInKnockoutBracket();

            if (nextMatchInKnockoutBracket != null && nextMatchInKnockoutBracket.getMatch().getResult() != null)
                throw ApiError.BAD_REQUEST(
                        Map.of("nextMatchInKnockoutBracket", "You can't change match when next match in knockout bracket has result"));

            if (match.getResult() != null && nextMatchInKnockoutBracket != null) {
                var nextMatch = nextMatchInKnockoutBracket.getMatch();
                var bracketPosition = matchInKnockoutBracket.getBracketPosition();
                bracketPosition /= 1L << matchInKnockoutBracket.getStage();

                if (bracketPosition % 2 == 0) nextMatch.setFirstPlayerId(match.getResult().getWinnerId());
                else nextMatch.setSecondPlayerId(match.getResult().getWinnerId());

                while (nextMatchInKnockoutBracket != null) {
                    mailReceivers.add(nextMatchInKnockoutBracket.getMatch().getFirstPlayer());
                    mailReceivers.add(nextMatchInKnockoutBracket.getMatch().getSecondPlayer());
                    nextMatchInKnockoutBracket = nextMatchInKnockoutBracket.getNextMatchInKnockoutBracket();
                }
            }
        } else {
            var group = tournament.getGroups().stream()
                                  .filter(g -> g.getMatches().stream().anyMatch(m -> m.getId().longValue() == id))
                                  .findFirst().orElse(null);
            if (group != null) mailReceivers.addAll(group.getPlayers());
        }


        match.setLastModifiedById(applicationUser.getId());
        match.setUpdatedDateTime(Instant.now());
        match = matchRepository.save(match);
        playerRatingService.updatePlayersRatings();

        var emails = mailReceivers.stream().filter(Objects::nonNull).map(ApplicationUser::getEmail).toArray(String[]::new);
        String vsString = "%s %s vs %s %s".formatted(
                match.getFirstPlayer().getFirstName(), match.getFirstPlayer().getLastName(),
                match.getSecondPlayer().getFirstName(), match.getSecondPlayer().getLastName());
        var matchResultString = new StringBuilder();
        matchResultString.append("%s:%s\n".formatted(match.getResult().getFirstPlayerScore(), match.getResult().getSecondPlayerScore()));
        matchResultString.append(
                match.getResult().getSetResults().stream().map(sr ->
                        "(%s:%s)".formatted(sr.getFirstPlayerScore(), sr.getSecondPlayerScore())
                ).collect(Collectors.joining(", "))
        );

        emailService.sendSimpleMessage(emails, "Wynik meczu " + vsString,
                "Wpisano wynik meczu " + vsString + "\nWynik: " + matchResultString + "\nZobacz na stronie: " + origin + "/rozgrywki/" + tournament.getId());

        return matchMapper.toDto(match);
    }

    private void validate(Long id, Match match) throws ExceptionWithResponseEntity {

    }

    public MatchDto getDto(Long id) throws ExceptionWithResponseEntity {
        return matchMapper.toDto(get(id));
    }
}
