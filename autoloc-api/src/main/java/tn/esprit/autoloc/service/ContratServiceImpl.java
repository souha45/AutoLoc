package tn.esprit.autoloc.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;

@Service
public class ContratServiceImpl extends GenericServiceImpl<Contrat, Long> implements IContratService {
    public ContratServiceImpl(IContratRepository repository) { super(repository); }

    @Override protected String getEntityName() { return "Contrat"; }
    @Override protected Long getId(Contrat e) { return e.getIdContrat(); }
    @Override protected void setFields(Contrat source, Contrat target) {
        BeanUtils.copyProperties(source, target, "idContrat");
    }
    @Override public boolean check(Contrat e, boolean isNew) {
        return e != null; // TODO: champs obligatoires
    }
}
