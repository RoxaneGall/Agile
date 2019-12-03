package Donnees;

import Algo.Computations;
import Modeles.*;
import com.sothawo.mapjfx.Coordinate;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;

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
        Graphe.shared.clearGraph();
        lectureXML.chargerPlan("../datas/petitPlan.xml");
        long idTest = 25611760;
        assertTrue(Graphe.shared.getIntersectionMap().containsKey(idTest));
        assertTrue(Graphe.shared.getIntersectionMap().size()==308);
    }

    @Test
    void chargerMoyenPlan_shouldSucceed() throws Exception {
        Graphe.shared.clearGraph();
        lectureXML.chargerPlan("../datas/moyenPlan.xml");
        long idTest = 2512682687L;
        assertTrue(Graphe.shared.getIntersectionMap().containsKey(idTest));
        assertTrue(Graphe.shared.getIntersectionMap().size()==1448);
    }
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
    void chargerGrandPlan_shouldSucceed() throws Exception {
        Graphe.shared.clearGraph();
        lectureXML.chargerPlan("../datas/grandPlan.xml");
        long idTest = 26576932;
        assertTrue(Graphe.shared.getIntersectionMap().containsKey(idTest));
        assertTrue(Graphe.shared.getIntersectionMap().size()==3736);
    }

    @Test
    void chargerDemandeGrand7_shouldSucceed() throws Exception {
        Demande testDemandeGrand7 = lectureXML.chargerDemande("../datas/demandeGrand7.xml");
        assertTrue((BooleanSupplier) testDemandeGrand7.getHeureDepart());
        assertTrue((BooleanSupplier) testDemandeGrand7.getEntrepot());
        assertTrue(Graphe.shared.getIntersectionMap().containsValue(testDemandeGrand7.getEntrepot()));
        assertTrue(testDemandeGrand7.getLivraisons().size()==7);
    }

    @Test
    void chargerDemandeMoyen3_shouldSucceed() throws Exception {
        Demande testDemandeMoyen3 = lectureXML.chargerDemande("../datas/demandeMoyen3.xml");
        assertTrue((BooleanSupplier) testDemandeMoyen3.getHeureDepart());
        assertTrue((BooleanSupplier) testDemandeMoyen3.getEntrepot());
        assertTrue(Graphe.shared.getIntersectionMap().containsValue(testDemandeMoyen3.getEntrepot()));
        assertTrue(testDemandeMoyen3.getLivraisons().size()==3);
    }

    @Test
    void chargerDemandePetit1_shouldSucceed() throws Exception {
        Demande testDemandePetit1 = lectureXML.chargerDemande("../datas/demandePetit1.xml");
        assertTrue((BooleanSupplier) testDemandePetit1.getHeureDepart());
        assertTrue((BooleanSupplier) testDemandePetit1.getEntrepot());
        assertTrue(Graphe.shared.getIntersectionMap().containsValue(testDemandePetit1.getEntrepot()));
        assertTrue(testDemandePetit1.getLivraisons().size()==1);
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
        try {
            lectureXML.chargerPlan("../datas/moyenPlan.xml");
            System.out.println(Graphe.shared.getIntersectionMap().values().size());
            Demande demande = lectureXML.chargerDemande("../datas/demandeGrand7.xml");
            Tournee t = Computations.getTourneeFromDemande(demande, Graphe.shared);
            System.out.println(t.getHeureArrivee());
            System.out.println(t.getDemande().getHeureDepart());
            System.out.println(t.getTotalDistance());
            System.out.println(t.getTotalDuration());
        }catch(Exception e){
            throw new NotImplementedException();
        }

    }


}