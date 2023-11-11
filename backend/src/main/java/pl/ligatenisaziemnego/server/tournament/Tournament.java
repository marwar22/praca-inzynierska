package pl.ligatenisaziemnego.server.tournament;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;
import pl.ligatenisaziemnego.server.tournament.group.TournamentGroup;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tournament")
public class Tournament {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "name can't be blank")
    @Size(min = 5, max = 255, message = "name must be at least 5 and at most 255 characters long")
    @Column(name = "name", nullable = false)
    private String name;

    // TODO "magic" numbers
    @Range(min = 1, max = 128, message = "numberOfPlayers must be in range <1, 128>")
    @NotNull(message = "numberOfPlayers can't be null")
    @Column(name = "number_of_players", nullable = false)
    private Long numberOfPlayers;

    // TODO "magic" numbers
    @Range(min = 1, max = 24, message = "numberOfGroups must be in range <1, 24>")
    @NotNull(message = "numberOfGroups can't be null")
    @Column(name = "number_of_groups", nullable = false)
    private Long numberOfGroups;

    @NotEmpty(message = "players can't be empty")
    @ManyToMany
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinTable(name = "tournament_player",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private List<@NotNull(message = "applicationUser can't be null") ApplicationUser> players;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "group_number")
    @JoinColumn(name = "tournament_id")
    private List<@NotNull(message = "tournamentGroup can't be null") TournamentGroup> groups;


    @NotNull(message = "start_date can't be null")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "end_date can't be null")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @CreationTimestamp
    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name = "updated_date_time", nullable = false)
    private LocalDateTime updatedDateTime;
}
