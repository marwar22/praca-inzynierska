package pl.ligatenisaziemnego.server.tournament;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Tournament}
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TournamentCreateDto implements Serializable {
    @Size(min = 5, max = 255, message = "name must be at least 5 and at most 255 characters long")
    @NotBlank(message = "name can't be blank")
    String name;

    // TODO "magic" numbers
    @Range(min = 1, max = 128, message = "numberOfPlayers must be in range <1, 128>")
    @NotNull(message = "numberOfPlayers can't be null")
    Short numberOfPlayers;

    // TODO "magic" numbers
    @Range(min = 1, max = 24, message = "numberOfGroups must be in range <1, 24>")
    @NotNull(message = "numberOfGroups can't be null")
    Short numberOfGroups;

    // TODO "magic" numbers
    @Range(min = 1, max = 128, message = "numberOfPlayersInKnockoutBracket must be in range <1, 128>")
    @NotNull(message = "numberOfPlayersInKnockoutBracket can't be null")
    Short numberOfPlayersInKnockoutBracket;

    @NotNull(message = "setsToWin can't be null")
    @Range(message = "setsToWin must be in range <2, 3>", min = 2, max = 3)
    Long setsToWin;

    @NotEmpty(message = "playerIds can't be empty")
    List<@NotNull(message = "playerId can't be null") Long> playerIds;

    @NotEmpty(message = "groups can't be empty")
    List<@NotNull(message = "group can't be null") TournamentGroupDto> groups;

    @NotNull(message = "start_date can't be null")
    LocalDateTime startDate;

    @NotNull(message = "end_date can't be null")
    LocalDateTime endDate;


    /**
     * DTO for {@link pl.ligatenisaziemnego.server.tournament.group.TournamentGroup}
     */
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TournamentGroupDto implements Serializable {
        @NotEmpty(message = "playerIds can't be empty")
        List<@NotNull(message = "playerId can't be null") Long> playerIds;
    }
}