package Vue;

import Algo.Computations;
import Modeles.*;
import Donnees.*;

import com.sun.org.apache.xpath.internal.operations.Bool;
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
import java.io.IOException;
import java.net.CookieHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import Service.Service;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.Triple;

import static Modeles.Trajet.Type.DELIVERY;
import static org.apache.commons.lang3.tuple.MutableTriple.of;

public class Controller implements ActionListener {

    /**
     * Attributs utiles au fonctionnement global de l'IHM
     */
    public Scene scene;
    public Service service = new Service();
    public Stage primaryStage = new Stage();
    public FileChooser fileChooser = new FileChooser(); // explorateur pour sélectionner un fichier
    public DirectoryChooser directoryChooser = new DirectoryChooser(); // explorateur pour sélectionner un dossier
    public SimpleDateFormat formater = new SimpleDateFormat("HH:mm"); // permet de formater les objets Date au format HH:mm
    public String path = "file://" + System.getProperty("user.dir").replace('\\', '/').substring(0, System.getProperty("user.dir").replace('\\', '/').lastIndexOf('/'));
    // path correspondant au chemin du dossier contenant ce code

    /**
     * Composants JFX des principales fonctionnalités de l'application
     * partie en bas (bottom) de l'IHM
     */
    @FXML
    public Button chargerPlan; // permet d'ouvrir un explorateur de fichier et de sélectionner un fichier .xml représentant un plan
    @FXML
    public Button chargerDemande; // pour sélectionner un fichier de demande
    @FXML
    public Button calculTournee; // calcul la tournée de la demande actuellement chargée
    @FXML
    public Button stopTournee; // bouton de stop qui arrête le calcul de la tournée optimale en cours
    @FXML
    public ProgressIndicator loading = new ProgressIndicator(); // indicateur de calcul de la tournée optimale en cours

    /**
     * Composant JFX de la Carte
     */
    @FXML
    public MapView mapView; // composant mapJFX de www.sothawo.com
    /**
     * Attributs pour définir le plan
     */
    public Extent mapExtent; // correspondant au cadrage de la carte
    public static final int ZOOM_DEFAULT = 14; // valeur par défaut du ZOOM

    /**
     * Composants JFX de controle de l'affichage de la carte
     * partie haute (top) de l'IHM
     */
    @FXML
    public HBox topControls; // conteneur des controls de la carte
    @FXML
    public Slider sliderZoom; // slider pour régler le zoom
    @FXML
    public Button buttonZoom; // button pour reset le zoom de la carte
    @FXML
    public Button buttonResetExtent; // bouton pour reset le cadrage de la carte

    /**
     * Composants JFX  d'affichage de tournée
     * partie droite (right) de l'IHM
     */
    @FXML
    public VBox detailsLivraisons; // conteneur des détails des livraisons
    @FXML
    public ScrollPane scroll; // permet de pouvoir scroller sur le conteneur
    @FXML
    public Button supprLivraison; // bouton pour supprimer la livraison sélectionnée
    @FXML
    public Button ajoutLivraison; // bouton pour ajouter une livraison à la demande
    @FXML
    public Text ajoutPickUp; // texte donnant les instructions pour ajouter une livraison
    @FXML
    public Button exportFeuille; // bouton pour exporter une feuille de route

    @FXML
    public ToggleGroup groupButtons = new ToggleGroup(); // groupe des boutons de livraisons
    public ToggleButton lastSelected; // dernier bouton sélectionné
    public ToggleButton lastPairSelected; // bouton jumelé au dernier bouton sélectionné
    /* Des boutons sont dits jumelés ou "paired" s'ils appartiennent à une même livraison (pick-up ou delivery) */
    public HashMap<ToggleButton, Triple<Coordinate, Long, Trajet.Type>> livrButtons = new HashMap<>(); // stockage des différents boutons et de leurs informations utiles

