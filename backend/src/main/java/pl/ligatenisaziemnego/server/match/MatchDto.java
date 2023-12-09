package pl.ligatenisaziemnego.server.match;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link Match}
 */
@Value
public class MatchDto implements Serializable {
    Long id;
    MatchResultDto result;
    Long firstPlayerId;
    Long secondPlayerId;
    Long tournamentGroupId;
    Long tournamentId;
    Long lastModifiedById;
    Instant createdDateTime;
    Instant updatedDateTime;

    /**
     * DTO for {@link MatchResult}
     */
    @Value
    public static class MatchResultDto implements Serializable {
        Long id;

        @NotNull(message = "winnerId can't be null")
        Long winnerId;

        @NotNull(message = "walkover can't be null")
        Boolean walkover;

        @NotNull(message = "scratch can't be null")
        Boolean scratch;

        @NotNull(message = "setResults can't be null")
        List<@NotNull(message = "setResult can't be null") SetResultDto> setResults;

        Long firstPlayerScore;

        Long secondPlayerScore;

        /**
         * DTO for {@link SetResult}
         */
        @Value
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