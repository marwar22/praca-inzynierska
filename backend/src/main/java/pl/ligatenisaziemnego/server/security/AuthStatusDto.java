package pl.ligatenisaziemnego.server.security;

import lombok.*;
import org.springframework.security.core.Authentication;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserPermission;
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
    private List<ApplicationUserPermission> permissions;

    public AuthStatusDto(Authentication authentication) {
        var principal = authentication.getPrincipal();
        if (principal instanceof ApplicationUserPrincipal applicationUserPrincipal) {
            isLoggedIn = true;
            username = applicationUserPrincipal.getUsername();
            applicationUserId = applicationUserPrincipal.getApplicationUser().getId();
            roles = applicationUserPrincipal.getApplicationUser().getRoles();
            permissions = applicationUserPrincipal.getApplicationUser().getPermissions().stream().toList();
        } else {
            isLoggedIn = false;
            username = "";
            applicationUserId = -1;
            roles = List.of();
            permissions = List.of();
        }
    }
}