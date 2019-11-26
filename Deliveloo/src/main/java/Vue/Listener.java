package Vue;

import Controleur.Controleur;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Listener implements ActionListener {
    private Controleur controleur;

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Charger Plan":
                String chemin="";
                try {
                    chargerPlan(chemin);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }

    }

    public void chargerPlan( String chemin) throws Exception {
        controleur.chargerPlan(chemin);
    }
}
