package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.calcul.IStrategieTarif;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehiculeServiceImplTest {

    @Mock private IVehiculeRepository repository;
    @Mock private IAgenceRepository agenceRepository;
    @Mock private IEquipementRepository equipementRepository;
    @Mock private IStrategieTarif strategieStandard;
    @Mock private IStrategieTarif strategieWeekend;

    private final Map<String, IStrategieTarif> strategies = new HashMap<>();
    private VehiculeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new VehiculeServiceImpl(repository, strategies, agenceRepository, equipementRepository);
    }

    private Vehicule vehiculeValide() {
        Vehicule v = new Vehicule();
        v.setImmatriculation("123TU4567");
        v.setMarque("Toyota");
        v.setModele("Yaris");
        v.setCategorie(CategorieVehicule.SUV);
        v.setStatut(StatutVehicule.DISPONIBLE);
        v.setTarifJournalier(new BigDecimal("100"));
        return v;
    }

    // ---------- méthodes de base ----------

    @Test
    void getEntityName_getId() {
        Vehicule v = new Vehicule();
        v.setIdVehicule(2L);
        assertEquals("Vehicule", service.getEntityName());
        assertEquals(2L, service.getId(v));
    }

    @Test
    void setFields_copieLesChamps() {
        Vehicule source = vehiculeValide();
        Vehicule target = new Vehicule();

        service.setFields(source, target);

        assertEquals("123TU4567", target.getImmatriculation());
        assertEquals("Toyota", target.getMarque());
        assertEquals("Yaris", target.getModele());
        assertEquals(CategorieVehicule.SUV, target.getCategorie());
        assertEquals(StatutVehicule.DISPONIBLE, target.getStatut());
        assertEquals(new BigDecimal("100"), target.getTarifJournalier());
    }

    @Test
    void check_valide_retourneTrue() {
        assertTrue(service.check(vehiculeValide(), true));
    }

    @Test
    void check_null_retourneFalse() {
        assertFalse(service.check(null, true));
    }

    @Test
    void check_marqueVide_retourneFalse() {
        Vehicule v = vehiculeValide();
        v.setMarque("");
        assertFalse(service.check(v, true));
    }

    @Test
    void check_tarifNull_retourneFalse() {
        Vehicule v = vehiculeValide();
        v.setTarifJournalier(null);
        assertFalse(service.check(v, true));
    }

    @Test
    void check_statutNull_retourneFalse() {
        Vehicule v = vehiculeValide();
        v.setStatut(null);
        assertFalse(service.check(v, true));
    }

    // ---------- calculTarif ----------

    @Test
    void calculTarif_utiliseLaStrategieDeLOffre() {
        strategies.put("WEEKEND", strategieWeekend);
        when(repository.findById(1L)).thenReturn(Optional.of(vehiculeValide()));
        when(strategieWeekend.calculer(100.0, 3)).thenReturn(270.0);

        double result = service.calculTarif(1L, 3, VehiculeOffre.WEEKEND);

        assertEquals(270.0, result);
    }

    @Test
    void calculTarif_offreInconnue_utiliseStandard() {
        strategies.put("STANDARD", strategieStandard);   // pas de clé WEEKEND
        when(repository.findById(1L)).thenReturn(Optional.of(vehiculeValide()));
        when(strategieStandard.calculer(100.0, 3)).thenReturn(300.0);

        double result = service.calculTarif(1L, 3, VehiculeOffre.WEEKEND);

        assertEquals(300.0, result);
    }

    @Test
    void calculTarif_vehiculeAbsent_leveResourceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.calculTarif(1L, 3, VehiculeOffre.WEEKEND));
    }

    // ---------- affectations ----------

    @Test
    void desaffecte_retireLAgence() {
        Vehicule v = vehiculeValide();
        v.setAgence(new Agence());
        when(repository.findById(1L)).thenReturn(Optional.of(v));
        when(repository.save(any(Vehicule.class))).thenAnswer(inv -> inv.getArgument(0));

        Vehicule result = service.desaffecte(1L);

        assertNull(result.getAgence());
    }

    @Test
    void affecte_ok_associeLAgence() {
        Vehicule v = vehiculeValide();
        Agence a = new Agence();
        when(repository.findById(1L)).thenReturn(Optional.of(v));
        when(agenceRepository.findById(2L)).thenReturn(Optional.of(a));
        when(repository.save(any(Vehicule.class))).thenAnswer(inv -> inv.getArgument(0));

        Vehicule result = service.affecte(1L, 2L);

        assertSame(a, result.getAgence());
    }

    @Test
    void affecte_agenceAbsente_leveResourceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(vehiculeValide()));
        when(agenceRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.affecte(1L, 2L));
        verify(repository, never()).save(any());
    }

    @Test
    void affecteEquipement_ok_ajouteLeVehiculeAuxVehiculesDeLEquipement() {
        Vehicule v = vehiculeValide();
        Equipement eq = new Equipement();
        when(repository.findById(1L)).thenReturn(Optional.of(v));
        when(equipementRepository.findById(5L)).thenReturn(Optional.of(eq));

        Vehicule result = service.affecteEquipement(1L, 5L);

        assertSame(v, result);
        assertTrue(eq.getVehicules().contains(v));
    }

    @Test
    void affecteEquipement_equipementAbsent_leveResourceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(vehiculeValide()));
        when(equipementRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.affecteEquipement(1L, 5L));
    }
}