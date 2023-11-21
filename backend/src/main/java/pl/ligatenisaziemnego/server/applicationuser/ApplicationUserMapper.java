package pl.ligatenisaziemnego.server.applicationuser;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ApplicationUserMapper {
    ApplicationUserBasicDto toDto(ApplicationUser applicationUser);

}