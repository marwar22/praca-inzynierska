package pl.ligatenisaziemnego.server.applicationuser;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    public ApplicationUserController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllApplicationUsers(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "limit", defaultValue = "24") String limitString) {

        int limit = Integer.parseInt(limitString);

        var applicationUsers = applicationUserService.findAllByName(name, limit);
        return ResponseEntity.ok(applicationUsers);
    }
}
