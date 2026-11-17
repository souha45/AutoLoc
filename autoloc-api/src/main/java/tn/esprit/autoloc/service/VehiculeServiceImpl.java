package tn.esprit.autoloc.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.IStrategieTarif; // Import ajouté
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.Map; // Import ajouté

@Service
public class VehiculeServiceImpl extends GenericServiceImpl<Vehicule, Long> implements IVehiculeService {

    // 1. Injection de la Map des stratégies par Spring
    private final Map<String, IStrategieTarif> strategies;

    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;

    // 2. Modification du constructeur
    public VehiculeServiceImpl(IVehiculeRepository repository, Map<String, IStrategieTarif> strategies ,IAgenceRepository agenceRepository, IEquipementRepository equipementRepository) {
        super(repository);
        this.strategies = strategies;
        this.agenceRepository=agenceRepository;
        this.equipementRepository=equipementRepository;
    }

    @Override
    protected String getEntityName() {
        return "Vehicule";
    }

    @Override
    protected Long getId(Vehicule v) {
        return v.getIdVehicule();
    }

    @Override
    protected void setFields(Vehicule source, Vehicule target) {
        target.setImmatriculation(source.getImmatriculation());
        target.setMarque(source.getMarque());
        target.setModele(source.getModele());
        target.setCategorie(source.getCategorie());
        target.setTarifJournalier(source.getTarifJournalier());
        target.setStatut(source.getStatut());
    }

    @Override
    public boolean check(Vehicule v, boolean isNew) {
        return v != null
                && Utils.isNotEmpty(v.getImmatriculation())
                && Utils.isNotEmpty(v.getMarque())
                && Utils.isNotEmpty(v.getModele())
                && v.getCategorie() != null
                && v.getStatut() != null
                && v.getTarifJournalier() != null;
    }

    // --- LA NOUVELLE MÉTHODE SANS SWITCH ---
    @Override
    public double calculTarif(Long idVehicule, int nombreJours, VehiculeOffre offre) {
        // 1. Récupérer le véhicule
        Vehicule vehicule = this.findById(idVehicule);

        // 2. Récupérer la stratégie correspondant à l'offre (ex: "WEEKEND")
        IStrategieTarif strategie = strategies.get(offre.name());

        // Si l'offre n'est pas trouvée, on prend STANDARD par sécurité
        if (strategie == null) {
            strategie = strategies.get("STANDARD");
        }

        // 3. Déléguer le calcul à la stratégie
        return strategie.calculer(vehicule.getTarifJournalier().doubleValue(), nombreJours);
    }

    @Override
    public Vehicule desaffecte(Long id){
        var vehicule =findById(id);

        vehicule.setAgence(null);
        return repository.save(vehicule);
    }

    @Override
    public Vehicule affecte(Long id, Long idAgence) {
        var vehicule =findById(id);
        var agence =agenceRepository.findById(idAgence).orElseThrow( ()-> new ResourceNotFoundException("Agence", idAgence.toString()));

        vehicule.setAgence(agence);
        return repository.save(vehicule);
    }

    @Override
    @Transactional
    public Vehicule affecteEquipement(Long id, Long idEquipement) {
        var vehicule = findById(id);
        var equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", idEquipement.toString()));

        equipement.getVehicules().add(vehicule);    // côté propriétaire : persisté
        return vehicule;
    }
}