package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import tn.esprit.autoloc.domain.Contrat;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContratMapper {
    ContratDTO toDto(Contrat contrat);
    Contrat fromDto(ContratDTO contratDTO);
    List<ContratDTO> toDtoList(List<Contrat> contrats);
    List<Contrat> fromDtoList(List<ContratDTO> contratDTOs);
}
