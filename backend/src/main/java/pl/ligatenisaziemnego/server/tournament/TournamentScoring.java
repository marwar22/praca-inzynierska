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
import lombok.ToString;

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
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ranking_for_knockout_stage_participation", joinColumns = @JoinColumn(name = "tournament_id"))
    @Column(name = "ranking")
    @OrderColumn(name = "stage")
    private List<@NotNull(message = "ranking can't be null") Short> rankingForKnockoutStageParticipation;
}