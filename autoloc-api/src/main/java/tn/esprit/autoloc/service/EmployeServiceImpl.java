package tn.esprit.autoloc.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IEmployeRepository;

@Service
public class EmployeServiceImpl extends GenericServiceImpl<Employe, Long> implements IEmployeService {
    public EmployeServiceImpl(IEmployeRepository repository) { super(repository); }

    @Override protected String getEntityName() { return "Employe"; }
    @Override protected Long getId(Employe e) { return e.getIdEmploye(); }
    @Override protected void setFields(Employe source, Employe target) {
        BeanUtils.copyProperties(source, target, "idEmploye");
    }
    @Override public boolean check(Employe e, boolean isNew) {
        return e != null; // TODO: champs obligatoires
    }
}