package Vue;

import Modeles.Demande;
import Service.Service;
import com.sothawo.mapjfx.Configuration;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Projection;
import com.sothawo.mapjfx.event.MapLabelEvent;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import com.sothawo.mapjfx.offline.OfflineCache;
import javafx.beans.binding.Bindings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class Listener {
    private Service service;
    private JFileChooser choix = new JFileChooser();
    private Controller controller;

    private void InitMap(Projection projection) {
        // init MapView-Cache
        final OfflineCache offlineCache = controller.mapView.getOfflineCache();
        final String cacheDir = System.getProperty("java.io.tmpdir") + "/mapjfx-cache";

        // set the custom css file for the MapView
        controller.mapView.setCustomMapviewCssURL(getClass().getResource("/custom_mapview.css"));

        // set the controls to disabled, this will be changed when the MapView is intialized
        setTopControlsDisable(true);

        // wire the zoom button and connect the slider to the map's zoom
        controller.buttonZoom.setOnAction(event -> controller.mapView.setZoom(controller.ZOOM_DEFAULT));
        controller.sliderZoom.valueProperty().bindBidirectional(controller.mapView.zoomProperty());

        // bind the map's center and zoom properties to the corresponding labels and format them
        controller.labelCenter.textProperty().bind(Bindings.format("center: %s", controller.mapView.centerProperty()));
        controller.labelZoom.textProperty().bind(Bindings.format("zoom: %.0f", controller.mapView.zoomProperty()));


        // set the extents of the map
        chargerPlan();

        // add event Handlers to the mapView
        eventHandlers();


        // finally initialize the map view
        controller.mapView.initialize(Configuration.builder()
                .projection(projection)
                .showZoomControls(false)
                .build());

    }

    public void eventHandlers() {

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


    }

    private void chargerPlan() {

        controller.chargerPlan.setOnAction(event -> {
            String pathPlan = "";
            try {
                choix.setCurrentDirectory(new File("./datas"));
                if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    pathPlan = choix.getSelectedFile().getAbsolutePath();
                    ArrayList<Coordinate> limites = new ArrayList<Coordinate>();
                    limites = service.chargerPlan(pathPlan);
                    controller.setMapExtent(limites);
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
                Demande d = service.chargerDemande(pathDemande);
                controller.chargerDemande(d);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setTopControlsDisable(boolean flag) {
        controller.topControls.setDisable(flag);
    }

}


