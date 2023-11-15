package pl.ligatenisaziemnego.server.match;

import jakarta.persistence.Embeddable;
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
    @PositiveOrZero(message = "firstPlayerScore be positive or zero")
    private Long firstPlayerScore;
    @PositiveOrZero(message = "secondPlayerScore must be positive or zero")
    private Long secondPlayerScore;
}
