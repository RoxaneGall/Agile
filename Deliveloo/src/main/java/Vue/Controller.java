package Vue;

import Algo.Computations;
import Donnees.LectureXML;
import Modeles.*;

import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.Projection;
import com.sothawo.mapjfx.event.MapLabelEvent;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import com.sothawo.mapjfx.offline.OfflineCache;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Service.Service;

public class Controller {

    /**
     * FX Elements pour charger les plans et demandes
     * partie en bas de l'IHM
     */

    public Service service = new Service();

    public JFileChooser choix = new JFileChooser();

    @FXML
    public Button chargerPlan;
    @FXML
    public Button chargerDemande;
    @FXML
    public Button calculTournee;

    /**
     * Carte
     */
    @FXML
    public MapView mapView;

    /**
     * FX Elements de controle de l'affichage de la carte
     * partie haute de l'IHM
     */
    /* the box containing the top controls, must be enabled when mapView is initialized */
    @FXML
    public HBox topControls; // POURQUOI ON DECLARE CETTE HBOX ET PAS LES AUTRES ?
    /* slider pour régler le zoom */
    @FXML
    public Slider sliderZoom;
    @FXML
    public Button buttonZoom;
    /* button to reset the map's extent. */
    @FXML
    public Button buttonResetExtent;

    /**
     * FX Elements d'affichage sur la tournée demandée
     * panneau à droite de l'IHM
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
    public Demande demande = new Demande();
    /* Entrepot */
    public Coordinate entrepot;
    public Marker entrepotMarker;
    /* Livraisons */
    public List<Pair<Coordinate, Coordinate>> deliveries = new ArrayList<>();
    public List<Pair<Marker, Marker>> deliveriesMarkers = new ArrayList<>();
    public List<MapLabel> deliveriesNumbers;

    /**
     * Attributs pour le trajet/la tournee
     */
    public ArrayList<Coordinate> tournee = new ArrayList<>();
    /* Ligne du trajet de la tournée (Coordinateline) */
    public CoordinateLine trackMagenta;
    /* Ligne du trajet d'une partie seulement de la tournée (Coordinateline) */
    public CoordinateLine trackCyan = new CoordinateLine().setColor(Color.CYAN).setWidth(7);
    /* display track tournee*/
    // ENUM COULEURS


