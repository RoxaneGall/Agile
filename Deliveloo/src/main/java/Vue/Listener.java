package Vue;

import Modeles.Demande;
import Service.Service;
import com.sothawo.mapjfx.Coordinate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class Listener {
    private Service service;
    private JFileChooser choix = new JFileChooser();
    private Controller controller;

    private void setupEventHandlers() {






    }

    
}


/*    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Charger Plan":
                String pathPlan="";
                try {
                    choix.setCurrentDirectory(new File("./datas"));
                    if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        pathPlan = choix.getSelectedFile().getAbsolutePath();
                        chargerPlan(pathPlan);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case "Charger Demande":
                String pathDemande="";
                choix.setCurrentDirectory(new File("./datas"));
                if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    pathDemande = choix.getSelectedFile().getAbsolutePath();
                    try {
                        chargerDemande(pathDemande);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;

        }

    }

    public void chargerPlan( String path) throws Exception {
        ArrayList<Coordinate> limites=new ArrayList<Coordinate>();
        limites= service.chargerPlan(path);
        controller.setMapExtent(limites);
    }

    public void chargerDemande(String path) throws Exception {
        Demande d= service.chargerDemande(path);
        controller.chargerDemande(d);
    }
} */
