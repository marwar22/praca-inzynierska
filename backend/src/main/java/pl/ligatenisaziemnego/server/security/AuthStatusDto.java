package pl.ligatenisaziemnego.server.security;

import lombok.*;
import org.springframework.security.core.Authentication;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserPrincipal;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserRole;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthStatusDto {
    private long applicationUserId;
    private String username;
    private boolean isLoggedIn;
    private List<ApplicationUserRole> roles;

    public AuthStatusDto(Authentication authentication) {
        var principal = authentication.getPrincipal();
        if (principal instanceof ApplicationUserPrincipal applicationUserPrincipal) {
            this.setLoggedIn(true);
            this.setUsername(applicationUserPrincipal.getUsername());
            this.setApplicationUserId(applicationUserPrincipal.getApplicationUser().getId());
            this.setRoles(applicationUserPrincipal.getApplicationUser().getRoles());
        } else {
            this.setLoggedIn(false);
            this.setApplicationUserId(-1);
            this.setRoles(List.of());
        }
    }
}