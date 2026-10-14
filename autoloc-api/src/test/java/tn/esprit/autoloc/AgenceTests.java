package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.IAgenceService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Repository
interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {
}

@SpringBootTest
public class AgenceTests {

    @Autowired
    private IAgenceService agenceService;

    @Autowired
    private AgenceRepositoryMock basicAgenceRepository;

    @Autowired
    private IAgenceRepository fullAgenceRepository;

    private void addAgence(CrudRepository<Agence, Long> repository) {

        int ms = (int) System.currentTimeMillis();

        Agence agence = new Agence();
        agence.setNom("Agence ariana");
        agence.setAdresse("1 Rue Hedi");
        agence.setTelephone("71585874");
        agence.setVille("Tunis");

        Vehicule v1 = new Vehicule();
        v1.setImmatriculation("785414TU96" + ms);
        v1.setMarque("Isuzu");
        v1.setModele("DMax");
        v1.setCategorie(CategorieVehicule.SUV);
        v1.setStatut(StatutVehicule.MAINTENANCE);
        v1.setTarifJournalier(new BigDecimal("100"));

        Vehicule v2 = new Vehicule();
        v2.setImmatriculation("785414TU95" + ms);
        v2.setMarque("Toyota");
        v2.setModele("Yaris");
        v2.setCategorie(CategorieVehicule.UTILITAIRE);
        v2.setStatut(StatutVehicule.DISPONIBLE);
        v2.setTarifJournalier(new BigDecimal("80"));

        agence.addVehicule(v1);
        agence.addVehicule(v2);

        repository.save(agence);
    }

    @Test
    public void fullAddAgence() {
        addAgence(fullAgenceRepository);
    }

    @Test
    public void basicAddAgence() {
        addAgence(basicAgenceRepository);
    }

    private void loadAgence(CrudRepository<Agence, Long> repository) {

        StringBuilder sb = new StringBuilder("\n");

        sb.append("Type: ")
                .append(repository.getClass().getSimpleName())
                .append("\n");

        for (Agence agence : repository.findAll()) {

            sb.append(agence.getIdAgence())
                    .append(" | ")
                    .append(agence.getNom())
                    .append("\n");

            sb.append("Vehicules Count : ")
                    .append(agence.getVehicules().size())
                    .append("\n");

            for (Vehicule v : agence.getVehicules()) {

                sb.append("=== ")
                        .append(v.getIdVehicule())
                        .append(" | ")
                        .append(v.getImmatriculation())
                        .append("\n");
            }
        }

        fail(sb.toString());
    }

    @Test
    public void fullLoadAddAgence() {
        loadAgence(fullAgenceRepository);
    }

    @Test
    public void basicLoadAddAgence() {
        loadAgence(basicAgenceRepository);
    }

    @Test
    public void loadSortedAgences(){
        StringBuilder sb = new StringBuilder("\n");

        var sort= Sort.by("idAgence").descending();

        for (Agence agence : fullAgenceRepository.findAll(sort)) {

            sb.append(agence.getIdAgence())
                    .append(" | ")
                    .append(agence.getNom())
                    .append("\n");

            sb.append("Vehicules Count : ")
                    .append(agence.getVehicules().size())
                    .append("\n");

            for (Vehicule v : agence.getVehicules()) {

                sb.append("=== ")
                        .append(v.getIdVehicule())
                        .append(" | ")
                        .append(v.getImmatriculation())
                        .append("\n");
            }
        }

        fail(sb.toString());

    }

    @Test
    public void loadPagedAgences() {

        StringBuilder sb = new StringBuilder("\n");

        var sort = Sort.by("idAgence").descending();
        var pageRequest = PageRequest.of(0, 2, sort);
        Page<Agence> page = fullAgenceRepository.findAll(pageRequest);

        sb.append("Total pages : ").append(page.getTotalPages()).append("\n");

        do {
            sb.append("Page en cours : ").append(page.getNumber()).append("\n");

            for (Agence agence : page.getContent()) {

                sb.append(agence.getIdAgence())
                        .append(" | ")
                        .append(agence.getNom())
                        .append("\n");

                sb.append("Vehicules Count : ")
                        .append(agence.getVehicules().size())
                        .append("\n");

                for (Vehicule v : agence.getVehicules()) {
                    sb.append("=== ")
                            .append(v.getIdVehicule())
                            .append(" | ")
                            .append(v.getImmatriculation())
                            .append("\n");
                }
            }

            if (!page.hasNext()) {
                break;
            }
            page = fullAgenceRepository.findAll(page.nextPageable());

        } while (true);

        fail(sb.toString());
    }

    @Test
    void createAgence() {
        Agence agence = new Agence();
        agence.setNom("Agence Tunis");
        agence.setTelephone("71123456");
        agence.setAdresse("1 Rue Hedi");
        agence.setVille("Tunis");

        Agence saved = agenceService.create(agence);

        assertNotNull(saved);
        assertNotNull(saved.getIdAgence());   // l'id doit avoir été généré
        assertEquals("Agence Tunis", saved.getNom());
    }
}
