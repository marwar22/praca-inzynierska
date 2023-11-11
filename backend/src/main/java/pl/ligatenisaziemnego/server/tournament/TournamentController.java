package pl.ligatenisaziemnego.server.tournament;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return new ResponseEntity<>(tournaments, HttpStatus.CREATED);

    }

    @PostMapping
    public ResponseEntity<?> postTournament(
            @RequestBody @Valid TournamentCreateDto tournamentCreateDto) throws ExceptionWithResponseEntity {
        var tournament = tournamentService.create(tournamentCreateDto);
        return new ResponseEntity<>(tournament, HttpStatus.CREATED);

    }

}
