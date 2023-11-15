package pl.ligatenisaziemnego.server.match;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MatchMapper {
    Match update(MatchUpdateDto matchUpdateDto, @MappingTarget Match match);
}