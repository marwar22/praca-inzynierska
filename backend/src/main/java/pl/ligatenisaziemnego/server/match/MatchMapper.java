package pl.ligatenisaziemnego.server.match;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MatchMapper {
    Match update(MatchUpdateDto matchUpdateDto, @MappingTarget Match match);

    @Mappings({@Mapping(source = "tournamentGroup.id", target = "tournamentGroupId"),
            @Mapping(source = "tournamentGroup.tournament.id", target = "tournamentId"),
            @Mapping(source = "result.firstPlayerScore", target = "result.firstPlayerScore")
    })
    MatchDto toDto(Match match);
}