package pl.ligatenisaziemnego.server.applicationuser;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.ligatenisaziemnego.server.controlleradvice.ApiError;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;
import pl.ligatenisaziemnego.server.security.SecurityService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;
    private final SecurityService securityService;
    private final ApplicationUserMapper applicationUserMapper;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository, SecurityService securityService,
            ApplicationUserMapper applicationUserMapper) {
        this.applicationUserRepository = applicationUserRepository;
        this.securityService = securityService;
        this.applicationUserMapper = applicationUserMapper;
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
