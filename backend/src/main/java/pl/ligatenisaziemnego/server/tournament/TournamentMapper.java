package pl.ligatenisaziemnego.server.tournament;

import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;
import pl.ligatenisaziemnego.server.tournament.group.TournamentGroup;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TournamentMapper {
    Tournament toEntity(TournamentCreateDto tournamentCreateDto);

    static List<TournamentGroup> map(List<TournamentCreateDto.TournamentGroupDto> groups) {
        return groups.stream().map(tournamentGroupDto -> new TournamentGroup(null, null, tournamentGroupDto.getPlayerIds())).toList();
    }
}
