package Donnees;

import Modeles.Demande;
import Modeles.Intersection;
import Modeles.Tournee;
import Service.Service;
import com.sothawo.mapjfx.Coordinate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.fail;

public class EcritureXMLTest {
    EcritureXML ecritureXML;

    @BeforeEach
    void setUp() {
        ecritureXML = new EcritureXML();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void genererInstructionsPourTournee_shouldReturnNonEmptyString() throws Exception {
        Coordinate c = new Coordinate(45.7438, 4.893318);
        Intersection i = new Intersection(25610888, c);
        SimpleDateFormat formatter = new SimpleDateFormat("H:m:s");
        Date date = formatter.parse("8:0:0");
        Demande d = new Demande(i, date, "nomDemande");
        Service service = new Service();
        service.calculerTournee(d);
        Tournee t = service.recupererTournee();
        String instructions = "";
        instructions = ecritureXML.genererInstructionsPourTournee(t);
        assert (!instructions.equals(""));
    }

    @Test
    void ecrireFichier_shouldCreateANewFile() throws Exception {
        //tester avec une autre tournee, qui a du vrai contenu

        Coordinate c = new Coordinate(45.7438, 4.893318);
        Intersection i = new Intersection(25610888, c);
        SimpleDateFormat formatter = new SimpleDateFormat("H:m:s");
        Date date = formatter.parse("8:0:0");
        Demande d = new Demande(i, date, "nomDemande");
        Service service = new Service();
        service.calculerTournee(d);
        Tournee t = service.recupererTournee();
        try {
            ecritureXML.ecrireFichier(t, "../datas/testNouveauFichier");
        } catch (Exception e) {
        }
    }

    @Test
    void ecrireFichierCheminInexistant_shouldThrowException() throws Exception {
        Coordinate c = new Coordinate(45.7438, 4.893318);
        Intersection i = new Intersection(25610888, c);
        SimpleDateFormat formatter = new SimpleDateFormat("H:m:s");
        Date date = formatter.parse("8:0:0");
        Demande d = new Demande(i, date, "nomDemande");
        Service service = new Service();
        service.calculerTournee(d);
        Tournee t = service.recupererTournee();
        try {
            ecritureXML.ecrireFichier(t, "../datas/feuillesderoute/cheminInexistant");
            fail("Le test doit envoyer une exception car le chemin du fichier n'existe pas.");
        } catch (Exception e) {
        }
    }

    @Test
    void ecrireFichierExistant_shouldThrowException() throws Exception {
        Coordinate c = new Coordinate(45.7438, 4.893318);
        Intersection i = new Intersection(25610888, c);
        SimpleDateFormat formatter = new SimpleDateFormat("H:m:s");
        Date date = formatter.parse("8:0:0");
        Demande d = new Demande(i, date, "nomDemande");
        Service service = new Service();
        service.calculerTournee(d);
        Tournee t = service.recupererTournee();
        try {
            ecritureXML.ecrireFichier(t, "../datas/testFichierExistant");
            ecritureXML.ecrireFichier(t, "../datas/testFichierExistant");
            fail("Le test doit envoyer une exception car le fichier existe déjà.");
        } catch (Exception e) {
        }
    }
}
