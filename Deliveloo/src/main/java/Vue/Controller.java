package Vue;

import Algo.Computations;
import Modeles.*;

import javafx.scene.Scene;
import javafx.scene.layout.Background;
import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.Projection;
import com.sothawo.mapjfx.event.MapLabelEvent;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import com.sothawo.mapjfx.offline.OfflineCache;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ProgressIndicator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import Service.Service;
import javafx.util.Pair;

public class Controller implements ActionListener {

    /**
     * FX Elements pour charger les plans et demandes
     * partie en bas de l'IHM
     */
    public  Scene scene;
    public Service service = new Service();
    public Stage primaryStage = new Stage();
    public FileChooser fileChooser = new FileChooser();

    @FXML
    public Button chargerPlan;
    @FXML
    public Button chargerDemande;
    @FXML
    public Button calculTournee;
    @FXML
    public Button stopTournee;
    @FXML
    public ProgressIndicator loading = new ProgressIndicator();
    @FXML
    public Text ajoutPickUp;

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
    /* section contenant les infos sur les livraisons  */
    @FXML
    public VBox detailsLivraisons;
    @FXML
    public Button supprLivraison;
    @FXML
    public Button ajoutLivraison;
    @FXML
    public ScrollPane scroll;
    @FXML
    public ToggleGroup groupButtons = new ToggleGroup();
    @FXML
    public Pair<ToggleButton,ToggleButton> lastSelected;
    public HashMap<Coordinate, ToggleButton> livrButtons = new HashMap<>();

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
    public Demande demande;
    /* Entrepot */
    public Coordinate entrepot;
    public Marker entrepotMarker;
    /* Livraisons */
    public HashMap<Coordinate, Coordinate> deliveries = new HashMap<>();
    public HashMap<Coordinate, Marker> deliveriesMarkers = new HashMap<>();
    public HashMap<Coordinate, MapLabel> deliveriesNumbers = new HashMap<>();

    /**
     * Attributs pour le trajet/la tournee
     */
    public ArrayList<Coordinate> tournee = new ArrayList<>();
    public ArrayList<Coordinate> tourneePart = new ArrayList<>();
    /* Ligne du trajet de la tournée (Coordinateline) */
    // Pour les couleurs en JFX : go là https://docs.oracle.com/javase/8/javafx/api/javafx/scene/paint/Color.html
    public CoordinateLine trackTrajet = new CoordinateLine();
    /* Ligne du trajet d'une partie seulement de la tournée (Coordinateline) */
    public CoordinateLine trackPart = new CoordinateLine();

    /**
     * Parametres pour le serveur WMS
     */
    public WMSParam wmsParam = new WMSParam()
            .setUrl("http://ows.terrestris.de/osm/service?")
            .addParam("layers", "OSM-WMS");

    public XYZParam xyzParams = new XYZParam()
            .withUrl("https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x})")
            .withAttributions(
                    "'Tiles &copy; <a href=\"https://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer\">ArcGIS</a>'");

    public Controller() throws Exception {
    }

    public void setDeliveriesFromLivraisons(ArrayList<Livraison> livraisons) {
        deliveries.clear();
        for (Livraison livr : livraisons) {
            deliveries.put(livr.getPickup().getCoordinate(), livr.getDelivery().getCoordinate());
            deliveries.put(livr.getDelivery().getCoordinate(), livr.getPickup().getCoordinate());
        }
    }

