package pl.ligatenisaziemnego.server.applicationuser;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.ligatenisaziemnego.server.controlleradvice.ApiError;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;
import pl.ligatenisaziemnego.server.security.SecurityService;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;
    private final SecurityService securityService;
    private final ApplicationUserMapper applicationUserMapper;
    private final PasswordEncoder passwordEncoder;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository, SecurityService securityService,
            ApplicationUserMapper applicationUserMapper, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.securityService = securityService;
        this.applicationUserMapper = applicationUserMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Value("${rozgrywkitenisa.first_super_admin_email}")
    private String firstSuperAdminEmail;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        var applicationUsersCount = applicationUserRepository.count();
        if (applicationUsersCount > 0) {
            log.info("There are " + applicationUsersCount + " application users didn't have to create SUPER_ADMIN");
            return;
        }
        log.warn("There are no users, creating first user");
        // TODO important generateRandomSecurePassword
        var password = "admin123";
        var applicationUser = new ApplicationUser("admin", firstSuperAdminEmail, "Admin", "Admin", passwordEncoder.encode(password),
                List.of(ApplicationUserRole.SUPER_ADMIN));
        applicationUserRepository.save(applicationUser);
        log.info("Created first user with SUPER_ADMIN role");
    }

    public ApplicationUser getById(long id) throws ExceptionWithResponseEntity {
        return applicationUserRepository.findById(id).orElseThrow(() -> ApiError.NOT_FOUND_ID(ApplicationUser.class, id));
    }

    private List<ApplicationUserBasicDto> findAllByName(String name, int limit, List<Long> exclude) {
        if (exclude.isEmpty()) return applicationUserRepository.findAllByFirstNameWithLastName(name, PageRequest.of(0, limit)).stream()
                                                               .map(applicationUserMapper::toBasicDto).collect(Collectors.toList());

        return applicationUserRepository.findAllByFirstNameWithLastNameAndIdNotIn(name, exclude, PageRequest.of(0, limit)).stream()
                                        .map(applicationUserMapper::toBasicDto).collect(Collectors.toList());
    }

    public List<ApplicationUserBasicDto> getAllByName(String name, int limit, List<Long> exclude) {
        var users = findAllByName(name, limit, exclude);
        if (users.size() < limit) {
            var extraUsersLimit = limit - users.size();
            List<Long> foundUsersIds = users.stream().map(au -> au.id).toList();
            users.addAll(findAllByName(name, extraUsersLimit, foundUsersIds));
        }
        return users;
    }

    public Object getMyApplicationUser() throws ExceptionWithResponseEntity {
        var applicationUser = securityService.getApplicationUserFromAuthentication();
        return getById(applicationUser.getId());
    }
}
