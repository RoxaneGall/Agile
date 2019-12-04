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
    void setUp() throws Exception {
        lectureXML = new LectureXML();
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void chargerFichierNonXML_shouldThrowException() throws Exception {
        Graphe.shared.clearGraph();
        try {
            lectureXML.chargerPlan("../datas/petitPlan");
            fail("The test should throw an exception because the file is not an XML file.");
        }catch(Exception e){}
    }

    @Test
    void chargerFichierInexistant_shouldThrowException() throws Exception {
        Graphe.shared.clearGraph();
        try {
            lectureXML.chargerPlan("../datas/fichierInexistant");
            fail("The test should throw an exception because the file does not exist.");
        }catch(Exception e){}
    }

    @Test
    void chargerPetitPlan_shouldLoadMap() throws Exception {
        Graphe.shared.clearGraph();
        lectureXML.chargerPlan("../datas/petitPlan.xml");
        long idTest = 25611760;
        assertTrue(Graphe.shared.getIntersectionMap().containsKey(idTest));
        assertTrue(Graphe.shared.getIntersectionMap().size()==308);
    }

    @Test
    void chargerMoyenPlan_shouldLoadMap() throws Exception {
        Graphe.shared.clearGraph();
        lectureXML.chargerPlan("../datas/moyenPlan.xml");
        long idTest = 2512682687L;
        assertTrue(Graphe.shared.getIntersectionMap().containsKey(idTest));
        assertTrue(Graphe.shared.getIntersectionMap().size()==1448);
    }

    @Test
    void chargerGrandPlan_shouldLoadMap() throws Exception {
        Graphe.shared.clearGraph();
        lectureXML.chargerPlan("../datas/grandPlan.xml");
        long idTest = 26576932;
        assertTrue(Graphe.shared.getIntersectionMap().containsKey(idTest));
        assertTrue(Graphe.shared.getIntersectionMap().size()==3736);
    }

    @Test
    void chargerDemandeGrand7_shouldCreateDemand() throws Exception {
        Graphe.shared.clearGraph();
        lectureXML.chargerPlan("../datas/petitPlan.xml");
        lectureXML.chargerPlan("../datas/moyenPlan.xml");
        lectureXML.chargerPlan("../datas/grandPlan.xml");
        Demande testDemandeGrand7 = lectureXML.chargerDemande("../datas/demandeGrand7.xml");
        assertTrue(testDemandeGrand7.getLivraisons().size()==7);
    }

    @Test
    void chargerDemandeMoyen3_shouldCreateDemand() throws Exception {
        Graphe.shared.clearGraph();
        lectureXML.chargerPlan("../datas/petitPlan.xml");
        lectureXML.chargerPlan("../datas/moyenPlan.xml");
        lectureXML.chargerPlan("../datas/grandPlan.xml");
        Demande testDemandeMoyen3 = lectureXML.chargerDemande("../datas/demandeMoyen3.xml");
        assertTrue(testDemandeMoyen3.getLivraisons().size()==3);
    }

    @Test
    void chargerDemandePetit1_shouldCreateDemand() throws Exception {
        Graphe.shared.clearGraph();
        lectureXML.chargerPlan("../datas/petitPlan.xml");
        lectureXML.chargerPlan("../datas/moyenPlan.xml");
        lectureXML.chargerPlan("../datas/grandPlan.xml");
        Demande testDemandePetit1 = lectureXML.chargerDemande("../datas/demandePetit1.xml");
        assertTrue(testDemandePetit1.getLivraisons().size()==1);
    }

    @Test
    void getLimitesPlan_shouldSucceed() throws Exception {
        ArrayList<Coordinate> testList = new ArrayList<>();
        testList = lectureXML.getLimitesPlan();
        int compteur = 1;
        for(Coordinate c : testList){
            System.out.println("Coordinate "+compteur);
            System.out.println("latitude : " + c.getLatitude());
            System.out.println("longitude : "+ c.getLongitude());
            compteur++;
        }
        assertTrue(!testList.equals(null));
    }
}