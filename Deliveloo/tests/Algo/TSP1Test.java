package Algo;

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
    void realiserTourneeDepuisPetitPlanEtPetiteDemande_shouldSucceed() throws Exception {
        Service service = new Service();
        service.chargerPlan("datas/grandPlan.xml");
        System.out.println(Graphe.shared.getIntersectionMap().values().size());
        Demande demande = service.chargerDemande("datas/demandeGrand9.xml");
        service.calculerTournee(demande);
        Tournee t = service.recupererTournee();
        System.out.println(t.getHeureArrivee());
        System.out.println(t.getDemande().getHeureDepart());
        System.out.println(t.getTotalDistance());
        System.out.println(t.getTotalDuration());
        System.out.println(t.getTrajets().get(0).toString());
    }
}