package Vue;

import Controleur.Controleur;
import com.sothawo.mapjfx.Coordinate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class Listener implements ActionListener {
    private Controleur controleur;
    private JFileChooser choix = new JFileChooser();


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
        limites=controleur.chargerPlan(path);

    }
}
