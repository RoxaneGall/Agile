package Vue;

import Modeles.Demande;

import Modeles.Livraison;
import com.sothawo.mapjfx.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    /**
     * FX Elements pour charger les plans et demandes
     * partie en bas de l'IHM
     */
    @FXML
    public Button chargerPlan;
    @FXML
    public Button chargerDemande;

    /** Carte */
    @FXML
    public MapView mapView;

    /**
     * FX Elements de controle de l'affichage de la carte
     * partie haute de l'IHM
     */
    /* the box containing the top controls, must be enabled when mapView is initialized */
    @FXML
    public HBox topControls;
    /* slider pour régler le zoom */
    @FXML
    public Slider sliderZoom;
    @FXML
    public Button buttonZoom;
    /* button to reset the map's extent. */
    @FXML
    public Button buttonResetExtent;

    /**
     *  FX Elements d'affichage sur la tournée demandée
     *  panneau à droite de l'IHM
     */
    /* Accordion for all the different options */
    @FXML
    public Accordion rightControls;
    /* section contenant les infos sur les livraisons  */
    @FXML
    public TitledPane detailsLivraisons;

    /**
     * FX elements d'affichage pour debug
     * partie basse de l'IHM
     */
    /* Label de debug pour afficher les infos sur la map et les events */
    @FXML
    public Label labelCenter;
    @FXML
    public Label labelExtent;
    @FXML
    public Label labelZoom;
    @FXML
    public Label labelEvent;

    /**
     * FX Marker au click pour ajout de livraison (ITERATION 2)
     */
    /* Checkbox for click marker */
    @FXML
    public CheckBox checkClickMarker;

    /**
     * Attributs pour définir le plan
     */
    /* cadrage de la map */
    public Extent mapExtent;
    /* default zoom value. */
    public static final int ZOOM_DEFAULT = 14;

    /**
     * Attributs pour la demande
     */
    /* Entrepot */
    public Coordinate entrepot;
    public Marker entrepotMarker;
    /* Livraisons */
    public List<Pair<Coordinate, Coordinate>> deliveries;
    public List<Pair<Marker,Marker>> deliveriesMarkers;
    public List<MapLabel> deliveriesNumbers;
    /* Ligne du trajet de la tournée (Coordinateline) */
    public CoordinateLine trackMagenta;
    /* Ligne du trajet d'une partie seulement de la tournée (Coordinateline) */
    public CoordinateLine trackCyan;

    // ENUM COULEURS


    /** Parametres for the WMS server. */
    public WMSParam wmsParam = new WMSParam()
            .setUrl("http://ows.terrestris.de/osm/service?")
            .addParam("layers", "OSM-WMS");

    public XYZParam xyzParams = new XYZParam()
            .withUrl("https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x})")
            .withAttributions(
                    "'Tiles &copy; <a href=\"https://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer\">ArcGIS</a>'");


    /**
     * Methodes pour définir les éléments/attributs
     */
    public void setMapExtent(ArrayList<Coordinate> planCoords) {
        mapExtent = Extent.forCoordinates(planCoords);
    }

    public void setDeliveriesFromLivraisons(ArrayList<Livraison> livraisons) {

        for (Livraison livr : livraisons) {
            Pair delivery = new Pair(livr.getDepart().getCoordinate(), livr.getArrivee().getCoordinate());
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

    public void initializeView(Projection projection) {

    }


}
