package pl.ligatenisaziemnego.server.applicationuser;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {@link ApplicationUser}
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApplicationUserBasicDto implements Serializable {
    Long id;

    @NotEmpty(message = "username can't be empty")
    String username;

    @NotEmpty(message = "firstName can't be empty")
    String firstName;

    @NotEmpty(message = "lastName can't be empty")
    String lastName;

    @PositiveOrZero(message = "rating must be positive or zero")
    Long rating;

    @PositiveOrZero(message = "prvRating must be positive or zero")
    Long prvRating;

    @PositiveOrZero(message = "ranking must be positive or zero")
    Integer ranking;

    @PositiveOrZero(message = "prvRanking must be positive or zero")
    Integer prvRanking;
}