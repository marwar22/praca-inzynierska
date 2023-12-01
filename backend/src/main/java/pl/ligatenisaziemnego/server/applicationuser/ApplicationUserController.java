package pl.ligatenisaziemnego.server.applicationuser;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;
    private final ApplicationUserMapper applicationUserMapper;

    public ApplicationUserController(ApplicationUserService applicationUserService, ApplicationUserMapper applicationUserMapper) {
        this.applicationUserService = applicationUserService;
        this.applicationUserMapper = applicationUserMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getApplicationUser() throws ExceptionWithResponseEntity {
        return ResponseEntity.ok(applicationUserService.getMyApplicationUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicationUser(@PathVariable Long id) throws ExceptionWithResponseEntity {
        var applicationUser = applicationUserService.getById(id);
        var applicationUserDto = applicationUserMapper.toBasicDto(applicationUser);
        return ResponseEntity.ok(applicationUserDto);
    }

    @GetMapping()
    public ResponseEntity<?> getAllApplicationUsers(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "limit", defaultValue = "24") String limitString,
            @RequestParam(name = "exclude", defaultValue = "") Long[] exclude) {

        int limit = Integer.parseInt(limitString);
        var applicationUsers = applicationUserService.getAllByName(name, limit, List.of(exclude));
        return ResponseEntity.ok(applicationUsers);
    }
}
