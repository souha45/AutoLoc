package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import tn.esprit.autoloc.domain.Equipement;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EquipementMapper {
    EquipementDTO toDto(Equipement equipement);
    Equipement fromDto(EquipementDTO equipementDTO);
    List<EquipementDTO> toDtoList(List<Equipement> equipements);
    List<Equipement> fromDtoList(List<EquipementDTO> equipementDTOs);
}
