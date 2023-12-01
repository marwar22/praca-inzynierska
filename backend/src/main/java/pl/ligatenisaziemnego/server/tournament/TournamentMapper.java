package pl.ligatenisaziemnego.server.tournament;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.ligatenisaziemnego.server.tournament.group.TournamentGroup;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TournamentMapper {
    Tournament toEntity(TournamentCreateDto tournamentCreateDto);

    static List<TournamentGroup> map(List<TournamentCreateDto.TournamentGroupDto> groups) {
        return groups.stream()
                     .map(tournamentGroupDto -> new TournamentGroup(null, List.of(), null, tournamentGroupDto.getPlayerIds()))
                     .toList();
    }


    TournamentBasicDto toBasicDto(Tournament tournament);

}
