package pl.ligatenisaziemnego.server.match;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MatchMapper {
    @Mapping(target = "result.setsScored", ignore = true)
    Match update(MatchUpdateDto matchUpdateDto, @MappingTarget Match match);

    default SetResult setMapper(MatchUpdateDto.MatchResultDto.SetResultDto setResultDto) {
        var result = new SetResult();
        result.setFirstPlayerScore(setResultDto.getGamesScored().get(0));
        result.setSecondPlayerScore(setResultDto.getGamesScored().get(1));
        result.setGamesScored(setResultDto.getGamesScored());
        return result;
    }

    MatchDto toDto(Match match);
}