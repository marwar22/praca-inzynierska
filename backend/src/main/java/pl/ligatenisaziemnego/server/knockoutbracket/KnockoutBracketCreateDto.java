package pl.ligatenisaziemnego.server.knockoutbracket;

import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link KnockoutBracket}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KnockoutBracketCreateDto implements Serializable {
    @Min(message = "numberOfPlayers must be greater or equal to 2", value = 2)
    Long numberOfPlayers;
}