package tn.esprit.autoloc.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.repository.IReservationRepository;

@Service
public class ReservationServiceImpl extends GenericServiceImpl<Reservation, Long> implements IReservationService {
    public ReservationServiceImpl(IReservationRepository repository) { super(repository); }

    @Override protected String getEntityName() { return "Reservation"; }
    @Override protected Long getId(Reservation e) { return e.getIdReservation(); }
    @Override protected void setFields(Reservation source, Reservation target) {
        BeanUtils.copyProperties(source, target, "idReservation");
    }
    @Override public boolean check(Reservation e, boolean isNew) {
        return e != null; // TODO: champs obligatoires
    }
}
