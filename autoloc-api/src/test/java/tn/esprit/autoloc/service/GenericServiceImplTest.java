package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenericServiceImplTest {

    // Entité factice, indépendante du domaine
    static class FakeEntity {
        private Long id;
        private String name;

        FakeEntity(Long id, String name) {
            this.id = id;
            this.name = name;
        }
        Long getId() { return id; }
        String getName() { return name; }
        void setName(String name) { this.name = name; }
    }

    // Service concret minimal pour tester la classe abstraite
    static class FakeService extends GenericServiceImpl<FakeEntity, Long> {
        boolean valid = true;

        FakeService(JpaRepository<FakeEntity, Long> repository) {
            super(repository);
        }

        @Override protected String getEntityName() { return "Fake"; }
        @Override protected Long getId(FakeEntity e) { return e.getId(); }
        @Override protected void setFields(FakeEntity source, FakeEntity target) {
            target.setName(source.getName());
        }
        @Override public boolean check(FakeEntity e, boolean isNew) { return valid; }
    }

    @Mock
    private JpaRepository<FakeEntity, Long> repository;

    private FakeService service;

    @BeforeEach
    void setUp() {
        service = new FakeService(repository);
    }

    // ---------- create ----------

    @Test
    void create_ok_sauvegardeEtRetourne() {
        FakeEntity e = new FakeEntity(null, "A");
        when(repository.save(e)).thenReturn(e);

        FakeEntity result = service.create(e);

        assertSame(e, result);
        verify(repository).save(e);
    }

    @Test
    void create_idNonNull_leveIllegalArgument() {
        FakeEntity e = new FakeEntity(1L, "A");

        assertThrows(IllegalArgumentException.class, () -> service.create(e));
        verify(repository, never()).save(any());
    }

    @Test
    void create_checkFalse_leveIllegalArgument() {
        service.valid = false;
        FakeEntity e = new FakeEntity(null, "A");

        assertThrows(IllegalArgumentException.class, () -> service.create(e));
        verify(repository, never()).save(any());
    }

    // ---------- findById / findAll ----------

    @Test
    void findById_existant_retourneEntite() {
        FakeEntity e = new FakeEntity(1L, "A");
        when(repository.findById(1L)).thenReturn(Optional.of(e));

        assertSame(e, service.findById(1L));
    }

    @Test
    void findById_absent_leveResourceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void findAll_retourneLaListe() {
        List<FakeEntity> list = List.of(new FakeEntity(1L, "A"), new FakeEntity(2L, "B"));
        when(repository.findAll()).thenReturn(list);

        assertEquals(2, service.findAll().size());
    }

    // ---------- update ----------

    @Test
    void update_ok_copieLesChampsEtSauvegarde() {
        FakeEntity existing = new FakeEntity(1L, "Ancien");
        FakeEntity nouveau = new FakeEntity(null, "Nouveau");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        FakeEntity result = service.update(1L, nouveau);

        assertEquals("Nouveau", result.getName());
        assertEquals(1L, result.getId());
        verify(repository).save(existing);
    }

    @Test
    void update_checkFalse_leveIllegalArgument() {
        service.valid = false;
        when(repository.findById(1L)).thenReturn(Optional.of(new FakeEntity(1L, "A")));

        FakeEntity nouveau = new FakeEntity(null, "B");
        assertThrows(IllegalArgumentException.class, () -> service.update(1L, nouveau));
        verify(repository, never()).save(any());
    }

    @Test
    void update_idAbsent_leveResourceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        FakeEntity nouveau = new FakeEntity(null, "B");
        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, nouveau));
        verify(repository, never()).save(any());
    }

    // ---------- deleteById ----------

    @Test
    void deleteById_existant_supprime() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteById_absent_leveResourceNotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(1L));
        verify(repository, never()).deleteById(any());
    }
}