    /**
     * Méthode d'initialisation de l'IHM
     *
     * @param projection
     */
    public void initializeView(Scene mainScene, Projection projection, Stage primaryStageFromMain) {
        scene = mainScene;
        fileChooser.setInitialDirectory(new File("../datas"));
        loading.visibleProperty().setValue(false);
        //loading.toFront();
        primaryStage = primaryStageFromMain;

        // init MapView-Cache
        final OfflineCache offlineCache = mapView.getOfflineCache();
        final String cacheDir = System.getProperty("java.io.tmpdir") + "/mapjfx-cache";

        // set the custom css file for the MapView
        mapView.setCustomMapviewCssURL(getClass().getResource("/custom_mapview.css"));
        mapView.toBack();

        // set the controls to disabled, this will be changed when the MapView is initialized
        setTopControlsDisable(true);

        buttonResetExtent.setOnAction(event -> {
            mapView.setExtent(mapExtent);
        });

        // wire the zoom button and connect the slider to the map's zoom
        buttonZoom.setOnAction(event -> mapView.setZoom(ZOOM_DEFAULT));
        sliderZoom.valueProperty().bindBidirectional(mapView.zoomProperty());

        // bind the map's center and zoom properties to the corresponding labels and format them
        labelCenter.textProperty().bind(Bindings.format("center: %s", mapView.centerProperty()));
        labelZoom.textProperty().bind(Bindings.format("zoom: %.0f", mapView.zoomProperty()));

        setButtonSupprLivraison();
        setButtonAjoutLivraison();

        setButtonChargerDemande();

        setButtonStopTournee();

        // enable le bouton calculer une tournée avec l'event correspondant
        setButtonCalculerTournee();

        // add event Handlers to the mapView
        eventHandlers();

        // finally initialize the map view
        mapView.initialize(Configuration.builder()
                .projection(projection)
                .showZoomControls(false)
                .build());

        System.out.println("Map is init");

        // watch the MapView's initialized property to finish initialization
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                afterMapIsInitialized();
            }
        });
    }


    /**
     * Méthode appelée une fois que l'IHM est initialisée
     * --> On charge le grand plan
     */
    private void afterMapIsInitialized() {
        chargerPlan();
    }

    /**
     *
     */
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

    /**
     *
     */
    public void chargerPlan() {
        System.out.println("Chargement du plan");
        try {
            ArrayList<Coordinate> limites = service.chargerPlan("../datas/grandPlan.xml");

            // POUR TESTER :
            Coordinate c1 = new Coordinate(45.778579, 4.852096);
            Coordinate c2 = new Coordinate(45.781901, 4.791063);
            Coordinate c3 = new Coordinate(45.730995, 4.859773);
            Coordinate c4 = new Coordinate(45.714939, 4.901873);
            limites.add(c1);
            limites.add(c2);
            limites.add(c3);
            limites.add(c4);
            System.out.println("Limites du plan :" + limites);

            mapExtent = Extent.forCoordinates(limites);
            if (mapView != null) {
                mapView.setExtent(mapExtent);
            }

            setTopControlsDisable(false); // on permet les topControls maintenant que le plan est chargé

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setButtonStopTournee() {
        stopTournee.setOnAction(event -> {
            arreterChargementMeilleureTournee();
        });
    }

    /**
     *
     */
    private void setButtonSupprLivraison() {
        supprLivraison.setOnAction(event -> {
            // récupérer le point cliqué
            Coordinate c1 = null;
            Coordinate c2 = null;
            for (Map.Entry<Coordinate,ToggleButton> entry : livrButtons.entrySet()) {
                if (entry.getValue().isSelected()) {
                    c1 = entry.getKey();
                    break;
                }
            }

            c2 = deliveries.get(c1);

            deleteMarkerByCoord(c1);
            deleteMarkerByCoord(c2);
            deleteLabelByCoord(c1);
            deleteLabelByCoord(c2);
            demande.removeLivraison(c1);
            deliveries.remove(c1);

            calculerTournee();
            afficherTourneeCalculee();
            System.out.println("deliveries after removal : " + deliveries);
        });
    }
    private void setButtonAjoutLivraison() {
        ajoutLivraison.setOnAction(event -> {
            ajoutPickUp.setText("Veuillez faire un clic droit sur votre point pick up");
            ArrayList<Intersection> interLivraison = new ArrayList<Intersection>();

            mapView.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, eventClick -> {
                eventClick.consume();
                labelEvent.setText("Event: map right clicked at: " + eventClick.getCoordinate());
                Coordinate pickUp=eventClick.getCoordinate();
                Intersection i= service.intersectionPlusProche(pickUp);
                interLivraison.add(i);
                System.out.println("pickup : "+i); // interPickUp est bien récupérée
                Marker m = Marker.createProvided(Marker.Provided.ORANGE).setPosition(i.getCoordinate()).setVisible(true);
                mapView.addMarker(m);
                deliveriesMarkers.put(m.getPosition(), m);
            });
            System.out.println("*****"+ interLivraison.size());
            if(interLivraison.size()>=2) {
                Intersection interPickUp = interLivraison.get(0);
                Intersection interDelivery = interLivraison.get(1);
                Livraison l = new Livraison (interPickUp,interDelivery,0,0);
                demande.getLivraisons().add(l);
                calculerTournee();
                afficherTourneeCalculee();
            }


            // dès qu'on sort de addEventHandler interPickUp redevient nulle wtf


        });
    }






    /**
     *
     */
    private void setButtonChargerDemande() {
        chargerDemande.setOnAction(event -> {
            // enable le bouton charger demande avec l'event correspondant
            try {
                System.out.println("Chargement d'une demande");
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                demande = service.chargerDemande(selectedFile.getAbsolutePath());

                mapView.removeCoordinateLine(trackTrajet);
                if (entrepotMarker != null) {
                    mapView.removeMarker(entrepotMarker);
                }
                for (Map.Entry<Coordinate, Marker> entry : deliveriesMarkers.entrySet()) {
                    mapView.removeMarker(entry.getValue());
                }
                for (Map.Entry<Coordinate, MapLabel> entry : deliveriesNumbers.entrySet()) {
                    mapView.removeLabel(entry.getValue());
                }

                deliveriesMarkers.clear();

                entrepot = demande.getEntrepot().getCoordinate();
                setDeliveriesFromLivraisons(demande.getLivraisons());
                System.out.println("Demande : " + demande);

                entrepotMarker = Marker.createProvided(Marker.Provided.GREEN).setPosition(entrepot).setVisible(true);
                mapView.addMarker(entrepotMarker);

                for (int i = 0; i < demande.getLivraisons().size(); i++) {
                    Marker markerPickUp;
                    Coordinate pickUp = demande.getLivraisons().get(i).getPickup().getCoordinate();
                 /*   URL imageURL = new URL("file:///C:/Users/manal/Documents/GitHub/Agile/datas/logos/p_"+i+".png");
                    markerPickUp = new Marker(imageURL, 0, 0).setPosition(pickUp);*/
                    markerPickUp = Marker.createProvided(Marker.Provided.ORANGE).setPosition(pickUp);

                    Marker markerDelivery;
                    Coordinate delivery = demande.getLivraisons().get(i).getDelivery().getCoordinate();
                 /*   URL imageURL2 = new URL("file:///C:/Users/manal/Documents/GitHub/Agile/datas/logos/d_" + i + ".png");
                    markerDelivery = new Marker(imageURL2, 0, 0).setPosition(delivery);*/
                   markerDelivery = Marker.createProvided(Marker.Provided.RED).setPosition(delivery);


                    deliveriesMarkers.put(markerPickUp.getPosition(), markerPickUp);
                    deliveriesMarkers.put(markerDelivery.getPosition(), markerDelivery);
                }

                for (Map.Entry<Coordinate, Marker> entry : deliveriesMarkers.entrySet()) {
                    entry.getValue().setVisible(true);
                    mapView.addMarker(entry.getValue().setVisible(true));
                }

                System.out.println(deliveriesMarkers.size());
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }


    private void setButtonCalculerTournee() {
        calculTournee.setOnAction(event -> {
            calculerTournee();
        });
    }

    private void calculerTournee() {
        mapView.removeCoordinateLine(trackTrajet);
        tournee.clear();
        System.out.println("Calcul d'une tournée");
        try {
            if (demande != null) {
                arreterChargementMeilleureTournee();
                Computations.setDelegate(this);
                //TODO: DEBUT CHARGEMENT TRAJET
                loading.visibleProperty().setValue(true);
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        service.calculerTournee(demande);
                    }
                });
                t1.start();

            } else {
                System.out.println("IMPOSSIBLE DE CALCULER UNE TOURNEE aucune demande n'a été chargée");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void arreterChargementMeilleureTournee() {
        Computations.endComputations();
    }

    private void afficherTourneeCalculee() {
        Tournee t = service.recupererTournee();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mapView.removeCoordinateLine(trackTrajet);
                mapView.removeCoordinateLine(trackPart);
                tournee.clear();
                afficherTournee(t);
            }
        });
    }

    private void livraisonSelected(ToggleButton button) {
        if (lastSelected != null) {
            lastSelected.getKey().setStyle(null);
            lastSelected.getValue().setStyle(null);
        }
        mapView.removeCoordinateLine(trackPart);
        tourneePart.clear();

        ToggleButton pairedButton;
        for(Map.Entry<Coordinate,ToggleButton> entry : livrButtons.entrySet()) {
            if (entry.getValue() == button) {
                pairedButton = livrButtons.get(deliveries.get(entry.getKey()));
                lastSelected = new Pair<>(button, pairedButton);
                button.setStyle("-fx-base: lightblue;");
                pairedButton.setStyle("-fx-base: lightblue;");
                break;
            }
        }

        for(Map.Entry<Coordinate,ToggleButton> entry : livrButtons.entrySet()) {
            if (entry.getValue() == button) {
                int i = 0;
                while (tournee.get(i) != entry.getKey()) {
                    tourneePart.add(tournee.get(i));
                    i++;
                }
                break;
            }
        }

        trackPart = new CoordinateLine(tourneePart).setColor(Color.DARKTURQUOISE).setWidth(8);
        trackPart.setVisible(true);
        mapView.addCoordinateLine(trackPart);
    }

    private void livraisonDeselected(ToggleButton button) {
        button.setStyle(null);
        mapView.removeCoordinateLine(trackPart);
        tourneePart.clear();
    }

    private void afficherTournee(Tournee t) {
        if (demande != null) {
            livrButtons.clear();
            for (Map.Entry<Coordinate,MapLabel> entry : deliveriesNumbers.entrySet()) {
                mapView.removeLabel(entry.getValue());
            }
            deliveriesNumbers.clear();
            detailsLivraisons.getChildren().clear();
            scroll.setVisible(true);
            scroll.setContent(detailsLivraisons);
            // On parcourt la tournée pour ajouter toutes les coordonnées par laquelle le trajet passe à la List de Coordinate tournee
            int compteur = 1;
            Coordinate coord;
            for (int i = 0; i < t.getTrajets().size(); i++) {;
                coord = t.getTrajets().get(i).getOrigine().getCoordinate();
                tournee.add(coord);
                MapLabel l = new MapLabel(Integer.toString(compteur), 10, -10).setPosition(t.getTrajets().get(i).getOrigine().getCoordinate()).setVisible(true);
                mapView.addLabel(l);
                deliveriesNumbers.put(coord, l);
                compteur++;
                for (Troncon troncon : t.getTrajets().get(i).getTroncons()) {
                    tournee.add(troncon.getDestination().getCoordinate());
                }

                ToggleButton button = new ToggleButton("Livraison " + i + "\n Arrivée à :");
                button.setOnAction(event -> {
                    if (button.isSelected()) {
                        livraisonSelected(button);
                    } else {
                        livraisonDeselected(button);
                    }
                });
                button.setId("" + i);
                button.setToggleGroup(groupButtons);
                livrButtons.put(coord, button);
                detailsLivraisons.getChildren().add(button);
            }

            detailsLivraisons.getChildren().add(supprLivraison);
            detailsLivraisons.getChildren().add(ajoutLivraison);

            detailsLivraisons.setVisible(true);

            trackTrajet = new CoordinateLine(tournee).setColor(Color.DARKRED).setWidth(8);
            trackTrajet.setVisible(true);
            // add the tracks
            mapView.addCoordinateLine(trackTrajet);
            // System.out.println("Tournee: " + trackTrajet.toString());

        } else {
            System.out.println("IMPOSSIBLE DE CALCULER UNE TOURNEE aucune demande n'a été chargée");
        }
    }


    /**
     * @param flag
     */
    private void setTopControlsDisable(boolean flag) {
        topControls.setDisable(flag);
    }

    public void deleteMarkerByCoord(Coordinate c) {
        mapView.removeMarker(deliveriesMarkers.get(c));
        deliveriesMarkers.remove(c);
    }

    public void deleteLabelByCoord(Coordinate c) {
        System.out.println("LABEL A SUPPR " + deliveriesNumbers.get(c));
        mapView.removeLabel(deliveriesNumbers.get(c));
        deliveriesNumbers.remove(c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ended")) {
            loading.visibleProperty().setValue(false);
            //TODO: CHARGMENT DES TRAJETS TERMINÉS
        } else if (e.getActionCommand().equals("newResultFound")) {
            afficherTourneeCalculee();
        }
    }
}