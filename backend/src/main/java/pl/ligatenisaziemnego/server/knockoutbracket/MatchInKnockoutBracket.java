package pl.ligatenisaziemnego.server.knockoutbracket;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import pl.ligatenisaziemnego.server.match.Match;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "match_in_knockout_bracket")
public class MatchInKnockoutBracket {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "match can't be null")
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "match_id")
    private Match match;

    @NotNull(message = "stage can't be null")
    @PositiveOrZero(message = "stage must be positive or zero")
    @Column(name = "stage")
    private Long stage;

    @Column(name = "bracket_position", updatable = false, insertable = false)
    private Long bracketPosition;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "next_match_in_knockout_bracket_id")
    private MatchInKnockoutBracket nextMatchInKnockoutBracket;

    @Column(name = "next_match_in_knockout_bracket_id", updatable = false, insertable = false)
    private Long nextMatchInKnockoutBracketId;
}
