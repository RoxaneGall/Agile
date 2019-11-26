package Vue;

import Modeles.Demande;
import Modeles.Intersection;
import com.sothawo.mapjfx.Coordinate;
import javafx.util.Pair;

import java.util.List;

public class Elements {

    /** Elements pour le plan */
    private List<Coordinate> mapExtent;

    /** Elements pour la demande */
    private Coordinate entrepot;
    private List<Pair<Coordinate,Coordinate>> deliveries;


    public void chargeDemande(Demande demande) {
        entrepot = demande.getEntrepot().getCoordinate();
    }

    public void addDelivery(Coordinate pickup, Coordinate deliver) {
        //deliveries.add(Pair<pickup,deliver>);
    }

}
