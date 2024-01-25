package pl.ligatenisaziemnego.server.security;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserRole;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserService;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;
import pl.ligatenisaziemnego.server.security.passwordreset.ChangePasswordWithTokenDto;
import pl.ligatenisaziemnego.server.security.passwordreset.PasswordResetDto;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class SecurityController {
    private final SecurityService securityService;
    private final ApplicationUserService applicationUserService;

    public SecurityController(SecurityService securityService, ApplicationUserService applicationUserService) {
        this.securityService = securityService;
        this.applicationUserService = applicationUserService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto registerDto) throws ExceptionWithResponseEntity {
        var applicationUser = securityService.register(registerDto);
        return new ResponseEntity<>(applicationUser, HttpStatus.CREATED);
    }

    @GetMapping("/auth/status")
    public ResponseEntity<?> checkStatus() {
        var statusDTO = new AuthStatusDto(SecurityContextHolder.getContext().getAuthentication());
        return new ResponseEntity<>(statusDTO, HttpStatus.OK);
    }

    @PostMapping("/auth/reset-password")
    ResponseEntity<?> resetPassword(@RequestBody @Valid PasswordResetDto passwordResetDto) {
        securityService.resetPassword(passwordResetDto);
        // Always return OK because we don't want attacker to know if there is user with given email.
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/auth/reset-password/{token}")
    ResponseEntity<?> changePasswordWithToken(@PathVariable String token,
            @RequestBody @Valid ChangePasswordWithTokenDto changePasswordWithTokenDto) throws ExceptionWithResponseEntity {
        securityService.changePasswordWithToken(token, changePasswordWithTokenDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user/{id}/role/change")
    public ResponseEntity<?> getApplicationUser(@PathVariable long id, @RequestBody List<ApplicationUserRole> roles)
            throws ExceptionWithResponseEntity {
        var applicationUser = applicationUserService.setRoles(id, roles);
        return ResponseEntity.ok(applicationUser.getRoles());
    }
}