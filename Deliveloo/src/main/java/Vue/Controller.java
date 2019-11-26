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
    private HBox topControls;
    /* slider pour régler le zoom */
    @FXML
    private Slider sliderZoom;
    @FXML
    private Button buttonZoom;
    /* button to reset the map's extent. */
    @FXML
    private Button buttonResetExtent;

    /**
     *  FX Elements d'affichage sur la tournée demandée
     *  panneau à droite de l'IHM
     */
    /* Accordion for all the different options */
    @FXML
    private Accordion rightControls;
    /* section contenant les infos sur les livraisons  */
    @FXML
    private TitledPane detailsLivraisons;

    /**
     * FX elements d'affichage pour debug
     * partie basse de l'IHM
     */
    /* Label de debug pour afficher les infos sur la map et les events */
    @FXML
    private Label labelCenter;
    @FXML
    private Label labelExtent;
    @FXML
    private Label labelZoom;
    @FXML
    private Label labelEvent;

    /**
     * FX Marker au click pour ajout de livraison (ITERATION 2)
     */
    /* Checkbox for click marker */
    @FXML
    private CheckBox checkClickMarker;

    /**
     * Attributs pour définir le plan
     */
    /* cadrage de la map */
    private Extent mapExtent;
    /* default zoom value. */
    private static final int ZOOM_DEFAULT = 14;

    /**
     * Attributs pour la demande
     */
    /* Entrepot */
    private Coordinate entrepot;
    private Marker entrepotMarker;
    /* Livraisons */
    private List<Pair<Coordinate, Coordinate>> deliveries;
    private List<Pair<Marker,Marker>> deliveriesMarkers;
    private List<MapLabel> deliveriesNumbers;
    /* Ligne du trajet de la tournée (Coordinateline) */
    private CoordinateLine trackMagenta;
    /* Ligne du trajet d'une partie seulement de la tournée (Coordinateline) */
    private CoordinateLine trackCyan;

    // ENUM COULEURS


    /** Parametres for the WMS server. */
    private WMSParam wmsParam = new WMSParam()
            .setUrl("http://ows.terrestris.de/osm/service?")
            .addParam("layers", "OSM-WMS");

    private XYZParam xyzParams = new XYZParam()
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


}
