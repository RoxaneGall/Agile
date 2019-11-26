package Vue;

import Service.Service;
import com.sothawo.mapjfx.Coordinate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class Listener implements ActionListener {
    private Service service;
    private JFileChooser choix = new JFileChooser();
    private Controller controller;


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Charger Plan":
                String path="";
                try {
                    choix.setCurrentDirectory(new File("./datas"));
                    if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        path = choix.getSelectedFile().getAbsolutePath();
                        chargerPlan(path);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }

    }

    public void chargerPlan( String path) throws Exception {
        ArrayList<Coordinate> limites=new ArrayList<Coordinate>();
        limites= service.chargerPlan(path);
        controller.setExtentMap(limites);
    }

    public void chargerDemande()  {

    }
}
