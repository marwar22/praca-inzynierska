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

import static pl.ligatenisaziemnego.server.tournament.Tournament.*;

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

    @Range(min = MIN_NUMBER_OF_PLAYERS, max = MAX_NUMBER_OF_PLAYERS, message = "numberOfPlayers must be in range <" + MIN_NUMBER_OF_PLAYERS + ", " + MAX_NUMBER_OF_PLAYERS + ">")
    @NotNull(message = "numberOfPlayers can't be null")
    Short numberOfPlayers;

    @NotNull(message = "hasGroupStage can't be null")
    private Boolean hasGroupStage;

    @Range(min = 0, max = 24, message = "numberOfGroups must be in range <" + MIN_NUMBER_OF_GROUPS + ", " + MAX_NUMBER_OF_GROUPS + ">")
    @NotNull(message = "numberOfGroups can't be null")
    Short numberOfGroups;

    @NotNull(message = "numberOfPlayersInKnockoutBracket can't be null")
    @Range(min = 2, max = 128, message = "numberOfPlayersInKnockoutBracket must be in range <" + MIN_NUMBER_OF_PLAYERS_IN_KNOCKOUT_BRACKET + ", " + MAX_NUMBER_OF_PLAYERS_IN_KNOCKOUT_BRACKET + ">")
    Short numberOfPlayersInKnockoutBracket;

    @NotNull(message = "setsToWin can't be null")
    @Range(message = "setsToWin must be in range <2, 3>", min = 2, max = 3)
    Long setsToWin;

    @NotEmpty(message = "playerIds can't be empty")
    List<@NotNull(message = "playerId can't be null") Long> playerIds;

    @NotNull(message = "groups can't be null")
    List<@NotNull(message = "group can't be null") TournamentGroupDto> groups;

    @NotNull(message = "start_date can't be null")
    LocalDateTime startDate;

    @NotNull(message = "end_date can't be null")
    LocalDateTime endDate;

    @NotNull(message = "scoring can't be null")
    TournamentScoringDto scoring;


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