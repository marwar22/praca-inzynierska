package pl.ligatenisaziemnego.server.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.*;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;
import pl.ligatenisaziemnego.server.knockoutbracket.MatchInKnockoutBracket;
import pl.ligatenisaziemnego.server.tournament.Tournament;
import pl.ligatenisaziemnego.server.tournament.group.TournamentGroup;

import java.time.Instant;


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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @ToString.Exclude
//    @JsonIgnore
//    @JoinTable(
//            name = "tournament_group_match",
//            joinColumns = @JoinColumn(name = "match_id", insertable = false, updatable = false),
//            inverseJoinColumns = @JoinColumn(name = "tournament_group_id", insertable = false, updatable = false)
//    )
//    private TournamentGroup tournamentGroup;
//
//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "match")
//    @LazyToOne(LazyToOneOption.NO_PROXY)
//    @JsonIgnore
//    @ToString.Exclude
//    private MatchInKnockoutBracket matchInKnockoutBracket;

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
}
