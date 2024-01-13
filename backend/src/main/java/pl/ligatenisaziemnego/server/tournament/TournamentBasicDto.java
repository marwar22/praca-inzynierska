package pl.ligatenisaziemnego.server.tournament;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.time.LocalDateTime;

import static pl.ligatenisaziemnego.server.tournament.Tournament.*;

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
    @Range(min = MIN_NUMBER_OF_PLAYERS, max = MAX_NUMBER_OF_PLAYERS, message = "numberOfPlayers must be in range <" + MIN_NUMBER_OF_PLAYERS + ", " + MAX_NUMBER_OF_PLAYERS + ">")
    Short numberOfPlayers;

    @NotNull(message = "hasGroupStage can't be null")
    Boolean hasGroupStage;

    @NotNull(message = "numberOfGroups can't be null")
    @Range(min = 0, max = 24, message = "numberOfGroups must be in range <" + MIN_NUMBER_OF_GROUPS + ", " + MAX_NUMBER_OF_GROUPS + ">")
    Short numberOfGroups;

    @NotNull(message = "numberOfPlayersInKnockoutBracket can't be null")
    @Range(min = 2, max = 128, message = "numberOfPlayersInKnockoutBracket must be in range <" + MIN_NUMBER_OF_PLAYERS_IN_KNOCKOUT_BRACKET + ", " + MAX_NUMBER_OF_PLAYERS_IN_KNOCKOUT_BRACKET + ">")
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