package pl.ligatenisaziemnego.server.knockoutbracket;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import pl.ligatenisaziemnego.server.match.Match;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "knockout_bracket_match")
public class KnockoutBracketMatch {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "stage can't be null")
    @PositiveOrZero(message = "stage must be positive or zero")
    @Column(name = "stage")
    private Long stage;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "match_id")
    private Match match;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "next_knockout_bracket_match_id", updatable = false, insertable = false)
    private KnockoutBracketMatch nextKnockoutBracketMatch;

    @Column(name = "next_knockout_bracket_match_id")
    private Long nextKnockoutBracketMatchId;
}
