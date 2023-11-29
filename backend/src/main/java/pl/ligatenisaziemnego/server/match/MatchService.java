package pl.ligatenisaziemnego.server.match;

import org.springframework.stereotype.Service;
import pl.ligatenisaziemnego.server.controlleradvice.ApiError;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;
import pl.ligatenisaziemnego.server.security.SecurityService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final SecurityService securityService;

    public MatchService(MatchRepository matchRepository, MatchMapper matchMapper, SecurityService securityService) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
        this.securityService = securityService;
    }

    public Match get(Long id) throws ExceptionWithResponseEntity {
        return matchRepository.findById(id).orElseThrow(() -> ApiError.NOT_FOUND_ID(Match.class, id));
    }

    public Object update(Long id, MatchUpdateDto matchUpdateDto) throws ExceptionWithResponseEntity {
        var match = matchMapper.update(matchUpdateDto, get(id));
        validate(id, match);

        var applicationUser = securityService.getApplicationUserFromAuthentication();

        var tournamentGroup = match.getTournamentGroup();
        if (tournamentGroup != null) {
            var tournament = tournamentGroup.getTournament();
            if (!applicationUser.getId().equals(tournament.getOrganizer().getId())) {
                if (!applicationUser.getId().equals(match.getFirstPlayerId()) && !applicationUser.getId().equals(match.getSecondPlayerId()))
                    throw ApiError.FORBIDDEN("You can't change match that you don't participate in or organize");
            }
            if (tournament.getKnockoutBracket() != null) {
                throw ApiError.FORBIDDEN("You can't change match when knockout bracket is created");
            }
        }


        match.setLastModifiedById(applicationUser.getId());
        match.setUpdatedDateTime(Instant.now());
        match = matchRepository.save(match);
        return matchMapper.toDto(match);
    }

    private void validate(Long id, Match match) throws ExceptionWithResponseEntity {

    }

    public MatchDto getDto(Long id) throws ExceptionWithResponseEntity {
        return matchMapper.toDto(get(id));
    }
}
