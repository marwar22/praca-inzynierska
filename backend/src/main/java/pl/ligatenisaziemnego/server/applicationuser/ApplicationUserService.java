package pl.ligatenisaziemnego.server.applicationuser;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.ligatenisaziemnego.server.controlleradvice.ApiError;
import pl.ligatenisaziemnego.server.controlleradvice.ExceptionWithResponseEntity;
import pl.ligatenisaziemnego.server.security.SecurityService;

import java.util.List;

@Service
public class ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;
    private final SecurityService securityService;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository, SecurityService securityService) {
        this.applicationUserRepository = applicationUserRepository;
        this.securityService = securityService;
    }

    public ApplicationUser getById(long id) throws ExceptionWithResponseEntity {
        return applicationUserRepository.findById(id).orElseThrow(() -> ApiError.NOT_FOUND_ID(ApplicationUser.class, id));
    }

    public List<ApplicationUser> getAllByName(String name, int limit) {
        return applicationUserRepository.findAllByFirstNameWithLastName(name, PageRequest.of(0, limit));
    }

    public Object getMyApplicationUser() throws ExceptionWithResponseEntity {
        var applicationUser = securityService.getApplicationUserFromAuthentication();
        return getById(applicationUser.getId());
    }
}
