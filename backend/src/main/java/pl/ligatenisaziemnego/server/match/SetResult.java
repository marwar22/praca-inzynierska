package pl.ligatenisaziemnego.server.match;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
}
