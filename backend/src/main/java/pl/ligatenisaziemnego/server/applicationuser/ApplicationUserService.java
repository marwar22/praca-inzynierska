package pl.ligatenisaziemnego.server.applicationuser;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    public List<ApplicationUser> findAllByName(String name, int limit) {
        return applicationUserRepository.findAllByFirstNameWithLastName(name, PageRequest.of(0, limit));
    }
}
