package pl.ligatenisaziemnego.server.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @CollectionTable(name = "match_result_set_result", joinColumns = @JoinColumn(name = "match_result_id"))
    private List<@NotNull(message = "setResult can't be null") SetResult> setResults;

    private Long getFirstPlayerScore() {
        return setResults.stream().mapToLong(SetResult::getFirstPlayerScore).sum();
    }
    private Long getSecondPlayerScore() {
        return setResults.stream().mapToLong(SetResult::getSecondPlayerScore).sum();
    }
}
