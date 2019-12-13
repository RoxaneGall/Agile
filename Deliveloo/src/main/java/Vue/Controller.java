package Vue;

import Algo.Computations;
import Modeles.*;
import Donnees.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Alert.AlertType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.CookieHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import Service.Service;
import javafx.util.Pair;

public class Controller implements ActionListener {

    /**
     * FX Elements pour charger les plans et demandes
     * partie en bas de l'IHM
     */
    public Scene scene;
    public Service service = new Service();
    public Stage primaryStage = new Stage();
    public FileChooser fileChooser = new FileChooser();
    public DirectoryChooser directoryChooser = new DirectoryChooser();
    public SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
    ;
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
    public Button exportFeuille;
    @FXML
    public ScrollPane scroll;
    @FXML
    public ToggleGroup groupButtons = new ToggleGroup();

    public ToggleButton lastSelected;
    public ToggleButton lastPairSelected;
    public HashMap<ToggleButton, Pair<Coordinate, Long>> livrButtons = new HashMap<>();

    /**
     * FX elements d'affichage pour debug
     * partie basse de l'IHM
     */
    /* Label de debug pour afficher les infos sur la map et les events */
    @FXML
    public Label labelTourneeDistance;
    @FXML
    public Label labelTourneeTemps;
    @FXML
    public Label labelTourneeNbLivraison;

    public String path = "file://" + System.getProperty("user.dir").replace('\\', '/').substring(0, System.getProperty("user.dir").replace('\\', '/').lastIndexOf('/'));

    /**
     * Attributs pour définir le plan
     */
    /* cadrage de la map */
    public Extent mapExtent;
    /* default zoom value. */
    public static final int ZOOM_DEFAULT = 12;

    /**
     * Attributs pour la tournee
     */
    public Demande demande; // demande de départ obtenue avec chargerDemande
    public Tournee tournee; // tournee calculée, qui contient donc également la demande, utilisée également quand on modifie la demande avec Ajout/Suppr
    public ArrayList<Tournee> historique = new ArrayList<>(); // liste historique des tournees calculées, au clique de précédent ou suivant on charge la tournee correspondante de l'historique
    public Button retour;
    public Button suivant;
    public int indexHistorique = -1;

    /* Entrepot */
    public Coordinate entrepot;
    public Marker entrepotMarker;
    /* Livraisons */
    public HashMap<Coordinate, Marker> deliveriesMarkers = new HashMap<>();
    public HashMap<Coordinate, MapLabel> deliveriesNumbers = new HashMap<>();

    /**
     * Attributs pour le trajet/la tournee
     */
    public ArrayList<Coordinate> tourneeCoordinate = new ArrayList<>();
    public ArrayList<Coordinate> tourneePartCoordinate = new ArrayList<>();
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

    /**
     * Méthode d'initialisation de l'IHM
     *
     * @param projection
     */
    public void initializeView(Scene mainScene, Projection projection, Stage primaryStageFromMain) {
        primaryStage = primaryStageFromMain;
        scene = mainScene;
        fileChooser.setInitialDirectory(new File("../datas"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        directoryChooser.setInitialDirectory(new File("../datas"));
        loading.visibleProperty().setValue(false);

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
            mapView.setZoom(ZOOM_DEFAULT);
        });
        // wire the zoom button and connect the slider to the map's zoom
        buttonZoom.setOnAction(event -> mapView.setZoom(ZOOM_DEFAULT));
        sliderZoom.valueProperty().bindBidirectional(mapView.zoomProperty());

        // bind the map's center and zoom properties to the corresponding labels and format them

        setButtonSupprLivraison();
        setButtonAjoutLivraison();
        setButtonExportFeuille();
        setButtonChargerDemande();
        setButtonChargerPlan();
        setButtonRetour();
        setButtonSuivant();

        disableButtonsTournee(true); // disable les boutons de tournée

        setButtonStopCalculTourneeOptimale();

        // enable le bouton calculer une tournée avec l'event correspondant
        setButtonCalculerTourneeOptimale();

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


        mapView.setZoom(ZOOM_DEFAULT);
    }


