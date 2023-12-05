package pl.ligatenisaziemnego.server.applicationuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsernameOrEmail(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new ApplicationUserPrincipal(applicationUser);
    }
}