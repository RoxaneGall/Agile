package Vue;

import Modeles.Intersection;
import com.sothawo.mapjfx.Coordinate;
import javafx.util.Pair;

import java.util.List;

public class Elements {

    /** Coordinates to set the map extent */
    private List<Coordinate> mapExtent;

    /** Coordinates du dépot et des points de livraison */
    private Coordinate entrepot;
    private List<Pair<Coordinate,Coordinate>> deliveries;


    public void setEntrepot(Intersection inter) {
        this.entrepot = Intersection.getCoordinate();
    }

    public void addDelivery(Coordinate pickup, Coordinate deliver) {
        //deliveries.add(Pair<pickup,deliver>);
    }

}
