package pl.ligatenisaziemnego.server.applicationuser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class ApplicationUserPrincipal implements UserDetails {
    private final ApplicationUser applicationUser;

    public ApplicationUserPrincipal(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var grantedAuthorities = new HashSet<GrantedAuthority>();
        applicationUser.getRoles().forEach(
                applicationUserRole -> grantedAuthorities.addAll(applicationUserRole.getGrantedAuthorities()));
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return applicationUser.getPassword();
    }

    @Override
    public String getUsername() {
        return applicationUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }
}