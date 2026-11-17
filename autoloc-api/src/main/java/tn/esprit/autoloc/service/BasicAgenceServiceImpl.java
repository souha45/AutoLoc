package tn.esprit.autoloc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAgenceServiceImpl implements IAgenceService{
    private final IAgenceRepository repository;
    private final IEmployeService employeService;
    @Override
    public boolean check(Agence agence, boolean isNew) {
        return Utils.isNotEmpty(agence.getNom()) && Utils.isNotEmpty(agence.getTelephone());
    }

    @Override
    public Agence create(Agence agence) {
        if (agence.getIdAgence()!= null){
            throw new IllegalArgumentException("Id agence doit etre null !!!");
        }
        if (!check(agence,true)){
            throw new IllegalArgumentException("Certains champs sont invalides!");
        }
        return repository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        return repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Agence", id.toString()));
    }

    @Override
    public List<Agence> findAll() {
        return List.of();
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence existing = findById(id);          // lève ResourceNotFoundException si introuvable
        if (!check(agence, false)) {
            throw new IllegalArgumentException("Certains champs sont invalides!");
        }
        existing.setNom(agence.getNom());
        existing.setTelephone(agence.getTelephone());
        existing.setAdresse(agence.getAdresse());
        existing.setVille(agence.getVille());
        existing.setEmployes(agence.getEmployes());
        existing.setVehicules(agence.getVehicules());
        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {         // pas de récupération des données
            throw new ResourceNotFoundException("Agence", id.toString());
        }
        repository.deleteById(id);
    }

    @Override
    public boolean hasVehicules(Long id) {
        return false;
    }

    @Override
    @Transactional
    public Agence updateAvecEmployes(Long id, Agence agence) {
        // 1. mettre à jour d'abord l'agence (champs simples uniquement)
        Agence existing = findById(id);
        if (!check(agence, false)) {
            throw new IllegalArgumentException("Certains champs sont invalides!");
        }
        existing.setNom(agence.getNom());
        existing.setTelephone(agence.getTelephone());
        existing.setAdresse(agence.getAdresse());
        existing.setVille(agence.getVille());
        Agence saved = repository.save(existing);

        // 2. insérer les employés avec le service des employés
        List<Employe> nouveaux = new ArrayList<>(agence.getEmployes());
        for (Employe e : nouveaux) {
            saved.addEmploye(e);          // relation symétrique
            employeService.create(e);
        }

        // 3. mettre à jour l'effectif
        saved.setEffectif(nouveaux.size());
        return repository.save(saved);
    }
}