    /**
     * Parametres for the WMS server.
     */
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
        mapView.setExtent(mapExtent);
    }

    public void setDeliveriesFromLivraisons(ArrayList<Livraison> livraisons) {

        for (Livraison livr : livraisons) {
            Pair delivery = new Pair(livr.getPickup().getCoordinate(), livr.getDelivery().getCoordinate());
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
        choix.setCurrentDirectory(new File("./datas"));

        // init MapView-Cache
        final OfflineCache offlineCache = mapView.getOfflineCache();
        final String cacheDir = System.getProperty("java.io.tmpdir") + "/mapjfx-cache";

        // set the custom css file for the MapView
        mapView.setCustomMapviewCssURL(getClass().getResource("/custom_mapview.css"));

        // set the controls to disabled, this will be changed when the MapView is initialized
        setTopControlsDisable(true);

        // wire the zoom button and connect the slider to the map's zoom
        buttonZoom.setOnAction(event -> mapView.setZoom(ZOOM_DEFAULT));
        sliderZoom.valueProperty().bindBidirectional(mapView.zoomProperty());

        // bind the map's center and zoom properties to the corresponding labels and format them
        labelCenter.textProperty().bind(Bindings.format("center: %s", mapView.centerProperty()));
        labelZoom.textProperty().bind(Bindings.format("zoom: %.0f", mapView.zoomProperty()));

        // enable le bouton charger plan avec l'event correspondant
        setButtonChargerPlan();
        // enable le bouton charger demande avec l'event correspondant
        setButtonChargerDemande();
        // enable le bouton calculer une tournée avec l'event correspondant
        setCalculerTournee();

        // add event Handlers to the mapView
        eventHandlers();

        // finally initialize the map view
        mapView.initialize(Configuration.builder()
                .projection(projection)
                .showZoomControls(false)
                .build());

        System.out.println("Map is init");

        //reset the extent of the map using the resetButton
        // Pas sûre que ça doit être ici
        buttonResetExtent.setOnAction(event -> {
            ArrayList<Coordinate> limites = new ArrayList<Coordinate>();
            // POUR TESTER :
            Coordinate c1 = new Coordinate(45.778579, 4.852096);
            Coordinate c2 = new Coordinate(45.781901, 4.791063);
            Coordinate c3 = new Coordinate(45.730995, 4.859773);
            Coordinate c4 = new Coordinate(45.714939, 4.901873);
            limites.add(c1);
            limites.add(c2);
            limites.add(c3);
            limites.add(c4);
            setMapExtent(limites);
        });
    }


    public void eventHandlers() {

        // add an event handler for MapViewEvent#MAP_EXTENT and set the extent in the map
        mapView.addEventHandler(MapViewEvent.MAP_EXTENT, event -> {
            event.consume();
            mapView.setExtent(event.getExtent());
        });

        // add an event handler for extent changes and display them in the status label
        mapView.addEventHandler(MapViewEvent.MAP_BOUNDING_EXTENT, event -> {
            event.consume();
            labelExtent.setText(event.getExtent().toString());
        });

        mapView.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: map right clicked at: " + event.getCoordinate());
        });
        mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: marker clicked: " + event.getMarker().getId());
        });
        mapView.addEventHandler(MarkerEvent.MARKER_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: marker right clicked: " + event.getMarker().getId());
        });
        mapView.addEventHandler(MapLabelEvent.MAPLABEL_CLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: label clicked: " + event.getMapLabel().getText());
        });
        mapView.addEventHandler(MapLabelEvent.MAPLABEL_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: label right clicked: " + event.getMapLabel().getText());
        });

        mapView.addEventHandler(MapViewEvent.MAP_POINTER_MOVED, event -> {
        });
    }

    private void setButtonChargerPlan() {

        chargerPlan.setOnAction(event -> {
            String pathPlan = "";
            try {
                System.out.println("Chargement d'un plan");
                choix.setCurrentDirectory(new File("../datas"));
                if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    pathPlan = choix.getSelectedFile().getAbsolutePath();
                }
                ArrayList<Coordinate> limites = new ArrayList<Coordinate>();

                //APPEL METHODE ALICE
                //String pathPlan = "../datas/PetitPlan.xml";
                limites = service.chargerPlan(pathPlan);

                // POUR TESTER :

                Coordinate c1 = new Coordinate(45.778579, 4.852096);
                Coordinate c2 = new Coordinate(45.781901, 4.791063);
                Coordinate c3 = new Coordinate(45.730995, 4.859773);
                Coordinate c4 = new Coordinate(45.714939, 4.901873);
                limites.add(c1);
                limites.add(c2);
                limites.add(c3);
                limites.add(c4);
                System.out.println(limites);
                setMapExtent(limites);
                setTopControlsDisable(false); // on permet les topControls maintenant que le plan est chargé

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setButtonChargerDemande() {
        chargerDemande.setOnAction(event -> {
            mapView.removeMarker(entrepotMarker);
            for (int i = 0; i < deliveriesMarkers.size(); i++)
            {
                mapView.removeMarker(deliveriesMarkers.get(i).getKey());
                mapView.removeMarker(deliveriesMarkers.get(i).getValue());
            }
            deliveriesMarkers.clear();
            System.out.println("******"+deliveriesMarkers.size());
            String pathDemande = "";
            try {
                System.out.println("Chargement d'une demande");
                if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    pathDemande = choix.getSelectedFile().getAbsolutePath();
                }
                demande = service.chargerDemande(pathDemande);
                entrepot = demande.getEntrepot().getCoordinate();
                chargerDemande(demande);
                System.out.println(demande);

                entrepotMarker = Marker.createProvided(Marker.Provided.GREEN).setPosition(entrepot).setVisible(true);
                mapView.addMarker(entrepotMarker);

                for (int i = 0; i < demande.getLivraisons().size(); i++) {
                    Marker markerPickUp;
                    Coordinate pickUp = demande.getLivraisons().get(i).getPickup().getCoordinate();
                    markerPickUp = Marker.createProvided(Marker.Provided.BLUE).setPosition(pickUp);

                    Marker markerDelivery;
                    Coordinate delivery = demande.getLivraisons().get(i).getDelivery().getCoordinate();
                    System.out.println(pickUp + "/////" + delivery);

                    markerDelivery = Marker.createProvided(Marker.Provided.RED).setPosition(delivery);
                    deliveriesMarkers.add(new Pair<Marker, Marker>(markerPickUp, markerDelivery));


                }

                for (int i = 0; i < deliveriesMarkers.size(); i++) {
                    deliveriesMarkers.get(i).getKey().setVisible(true);
                    deliveriesMarkers.get(i).getValue().setVisible(true);
                    mapView.addMarker(deliveriesMarkers.get(i).getKey());
                    mapView.addMarker(deliveriesMarkers.get(i).getValue());
                }

                System.out.println(deliveriesMarkers.size());


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setCalculerTournee() {

        calculTournee.setOnAction(event -> {
            System.out.println("Calcul d'une tournée");
            try {
                if (demande != null) {
                    Tournee t = Computations.getTourneeFromDemande(demande, Graphe.shared);
                    // On parcourt la tournée pour ajouter toutes les coordonnées par laquelle le trajet passe à la List de Coordinate tournee
                    for (Trajet trajet : t.getTrajets()) {
                        tournee.add(trajet.getOrigine().getCoordinate());
                        for (Troncon troncon : trajet.getTroncons()) {
                            tournee.add(troncon.getDestination().getCoordinate());
                        }
                    }
                    System.out.println("LINE :" + trackCyan);
                    trackCyan = new CoordinateLine(tournee).setColor(Color.CYAN);
                    trackCyan.setVisible(true);
                    // add the tracks
                    System.out.println("ADD TRACK TO MAP");
                    mapView.addCoordinateLine(trackCyan);
                    System.out.println("Tournee: " + trackCyan.toString());
                } else {
                    System.out.println("IMPOSSIBLE DE CALCULER UNE TOURNEE aucune demande n'a été chargée");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setTopControlsDisable(boolean flag) {
        topControls.setDisable(flag);
    }

}