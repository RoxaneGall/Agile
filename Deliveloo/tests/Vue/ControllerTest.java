package Vue;

import Modele.Demande;
import Service.Service;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Extent;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.Projection;
import com.sun.javafx.application.ParametersImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import javafx.application.Application;

import java.io.File;
import java.util.ArrayList;

class ControllerTest {
    @Test
    public void mapExtentTest() throws Exception {
        ArrayList<Coordinate> limites = new ArrayList<>();
        Coordinate c1 = new Coordinate(45.778579, 4.852096);
        Coordinate c2 = new Coordinate(45.781901, 4.791063);
        Coordinate c3 = new Coordinate(45.730995, 4.859773);
        Coordinate c4 = new Coordinate(45.714939, 4.901873);
        limites.add(c1);
        limites.add(c2);
        limites.add(c3);
        limites.add(c4);
        Extent mapExtent = Extent.forCoordinates(limites);
        // min latitude
        Assertions.assertEquals(mapExtent.getMin().getLatitude(), c4.getLatitude());
        // min longitude
        Assertions.assertEquals(mapExtent.getMin().getLongitude(), c2.getLongitude());
        // max latitude
        Assertions.assertEquals(mapExtent.getMax().getLatitude(), c2.getLatitude());
        // max longitude
        Assertions.assertEquals(mapExtent.getMax().getLongitude(), c4.getLongitude());
        System.out.println(" Extent Test : PASSED ");
    }

    @Test
    public void markersPositionTest() throws Exception {

        ArrayList<Pair<Marker, Marker>> deliveriesMarkers = new ArrayList<Pair<Marker, Marker>>();
        Service service = new Service();
        service.chargerPlan("../datas/grandPlan.xml");
        String path = "../datas/demandeGrand7.xml";
        Demande demande = service.chargerDemande(path);
        for (int i = 0; i < demande.getLivraisons().size(); i++) {
            Coordinate pickUp = demande.getLivraisons().get(i).getPickup().getCoordinate();
            Coordinate delivery = demande.getLivraisons().get(i).getDelivery().getCoordinate();
            Marker markerPickUp = Marker.createProvided(Marker.Provided.BLUE).setPosition(pickUp);
            Marker markerDelivery = Marker.createProvided(Marker.Provided.BLUE).setPosition(delivery);;
            deliveriesMarkers.add(new Pair<Marker, Marker>(markerPickUp, markerDelivery));
        }
        for (int i = 0; i < deliveriesMarkers.size(); i++) {
            Assertions.assertEquals(deliveriesMarkers.get(i).getKey().getPosition(), demande.getLivraisons().get(i).getPickup().getCoordinate());
            Assertions.assertEquals(deliveriesMarkers.get(i).getValue().getPosition(), demande.getLivraisons().get(i).getDelivery().getCoordinate());
            Assertions.assertEquals(deliveriesMarkers.get(i).getKey().getPosition(), demande.getLivraisons().get(i).getPickup().getCoordinate());
            Assertions.assertEquals(deliveriesMarkers.get(i).getValue().getPosition(), demande.getLivraisons().get(i).getDelivery().getCoordinate());
        }

        System.out.println(" Deliveries markers position Test : PASSED ");


    }


}