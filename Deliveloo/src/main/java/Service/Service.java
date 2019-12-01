package Service;

import Algo.Computations;
import Donnees.LectureXML;
import Modeles.Demande;
import Modeles.Graphe;
import Modeles.Tournee;
import com.sothawo.mapjfx.Coordinate;

import java.util.*;

public class Service {
    public Service() {
    }

    private LectureXML lec = new LectureXML();

    public ArrayList<Coordinate> chargerPlan( String path) throws Exception {
        lec.chargerPlan(path);
        ArrayList<Coordinate> limites = lec.getLimitesPlan();
        return limites;
    }

    public Demande chargerDemande(String path) throws Exception {
        Demande d = lec.chargerDemande(path);
        return d;

    }

    public Tournee calculerTournee(Demande demande) throws Exception {
        Tournee t = Computations.getTourneeFromDemande(demande);
        return t;
    }




}