    @FXML
    public Label labelTourneeDistance; // distance totale de la tournée
    @FXML
    public Label labelTourneeTemps; // durée totale de la tournée
    @FXML
    public Label labelTourneeNbLivraison; // nombre de livraisons de la tournée
    /**
     * Attributs pour la tournee
     */
    public Demande demande; // demande de départ obtenue avec chargerDemande
    public Tournee tournee; // tournée calculée, utilisée quand on modifie la demande avec Ajout/Suppression de livraison
    public ArrayList<Tournee> historique = new ArrayList<>(); // historique des tournees calculées
    public int indexHistorique = -1; // au clique de précédent ou suivant on charge la tournee correspondante à cet index de l'historique
    @FXML
    public Button retour;
    @FXML
    public Button suivant;

    public Coordinate entrepot;
    public Marker entrepotMarker;

    public Boolean isAlreadyAdded = false; //paramètre qui permet de savoir si une livraison a été ajoutée


    public HashMap<Coordinate, Marker> deliveriesMarkers = new HashMap<>(); // marqueurs visuels des livraisons sur la carte
    public HashMap<Coordinate, MapLabel> deliveriesNumbers = new HashMap<>(); // numéros correspondant à l'ordre de passge des livraisons

    public CoordinateLine trackTrajet = new CoordinateLine(); // Ligne du trajet de la tournée
    public ArrayList<Coordinate> tourneeCoordinate = new ArrayList<>(); // Coordonnées traversées par la tournée

    public CoordinateLine trackPart = new CoordinateLine(); // Ligne du trajet d'une partie seulement de la tournée
    public ArrayList<Coordinate> tourneePartCoordinate = new ArrayList<>(); // Coordonnées de la tournée traversées jusqu'à la livraison sélectionnée