    /**
     * Méthode appelée une fois que l'IHM est initialisée
     * --> On charge le grand plan
     */
    private void afterMapIsInitialized() {
        chargerPlan("../datas/grandPlan.xml");
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
        mapView.addEventHandler(MapViewEvent.MAP_POINTER_MOVED, event -> {
        });
    }

    public void setButtonChargerPlan() {
        chargerPlan.setOnAction(event -> {
            selectPlan();
        });
    }

    /**
     *
     */
    public void selectPlan() {
        File selectedFile = null;
        String path = "";
        try {
            System.out.println("Sélection d'un plan");
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            path = selectedFile.getAbsolutePath();
        } catch (Exception e) {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Erreur chargement demande");
            dialog.getDialogPane().setContent(new Text(e.getMessage()));
            //TODO : Faire des tests pour montrer qu'on throw bien les erreurs
        }
        if (selectedFile != null) {
            try {
                chargerPlan(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     */
    public void chargerPlan(String path) {
        System.out.println("Chargement du plan " + path);
        try {
            ArrayList<Coordinate> limites = service.chargerPlan(path);
            System.out.println("Limites du plan :" + limites);
            mapExtent = Extent.forCoordinates(limites);
            if (mapView != null) {
                System.out.println("Extent of Map reset");
                mapView.setExtent(mapExtent);
            }
            setTopControlsDisable(false); // on permet les topControls maintenant que le plan est chargé
        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Erreur chargement plan");
            alert.setHeaderText("Erreur chargement plan");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            ex.printStackTrace();
        }
    }

    private void setButtonStopCalculTourneeOptimale() {
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
            Long idLivrSupr = null;
            for (Map.Entry<ToggleButton, Pair<Coordinate, Long>> entry : livrButtons.entrySet()) {
                if (entry.getKey().isSelected()) {
                    c1 = entry.getValue().getKey();
                    idLivrSupr = entry.getValue().getValue();
                    break;
                }
            }

            for (Map.Entry<ToggleButton, Pair<Coordinate, Long>> entry1 : livrButtons.entrySet()) {
                if (c1 != entry1.getValue().getKey() && entry1.getValue().getValue() == idLivrSupr) {
                    c2 = entry1.getValue().getKey();
                    break;
                }

            }

            deleteMarkerByCoord(c1);
            deleteMarkerByCoord(c2);
            deleteLabelByCoord(c1);
            deleteLabelByCoord(c2);
            tournee = service.supprimerLivraison(tournee, idLivrSupr);
            demande = tournee.getDemande();
            afficherTournee(tournee);
            mapView.removeCoordinateLine(trackPart);

            //TODO : c coordonnée du premier point à supprimer, c2 coordonnée du 2ème point à supprimer
            //TODO : remplacer cette méthode calculerTournee Optimale
        });
    }

    private void addRightClickEvent(ArrayList<Intersection> interLivraison) {

        mapView.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, eventClick -> {
            eventClick.consume();
            Coordinate pickUp = eventClick.getCoordinate();
            Intersection i = service.intersectionPlusProche(pickUp);
            System.out.println("inter trouvée : " + i);
            if (i != null) {
                int size = demande.getLivraisons().size() + 1;
                int nbLivrAjoute = interLivraison.size();
                if (nbLivrAjoute == 0) { //premier clic
                    URL imageURL = null;
                    try {
                        imageURL = new URL(path + "/datas/logos/p_" + size + ".png");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    Marker m = new Marker(imageURL, -32, -64).setPosition(i.getCoordinate()).setVisible(true);
                    mapView.addMarker(m);
                    deliveriesMarkers.put(m.getPosition(), m);
                    interLivraison.add(i);
                    ajouterLivraison(interLivraison);
                }
                if (nbLivrAjoute == 1) { //deuxieme clic
                    URL imageURL = null;
                    try {
                        imageURL = new URL(path + "/datas/logos/d_" + size + ".png");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    Marker m = new Marker(imageURL, -32, -64).setPosition(i.getCoordinate()).setVisible(true);
                    mapView.addMarker(m);
                    deliveriesMarkers.put(m.getPosition(), m);
                    interLivraison.add(i);
                    ajouterLivraison(interLivraison);
                }
                if (nbLivrAjoute == 2) {
                    ajoutPickUp.setText("Livraison ajoutée !");
                }
            }
            if (i == null) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Erreur ajout livraison");
                alert.setHeaderText("Erreur ajout livraison");
                alert.setContentText("Veuillez sélectionner un point dans le plan !");
                alert.show();
            }
        });
    }

    private void ajouterLivraison(ArrayList<Intersection> interLivraison) {
        if (interLivraison.size() == 2) {
            ajoutPickUp.setText("");
            Intersection interPickUp = interLivraison.get(0);
            Intersection interDelivery = interLivraison.get(1);
            genererLivraison(interPickUp, interDelivery);
            ajoutPickUp.setText("Livraison ajoutée !");
        }
    }

    private void genererLivraison(Intersection interPickUp, Intersection interDelivery) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Veuillez rentrer la durée d'enlèvement et de livraison");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField dEnlevement = new TextField();
        dEnlevement.setPromptText("Durée d'enlèvement : ");
        TextField dLivraison = new TextField();
        dLivraison.setPromptText("Durée de livraison : ");

        gridPane.add(new Label("Durée d'enlèvement :"), 0, 0);
        gridPane.add(dEnlevement, 1, 0);
        gridPane.add(new Label("Durée de livraison : "), 2, 0);
        gridPane.add(dLivraison, 3, 0);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> dEnlevement.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(dEnlevement.getText(), dLivraison.getText());
            }
            //On supprime les markers ajoutés si on rentre pas la durée d'enlèvement et de livraison
            mapView.removeMarker(deliveriesMarkers.get(interPickUp.getCoordinate()));
            mapView.removeMarker(deliveriesMarkers.get(interDelivery.getCoordinate()));
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();

        Tournee nvTournee = service.ajouterLivraison(tournee, interPickUp, interDelivery, Integer.parseInt(result.get().getKey()), Integer.parseInt(result.get().getValue()));
        tournee = nvTournee;
        demande = nvTournee.getDemande();
        if (indexHistorique < historique.size()-1) {
            for(int i = indexHistorique+1; i<historique.size(); i++) {
                historique.remove(i);
            }
        }
        afficherTournee(nvTournee);
        ajoutPickUp.setText("Livraison ajoutée !");
    }


