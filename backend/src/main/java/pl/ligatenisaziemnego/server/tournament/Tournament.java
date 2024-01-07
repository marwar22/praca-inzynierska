package pl.ligatenisaziemnego.server.tournament;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import pl.ligatenisaziemnego.server.knockoutbracket.KnockoutBracket;
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
    private Short numberOfPlayers;

    // TODO "magic" numbers
    @Range(min = 0, max = 24, message = "numberOfGroups must be in range <0, 24>")
    @NotNull(message = "numberOfGroups can't be null")
    @Column(name = "number_of_groups", nullable = false)
    private Short numberOfGroups;

    // TODO "magic" numbers
    @Range(min = 1, max = 128, message = "numberOfPlayersInKnockoutBracket must be in range <1, 128>")
    @NotNull(message = "numberOfPlayersInKnockoutBracket can't be null")
    @Column(name = "number_of_players_in_knockout_bracket", nullable = false)
    private Short numberOfPlayersInKnockoutBracket;

    @Range(min = 2, max = 3, message = "setsToWin must be in range <2, 3>")
    @NotNull(message = "setsToWin can't be null")
    @Column(name = "sets_to_win", nullable = false)
    private Long setsToWin;

    @NotEmpty(message = "players can't be empty")
    @ManyToMany
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinTable(name = "tournament_player",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private List<@NotNull(message = "applicationUser can't be null") ApplicationUser> players;

    @Column(name = "has_group_stage")
    @NotNull(message = "hasGroupStage can't be null")
    private Boolean hasGroupStage;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "group_number")
    @JoinColumn(name = "tournament_id")
    private List<@NotNull(message = "tournamentGroup can't be null") TournamentGroup> groups;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "knockout_bracket_id")
    private KnockoutBracket knockoutBracket;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "tournament_scoring_id", nullable = false)
    private TournamentScoring scoring;

    @NotNull(message = "start_date can't be null")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "end_date can't be null")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @NotNull(message = "organizer_id can't be null")
    @Column(name = "organizer_id")
    private Long organizerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "organizer_id", insertable = false, updatable = false)
    private ApplicationUser organizer;

    @CreationTimestamp
    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name = "updated_date_time", nullable = false)
    private LocalDateTime updatedDateTime;
}
