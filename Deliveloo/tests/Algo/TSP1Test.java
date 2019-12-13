package Algo;

import Donnees.EcritureXML;
import Modeles.Demande;
import Modeles.Tournee;
import Service.Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.time.Duration.*;
import static org.junit.jupiter.api.Assertions.assertTimeout;

class TSP1Test {


    @Test
    void petitPlanEtPetiteDemande_timeoutNotExceeded1second()
    {
        //The following assertion succeeds.
        assertTimeout(ofSeconds(1), () -> {
            Service service = new Service();
            service.chargerPlan("../datas/petitPlan.xml");
            Demande demande = service.chargerDemande("../datas/demandePetit2.xml");
            service.calculerTournee(demande);
            Tournee t = service.recupererTournee();
        });
    }

    @Test
    void moyenPlanEtMoyenneDemande_timeoutNotExceeded1second()
    {
        //The following assertion succeeds.
        assertTimeout(ofMillis(1500), () -> {
            Service service = new Service();
            service.chargerPlan("../datas/moyenPlan.xml");
            Demande demande = service.chargerDemande("../datas/demandeMoyen3.xml");
            service.calculerTournee(demande);
            Tournee t = service.recupererTournee();
        });
    }

    @Test
    void grandPlanEtGrandeDemande_timeoutNotExceeded1second()
    {
        //The following assertion succeeds.
        assertTimeout(ofSeconds(5), () -> {
            Service service = new Service();
            service.chargerPlan("../datas/grandPlan.xml");
            Demande demande = service.chargerDemande("../datas/demandeGrand9.xml");
            service.calculerTournee(demande);
            Tournee t = service.recupererTournee();
        });
    }

    @Test
    void realiserTourneeDepuisGrandPlanEt15Livraisons_shouldSucceed(){
        assertTimeout(ofMinutes(5), () -> {
            Service service = new Service();
            service.chargerPlan("../datas/grandPlan.xml");
            Demande demande = service.chargerDemande("../datas/demandeTest5.xml");
            service.calculerTournee(demande);
            Tournee t = service.recupererTournee();
        });
    }

    @Test
    void realiserTourneeDepuisGrandPlanEt12Livraisons_shouldSucceed(){
        assertTimeout(ofMinutes(1), () -> {
            Service service = new Service();
            service.chargerPlan("../datas/grandPlan.xml");
            Demande demande = service.chargerDemande("../datas/demandeTest4.xml");
            service.calculerTournee(demande);
            Tournee t = service.recupererTournee();
        });
    }



    @Test
    void realiserTourneeDepuisPetitPlanEtDeuxLivraisonsMemeEndroit_shouldSucceed() {
        assertTimeout(ofSeconds(1), () -> {
            Service service = new Service();
            service.chargerPlan("../datas/petitPlan.xml");
            Demande demande = service.chargerDemande("../datas/demandeTest2.xml");
            service.calculerTournee(demande);
            Tournee t = service.recupererTournee();
        });
    }

    @Test
    void realiserTourneeDepuisPetitPlanEtSamePUEtD_shouldSucceed(){
        assertTimeout(ofSeconds(1), () -> {
            Service service = new Service();
            service.chargerPlan("../datas/petitPlan.xml");
            Demande demande = service.chargerDemande("../datas/demandeTestSamePUEtD.xml");
            service.calculerTournee(demande);
            Tournee t = service.recupererTournee();
            System.out.println(t.getDemande().getHeureDepart());
        });
    }

}