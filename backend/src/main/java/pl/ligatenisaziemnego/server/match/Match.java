package pl.ligatenisaziemnego.server.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.*;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;
import pl.ligatenisaziemnego.server.tournament.Tournament;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;


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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "match_result_id")
    private MatchResult result;

    @Column(name = "first_player_id")
    private Long firstPlayerId;

    @Column(name = "second_player_id")
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

    @Getter(AccessLevel.NONE)
    @Transient
    private List<Long> playerIds;

    @NotNull(message = "tournament_id can't be null")
    @Column(name = "tournament_id")
    private Long tournamentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "tournament_id", updatable = false, insertable = false)
    private Tournament tournament;

    @Column(name = "last_modified_by_id")
    private Long lastModifiedById;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "last_modified_by_id", insertable = false, updatable = false)
    private ApplicationUser lastModifiedBy;

    @CreationTimestamp
    @Column(name = "created_date_time", nullable = false, updatable = false)
    private Instant createdDateTime;

    @UpdateTimestamp
    @Column(name = "updated_date_time", nullable = false)
    private Instant updatedDateTime;

    public List<Long> getPlayerIds() {
        return Arrays.asList(firstPlayerId, secondPlayerId, null);
    }
}
