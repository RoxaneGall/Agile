package Vue;

import Modeles.Intersection;
import com.sothawo.mapjfx.Coordinate;
import javafx.util.Pair;

import java.util.List;

public class Elements {

<<<<<<< Updated upstream
    /** Coordinates to set the map extent */
    private List<Coordinate> mapExtent;

    /** Coordinates du dépot et des points de livraison */
=======
    /** Elements nécessaires au plan */
    private List<Coordinate> mapExtent = new ArrayList();

    /** Elements nécessaire à la demande */
>>>>>>> Stashed changes
    private Coordinate entrepot;
    private List<Pair<Coordinate,Coordinate>> deliveries;


<<<<<<< Updated upstream
    public void setEntrepot(Intersection inter) {
        this.entrepot = inter.getCoordinate();
=======

    public void chargeDemande(Demande demande) {
        entrepot = demande.getEntrepot().getCoordinate();
>>>>>>> Stashed changes
    }

    public void addDelivery(Coordinate pickup, Coordinate deliver) {
        //deliveries.add(Pair<pickup,deliver>);
    }

}
