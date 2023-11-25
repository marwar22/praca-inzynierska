package pl.ligatenisaziemnego.server.knockoutbracket;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.ligatenisaziemnego.server.match.Match;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "knockout_bracket")
public class KnockoutBracket {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(cascade = CascadeType.PERSIST)
    @NotNull(message = "matches can't be null")
    @JoinTable(
            name = "knockout_bracket_match",
            joinColumns = @JoinColumn(name = "knockout_bracket_id"),
            inverseJoinColumns = @JoinColumn(name = "match_id")
    )
    private List<@NotNull(message = "match can't be null") Match> matches;

    @Min(value = 2, message = "numberOfPlayers must be greater or equal to 2")
    @Column(name = "number_of_players")
    private Long numberOfPlayers;
}
