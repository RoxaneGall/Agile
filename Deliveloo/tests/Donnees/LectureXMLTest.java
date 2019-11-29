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
    void chargerPetitPlan_shouldSucceed() throws Exception {
        lectureXML.chargerPlan("../datas.petitPlan.xml");
        /* à continuer*/
    }

    @Test
    void chargerDemande_shouldSucceed() throws Exception {
        Demande testGrandeDemande = lectureXML.chargerDemande("../datas/demandeGrand7.xml");
        /* à continuer*/
    }

    @Test
    void getLimitesPlan_shouldSucceed() {
        ArrayList<Coordinate> testList = new ArrayList<>();
        testList = lectureXML.getLimitesPlan();
        int compteur = 1;
        for(Coordinate c : testList){
            System.out.println("Coordinate "+compteur);
            System.out.println("latitude : " + c.getLatitude());
            System.out.println("longitude : "+ c.getLongitude());
            compteur++;
        }
    }

    @Test
    void realiserTourneeDepuisPetitPlanEtPetiteDemande_shouldSucceed() throws Exception {
        lectureXML.chargerPlan("../datas/petitPlan.xml");
        System.out.println(Graphe.shared.getIntersectionMap().values().size());
        Demande demande = lectureXML.chargerDemande("../datas/demandePetit1.xml");
        Tournee t = Computations.getTourneeFromDemande(demande,Graphe.shared);
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
    }

    @Test
    void realiserTourneeDepuisMoyenPlanEtPetiteDemande_shouldSucceed() throws Exception {
        lectureXML.chargerPlan("../datas/moyenPlan.xml");
        System.out.println(Graphe.shared.getIntersectionMap().values().size());
        Demande demande = lectureXML.chargerDemande("../datas/demandePetit1.xml");
        Tournee t = Computations.getTourneeFromDemande(demande,Graphe.shared);
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
    }

    @Test
    void realiserTourneeDepuisGrandPlanEtPetiteDemande_shouldSucceed() throws Exception {
        lectureXML.chargerPlan("../datas/grandPlan.xml");
        System.out.println(Graphe.shared.getIntersectionMap().values().size());
        Demande demande = lectureXML.chargerDemande("../datas/demandePetit1.xml");
        Tournee t = Computations.getTourneeFromDemande(demande,Graphe.shared);
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
    }

    @Test
    void realiserTourneeDepuisMoyenPlanEtMoyenneDemande_shouldSucceed() throws Exception {
        lectureXML.chargerPlan("../datas/moyenPlan.xml");
        System.out.println(Graphe.shared.getIntersectionMap().values().size());
        Demande demande = lectureXML.chargerDemande("../datas/demandeMoyen3.xml");
        Tournee t = Computations.getTourneeFromDemande(demande,Graphe.shared);
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
    }

    @Test
    void realiserTourneeDepuisGrandPlanEtGrandeDemande_shouldSucceed() throws Exception {
        System.out.println("test LectureXMLTest.testTournee");
        lectureXML.chargerPlan("../datas/grandPlan.xml");
        System.out.println(Graphe.shared.getIntersectionMap().values().size());
        Demande demande = lectureXML.chargerDemande("../datas/demandeGrand7.xml");
        Tournee t = Computations.getTourneeFromDemande(demande,Graphe.shared);
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
    }

    @Test
    void realiserTourneeDepuisPetitPlanEtMoyenneDemande_shouldFail() throws Exception {
        System.out.println("test LectureXMLTest.testTournee");
        lectureXML.chargerPlan("../datas/petitPlan.xml");
        System.out.println(Graphe.shared.getIntersectionMap().values().size());
        Demande demande = lectureXML.chargerDemande("../datas/demandeMoyen3.xml");
        Tournee t = Computations.getTourneeFromDemande(demande,Graphe.shared);
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
    }

    @Test
    void realiserTourneeDepuisPetitPlanEtGrandeDemande_shouldFail() throws Exception {
        System.out.println("test LectureXMLTest.testTournee");
        lectureXML.chargerPlan("../datas/petitPlan.xml");
        System.out.println(Graphe.shared.getIntersectionMap().values().size());
        Demande demande = lectureXML.chargerDemande("../datas/demandeGrand7.xml");
        Tournee t = Computations.getTourneeFromDemande(demande,Graphe.shared);
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
    }

    @Test
    void realiserTourneeDepuisMoyenPlanEtGrandeDemande_shouldFail() throws Exception {
        System.out.println("test LectureXMLTest.testTournee");
        lectureXML.chargerPlan("../datas/moyenPlan.xml");
        System.out.println(Graphe.shared.getIntersectionMap().values().size());
        Demande demande = lectureXML.chargerDemande("../datas/demandeGrand7.xml");
        Tournee t = Computations.getTourneeFromDemande(demande,Graphe.shared);
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
    }


}