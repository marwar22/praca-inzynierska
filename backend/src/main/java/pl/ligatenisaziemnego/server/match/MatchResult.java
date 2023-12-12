package pl.ligatenisaziemnego.server.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "match_result")
public class MatchResult {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "winnerId can't be null")
    @Column(name = "winner_id")
    private Long winnerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "winner_id", insertable = false, updatable = false)
    private ApplicationUser winner;

    @NotNull(message = "walkover can't be null")
    @Column(name = "walkover")
    private Boolean walkover;

    @NotNull(message = "scratch can't be null")
    @Column(name = "scratch")
    private Boolean scratch;

    @NotNull(message = "playedSetResults can't be null")
    @ElementCollection
    @OrderColumn(name = "set_number")
    @CollectionTable(name = "match_result_played_set_result", joinColumns = @JoinColumn(name = "match_result_id"))
    private List<@NotNull(message = "setResult can't be null") SetResult> playedSetResults;

    @NotNull(message = "setResults can't be null")
    @ElementCollection
    @OrderColumn(name = "set_number")
    @CollectionTable(name = "match_result_set_result", joinColumns = @JoinColumn(name = "match_result_id"))
    private List<@NotNull(message = "setResult can't be null") SetResult> setResults;

    @Getter(AccessLevel.NONE)
    @Transient
    private List<Long> setsScored;

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(mappedBy = "result")
    private Match match;

    public Long getFirstPlayerScore() {
        return setResults.stream().mapToLong((sr) -> sr.getFirstPlayerScore() > sr.getSecondPlayerScore() ? 1 : 0).sum();
    }

    public Long getSecondPlayerScore() {
        return setResults.stream().mapToLong((sr) -> sr.getSecondPlayerScore() > sr.getFirstPlayerScore() ? 1 : 0).sum();
    }

    public List<Long> getSetsScored() {
        return List.of(getFirstPlayerScore(), getSecondPlayerScore());
//        if (!this.scratch) return List.of(getFirstPlayerScore(), getSecondPlayerScore());
//        var winnerIndex = winnerId.equals(match.getFirstPlayerId()) ? 0 : 1;
////        TODO count as played till end
//        var setsScored = new ArrayList<>(List.of(0L, 0L));
//        Long setsToWin = match.getTournament().getSetsToWin();
//
//        for (int i = 0; i < setResults.size() - 1; i++) {
//            var gamesScored = setResults.get(i).getGamesScored();
//            if (gamesScored.get(0) > gamesScored.get(1)) setsScored.set(0, setsScored.get(0) + 1);
//            if (gamesScored.get(0) < gamesScored.get(1)) setsScored.set(1, setsScored.get(1) + 1);
//        }
//        setsScored.set(winnerIndex, setsToWin);
//        return setsScored;
    }
}
