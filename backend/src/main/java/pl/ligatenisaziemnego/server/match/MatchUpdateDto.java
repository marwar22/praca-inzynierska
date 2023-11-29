package pl.ligatenisaziemnego.server.match;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Match}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchUpdateDto implements Serializable {
    MatchResultDto result;

    /**
     * DTO for {@link MatchResult}
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchResultDto implements Serializable {
        @NotNull(message = "winnerId can't be null")
        Long winnerId;
        @NotEmpty(message = "setResults can't be empty")
        List<@NotNull(message = "setResult can't be null") SetResultDto> setResults;

        /**
         * DTO for {@link SetResult}
         */
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SetResultDto implements Serializable {
            @NotNull(message = "firstPlayerScore can't be null")
            @PositiveOrZero(message = "firstPlayerScore be positive or zero")
            Long firstPlayerScore;
            @NotNull(message = "secondPlayerScore can't be null")
            @PositiveOrZero(message = "secondPlayerScore must be positive or zero")
            Long secondPlayerScore;
        }
    }
}