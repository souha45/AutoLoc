package tn.esprit.autoloc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.ClientServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private IClientRepository repository;

    private ClientServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ClientServiceImpl(repository);
    }

    @Test
    void getEntityName_retourneClient() {
        assertEquals("Client", service.getEntityName());
    }

    @Test
    void getId_retourneIdClient() {
        Client c = new Client();
        c.setIdClient(3L);
        assertEquals(3L, service.getId(c));
    }

    @Test
    void setFields_copieLeNom() {
        Client source = new Client();
        source.setNom("Ali");
        Client target = new Client();

        service.setFields(source, target);

        assertEquals("Ali", target.getNom());
    }

    @Test
    void check_nomRenseigneRetourneTrue() {
        Client c = new Client();
        c.setNom("Ali");
        assertTrue(service.check(c, true));
    }

    @Test
    void check_nomVideOuNullRetourneFalse() {
        Client vide = new Client();
        vide.setNom("   ");
        assertFalse(service.check(vide, true));
        assertFalse(service.check(new Client(), true));
    }
}