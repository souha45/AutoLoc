package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContratServiceImplTest {

    @Mock
    private IContratRepository repository;

    private ContratServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContratServiceImpl(repository);
    }

    @Test
    void getEntityName_retourneContrat() {
        assertEquals("Contrat", service.getEntityName());
    }

    @Test
    void getId_retourneIdContrat() {
        Contrat c = new Contrat();
        c.setIdContrat(5L);
        assertEquals(5L, service.getId(c));
    }

    @Test
    void setFields_copieSansEcraserLId() {
        Contrat source = new Contrat();
        source.setIdContrat(1L);
        Contrat target = new Contrat();
        target.setIdContrat(99L);

        service.setFields(source, target);

        assertEquals(99L, target.getIdContrat());
    }

    @Test
    void check_nullRetourneFalse() {
        assertFalse(service.check(null, true));
    }
}