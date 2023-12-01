package pl.ligatenisaziemnego.server.match;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MatchMapper {
    Match update(MatchUpdateDto matchUpdateDto, @MappingTarget Match match);

    MatchDto toDto(Match match);
}