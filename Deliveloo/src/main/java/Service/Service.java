package Service;

import Donnees.LectureXML;
import Modeles.Demande;
import com.sothawo.mapjfx.Coordinate;

import java.util.*;

public class Service {
    public Service() {
    }

    private LectureXML lec = new LectureXML();

    public ArrayList<Coordinate> chargerPlan( String path) throws Exception {
        lec.chargerPlan(path);
        ArrayList<Coordinate> limites = new ArrayList<Coordinate>();
        limites= lec.getLimitesPlan();
        return limites;
    }

    public Demande chargerDemande(String path) throws Exception {
        Demande d = lec.chargerDemande(path);
        return d;

    }


}
