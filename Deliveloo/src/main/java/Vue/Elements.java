package Vue;

import com.sothawo.mapjfx.Coordinate;
import javafx.util.Pair;

import java.util.List;

public class Elements {

    /** Coordinates to set the map extent */
    private List<Coordinate> mapExtent;

    /** Coordinates du dépot et des points de livraison */
    private Coordinate entrepot;
    private List<Pair<Coordinate,Coordinate>> deliveries;


    public void setEntrepot(double latitude, double longitude) {
        this.entrepot = new Coordinate(latitude,longitude);
    }

    public void

}
