package tn.esprit.autoloc.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@Service
public class VehiculeServiceImpl extends GenericServiceImpl<Vehicule, Long> implements IVehiculeService {
    public VehiculeServiceImpl(IVehiculeRepository repository) { super(repository); }

    @Override protected String getEntityName() { return "Vehicule"; }
    @Override protected Long getId(Vehicule e) { return e.getIdVehicule(); }
    @Override protected void setFields(Vehicule source, Vehicule target) {
        BeanUtils.copyProperties(source, target, "idVehicule");
    }
    @Override public boolean check(Vehicule e, boolean isNew) {
        return e != null; // TODO: ex. immatriculation non vide
    }
}
