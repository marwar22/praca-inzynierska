package pl.ligatenisaziemnego.server.tournament.group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;
import pl.ligatenisaziemnego.server.match.Match;
import pl.ligatenisaziemnego.server.tournament.Tournament;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tournament_group")
public class TournamentGroup {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(cascade = CascadeType.PERSIST)
    @NotNull(message = "matches can't be null")
    @JoinTable(
            name = "tournament_group_match",
            joinColumns = @JoinColumn(name = "tournament_group_id"),
            inverseJoinColumns = @JoinColumn(name = "match_id")
    )
    private List<@NotNull(message = "match can't be null") Match> matches;

    @Setter(AccessLevel.NONE)
    @JsonIgnore
    @ManyToMany
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinTable(name = "tournament_group_player",
            joinColumns = @JoinColumn(name = "tournament_group_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "player_id", insertable = false, updatable = false))
    private List<@NotNull(message = "player can't be null") ApplicationUser> players;

    @NotEmpty(message = "playerIds can't be empty")
    @ElementCollection
    @CollectionTable(name = "tournament_group_player", joinColumns = @JoinColumn(name = "tournament_group_id"))
    @Column(name = "player_id")
    private List<@NotNull(message = "playerId can't be null") Long> playerIds;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "tournament_id", insertable = false, updatable = false)
    private Tournament tournament;
}
