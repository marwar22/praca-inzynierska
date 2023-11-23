package pl.ligatenisaziemnego.server.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;
import pl.ligatenisaziemnego.server.tournament.group.TournamentGroup;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "match")
public class Match {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_result_id")
    private MatchResult result;

    @NotNull(message = "firstPlayerId can't be null")
    @Column(name = "first_player_id", nullable = false)
    private Long firstPlayerId;

    @NotNull(message = "secondPlayerId can't be null")
    @Column(name = "second_player_id", nullable = false)
    private Long secondPlayerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "first_player_id", insertable = false, updatable = false)
    private ApplicationUser firstPlayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "second_player_id", insertable = false, updatable = false)
    private ApplicationUser secondPlayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    @JoinTable(
            name = "tournament_group_match",
            joinColumns = @JoinColumn(name = "match_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "tournament_group_id", insertable = false, updatable = false)
    )
    private TournamentGroup tournamentGroup;


    @Column(name = "last_modified_by_id")
    private Long lastModifiedById;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "last_modified_by_id", insertable = false, updatable = false)
    private ApplicationUser lastModifiedBy;

    @CreationTimestamp
    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name = "updated_date_time", nullable = false)
    private LocalDateTime updatedDateTime;
}
