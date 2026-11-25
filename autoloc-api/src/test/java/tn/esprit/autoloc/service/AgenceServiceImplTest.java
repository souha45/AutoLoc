package tn.esprit.autoloc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.AgenceServiceImpl;
import tn.esprit.autoloc.service.IEmployeService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgenceServiceImplTest {

    @Mock
    private IAgenceRepository repository;

    @Mock
    private IEmployeService employeService;

    private AgenceServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AgenceServiceImpl(repository, employeService);
    }

    private Agence agenceValide() {
        Agence a = new Agence();
        a.setNom("Agence Tunis");
        a.setTelephone("71123456");
        a.setVille("Tunis");
        a.setAdresse("1 Rue Hedi");
        return a;
    }

    @Test
    void getEntityName_getId() {
        Agence a = new Agence();
        a.setIdAgence(4L);
        assertEquals("Agence", service.getEntityName());
        assertEquals(4L, service.getId(a));
    }

    @Test
    void setFields_copieLesQuatreChamps() {
        Agence source = agenceValide();
        Agence target = new Agence();

        service.setFields(source, target);

        assertEquals("Agence Tunis", target.getNom());
        assertEquals("71123456", target.getTelephone());
        assertEquals("Tunis", target.getVille());
        assertEquals("1 Rue Hedi", target.getAdresse());
    }

    @Test
    void check_valide_retourneTrue() {
        assertTrue(service.check(agenceValide(), true));
    }

    @Test
    void check_nomVide_retourneFalse() {
        Agence a = agenceValide();
        a.setNom("");
        assertFalse(service.check(a, true));
    }

    @Test
    void check_telephoneNull_retourneFalse() {
        Agence a = agenceValide();
        a.setTelephone(null);
        assertFalse(service.check(a, true));
    }

    @Test
    void hasVehicules_retourneFalse() {
        assertFalse(service.hasVehicules(1L));
    }

    @Test
    void updateAvecEmployes_creeLesEmployesEtMetAJourLEffectif() {
        Agence existante = agenceValide();
        existante.setIdAgence(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existante));
        when(repository.save(any(Agence.class))).thenAnswer(inv -> inv.getArgument(0));

        Agence nouvelle = agenceValide();
        nouvelle.setNom("Agence Sfax");
        Employe e1 = new Employe();
        e1.setIdEmploye(10L);
        Employe e2 = new Employe();
        e2.setIdEmploye(11L);
        nouvelle.addEmploye(e1);
        nouvelle.addEmploye(e2);

        Agence result = service.updateAvecEmployes(1L, nouvelle);

        assertEquals("Agence Sfax", result.getNom());
        assertEquals(2, result.getEffectif());
        verify(employeService).create(e1);
        verify(employeService).create(e2);
    }

    @Test
    void updateAvecEmployes_agenceAbsente_leveResourceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Agence a = agenceValide();

        assertThrows(ResourceNotFoundException.class, () -> service.updateAvecEmployes(1L, a));
        verifyNoInteractions(employeService);
    }
}