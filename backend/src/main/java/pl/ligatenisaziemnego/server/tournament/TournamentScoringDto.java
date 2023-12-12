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

    @NotNull(message = "rankingForTournamentParticipation can't be null")
    @PositiveOrZero(message = "rankingForTournamentParticipation must be positive or zero")
    private Short rankingForTournamentParticipation;

    @NotNull(message = "rankingForMatchWin can't be null")
    @PositiveOrZero(message = "rankingForMatchWin must be positive or zero")
    private Short rankingForMatchWin;

    @NotNull(message = "rankingForMatchLoss can't be null")
    @PositiveOrZero(message = "rankingForMatchLoss must be positive or zero")
    private Short rankingForMatchLoss;

    @NotNull(message = "rankingForMatchWalkover can't be null")
    @PositiveOrZero(message = "rankingForMatchWalkover must be positive or zero")
    private Short rankingForMatchWalkover;

    @NotNull(message = "groupPointsForWin can't be null")
    @PositiveOrZero(message = "rankingForTournamentWin must be positive or zero")
    private Short rankingForTournamentWin;

    @NotEmpty(message = "rankingForKnockoutStageParticipation can't be empty")
    private List<@NotNull(message = "ranking can't be null") Short> rankingForKnockoutStageParticipation;
}