    /**
     * Parametres pour le serveur WMS utilisés par la composant mapView
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
     * @param mainScene
     * @param projection
     * @param primaryStageFromMain
     */
    public void initializeView(Scene mainScene, Projection projection, Stage primaryStageFromMain) {
        primaryStage = primaryStageFromMain;
        scene = mainScene;

        // On initialise les explorateurs de fichiers/dossiers en leur donnant un répertoire initial (et une extension pour la sélection de fichiers)
        fileChooser.setInitialDirectory(new File("../datas"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        directoryChooser.setInitialDirectory(new File("../datas"));

        loading.visibleProperty().setValue(false);
        if (stopTournee != null) {
            stopTournee.setDisable(true);
        }

        // initialisation MapView-Cache
        createMapCache();

        // set the custom css file for the MapView
        mapView.setCustomMapviewCssURL(getClass().getResource("/custom_mapview.css"));
        mapView.toBack();

        // Donne la valeur de zoom par défaut à la carte
        mapView.setZoom(ZOOM_DEFAULT);

        setTopControlsDisable(true); // désactive les boutons de controle de la carte
        loading.visibleProperty().setValue(false); // rend invisible l'indicateur de calcul
        stopTournee.setDisable(true); // désactive le bouton STOP de calcul d'une tournée
        disableButtonsTournee(true); // désactive les boutons de tournée (ajout de livraison, suppression, boutons pour l'historique)

        // méthode qui configure les actions de tous les boutons de l'IHM
        setButtons();

        // ajoute le gestionnaire d'évènement à la carte
        eventHandlers();

        // initialise la mapView
        mapView.initialize(Configuration.builder()
                .projection(projection)
                .showZoomControls(false)
                .build());

        // attend la fin d'initialisation de la carte
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                afterMapIsInitialized(); // réalise le chargement final de la carte
            }
        });
    }

    /**
     * Méthode appelée une fois que l'IHM est initialisée, on charge par défaut le grand Plan
     */
    private void afterMapIsInitialized() {
        chargerPlan("../datas/grandPlan.xml");
    }

    public void initalizeMapView() {
        mapView.initialize();
    }

    /**
     * Méthode permettant de mettre en cache les données chargées de la carte
     * Cela évite des rechargements nuisant au fonctionnement de l'application.
     */
    private void createMapCache() {
        // init MapView-Cache
        final OfflineCache offlineCache = mapView.getOfflineCache();
        final String cacheDir = System.getProperty("java.io.tmpdir") + "/mapjfx-cache";
        try {
            Files.createDirectories(Paths.get(cacheDir));
            offlineCache.setCacheDirectory(cacheDir);
            offlineCache.setActive(true);
        } catch (Exception e) {
            System.out.println("could not activate offline cache :" + e.getStackTrace());
        }
    }

    /**
     * Gestionnaire des évènements d'interaction avec la carte (déplacement, zoom)
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

    /**
     * Configure les actions de tous les boutons de l'IHM
     */
    private void setButtons() {
        buttonResetExtent.setOnAction(event -> { // reset le cadrage de la carte
            mapView.setExtent(mapExtent);
            mapView.setZoom(ZOOM_DEFAULT);
        });
        buttonZoom.setOnAction(event -> mapView.setZoom(ZOOM_DEFAULT)); // reset le zoom à sa valeur par défaut
        sliderZoom.valueProperty().bindBidirectional(mapView.zoomProperty()); // connecte le slider au zoom de la carte

        stopTournee.setOnAction(event -> {
            arreterChargementMeilleureTournee(); // arrête le calcul de la tournée optimale
        });

        calculTournee.setOnAction(event -> {
            try {
                calculerTourneeOptimale(); // commence le calcul de la tournée optimale
            } catch (Exception ex) { // en cas d'exception un pop-up avec le message correspondant s'affiche et aucune tournée n'est affichée
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Erreur Calcul de Tournee");
                alert.setHeaderText("Erreur Calcul de Tournee");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
                ex.printStackTrace();
            }
        });

        supprLivraison.setOnAction(event -> {
            Triple<Coordinate, Long, Trajet.Type> toRemove = recupererInfosCliquées();
            Coordinate coordToRemove = toRemove.getLeft();
            Long idLivrSupr = toRemove.getMiddle();
            Coordinate pairedToRemove = recupererPairedCoord(coordToRemove, idLivrSupr);

            // supprime les coordonnées et label aux 2 coordonnées en paramètre
            deleteMarkerByCoord(coordToRemove);
            deleteMarkerByCoord(pairedToRemove);
            deleteLabelByCoord(coordToRemove);
            deleteLabelByCoord(pairedToRemove);

            tournee = service.supprimerLivraison(tournee, idLivrSupr); // supprime la livraison et recalcule la tournée
            demande = tournee.getDemande();

            afficherTournee();
        });

        ajoutLivraison.setOnAction(event -> {
            if (!isAlreadyAdded) { // ajoute une livraison si un ajout n'est pas déjà en cours
                isAlreadyAdded = true;
                ajoutPickUp.setText("Veuillez faire un clic droit sur votre point pick up & delivery"); // texte explicatif s'affiche sur l'IHM
                ArrayList<Intersection> interLivraison = new ArrayList<Intersection>();
                addRightClickEvent(interLivraison); // attend l'interaction de l'utilisateur avec la carte pour poursuivre l'ajout
            }
        });

        exportFeuille.setOnAction(event -> {
            try {
                File selectedDirectory = directoryChooser.showDialog(primaryStage); // ouvre un explorateur de sélection du dossier où exporter la feuille de route
                if (selectedDirectory == null) {
                    System.out.println("No Directory selected");
                } else {
                    service.ecrireFichier(tournee, selectedDirectory.getAbsolutePath()); // génère une feuille de route au format .txt dans le dossier sélectionné
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        chargerDemande.setOnAction(event -> {
            File selectedFile = selectDemande(); // ouvre un explorateur de sélection du fichier demande à charger
            if (selectedFile != null) {
                clearDemande();
                clearTournee();
                afficherDemande();
            }
        });

        chargerPlan.setOnAction(event -> {
            File selectedFile = selectPlan(); // ouvre un explorateur de sélection du plan à charger
            if (selectedFile != null) {
                try {
                    chargerPlan(selectedFile.getAbsolutePath()); // charge le plan précédemment sélectionné
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        retour.setOnAction(event -> {
            indexHistorique--;
            tournee = historique.get(indexHistorique);
            demande = tournee.getDemande();
            afficherDemande();
            afficherTournee();
        });

        suivant.setOnAction(event -> {
            indexHistorique++;
            tournee = historique.get(indexHistorique);
            demande = tournee.getDemande();
            afficherDemande();
            afficherTournee();
        });

    }

    /**
     * Action réalisée lorsque l'utilisateur souhaite charger un plan
     * Un explorateur de fichier est ouvert et l'utilisateur peut sélectionner le fichier à charger comme nouveau plan
     * Si le fichier est invalide un pop-up indiquant l'exception générée s'affiche
     */
    public File selectPlan() {
        String path = "";
        File selectedFile = null;
        try {
            System.out.println("Sélection d'un plan");
            selectedFile = fileChooser.showOpenDialog(primaryStage);
        } catch (Exception e) {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Erreur chargement demande");
            dialog.getDialogPane().setContent(new Text(e.getMessage()));
            //TODO : Faire des tests pour montrer qu'on throw bien les erreurs
        }
        return selectedFile;
    }

    /**
     * Charge en mémoire un plan grâce au service correspondant et cadre la carte selon les extrémités de ce plan
     * Le plan est désigné par un graphe dans un fichier .xml
     * Si le fichier du plan n'est pas conforme un pop-up avec le message de l'exception générée est affiché
     *
     * @param path chemin d'accès du fichier à charger
     */
    public void chargerPlan(String path) {
        System.out.println("Chargement du plan " + path);
        clearTournee(); // supprime les informations de la tournée précédente s'il y en a une
        clearDemande(); // supprime les informations de la demande précédente s'il y en a une
        try {
            ArrayList<Coordinate> limites = service.chargerPlan(path); // charge le plan en mémoire
            // limites correspond à une liste des quatres coins du plan
            mapExtent = Extent.forCoordinates(limites);
            System.out.println("extentMax :" + mapExtent.getMax());
            System.out.println("extentMin :" + mapExtent.getMin());


            if (mapView != null) {
                mapView.setExtent(mapExtent); // cadre la carte en fonction du plan chargé
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


    /**
     * Méthode qui parcourt l'ensemble des boutons des points de livraisons et renvoie les information de celui sélectionné
     *
     * @return un triplet des informations (coordonnée, lidentifiant de la livraison et type) du point sélectionné
     */

    private Triple<Coordinate, Long, Trajet.Type> recupererInfosCliquées() {
        Triple<Coordinate, Long, Trajet.Type> selected = null;
        for (Map.Entry<ToggleButton, Triple<Coordinate, Long, Trajet.Type>> entry : livrButtons.entrySet()) { // parcours la liste des boutons de livraison
            if (entry.getKey().isSelected()) {
                selected = entry.getValue();
                break;
            }
        }
        return selected;
    }

    /**
     * Renvoie la coordonnée du point jumelé à celui sélectionné (par le bouton qui lui est associé)
     *
     * @param coord      coordonnée du point sélectionné
     * @param idLivrSupr identifiant de la livraison dont le point est sélectionné
     * @return
     */
    private Coordinate recupererPairedCoord(Coordinate coord, Long idLivrSupr) {
        Coordinate paired = null;
        for (Map.Entry<ToggleButton, Triple<Coordinate, Long, Trajet.Type>> entry1 : livrButtons.entrySet()) {
            if (coord != entry1.getValue().getLeft() && entry1.getValue().getMiddle() == idLivrSupr) {
                paired = entry1.getValue().getLeft();
                break;
            }
        }
        return paired;
    }




    /**
     * Méthode qui permet d'identifier la coordonnée du point sélectionner quand l'utilisateur fait un clic droit sur la map
     *
     * @param interLivraison
     */
    private void addRightClickEvent(ArrayList<Intersection> interLivraison) {

        mapView.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, eventClick -> {
            eventClick.consume();
            Coordinate coord = eventClick.getCoordinate(); //on récupère le point sélectionné
            Intersection i = service.intersectionPlusProche(coord); //on cherche l'intersection la plus proche au point
            if (i != null) { //si le point sélectionné est compris dans le plan
                int size = demande.getLivraisons().size() + 1;
                int nbLivrAjoute = interLivraison.size();
                if (nbLivrAjoute == 0) { //premier point sélectionné qui correspond au point pickUp
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
                if (nbLivrAjoute == 1) { //deuxième point sélectionné qui correspond au point delivery
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
                if (nbLivrAjoute == 2) { //si on sélectionne plus que 2 points
                    ajoutPickUp.setText("Livraison ajoutée !");
                }
            } else { //si le point sélectionné n'est pas compris dans le plan
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Erreur ajout livraison");
                alert.setHeaderText("Erreur ajout livraison");
                alert.setContentText("Veuillez sélectionner un point dans le plan !");
                alert.show();
            }
            isAlreadyAdded = false;
        });
    }

    /**
     * Méthode qui permet d'ajouter la livraison à la demande et de recalculer la tournée en ayant les
     * moins de modifications possibles.
     *
     * @param interLivraison intersections des points de livraison à ajouter
     */
    void ajouterLivraison(ArrayList<Intersection> interLivraison) {
        if (interLivraison.size() == 2) {

            ajoutPickUp.setText("");
            Intersection interPickUp = interLivraison.get(0);
            Intersection interDelivery = interLivraison.get(1);
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Veuillez rentrer la durée d'enlèvement et de livraison");
            genererLivraison(interPickUp, interDelivery);
            ajoutPickUp.setText("Livraison ajoutée !");
        }
    }

    /**
     * @param interPickUp
     * @param interDelivery
     */
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
        if (indexHistorique < historique.size() - 1) {
            int historiqueSize = historique.size();
            System.out.println("Ajout à l'index=" + indexHistorique);
            for (int i = historiqueSize - 1; i > indexHistorique; i--) {
                System.out.println("CLEAR historique for index=" + i);
                historique.remove(i);
            }
        }
        afficherTournee();
        ajoutPickUp.setText("Livraison ajoutée !");
    }


    /**
     * Un explorateur de fichier s'ouvre et l'utilisateur peut sélectionner le fichier à charger comme nouvelle demande
     * Si le fichier est invalide un pop-up indiquant l'exception générée s'affiche
     */
    private File selectDemande() {
        File selectedFile = null;
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
        }
        return selectedFile;
    }

    /**
     * Supprime de l'IHM les composants décrivant la demande (comme les marqueurs des points de livraison sur la carte)
     * La demande est un attribut global à cette classe IHM
     * réinitialise les attributs concernant la demande et désactive les boutons cliquables une fois une demande chargée
     */
    private void clearDemande() {
        try {
            scroll.setContent(null);
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

    /**
     * Affiche sur l'IHM les composant décrivant la demande (comme les marqueurs des points de livraison sur la carte)
     * La demande est un attribut global à cette classe IHM
     * réinitialise les attributs concernant la demande
     * active les boutons cliquables une fois une demande chargée
     */
    void afficherDemande() {
        try {
            clearDemande(); // on supprime la demande d'avant

            /* On récupère l'entrepot et crée son marqueur */
            entrepot = demande.getEntrepot().getCoordinate();
            URL imageURLEntrepot = new URL(path + "/datas/logos/entrepot.png");
            entrepotMarker = new Marker(imageURLEntrepot, -32, -64).setPosition(entrepot).setVisible(true);
            mapView.addMarker(entrepotMarker);

            /* On récupère les points de livraison puis on crée les marqueurs correspondant */
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

            /* On affiche sur la carte tous les marqueurs préalablement créés */
            for (Map.Entry<Coordinate, Marker> entry : deliveriesMarkers.entrySet()) {
                entry.getValue().setVisible(true);
                mapView.addMarker(entry.getValue().setVisible(true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette méthode crée un thread qui calcule la tournée optimale pour la demande en cours
     * Si un calcul était déjà en cours, il est arrêté.
     * Elle met également à jour les éléments de l'IHM concernés :
     * l'indicateur de chargement indique qu'une calcule est en cours,
     * et le bouton STOP qui permet d'arrêter le calcul est cliquable
     *
     * @throws Exception
     */
    private void calculerTourneeOptimale() throws Exception {
        System.out.println("Calcul d'une tournée");
        try {
            if (demande != null) {
                arreterChargementMeilleureTournee(); // on arrête le calcul de la tournée optimale s'il y en avait un en cours
                Computations.setDelegate(this);
                loading.visibleProperty().setValue(true);
                stopTournee.setDisable(false);
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        service.calculerTournee(demande); // l'algorithme de calcule de la tournée optimale est appelée
                    }
                });
                t1.start();
            } else {
                throw new Exception("Aucune demande à traiter. Veuillez charger une demande pour calculer une tournée");
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage(), ex);
        }
    }

    /**
     * Arrête le calcul de la tournée optimale
     */
    private void arreterChargementMeilleureTournee() {
        Computations.endComputations();
    }

    /**
     * Affiche la dernière tournée optimale calculée
     */
    private void afficherTourneeCalculee() {
        tournee = service.recupererTournee();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                afficherTournee();
            }
        });
    }

    /**
     * Méthode appelée lorsque le bouton de l'entrepôt est sélectionné
     * Le bouton cliqué est surligné en bleu, les boutons mis en évidence auparavant ne le sont plus
     * Le trajet de toute la tournée est surligné en bleu
     *
     * @param button bouton de l'entrepôt sélectionné
     */
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

    /**
     * Méthode appelée lorsque le bouton de l'entrepôt est désélectionné
     * Le bouton cliqué n'est plus surligné, le trajet de la tournée est de nouveau rouge
     *
     * @param button bouton de l'entrepôt désélectionné
     */
    private void entrepotDeselected(ToggleButton button) {
        button.setStyle(null);
        mapView.removeCoordinateLine(trackTrajet);
        trackTrajet.setColor(Color.DARKRED).setWidth(8).setVisible(true);
        mapView.addCoordinateLine(trackTrajet);
    }

    /**
     * Méthode appelée lorsque le bouton d'un point de livraison est sélectionné
     * Le bouton cliqué est surligné en bleu, les boutons mis en évidence auparavant ne le sont plus
     * Le trajet de la tournée jusqu'à ce point est surligné en bleu
     *
     * @param button bouton sélectionné
     */
    private void livraisonSelected(ToggleButton button) {
        if (lastSelected != null) {
            lastSelected.setStyle(null);
        }
        if (lastPairSelected != null) {
            lastPairSelected.setStyle(null);
        }

        /* On réinitialise l'affichage du trajet de la tournée et du trajet jusqu'à un point */
        mapView.removeCoordinateLine(trackTrajet);
        trackTrajet.setColor(Color.DARKRED).setWidth(8).setVisible(true);
        mapView.addCoordinateLine(trackTrajet);
        mapView.removeCoordinateLine(trackPart);
        tourneePartCoordinate.clear();

        ToggleButton pairedButton = recupererPairedButton(button); // on récupère le bouton jumelé à celui cliqué

        afficherTrajetJusquaPointSelectionne(button, pairedButton); // on surligne le trajet de la tournée jusqu'au point sélectionné en bleu

        /* On surligne en bleus les 2 boutons de la livraison sélectionnée et on les mémorise */
        button.setStyle("-fx-base: lightblue;");
        pairedButton.setStyle("-fx-base: lightblue;");
        lastSelected = button;
        lastPairSelected = pairedButton;
    }

    /**
     * Méthode qui renvoie le bouton jumelé au bouton passé en paramètre
     *
     * @param button
     * @return bouton jumelé
     */
    private ToggleButton recupererPairedButton(ToggleButton button) {
        ToggleButton pairedButton = null;
        Triple<Coordinate, Long, Trajet.Type> entry = livrButtons.get(button);
        for (Map.Entry<ToggleButton, Triple<Coordinate, Long, Trajet.Type>> entry1 : livrButtons.entrySet()) {
            if (button != entry1.getKey() && entry1.getValue().getMiddle() == entry.getMiddle()) {
                pairedButton = entry1.getKey();
                break;
            }
        }
        return pairedButton;
    }

    /**
     * Affiche le tracé de la tournée jusqu'au point sélectionné, surligné en bleu
     *
     * @param button bouton sélectionné
     */
    private void afficherTrajetJusquaPointSelectionne(ToggleButton button, ToggleButton pairedButton) {
        Triple<Coordinate, Long, Trajet.Type> entry = livrButtons.get(button);
        int i = 0;
        Boolean nextIter;
        do {
            nextIter = true;
            if (livrButtons.get(button).getRight() == DELIVERY) {
                nextIter = tourneePartCoordinate.contains(livrButtons.get(pairedButton).getLeft());
                if (nextIter && tourneePartCoordinate.contains(entry.getLeft())) {
                    tourneePartCoordinate.remove(entry.getLeft());
                }
            }
            tourneePartCoordinate.add(tourneeCoordinate.get(i++));
        } while (!tourneePartCoordinate.contains(entry.getLeft()) || !nextIter);
        trackPart = new CoordinateLine(tourneePartCoordinate).setColor(Color.DARKTURQUOISE).setWidth(8);
        trackPart.setVisible(true);
        mapView.addCoordinateLine(trackPart);
    }

    /**
     * Méthode appelée lorsque le bouton d'un point de livraison est désélectionné
     * Le bouton cliqué n'est plus mis en évidence
     * Le trajet de la tournée jusqu'à ce point n'est plus surligné en bleu
     *
     * @param button bouton sélectionné
     */
    private void livraisonDeselected(ToggleButton button) {
        button.setStyle(null);
        if (lastPairSelected != null) {
            lastPairSelected.setStyle(null);
        }
        mapView.removeCoordinateLine(trackPart);
        tourneePartCoordinate.clear();
    }

    /**
     * Méthode qui affiche la tournée en cours
     * <p>
     * La tournée est un attribut global du Controller
     */
    public void afficherTournee() {
        /* On ajoute la tournée à l'historique */
        if (historique.size() == 0 || historique.contains(tournee) != true) {
            // On ajoute la tournée à l'historique
            historique.add(tournee);
            indexHistorique++;
        }
        // On supprime les infos de l'ancienne tournée de l'IHM
        clearTournee();

        disableButtonsTournee(false); // les boutons tournées sont cliquables

        // On affiche les informations générales de la tournée
        labelTourneeDistance.setText("Distance: " + tournee.getTotalDistance() / 1000 + "km");
        labelTourneeTemps.setText("Temps: " + tournee.getTotalDuration() + "min");
        labelTourneeNbLivraison.setText("Nombre de livraisons: " + tournee.getDemande().getLivraisons().size());

        // On parcourt tous les trajets de la tournée pour créer le te tracé du trajet réalisé et les boutons avec les détails de chaque point de livraison
        int compteur = 1;
        Coordinate origine;
        Trajet trajet;
        for (int i = 0; i < tournee.getTrajets().size(); i++) {
            trajet = tournee.getTrajets().get(i);
            origine = trajet.getOrigine().getCoordinate();
            tourneeCoordinate.add(origine);

            MapLabel l = new MapLabel(Integer.toString(compteur), 10, -10).setPosition(tournee.getTrajets().get(i).getArrivee().getCoordinate()).setVisible(true);
            mapView.addLabel(l);
            deliveriesNumbers.put(trajet.getArrivee().getCoordinate(), l);
            compteur++;
            for (Troncon troncon : tournee.getTrajets().get(i).getTroncons()) {
                tourneeCoordinate.add(troncon.getDestination().getCoordinate());
            }
            String infoButton = "";
            Long idLivr;
            if (i == 0) {
                infoButton = "Entrepôt \nDépart : " + formater.format(tournee.getDemande().getHeureDepart()) + "\nRetour : " + formater.format(tournee.getHeureArrivee());

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
            if (i == tournee.getTrajets().size() - 1) {
                idLivr = (long) -1;
                infoButton = i + 1 + " - Retour à l'entrepôt" + "\nDépart : " + formater.format(trajet.getHeureDepart()) + "    Arrivée : " + formater.format(trajet.getHeureArrivee());
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
                    infoButton = i + 1 + " - PICKUP Livraison n°" + trajet.getLivraison().getId() + "\nDépart : " + formater.format(trajet.getHeureDepart()) + "    Arrivée : " + formater.format(trajet.getHeureArrivee());
                } else {
                    infoButton = i + 1 + " - DELIVERY Livraison n°" + trajet.getLivraison().getId() + "\nDépart : " + formater.format(trajet.getHeureDepart()) + "    Arrivée : " + formater.format(trajet.getHeureArrivee());
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

            livrButtons.put(button, Triple.of(trajet.getArrivee().getCoordinate(), idLivr, trajet.getType()));
            detailsLivraisons.getChildren().add(button);
        }
        detailsLivraisons.setVisible(true);

        /* On affiche le trajet de la tournée */
        trackTrajet = new CoordinateLine(tourneeCoordinate).setColor(Color.DARKRED).setWidth(8);
        trackTrajet.setVisible(true);
        mapView.addCoordinateLine(trackTrajet);

    }

    /**
     * Supprime de l'IHM les composants décrivant la tournée : le trajet de la tournée, les numéros qui indiquent
     * l'ordre de passage du livreur aux points de livraison sur la carte, le trajet surligné, les boutons de détails des points de livraisons
     * Réinitialise les attributs concernant la tournée
     * La tournée est un attribut global du Controller
     */
    public void clearTournee() {
        labelTourneeDistance.setText(" ");
        labelTourneeNbLivraison.setText(" ");
        labelTourneeTemps.setText(" ");

        disableButtonsTournee(true);

        mapView.removeCoordinateLine(trackTrajet);
        tourneeCoordinate.clear();
        mapView.removeCoordinateLine(trackPart);
        tourneePartCoordinate.clear();
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
        if (indexHistorique < historique.size() - 1) {
            suivant.setDisable(value);
        } else {
            suivant.setDisable(true);
        }
    }

    /**
     * Active ou désactive les boutons de controle de la carte
     *
     * @param flag true s'il faut désactiver les controles, false sinon
     */
    private void setTopControlsDisable(boolean flag) {
        topControls.setDisable(flag);
    }

    /**
     * Suppime le marqueur dont la coordonnée est passée en paramètres, de l'IHM et de la liste des marqueurs
     *
     * @param c coordonnée du marqueur à supprimer
     */
    public void deleteMarkerByCoord(Coordinate c) {
        mapView.removeMarker(deliveriesMarkers.get(c));
        deliveriesMarkers.remove(c);
    }

    /**
     * Suppime le label dont la coordonnée est passée en paramètres, de l'IHM et de la liste des labels
     *
     * @param c coordonnée du marqueur à supprimer
     */
    public void deleteLabelByCoord(Coordinate c) {
        mapView.removeLabel(deliveriesNumbers.get(c));
        deliveriesNumbers.remove(c);
    }

    /**
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ended")) {
            loading.visibleProperty().setValue(false);
            stopTournee.setDisable(true);
        } else if (e.getActionCommand().equals("newResultFound")) {
            afficherTourneeCalculee();
        }
    }
}