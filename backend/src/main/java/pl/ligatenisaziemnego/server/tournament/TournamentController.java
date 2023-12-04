package pl.ligatenisaziemnego.server.tournament;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserMapper;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;
import pl.ligatenisaziemnego.server.knockoutbracket.KnockoutBracketCreateDto;


@RestController
@RequestMapping("/api/v1/tournament")
public class TournamentController {
    private final TournamentService tournamentService;
    private final ApplicationUserMapper applicationUserMapper;
    private final TournamentMapper tournamentMapper;

    public TournamentController(TournamentService tournamentService, ApplicationUserMapper applicationUserMapper,
            TournamentMapper tournamentMapper) {
        this.tournamentService = tournamentService;
        this.applicationUserMapper = applicationUserMapper;
        this.tournamentMapper = tournamentMapper;
    }

    // TODO paging
    @GetMapping
    public ResponseEntity<?> getAllTournaments(@RequestParam(name = "name", defaultValue = "") String name) {
        var tournaments = tournamentService.getAllByName(name);
        var tournamentsDto = tournaments.stream().map(tournamentMapper::toBasicDto).toList();
        return new ResponseEntity<>(tournamentsDto, HttpStatus.OK);

    }

    @GetMapping("{tournamentId}/group/{groupId}/contact")
    public ResponseEntity<?> getTournament(@PathVariable Long tournamentId, @PathVariable Long groupId) throws ExceptionWithResponseEntity {
        var applicationUsers = tournamentService.getContact(tournamentId, groupId).stream()
                                                .map(applicationUserMapper::toContactDto).toList();
        return new ResponseEntity<>(applicationUsers, HttpStatus.OK);

    }

    @GetMapping("{id}")
    public ResponseEntity<?> getTournament(@PathVariable Long id) throws ExceptionWithResponseEntity {
        var tournament = tournamentService.get(id);
        return new ResponseEntity<>(tournament, HttpStatus.OK);
    }

    @PostMapping("{id}/knockout-bracket")
    public ResponseEntity<?> createKnockoutBracket(@PathVariable Long id,
            @RequestBody @Valid KnockoutBracketCreateDto knockoutBracketCreateDto) throws ExceptionWithResponseEntity {
        tournamentService.createKnockoutBracket(id, knockoutBracketCreateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @DeleteMapping("{id}/knockout-bracket")
    public ResponseEntity<?> deleteKnockoutBracket(@PathVariable Long id) throws ExceptionWithResponseEntity {
        tournamentService.deleteKnockoutBracket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PreAuthorize("hasAuthority('TOURNAMENT:CREATE')")
    @PostMapping
    public ResponseEntity<?> postTournament(
            @RequestBody @Valid TournamentCreateDto tournamentCreateDto) throws ExceptionWithResponseEntity {
        var tournament = tournamentService.create(tournamentCreateDto);
        return new ResponseEntity<>(tournament, HttpStatus.CREATED);
    }

}
