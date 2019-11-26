package Service;

import Donnees.LectureXML;
import com.sothawo.mapjfx.Coordinate;

import java.util.*;

public class Service {

    private LectureXML lec;

    public ArrayList<Coordinate>  chargerPlan( String path) throws Exception {
        lec.chargerPlan(path);
        ArrayList<Coordinate> limites = new ArrayList<Coordinate>();
        limites= lec.getLimitesPlan();
        //limites= méthode Alice qui renvoie les 4 points
        return limites;
    }


}
