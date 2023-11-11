package pl.ligatenisaziemnego.server.tournament;

import org.springframework.stereotype.Service;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserRepository;
import pl.ligatenisaziemnego.server.controlleradvice.ApiError;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;

import java.util.List;
import java.util.Map;

@Service
public class TournamentService {
    private final TournamentMapper tournamentMapper;
    private final TournamentRepository tournamentRepository;
    private final ApplicationUserRepository applicationUserRepository;

    public TournamentService(TournamentMapper tournamentMapper,
            TournamentRepository tournamentRepository,
            ApplicationUserRepository applicationUserRepository) {
        this.tournamentMapper = tournamentMapper;
        this.tournamentRepository = tournamentRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }

    public Tournament create(TournamentCreateDto tournamentCreateDto) throws ExceptionWithResponseEntity {
        var tournament = tournamentMapper.toEntity(tournamentCreateDto);
        tournament.setPlayers(applicationUserRepository.findAllById(tournamentCreateDto.getPlayerIds()));

        if (tournament.getPlayers().size() != tournamentCreateDto.getPlayerIds().size())
            throw ApiError.BAD_REQUEST(Map.of("playerIds",
                    "playerIds contains at least one entry that doesn't correspond to applicationUserId"));

        validate(-1L, tournament);
        return tournamentRepository.save(tournament);
    }

    private void validate(Long id, Tournament tournament) throws ExceptionWithResponseEntity {
        if (tournamentRepository.existsByNameIgnoreCaseAndIdNot(tournament.getName(), id))
            throw ApiError.ANOTHER_WITH(Tournament.class, "name", tournament.getName());
    }

}
