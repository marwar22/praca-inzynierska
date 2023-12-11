package pl.ligatenisaziemnego.server.tournament;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Tournament}
 */
@Value
public class TournamentBasicDto implements Serializable {
    Long id;
    @Size(message = "name must be at least 5 and at most 255 characters long", min = 5, max = 255)
    @NotBlank(message = "name can't be blank")
    String name;

    @NotNull(message = "numberOfPlayers can't be null")
    @Range(min = 1, max = 128, message = "numberOfPlayers must be in range <1, 128>")
    Short numberOfPlayers;

    @NotNull(message = "numberOfGroups can't be null")
    @Range(min = 1, max = 24, message = "numberOfGroups must be in range <1, 24>")
    Short numberOfGroups;

    @Range(min = 1, max = 128, message = "numberOfPlayersInKnockoutBracket must be in range <1, 128>")
    @NotNull(message = "numberOfPlayersInKnockoutBracket can't be null")
    Short numberOfPlayersInKnockoutBracket;

    @NotNull(message = "setsToWin can't be null")
    @Range(message = "setsToWin must be in range <2, 3>", min = 2, max = 3)
    Long setsToWin;

    @NotNull(message = "start_date can't be null")
    LocalDateTime startDate;

    @NotNull(message = "end_date can't be null")
    LocalDateTime endDate;

    @NotNull(message = "organizer_id can't be null")
    Long organizerId;

    LocalDateTime createdDateTime;
    LocalDateTime updatedDateTime;
}