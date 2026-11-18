package tn.esprit.autoloc.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // L'annotation demandée par l'atelier
public class ConsoleRapportDecorator implements IRapport {

    private static final Logger LOG = LoggerFactory.getLogger(ConsoleRapportDecorator.class);

    private IRapport rapport;

    // Constructeur utilisé par Spring
    @Autowired
    public ConsoleRapportDecorator() {
    }

    // Constructeur pour envelopper un rapport
    public ConsoleRapportDecorator(IRapport rapport) {
        this.rapport = rapport;
    }

    @Override
    public String genererRapport() {
        if (rapport == null) {
            return "";
        }
        String contenu = rapport.genererRapport();

        // Ajout de la fonctionnalité (affichage console) via le logger
        LOG.info("=== AFFICHAGE CONSOLE DU RAPPORT ===");
        LOG.info("{}", contenu);
        LOG.info("====================================");

        // On retourne le contenu original pour que Postman l'affiche aussi
        return contenu;
    }
}