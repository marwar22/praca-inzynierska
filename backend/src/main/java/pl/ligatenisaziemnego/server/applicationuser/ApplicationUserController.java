package pl.ligatenisaziemnego.server.applicationuser;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;

@RestController
@RequestMapping("/api/v1/user")
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    public ApplicationUserController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getApplicationUser() throws ExceptionWithResponseEntity {
        return ResponseEntity.ok(applicationUserService.getMyApplicationUser());
    }

    // TODO only return only necessary info
    @GetMapping()
    public ResponseEntity<?> getAllApplicationUsers(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "limit", defaultValue = "24") String limitString) {

        int limit = Integer.parseInt(limitString);

        var applicationUsers = applicationUserService.getAllByName(name, limit);
        return ResponseEntity.ok(applicationUsers);
    }
}
