package pl.ligatenisaziemnego.server.applicationuser;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static pl.ligatenisaziemnego.server.applicationuser.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    ADMIN(Set.of(TOURNAMENT__CREATE)),
    SUPER_ADMIN(Set.of(TOURNAMENT__CREATE, TOURNAMENT__UPDATE_ANY, ROLE__CHANGE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions =
                getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
