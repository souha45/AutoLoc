package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IEmployeRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeServiceImplTest {

    @Mock
    private IEmployeRepository repository;

    private EmployeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeServiceImpl(repository);
    }

    @Test
    void getEntityName_retourneEmploye() {
        assertEquals("Employe", service.getEntityName());
    }

    @Test
    void getId_retourneIdEmploye() {
        Employe e = new Employe();
        e.setIdEmploye(5L);
        assertEquals(5L, service.getId(e));
    }

    @Test
    void setFields_neEcrasePasLId() {
        Employe source = new Employe();
        source.setIdEmploye(1L);
        Employe target = new Employe();
        target.setIdEmploye(99L);

        service.setFields(source, target);

        assertEquals(99L, target.getIdEmploye());
    }

    @Test
    void check_nullRetourneFalse() {
        assertFalse(service.check(null, true));
    }

    @Test
    void check_objetNonNullRetourneTrue() {
        assertTrue(service.check(new Employe(), true));
    }
}