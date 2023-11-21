package pl.ligatenisaziemnego.server.tournament;

import org.springframework.stereotype.Service;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserRepository;
import pl.ligatenisaziemnego.server.controlleradvice.ApiError;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;
import pl.ligatenisaziemnego.server.match.Match;
import pl.ligatenisaziemnego.server.security.SecurityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TournamentService {
    private final TournamentMapper tournamentMapper;
    private final TournamentRepository tournamentRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final SecurityService securityService;

    public TournamentService(TournamentMapper tournamentMapper,
            TournamentRepository tournamentRepository,
            ApplicationUserRepository applicationUserRepository, SecurityService securityService) {
        this.tournamentMapper = tournamentMapper;
        this.tournamentRepository = tournamentRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.securityService = securityService;
    }


    public Object get(Long id) throws ExceptionWithResponseEntity {
        return tournamentRepository.findById(id).orElseThrow(() -> ApiError.NOT_FOUND_ID(Tournament.class, id));
    }

    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }

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
        if (tournament.getPlayers().size() != tournamentCreateDto.getPlayerIds().size())
            throw ApiError.BAD_REQUEST(Map.of("playerIds",
                    "playerIds contains at least one entry that doesn't correspond to applicationUserId"));

        tournament.setOrganizerId(securityService.getApplicationUserFromAuthentication().getId());

        validate(-1L, tournament);
        return tournamentRepository.save(tournament);
    }

    private void validate(Long id, Tournament tournament) throws ExceptionWithResponseEntity {
        if (tournamentRepository.existsByNameIgnoreCaseAndIdNot(tournament.getName(), id))
            throw ApiError.ANOTHER_WITH(Tournament.class, "name", tournament.getName());
    }

}
