package Vue;

import Donnees.LectureXML;
import Modeles.*;
import Service.Service;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Extent;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.Projection;
import Modeles.Demande;
import Service.Service;
import com.sothawo.mapjfx.*;
import com.sun.javafx.application.ParametersImpl;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import javafx.application.Application;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import static com.sun.javafx.application.ParametersImpl.getParameters;

import javafx.application.Application;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    public void startAppTest() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new Main().start(new Stage()); // Create and
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // initialize
                        // your app.

                    }
                });
            }
        });
        thread.start();// Initialize the thread
        Thread.sleep(10000); // Time to use the app, with out this, the thread
        // will be killed before you can tell.
    }


    @Test
    public void mapExtentTest() throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Service service = new Service();
                            ArrayList<Coordinate> limites = service.chargerPlan("../datas/grandPlan.xml");
                            Controller contr = new Controller();
                            contr.chargerPlan("../datas/grandPlan.xml");
                            // max
                            Assertions.assertEquals(contr.mapExtent.getMax(), limites.get(2));
                            // min
                            Assertions.assertEquals(contr.mapExtent.getMin(), limites.get(1));
                            System.out.println(" Extent Test : PASSED ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        thread.start();
        Thread.sleep(10000);
    }


    @Test
    public void markersPositionTest() throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Service service = new Service();
                            Controller contr = new Controller();
                            contr.chargerPlan("../datas/grandPlan.xml");
                            String path = "../datas/demandeGrand9.xml";
                            Demande demande = service.chargerDemande(path);
                            contr.afficherDemande();
                            int i = 0;
                            for (Map.Entry<Coordinate, Marker> entry : contr.deliveriesMarkers.entrySet()) {
                                Assertions.assertTrue(entry.getValue().equals(demande.getLivraisons().get(i).getPickup().getCoordinate()) ||
                                        entry.getValue().equals(demande.getLivraisons().get(i).getDelivery().getCoordinate()));
                            }

                            System.out.println(" Deliveries markers position Test : PASSED ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        });
        thread.start();
        Thread.sleep(10000);
    }

    @Test
    static void coordinateLinePositionTest() throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Service service = new Service();
                            service.chargerPlan("../datas/grandPlan.xml");
                            String path = "../datas/demandeGrand9.xml";
                            Demande demande = service.chargerDemande(path);
                            service.calculerTournee(demande);
                            Tournee t = service.recupererTournee();
                            Controller contr = new Controller();
                            contr.afficherTournee();
                            ArrayList<Coordinate> tab = (ArrayList<Coordinate>) contr.trackTrajet.getCoordinateStream().collect(Collectors.toList());
                            int i = 0;
                            while (i < t.getTrajets().size()) {
                                Assertions.assertEquals(tab.get(i), t.getTrajets().get(i).getOrigine().getCoordinate());
                                int j = i + 1;
                                for (Troncon troncon : t.getTrajets().get(i).getTroncons()) {
                                    Assertions.assertEquals(tab.get(j), troncon.getDestination().getCoordinate());
                                    j++;
                                    i = j;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        });
        thread.start();
        Thread.sleep(10000);
    }

    @Test
    public void ajouterLivraisonTest() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Controller contr = new Controller();
                            contr.chargerPlan("../datas/grandPlan.xml");
                            Service service = new Service();
                            service.chargerDemande("../datas/grandeDemande9.xml");
                            Intersection interPickUp = new Intersection(0, new Coordinate(45.778979, 4.852126));
                            Intersection interDelivery = new Intersection(1, new Coordinate(45.738949, 4.34576));
                            ArrayList<Intersection> inters = new ArrayList<>();
                            int previousSize = contr.demande.getLivraisons().size();
                            inters.add(interPickUp);
                            inters.add(interDelivery);
                            contr.ajouterLivraison(inters);
                            Assertions.assertTrue(contr.demande.getLivraisons().size() == previousSize + 1);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        });
        thread.start();
        Thread.sleep(10000);

    }





}