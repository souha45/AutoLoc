package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import tn.esprit.autoloc.domain.Reservation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationDTO toDto(Reservation reservation);
    Reservation fromDto(ReservationDTO reservationDTO);
    List<ReservationDTO> toDtoList(List<Reservation> reservations);
    List<Reservation> fromDtoList(List<ReservationDTO> reservationDTOs);
}
