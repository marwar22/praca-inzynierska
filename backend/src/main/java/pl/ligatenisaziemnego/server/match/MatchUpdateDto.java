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

        @NotNull(message = "setResults can't be null")
        List<@NotNull(message = "setResult can't be null") SetResultDto> setResults;

        @NotNull(message = "walkover can't be null")
        private Boolean walkover;

        @NotNull(message = "scratch can't be null")
        private Boolean scratch;

        @NotEmpty(message = "setsScored can't be null")
        List<@NotNull(message = "setScore can't be null") @PositiveOrZero(message = "setScore must be positve or zero") Long> setsScored;

        /**
         * DTO for {@link SetResult}
         */
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SetResultDto implements Serializable {
            @NotEmpty(message = "gamesScored can't be null")
            List<@NotNull(message = "gameScore can't be null") @PositiveOrZero(message = "gameScore must be positve or zero") Long> gamesScored;
        }
    }
}