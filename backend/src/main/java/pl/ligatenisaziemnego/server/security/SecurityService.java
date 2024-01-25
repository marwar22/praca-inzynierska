package pl.ligatenisaziemnego.server.security;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ligatenisaziemnego.server.applicationuser.*;
import pl.ligatenisaziemnego.server.controlleradvice.ApiError;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;
import pl.ligatenisaziemnego.server.email.EmailService;
import pl.ligatenisaziemnego.server.security.passwordreset.ChangePasswordWithTokenDto;
import pl.ligatenisaziemnego.server.security.passwordreset.PasswordResetDto;
import pl.ligatenisaziemnego.server.security.passwordreset.PasswordResetToken;
import pl.ligatenisaziemnego.server.security.passwordreset.PasswordResetTokenRepository;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static pl.ligatenisaziemnego.server.applicationuser.ApplicationUserPermission.ROLE__CHANGE;

@Log4j2
@Service
public class SecurityService {
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    public SecurityService(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder,
            PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    public ApplicationUser getApplicationUserFromAuthentication() throws ExceptionWithResponseEntity {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken)
            throw ApiError.UNAUTHORIZED("Unauthenticated");

        return ((ApplicationUserPrincipal) authentication.getPrincipal()).getApplicationUser();
    }

    public ApplicationUser register(RegisterDto registerDto) throws ExceptionWithResponseEntity {
        if (applicationUserRepository.existsByUsername(registerDto.getUsername()))
            throw ApiError.BAD_REQUEST(Map.of("username", "There is already a user with this username"));
        if (applicationUserRepository.existsByEmail(registerDto.getEmail()))
            throw ApiError.BAD_REQUEST(Map.of("email", "There is already a user with this email"));
        if (!isPasswordSecure(registerDto.getPassword()))
            throw ApiError.BAD_REQUEST(Map.of("password", "Password is too weak"));


        ApplicationUser applicationUser = new ApplicationUser(registerDto.getUsername(), registerDto.getEmail(), registerDto.getFirstName(),
                registerDto.getLastName(), passwordEncoder.encode(registerDto.getPassword()), List.of());

        return applicationUserRepository.save(applicationUser);
    }

    private boolean isPasswordSecure(String password) {
        if (password.length() < 9) return false;
        if (!Pattern.matches(".*\\d.*", password)) return false;
        if (!Pattern.matches(".*[a-z].*", password)) return false;
        if (!Pattern.matches(".*[A-Z].*", password)) return false;
        if (!Pattern.matches(".*[!@#$%^&*()_\\-+=\\[\\]{};:'\"\\\\|,<.>/?].*", password)) return false;
        return true;
    }

    @Value("${rozgrywkitenisa.origin}")
    private String origin;

    public String generateSecureRandomPassword(int length) {
        if (length < 16) throw new IllegalArgumentException("Length must be greater or equal 16");
        char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()-_=+[{]}\\|<>/?".toCharArray());
        return RandomStringUtils.random(length, 0, possibleCharacters.length - 1, false, false, possibleCharacters, new SecureRandom());
    }

    private String generateSecureRandomToken() {
        char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray());
        return RandomStringUtils.random(32, 0, possibleCharacters.length - 1, false, false, possibleCharacters, new SecureRandom());
    }

    public void resetPassword(PasswordResetDto passwordResetDto) {
        var applicationUser = applicationUserRepository.findByEmail(passwordResetDto.getEmail()).orElse(null);
        if (applicationUser == null) return;
        if (passwordResetTokenRepository.existsByApplicationUser(applicationUser)) {
            return;
        }
        var expirationDate = Instant.now().plus(30, ChronoUnit.MINUTES);
        var token = new PasswordResetToken(null, applicationUser, generateSecureRandomToken(), expirationDate);
        passwordResetTokenRepository.save(token);
        emailService.sendSimpleMessage(new String[]{applicationUser.getEmail()}, "Rozgrywkitenisa - reset hasła",
                "Otrzymano prośbę o ustawienie nowego hasła,\njeśli nie byłeś to Ty zgłoś to administratorowi.\n\nMożesz ustawić nowe hasło używając linku poniżej:\n%s/reset-hasla/%s\n".formatted(
                        origin, token.getToken()));

    }

    public void changePasswordWithToken(String token, ChangePasswordWithTokenDto changePasswordWithTokenDto)
            throws ExceptionWithResponseEntity {

        var badRequestException = ApiError.BAD_REQUEST(Map.of("token", "Token is invalid or expired"));
        var passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> badRequestException);
        if (Instant.now().isAfter(passwordResetToken.getExpirationDate()))
            throw badRequestException;

        if (!isPasswordSecure(changePasswordWithTokenDto.getPassword()))
            throw ApiError.BAD_REQUEST(Map.of("password", "Password is too weak"));

        var applicationUser = passwordResetToken.getApplicationUser();
        applicationUser.setPassword(passwordEncoder.encode(changePasswordWithTokenDto.getPassword()));
        passwordResetTokenRepository.delete(passwordResetToken);
        applicationUserRepository.save(applicationUser);
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5) // 5 min
    @Transactional
    public void removeExpiredPasswordResetTokens() {
        long deletedRecords = passwordResetTokenRepository.deleteByExpirationDateLessThanEqual(Instant.now());
        if (deletedRecords > 0) log.info("Deleted " + deletedRecords + " expired PasswordResetTokens");
    }
}
