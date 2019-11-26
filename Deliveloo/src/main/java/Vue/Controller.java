package Vue;

import Modeles.Demande;
import Modeles.Intersection;


import Modeles.Livraison;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Extent;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import javafx.scene.control.Button;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    /** Elements pour l'IHM de base */
    public Button chargerPlan;
    public Button chargerDemande;
    public MapView mapView;

    /** Elements pour le plan */
    /* cadrage de la map */
    private Extent mapExtent;
    /* default zoom value. */
    private static final int ZOOM_DEFAULT = 14;




    /** Elements pour la demande */

    /* Entrepot */
    private Coordinate entrepot;
    private Marker entrepotMarker;

    /* */
    private List<Pair<Coordinate,Coordinate>> deliveries;





    /** Mise à jour des éléments nécessaires à l'affichage du plan */
    public void setMapExtent(ArrayList<Coordinate> planCoords) {
        mapExtent = Extent.forCoordinates(planCoords);
    }

    public void setDeliveriesFromLivraisons(ArrayList<Livraison> livraisons) {

        for (Livraison livr : livraisons) {
            Pair delivery = new Pair(livr.getDepart().getCoordinate(),livr.getArrivee().getCoordinate());
            deliveries.add(delivery);
        }
    }

    public void chargerDemande(Demande demande) {
        entrepot = demande.getEntrepot().getCoordinate();
        setDeliveriesFromLivraisons(demande.getLivraisons());

    }

    /*public void setEntrepot(Intersection inter) {
        this.entrepot = inter.getCoordinate();
    }*/

    public void addDelivery(Coordinate pickup, Coordinate deliver) {
        //deliveries.add(Pair<pickup,deliver>);
    }



}
