package pl.ligatenisaziemnego.server.security.passwordreset;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;

import java.time.Instant;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    boolean existsByApplicationUser(ApplicationUser applicationUser);

    Optional<PasswordResetToken> findByToken(String token);

    long deleteByExpirationDateLessThanEqual(Instant expirationDate);
}


