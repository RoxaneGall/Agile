package Vue;

import Donnees.LectureXML;
import Modeles.Demande;
import Service.Service;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Extent;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.Projection;
import com.sun.javafx.application.ParametersImpl;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import javafx.application.Application;

import java.util.ArrayList;

import javafx.application.Application;
import static org.junit.jupiter.api.Assertions.*;

class ControllerTest  {
    @Test
    public void mapExtentTest() {
      // ArrayList<Coordinate> col = new ArrayList<>();
        Coordinate c1 = new Coordinate(45.778579, 4.852096);
        Coordinate c2 = new Coordinate(45.781901, 4.791063);
        Coordinate c3 = new Coordinate(45.730995, 4.859773);
        Coordinate c4 = new Coordinate(45.714939, 4.901873);
      /*  col.add(c1);
        col.add(c2);
        col.add(c3);
        col.add(c4);
        Extent extent = Extent.forCoordinates(col);
        // min latitude
        Assertions.assertEquals(extent.getMin().getLatitude(),c4.getLatitude());
        // min longitude
        Assertions.assertEquals(extent.getMin().getLongitude(),c2.getLongitude());
        // max latitude
        Assertions.assertEquals(extent.getMax().getLatitude(),c2.getLatitude());
        // max longitude
        Assertions.assertEquals(extent.getMax().getLongitude(),c4.getLongitude()); */

        Controller contr = new Controller() ;
      //  Main.launch();
        contr.chargerPlan();
        // min latitude
        Assertions.assertEquals(contr.mapExtent.getMin().getLatitude(),c4.getLatitude());
        // min longitude
        Assertions.assertEquals(contr.mapExtent.getMin().getLongitude(),c2.getLongitude());
        // max latitude
        Assertions.assertEquals(contr.mapExtent.getMax().getLatitude(),c2.getLatitude());
        // max longitude
        Assertions.assertEquals(contr.mapExtent.getMax().getLongitude(),c4.getLongitude());
        System.out.println(contr.mapExtent);

    }

    @Test
    public void getDeliveriesTest() throws Exception {

        Service service =new Service();
        service.chargerPlan("../datas/grandPlan.xml");

        String pathDemandePetit1 = "../datas/demandePetit1.xml";
        Demande demande = service.chargerDemande(pathDemandePetit1);
        Assertions.assertEquals(demande.getLivraisons().size(),1);

        String pathDemandePetit2 = "../datas/demandePetit2.xml";
        demande = service.chargerDemande(pathDemandePetit2);
        Assertions.assertEquals(demande.getLivraisons().size(),2);

        ArrayList<Pair<Marker,Marker>>deliveriesMarkers=new ArrayList<Pair<Marker,Marker>>();
        for (int i = 0; i < demande.getLivraisons().size(); i++) {
            Marker markerPickUp;
            Coordinate pickUp = demande.getLivraisons().get(i).getPickup().getCoordinate();
            markerPickUp = Marker.createProvided(Marker.Provided.BLUE).setPosition(pickUp);
            Marker markerDelivery;
            Coordinate delivery = demande.getLivraisons().get(i).getDelivery().getCoordinate();
            markerDelivery = Marker.createProvided(Marker.Provided.RED).setPosition(delivery);
            deliveriesMarkers.add(new Pair<Marker, Marker>(markerPickUp, markerDelivery));
        }
        for (int i = 0; i < deliveriesMarkers.size(); i++) {
            Assertions.assertEquals(deliveriesMarkers.get(i).getKey().getPosition(),demande.getLivraisons().get(i).getPickup().getCoordinate());
            Assertions.assertEquals(deliveriesMarkers.get(i).getValue().getPosition(),demande.getLivraisons().get(i).getDelivery().getCoordinate());
        }


    }
}