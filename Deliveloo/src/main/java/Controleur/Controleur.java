package Controleur;

import Donnees.LectureXML;
import com.sothawo.mapjfx.Coordinate;

import java.util.*;

public class Controleur {

    private LectureXML lec;

    public ArrayList<Coordinate>  chargerPlan( String chemin) throws Exception {
        lec.chargerPlan(chemin);
        ArrayList<Coordinate> limites = new ArrayList<Coordinate>();
        return limites;
        //appel méthode Alice qui renvoie les 4 points
    }


}
