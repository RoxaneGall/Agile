package Algo;

import Donnees.EcritureXML;
import Donnees.LectureXML;
import Modeles.Demande;
import Modeles.Graphe;
import Modeles.Tournee;
import Service.Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TSP1Test {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test

    void realiserTourneeDepuisGrandPlanEt15Livraisons_shouldSucceed() throws Exception {
        Service service = new Service();
        service.chargerPlan("datas/grandPlan.xml");
        Demande demande = service.chargerDemande("datas/demandeTest5.xml");
        service.calculerTournee(demande);
        Tournee t = service.recupererTournee();
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
        System.out.println((new EcritureXML()).genererInstructionsPourTournee(t));
    }
    /*void realiserTourneeDepuisGrandPlanEt12Livraisons_shouldSucceed() throws Exception {
        Service service = new Service();
        service.chargerPlan("datas/grandPlan.xml");
        Demande demande = service.chargerDemande("datas/demandeTest4.xml");
        service.calculerTournee(demande);
        Tournee t = service.recupererTournee();
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
        System.out.println((new EcritureXML()).genererInstructionsPourTournee(t));
    }*/
    /*void realiserTourneeDepuisGrandPlanEt10Livraisons_shouldSucceed() throws Exception {
        Service service = new Service();
        service.chargerPlan("datas/grandPlan.xml");
        Demande demande = service.chargerDemande("datas/demandeTest3.xml");
        service.calculerTournee(demande);
        Tournee t = service.recupererTournee();
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
        System.out.println((new EcritureXML()).genererInstructionsPourTournee(t));
    }*/
    /*void realiserTourneeDepuisPetitPlanEtDeuxLivraisonsMemeEndroit_shouldSucceed() throws Exception {
        Service service = new Service();
        service.chargerPlan("datas/petitPlan.xml");
        Demande demande = service.chargerDemande("datas/demandeTest2.xml");
        service.calculerTournee(demande);
        Tournee t = service.recupererTournee();
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
        System.out.println((new EcritureXML()).genererInstructionsPourTournee(t));
    }*/

    /*void realiserTourneeDepuisPetitPlanEtSamePUEtD_shouldSucceed() throws Exception {
        Service service = new Service();
        service.chargerPlan("datas/petitPlan.xml");
        Demande demande = service.chargerDemande("datas/demandeTestSamePUEtD.xml");
        service.calculerTournee(demande);
        Tournee t = service.recupererTournee();
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
        System.out.println((new EcritureXML()).genererInstructionsPourTournee(t));
    }*/

    /*void realiserTourneeDepuisMoyenPlanEtMoyenDemande_shouldSucceed() throws Exception {
        Service service = new Service();
        service.chargerPlan("datas/moyenPlan.xml");
        Demande demande = service.chargerDemande("datas/demandeMoyen3.xml");
        service.calculerTournee(demande);
        Tournee t = service.recupererTournee();
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
        System.out.println((new EcritureXML()).genererInstructionsPourTournee(t));
    }*/

    /*void realiserTourneeDepuisPetitPlanEtPetiteDemande_shouldSucceed() throws Exception {
        Service service = new Service();
        service.chargerPlan("datas/petitPlan.xml");
        Demande demande = service.chargerDemande("datas/demandePetit2.xml");
        service.calculerTournee(demande);
        Tournee t = service.recupererTournee();
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
        System.out.println((new EcritureXML()).genererInstructionsPourTournee(t));
    }*/
}