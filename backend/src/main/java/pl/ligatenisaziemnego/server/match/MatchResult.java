package pl.ligatenisaziemnego.server.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;

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

    @NotEmpty(message = "setResults can't be empty")
    @ElementCollection
    @OrderColumn(name = "set_number")
    @CollectionTable(name = "match_result_set_result", joinColumns = @JoinColumn(name = "match_result_id"))
    private List<@NotNull(message = "setResult can't be null") SetResult> setResults;

    @Getter(AccessLevel.NONE)
    @Transient
    private Long firstPlayerScore;

    @Getter(AccessLevel.NONE)
    @Transient
    private Long secondPlayerScore;

    public Long getFirstPlayerScore() {
        return setResults.stream().mapToLong((sr) -> sr.getFirstPlayerScore() > sr.getSecondPlayerScore() ? 1 : 0).sum();
    }

    public Long getSecondPlayerScore() {
        return setResults.stream().mapToLong((sr) -> sr.getSecondPlayerScore() > sr.getFirstPlayerScore() ? 1 : 0).sum();
    }
}
