package pl.ligatenisaziemnego.server.tournament;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tournament_scoring")
public class TournamentScoring {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "scoring", optional = false)
    private Tournament tournament;

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
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "rating_for_knockout_stage_participation", joinColumns = @JoinColumn(name = "tournament_scoring_id"))
    @Column(name = "rating")
    @OrderColumn(name = "stage")
    private List<@NotNull(message = "rating can't be null") Short> ratingForKnockoutStageParticipation;
}