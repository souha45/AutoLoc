package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import tn.esprit.autoloc.domain.Client;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toDto(Client client);
    Client fromDto(ClientDTO clientDTO);
    List<ClientDTO> toDtoList(List<Client> clients);
    List<Client> fromDtoList(List<ClientDTO> clientDTOs);
}