    private void setButtonAjoutLivraison() {
        ajoutLivraison.setOnAction(event -> {
            ajoutPickUp.setText("Veuillez faire un clic droit sur votre point pick up & delivery");
            ArrayList<Intersection> interLivraison = new ArrayList<Intersection>();
            addRightClickEvent(interLivraison);
        });
    }

    /**
     *
     */
    private void setButtonExportFeuille() {
        exportFeuille.setOnAction(event ->
        {
            try {
                File selectedDirectory = directoryChooser.showDialog(primaryStage);

                if (selectedDirectory == null) {
                    System.out.println("No Directory selected");
                } else {
                    //System.out.println(selectedDirectory.getAbsolutePath());
                    EcritureXML ecr = new EcritureXML();
                    ecr.ecrireFichier(tournee, selectedDirectory.getAbsolutePath());
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }


    /**
     *
     */
    private void setButtonChargerDemande() {
        chargerDemande.setOnAction(event -> {
            // enable le bouton charger demande avec l'event correspondant
            disableButtonsTournee(true);
            File selectedFile = null;
            mapView.removeCoordinateLine(trackPart);
            mapView.removeCoordinateLine(trackTrajet);

            try {
                System.out.println("Chargement d'une demande");
                selectedFile = fileChooser.showOpenDialog(primaryStage);
                demande = service.chargerDemande(selectedFile.getAbsolutePath());
                historique.clear();
                indexHistorique = -1;
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Erreur chargement demande");
                alert.setHeaderText("Erreur chargement demande");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                //TODO : Faire des tests pour montrer qu'on throw bien les erreurs (si Alice l'a pas déjà fait)
                // TODO: Histoire que ce soit clean et pour le livrable est-ce qu'on ferait pas un tableu récapitulatif des erreurs possibles et du message correspondant ?
            }
            if (selectedFile != null) {
                clearDemande();
                clearTournee();
                afficherDemande();
            }
        });
    }

    private void clearDemande() {
        try {
            if (entrepotMarker != null) {
                mapView.removeMarker(entrepotMarker);
            }
            for (Map.Entry<Coordinate, Marker> entry : deliveriesMarkers.entrySet()) {
                mapView.removeMarker(entry.getValue());
            }
            deliveriesMarkers.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void afficherDemande() {
        try {
            clearDemande(); // on supprime la demande d'avant

            entrepot = demande.getEntrepot().getCoordinate();
            entrepotMarker = Marker.createProvided(Marker.Provided.GREEN).setPosition(entrepot).setVisible(true);
            mapView.addMarker(entrepotMarker);

            for (int i = 0; i < demande.getLivraisons().size(); i++) {
                Marker markerPickUp;
                Coordinate pickUp = demande.getLivraisons().get(i).getPickup().getCoordinate();
                URL imageURL = new URL(path + "/datas/logos/p_" + i + ".png");
                markerPickUp = new Marker(imageURL, -32, -64).setPosition(pickUp);
                Marker markerDelivery;
                Coordinate delivery = demande.getLivraisons().get(i).getDelivery().getCoordinate();
                URL imageURL2 = new URL(path + "/datas/logos/d_" + i + ".png");
                markerDelivery = new Marker(imageURL2, -32, -64).setPosition(delivery);
                deliveriesMarkers.put(markerPickUp.getPosition(), markerPickUp);
                deliveriesMarkers.put(markerDelivery.getPosition(), markerDelivery);
            }

            for (Map.Entry<Coordinate, Marker> entry : deliveriesMarkers.entrySet()) {
                entry.getValue().setVisible(true);
                mapView.addMarker(entry.getValue().setVisible(true));
            }

            scroll.setContent(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setButtonCalculerTourneeOptimale() {
        calculTournee.setOnAction(event -> {
            calculerTourneeOptimale();
        });
    }

    private void calculerTourneeOptimale() {
        System.out.println("Calcul d'une tournée");
        try {
            if (demande != null) {
                arreterChargementMeilleureTournee();
                Computations.setDelegate(this);
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
        tournee = service.recupererTournee();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                afficherTournee(tournee);
            }
        });
    }

    private void entrepotSelected(ToggleButton button) {
        if (lastSelected != null) {
            lastSelected.setStyle(null);
        }
        if (lastPairSelected != null) {
            lastPairSelected.setStyle(null);
        }
        lastSelected = button;
        mapView.removeCoordinateLine(trackPart);
        tourneePartCoordinate.clear();
        button.setStyle("-fx-base: lightblue;");
        mapView.removeCoordinateLine(trackTrajet);
        trackTrajet.setColor(Color.DARKTURQUOISE).setWidth(8).setVisible(true);
        mapView.addCoordinateLine(trackTrajet);
    }

    private void entrepotDeselected(ToggleButton button) {
        button.setStyle(null);
        mapView.removeCoordinateLine(trackTrajet);
        trackTrajet.setColor(Color.DARKRED).setWidth(8).setVisible(true);
        mapView.addCoordinateLine(trackTrajet);

    }

    private void livraisonSelected(ToggleButton button) {
        if (lastSelected != null) {
            lastSelected.setStyle(null);
        }
        if (lastPairSelected != null) {
            lastPairSelected.setStyle(null);
        }

        mapView.removeCoordinateLine(trackTrajet);
        trackTrajet.setColor(Color.DARKRED).setWidth(8).setVisible(true);
        mapView.addCoordinateLine(trackTrajet);

        mapView.removeCoordinateLine(trackPart);
        tourneePartCoordinate.clear();

        ToggleButton pairedButton = null;
        Pair<Coordinate, Long> entry = livrButtons.get(button);
        for (Map.Entry<ToggleButton, Pair<Coordinate, Long>> entry1 : livrButtons.entrySet()) {
            if (button != entry1.getKey() && entry1.getValue().getValue() == entry.getValue()) {
                pairedButton = entry1.getKey();
                break;
            }

        }
        lastSelected = button;
        lastPairSelected = pairedButton;
        button.setStyle("-fx-base: lightblue;");
        pairedButton.setStyle("-fx-base: lightblue;");

        int i = 0;
        while (tourneeCoordinate.get(i) != entry.getKey()) {
            tourneePartCoordinate.add(tourneeCoordinate.get(i++));
        }
        trackPart = new CoordinateLine(tourneePartCoordinate).setColor(Color.DARKTURQUOISE).setWidth(8);
        trackPart.setVisible(true);
        mapView.addCoordinateLine(trackPart);
    }

    private void livraisonDeselected(ToggleButton button) {
        button.setStyle(null);
        if (lastPairSelected != null) {
            lastPairSelected.setStyle(null);
        }
        mapView.removeCoordinateLine(trackPart);
        tourneePartCoordinate.clear();
    }

    private void afficherTournee(Tournee t) {
        System.out.println("*****" + historique.size() + " index :" + indexHistorique);
        if (demande != null) {
            disableButtonsTournee(false); // les boutons tournées sont cliquables
            // On supprime les infos de l'ancienne tournée de l'IHM
            clearTournee();
            if (historique.size()==0 || historique.contains(tournee)!=true) {
                // On ajoute la tournée à l'historique
                historique.add(tournee);
                indexHistorique++;
            }
            // On parcourt la tournée pour ajouter toutes les coordonnées par laquelle le trajet passe à la List de Coordinate tournee
            int compteur = 1;
            Coordinate origine;
            Trajet trajet;

            labelTourneeDistance.setText("Distance: "+t.getTotalDistance()/1000+"km");
            labelTourneeTemps.setText("Temps: "+t.getTotalDuration()+"min");
            labelTourneeNbLivraison.setText("Nombre de livraisons: "+t.getDemande().getLivraisons().size());

            for (int i = 0; i < t.getTrajets().size(); i++) {
                trajet = t.getTrajets().get(i);
                origine = trajet.getOrigine().getCoordinate();
                tourneeCoordinate.add(origine);

                MapLabel l = new MapLabel(Integer.toString(compteur), 10, -10).setPosition(t.getTrajets().get(i).getArrivee().getCoordinate()).setVisible(true);
                mapView.addLabel(l);
                deliveriesNumbers.put(trajet.getArrivee().getCoordinate(), l);
                compteur++;

                for (Troncon troncon : t.getTrajets().get(i).getTroncons()) {
                    tourneeCoordinate.add(troncon.getDestination().getCoordinate());
                }

                String infoButton = "";
                Long idLivr;
                if (i == 0) {
                    infoButton = "Entrepôt \nDépart : " + formater.format(t.getDemande().getHeureDepart()) + "\nRetour : "+ formater.format(t.getHeureArrivee());

                    ToggleButton button = new ToggleButton();
                    button.setText(infoButton);
                    button.setPrefWidth(250.0);
                    button.setAlignment(Pos.TOP_LEFT);
                    button.setToggleGroup(groupButtons);
                    detailsLivraisons.getChildren().add(button);
                    button.setOnAction(event -> {
                        entrepotDeselected(button);
                    });
                }

                ToggleButton button = new ToggleButton();
                if (i == t.getTrajets().size() - 1) {
                    idLivr = (long) -1;
                    infoButton = i + 1 + " - Retour à l'entrepôt"  + "\nDépart : " + formater.format(trajet.getHeureDepart()) + "    Arrivée : " + formater.format(trajet.getHeureArrivee()) ;
                    button.setOnAction(event -> {
                        if (button.isSelected()) {
                            entrepotSelected(button);
                        } else {
                            entrepotDeselected(button);
                        }
                    });
                } else {
                    idLivr = trajet.getLivraison().getId();
                    if (trajet.getType() == Trajet.Type.PICKUP) {
                        infoButton = i + 1 + " - PICKUP Livraison n°" + trajet.getLivraison().getId() + "\nDépart : " + formater.format(trajet.getHeureDepart()) + "    Arrivée : " + formater.format(trajet.getHeureArrivee()) ;
                    } else {
                        infoButton = i + 1 + " - DELIVERY Livraison n°" + trajet.getLivraison().getId() + "\nDépart : " + formater.format(trajet.getHeureDepart()) + "    Arrivée : " + formater.format(trajet.getHeureArrivee()) ;
                    }
                    button.setOnAction(event -> {
                        if (button.isSelected()) {
                            livraisonSelected(button);
                        } else {
                            livraisonDeselected(button);
                        }
                    });
                }
                button.setText(infoButton);
                button.setPrefWidth(250.0);
                button.setAlignment(Pos.TOP_LEFT);
                button.setId("" + i);
                button.setToggleGroup(groupButtons);
                livrButtons.put(button, new Pair<>(trajet.getArrivee().getCoordinate(), idLivr));
                detailsLivraisons.getChildren().add(button);
            }

            //infoButton = "Fin de tournée - \n Arrivée : " + formater.format(trajet.getHeureArrivee());

            detailsLivraisons.setVisible(true);

            trackTrajet = new CoordinateLine(tourneeCoordinate).setColor(Color.DARKRED).setWidth(8);
            trackTrajet.setVisible(true);

            // add the tracks
            mapView.addCoordinateLine(trackTrajet);
            // System.out.println("Tournee: " + trackTrajet.toString());

        } else {
            System.out.println("IMPOSSIBLE DE CALCULER UNE TOURNEE aucune demande n'a été chargée");
        }
    }

    public void clearTournee() {
        mapView.removeCoordinateLine(trackTrajet);
        tourneeCoordinate.clear();
        detailsLivraisons.getChildren().clear();
        livrButtons.clear();
        for (Map.Entry<Coordinate, MapLabel> entry : deliveriesNumbers.entrySet()) {
            mapView.removeLabel(entry.getValue());
        }
        deliveriesNumbers.clear();
        scroll.setVisible(true);
        scroll.setContent(detailsLivraisons);
    }

    public void disableButtonsTournee(boolean value) {
        ajoutLivraison.setDisable(value);
        supprLivraison.setDisable(value);
        exportFeuille.setDisable(value);
        if (historique.size() > 1 && indexHistorique > 0) {
            retour.setDisable(value);
        } else {
            retour.setDisable(true);
        }
        if (indexHistorique<historique.size()-1) {
            suivant.setDisable(value);
        } else {
            suivant.setDisable(true);
        }
    }

    /**
     *
     */
    public void setButtonRetour() {
        retour.setOnAction(event -> {
            indexHistorique--;
            System.out.println("RETOUR à la tournée d'index=" + indexHistorique);
            tournee = historique.get(indexHistorique);
            demande = tournee.getDemande();
            afficherDemande();
            afficherTournee(tournee);
        });
    }

    /**
     *
     */
    public void setButtonSuivant() {
        suivant.setOnAction(event -> {
            indexHistorique++;
            System.out.println("SUIVANT à la tournée d'index=" + indexHistorique);
            tournee = historique.get(indexHistorique);
            demande = tournee.getDemande();
            afficherDemande();
            afficherTournee(tournee);
        });
    }
    /** Méthode qui permet de créer
     *
     */

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
        } else if (e.getActionCommand().equals("newResultFound")) {
            afficherTourneeCalculee();
        }
    }
}