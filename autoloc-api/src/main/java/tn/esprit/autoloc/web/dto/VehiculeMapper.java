package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import tn.esprit.autoloc.domain.Vehicule;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {
    VehiculeDTO toDto(Vehicule vehicule);
    Vehicule fromDto(VehiculeDTO vehiculeDTO);
    List<VehiculeDTO> toDtoList(List<Vehicule> vehicules);
    List<Vehicule> fromDtoList(List<VehiculeDTO> vehiculeDTOs);
}
