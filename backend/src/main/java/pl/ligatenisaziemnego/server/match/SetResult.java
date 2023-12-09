package pl.ligatenisaziemnego.server.match;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Embeddable
public class SetResult {
    @NotNull(message = "firstPlayerScore can't be null")
    @PositiveOrZero(message = "firstPlayerScore be positive or zero")
    private Long firstPlayerScore;

    @NotNull(message = "secondPlayerScore can't be null")
    @PositiveOrZero(message = "secondPlayerScore must be positive or zero")
    private Long secondPlayerScore;

    @Getter(AccessLevel.NONE)
    @Transient
    private List<Long> gamesScored;

    public List<Long> getGamesScored() {
        return List.of(firstPlayerScore, secondPlayerScore);
    }


}
