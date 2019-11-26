package Vue;

import Modeles.Demande;
import Service.Service;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.event.MapLabelEvent;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class Listener {
    private Service service;
    private JFileChooser choix = new JFileChooser();
    private Controller controller;

    private void Init() {
        // add an event handler for MapViewEvent#MAP_EXTENT and set the extent in the map
        controller.mapView.addEventHandler(MapViewEvent.MAP_EXTENT, event -> {
            event.consume();
            controller.mapView.setExtent(event.getExtent());
        });

        // add an event handler for extent changes and display them in the status label
        controller.mapView.addEventHandler(MapViewEvent.MAP_BOUNDING_EXTENT, event -> {
            event.consume();
            controller.labelExtent.setText(event.getExtent().toString());
        });

        controller.mapView.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, event -> {
            event.consume();
            controller.labelEvent.setText("Event: map right clicked at: " + event.getCoordinate());
        });
        controller.mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
            event.consume();
            controller.labelEvent.setText("Event: marker clicked: " + event.getMarker().getId());
        });
        controller.mapView.addEventHandler(MarkerEvent.MARKER_RIGHTCLICKED, event -> {
            event.consume();
            controller.labelEvent.setText("Event: marker right clicked: " + event.getMarker().getId());
        });
        controller.mapView.addEventHandler(MapLabelEvent.MAPLABEL_CLICKED, event -> {
            event.consume();
            controller.labelEvent.setText("Event: label clicked: " + event.getMapLabel().getText());
        });
        controller.mapView.addEventHandler(MapLabelEvent.MAPLABEL_RIGHTCLICKED, event -> {
            event.consume();
            controller.labelEvent.setText("Event: label right clicked: " + event.getMapLabel().getText());
        });

        controller.mapView.addEventHandler(MapViewEvent.MAP_POINTER_MOVED, event -> {
        });


        controller.buttonZoom.setOnAction(event -> controller.mapView.setZoom(controller.ZOOM_DEFAULT));
        controller.sliderZoom.valueProperty().bindBidirectional(controller.mapView.zoomProperty());
    }

    private void chargerPlanEtLivraison() {

        controller.chargerPlan.setOnAction(event -> {
            String pathPlan = "";
            try {
                choix.setCurrentDirectory(new File("./datas"));
                if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    pathPlan = choix.getSelectedFile().getAbsolutePath();
                    chargerPlan(pathPlan);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        controller.chargerDemande.setOnAction(event -> {
            String pathDemande = "";
            try {
                choix.setCurrentDirectory(new File("./datas"));
                if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    pathDemande = choix.getSelectedFile().getAbsolutePath();
                }
                chargerDemande(pathDemande);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setTopControlsDisable(boolean flag) {
        controller.topControls.setDisable(flag);
    }
    

    public void chargerPlan(String path) throws Exception {
        ArrayList<Coordinate> limites = new ArrayList<Coordinate>();
        limites = service.chargerPlan(path);
        controller.setMapExtent(limites);
    }

    public void chargerDemande(String path) throws Exception {
        Demande d = service.chargerDemande(path);
        controller.chargerDemande(d);
    }
}


