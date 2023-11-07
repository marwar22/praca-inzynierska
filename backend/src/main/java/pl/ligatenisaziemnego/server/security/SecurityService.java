package pl.ligatenisaziemnego.server.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserRepository;
import pl.ligatenisaziemnego.server.controlleradvice.ApiError;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;

import java.util.List;
import java.util.Map;

@Service
public class SecurityService {
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityService(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApplicationUser register(RegisterDto registerDto) throws ExceptionWithResponseEntity {

        if (applicationUserRepository.existsByUsername(registerDto.getUsername()))
            throw ApiError.BAD_REQUEST(Map.of("username", "There is already a user with this username"));
        if (applicationUserRepository.existsByEmail(registerDto.getEmail()))
            throw ApiError.BAD_REQUEST(Map.of("email", "There is already a user with this email"));

        ApplicationUser applicationUser = new ApplicationUser(registerDto.getUsername(), registerDto.getEmail(),
                passwordEncoder.encode(registerDto.getPassword()), List.of());

        return applicationUserRepository.save(applicationUser);
    }
}
