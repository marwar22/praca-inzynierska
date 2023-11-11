package pl.ligatenisaziemnego.server.tournament.group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;

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

    @Setter(AccessLevel.NONE)
    @JsonIgnore
    @ManyToMany
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinTable(name = "tournament_group_player",
            joinColumns = @JoinColumn(name = "tournament_group_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "player_id", insertable = false, updatable = false))
    private List<ApplicationUser> players;

    @NotEmpty(message = "playerIds can't be empty")
    @ElementCollection
    @CollectionTable(name = "tournament_group_player", joinColumns = @JoinColumn(name = "tournament_group_id"))
    @Column(name = "player_id")
    private List<Long> playerIds;
}
