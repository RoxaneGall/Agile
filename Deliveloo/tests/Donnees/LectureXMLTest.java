package Donnees;

import Algo.Computations;
import Modeles.Demande;
import Modeles.Graphe;
import Modeles.Tournee;
import com.sothawo.mapjfx.Coordinate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LectureXMLTest {

    LectureXML lectureXML;

    @BeforeEach
    void setUp() {
        lectureXML = new LectureXML();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void chargerPlan() throws Exception {
        lectureXML.chargerPlan("../datas/GrandPlan.xml");
        System.out.println(Graphe.shared.getIntersectionMap().values().size());
        Demande demande = lectureXML.chargerDemande("../datas/demandeGrand7.xml");
        Tournee t = Computations.getTourneeFromDemande(demande);
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
        System.out.println(t.getTrajets().get(0).toString());
    }

    @Test
    void chargerDemande() {
    }

    @Test
    void getLimitesPlan() {
        ArrayList<Coordinate> testList = new ArrayList<>();
        testList = lectureXML.getLimitesPlan();
        System.out.println("test testList");
        int compteur = 1;
        for(Coordinate c : testList){
            System.out.println("Coordinate "+compteur);
            System.out.println("latitude : " + c.getLatitude());
            System.out.println("longitude : "+ c.getLongitude());
            compteur++;
        }
    }
}