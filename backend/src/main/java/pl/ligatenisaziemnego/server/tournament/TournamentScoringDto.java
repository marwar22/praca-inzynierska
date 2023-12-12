package pl.ligatenisaziemnego.server.tournament;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link TournamentScoring}
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TournamentScoringDto implements Serializable {
    @NotNull(message = "groupPointsForWin can't be null")
    @Positive(message = "groupPointsForWin must be positive")
    private Short groupPointsForWin;

    @NotNull(message = "groupPointsForLoss can't be null")
    @PositiveOrZero(message = "groupPointsForLoss must be positive or zero")
    private Short groupPointsForLoss;

    @NotNull(message = "groupPointsForWalkover can't be null")
    @PositiveOrZero(message = "groupPointsForWalkover must be positive or zero")
    private Short groupPointsForWalkover;

    @NotNull(message = "ratingForTournamentParticipation can't be null")
    @PositiveOrZero(message = "ratingForTournamentParticipation must be positive or zero")
    private Short ratingForTournamentParticipation;

    @NotNull(message = "ratingForMatchWin can't be null")
    @PositiveOrZero(message = "ratingForMatchWin must be positive or zero")
    private Short ratingForMatchWin;

    @NotNull(message = "ratingForMatchLoss can't be null")
    @PositiveOrZero(message = "ratingForMatchLoss must be positive or zero")
    private Short ratingForMatchLoss;

    @NotNull(message = "ratingForMatchWalkover can't be null")
    @PositiveOrZero(message = "ratingForMatchWalkover must be positive or zero")
    private Short ratingForMatchWalkover;

    @NotNull(message = "groupPointsForWin can't be null")
    @PositiveOrZero(message = "ratingForTournamentWin must be positive or zero")
    private Short ratingForTournamentWin;

    @NotEmpty(message = "ratingForKnockoutStageParticipation can't be empty")
    private List<@NotNull(message = "rating can't be null") Short> ratingForKnockoutStageParticipation;
}