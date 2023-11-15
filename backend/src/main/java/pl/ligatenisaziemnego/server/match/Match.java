package pl.ligatenisaziemnego.server.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;


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
}
