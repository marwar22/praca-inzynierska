package pl.ligatenisaziemnego.server.match;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;

@RestController
@RequestMapping("/api/v1/match")
public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getMatch(@PathVariable Long id) throws ExceptionWithResponseEntity {
        var matchDto = matchService.getDto(id);
        return new ResponseEntity<>(matchDto, HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> updateMatch(@PathVariable Long id,
            @RequestBody @Valid MatchUpdateDto matchUpdateDto) throws ExceptionWithResponseEntity {
        var matchDto = matchService.update(id, matchUpdateDto);
        return new ResponseEntity<>(matchDto, HttpStatus.OK);
    }
}
