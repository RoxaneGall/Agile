package Vue;

import Modeles.Demande;
import Modeles.Intersection;


import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Extent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    //private Demande = ;
    /** Coordinates to set the map extent */
    /** Elements pour le plan */
    private Extent mapExtent;

    /** Elements pour la demande */
    private Coordinate entrepot;
    private List<Pair<Coordinate,Coordinate>> deliveries;


    /** Mise à jour des éléments nécessaires à l'affichage du plan */
    public void setMapExtent(ArrayList<Coordinate> planCoords) {
        mapExtent = Extent.forCoordinates(planCoords);

    }

    public void chargerDemande(Demande demande) {
        entrepot = demande.getEntrepot().getCoordinate();
        //deliveries = demande.getLivraisons();

    }

    /*public void setEntrepot(Intersection inter) {
        this.entrepot = inter.getCoordinate();
    }*/

    public void addDelivery(Coordinate pickup, Coordinate deliver) {
        //deliveries.add(Pair<pickup,deliver>);
    }

    public void setExtentMap() {

    }

}
