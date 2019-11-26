package Vue;

import Modeles.Demande;
import Modeles.Intersection;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Extent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    /** Elements pour le plan */
    private Extent mapExtent;

    /** Elements pour la demande */
    private Coordinate entrepot;
    private List<Pair<Coordinate,Coordinate>> deliveries;


    public void setMapExtent(ArrayList<Coordinate> planCoords) {
        mapExtent = Extent.forCoordinates(planCoords);

    }

    public void chargeDemande(Demande demande) {
        entrepot = demande.getEntrepot().getCoordinate();
    }

    public void addDelivery(Coordinate pickup, Coordinate deliver) {
        //deliveries.add(Pair<pickup,deliver>);
    }

}
