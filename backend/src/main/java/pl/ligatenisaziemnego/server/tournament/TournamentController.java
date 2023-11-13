package pl.ligatenisaziemnego.server.tournament;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;

@RestController
@RequestMapping("/api/v1/tournament")
public class TournamentController {
    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    // TODO paging
    @GetMapping
    public ResponseEntity<?> getAllTournaments() {
        var tournaments = tournamentService.getAll();
        return new ResponseEntity<>(tournaments, HttpStatus.OK);

    }

    @GetMapping("{id}")
    public ResponseEntity<?> getTournament(@PathVariable Long id) throws ExceptionWithResponseEntity {
        var tournament = tournamentService.getById(id);
        return new ResponseEntity<>(tournament, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('TOURNAMENT:CREATEE')")
    @PostMapping
    public ResponseEntity<?> postTournament(
            @RequestBody @Valid TournamentCreateDto tournamentCreateDto) throws ExceptionWithResponseEntity {
        var tournament = tournamentService.create(tournamentCreateDto);
        return new ResponseEntity<>(tournament, HttpStatus.CREATED);

    }

}
