package tn.esprit.autoloc.service;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.ArrayList;
import java.util.List;

@Primary
@Service
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long> implements IAgenceService {
    private final IEmployeService employeService;

    public AgenceServiceImpl(IAgenceRepository repository, IEmployeService employeService) {
        super(repository);
        this.employeService= employeService;
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
        return Utils.isNotEmpty(a.getNom()) && Utils.isNotEmpty(a.getTelephone());
    }

    @Override
    public boolean hasVehicules(Long id) {
        return false; // à compléter si l'énoncé le demande
    }


    @Override
    @Transactional
    public Agence updateAvecEmployes(Long id, Agence agence) {
        // 1. mettre à jour d'abord l'agence
        Agence saved = update(id, agence);

        // 2. insérer les employés via le service des employés
        List<Employe> nouveaux = new ArrayList<>(agence.getEmployes());
        for (Employe e : nouveaux) {
            saved.addEmploye(e);          // symétrie
            employeService.create(e);
        }

        // 3. mettre à jour l'effectif avec la taille de la liste
        saved.setEffectif(nouveaux.size());
        return repository.save(saved);
    }
}
