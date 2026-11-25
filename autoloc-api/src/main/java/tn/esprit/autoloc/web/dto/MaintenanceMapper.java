package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import tn.esprit.autoloc.domain.Maintenance;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {
    MaintenanceDTO toDto(Maintenance maintenance);
    Maintenance fromDto(MaintenanceDTO maintenanceDTO);
    List<MaintenanceDTO> toDtoList(List<Maintenance> maintenances);
    List<Maintenance> fromDtoList(List<MaintenanceDTO> maintenanceDTOs);
}
