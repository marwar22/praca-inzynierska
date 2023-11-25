package pl.ligatenisaziemnego.server.applicationuser;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link ApplicationUser}
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApplicationUserContactDto implements Serializable {
    Long id;
    @NotEmpty(message = "username can't be empty")
    String username;
    @NotEmpty(message = "firstName can't be empty")
    String firstName;
    @NotEmpty(message = "lastName can't be empty")
    String lastName;
    @NotEmpty(message = "email can't be empty")
    String email;
}