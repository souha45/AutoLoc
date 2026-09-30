package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {
}


@SpringBootTest
public class AgenceTests {
    @Autowired
    private AgenceRepositoryMock agenceRepository;

    @Test
    void addAgence() {
        Agence agence = new Agence();
        agence.setNom("Agence ariana");
        agence.setAdresse("1 Rue Hedi");
        agence.setTelephone("71585874");
        agence.setVille("Tunis");

        Vehicule v1 = new Vehicule();
        v1.setImmatriculation("785414TU96");
        v1.setMarque("Isuzu");
        v1.setModele("DMax");
        v1.setCategorie(CategorieVehicule.SUV);
        v1.setStatut(StatutVehicule.MAINTENANCE);
        v1.setTarifJournalier(new BigDecimal("100"));

        Vehicule v2 = new Vehicule();
        v2.setImmatriculation("785414TU95");
        v2.setMarque("Toyota");
        v2.setModele("Yaris");
        v2.setCategorie(CategorieVehicule.UTILITAIRE);
        v2.setStatut(StatutVehicule.DISPONIBLE);
        v2.setTarifJournalier(new BigDecimal("80"));

        agence.addVehicule(v1);
        agence.addVehicule(v2);

        agenceRepository.save(agence);
    }

    @Test
    void loadAgence() {
        StringBuilder sb = new StringBuilder("\n");

        for (Agence agence : agenceRepository.findAll()) {
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
                        .append("|")
                        .append(v.getImmatriculation())
                        .append("\n");
            }
        }

        fail(sb.toString()); // assertion qui échoue toujours : affiche le résultat
    }
}
