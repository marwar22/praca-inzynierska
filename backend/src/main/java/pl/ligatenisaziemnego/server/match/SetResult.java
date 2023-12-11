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
    private Short firstPlayerScore;

    @NotNull(message = "secondPlayerScore can't be null")
    @PositiveOrZero(message = "secondPlayerScore must be positive or zero")
    private Short secondPlayerScore;

    @Getter(AccessLevel.NONE)
    @Transient
    private List<Short> gamesScored;

    public List<Short> getGamesScored() {
        return List.of(firstPlayerScore, secondPlayerScore);
    }


}
