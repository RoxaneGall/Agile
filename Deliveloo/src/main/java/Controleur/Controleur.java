package Controleur;

import Donnees.LectureXML;
import com.sothawo.mapjfx.Coordinate;

import java.util.*;

public class Controleur {

    private LectureXML lec;

    public ArrayList<Coordinate>  chargerPlan( String path) throws Exception {
        lec.chargerPlan(path);
        ArrayList<Coordinate> limites = new ArrayList<Coordinate>();
        //limites= méthode Alice qui renvoie les 4 points
        return limites;
    }


}
