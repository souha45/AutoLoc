package tn.esprit.autoloc.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

@Service
public class MaintenanceServiceImpl extends GenericServiceImpl<Maintenance, Long> implements IMaintenanceService {
    public MaintenanceServiceImpl(IMaintenanceRepository repository) { super(repository); }

    @Override protected String getEntityName() { return "Maintenance"; }
    @Override protected Long getId(Maintenance e) { return e.getIdMaintenance(); }
    @Override protected void setFields(Maintenance source, Maintenance target) {
        BeanUtils.copyProperties(source, target, "idMaintenance");
    }
    @Override public boolean check(Maintenance e, boolean isNew) {
        return e != null; // TODO: champs obligatoires
    }
}
