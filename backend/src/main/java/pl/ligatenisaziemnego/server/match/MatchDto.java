package pl.ligatenisaziemnego.server.match;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Match}
 */
@Value
public class MatchDto implements Serializable {
    Long id;
    MatchResultDto result;
    @NotNull(message = "firstPlayerId can't be null")
    Long firstPlayerId;
    @NotNull(message = "secondPlayerId can't be null")
    Long secondPlayerId;
    Long tournamentGroupId;
    Long tournamentId;
    Long lastModifiedById;
    LocalDateTime createdDateTime;
    LocalDateTime updatedDateTime;

    /**
     * DTO for {@link MatchResult}
     */
    @Value
    public static class MatchResultDto implements Serializable {
        Long id;
        @NotNull(message = "winnerId can't be null")
        Long winnerId;
        List<SetResultDto> setResults;
        Long firstPlayerScore;
        Long secondPlayerScore;

        /**
         * DTO for {@link SetResult}
         */
        @Value
        public static class SetResultDto implements Serializable {
            @PositiveOrZero(message = "firstPlayerScore be positive or zero")
            Long firstPlayerScore;
            @PositiveOrZero(message = "secondPlayerScore must be positive or zero")
            Long secondPlayerScore;
        }
    }
}