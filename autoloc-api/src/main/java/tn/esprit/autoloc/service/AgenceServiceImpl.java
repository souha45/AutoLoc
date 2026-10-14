package tn.esprit.autoloc.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;

@Primary
@Service
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long> implements IAgenceService {

    public AgenceServiceImpl(IAgenceRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() { return "Agence"; }

    @Override
    protected Long getId(Agence a) { return a.getIdAgence(); }

    @Override
    protected void setFields(Agence source, Agence target) {
        target.setNom(source.getNom());
        target.setTelephone(source.getTelephone());
        target.setVille(source.getVille());
        target.setAdresse(source.getAdresse());
    }

    @Override
    public boolean check(Agence a, boolean isNew) {
        return Utils.isNoEmpty(a.getNom()) && Utils.isNoEmpty(a.getTelephone());
    }

    @Override
    public boolean hasVehicules(Long id) {
        return false; // à compléter si l'énoncé le demande
    }
}
