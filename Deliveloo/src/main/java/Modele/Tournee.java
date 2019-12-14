package Modele;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Tournee {

    private ArrayList<Trajet> trajets;
    private Demande demande;

    public Tournee(Demande demande) {
        this.demande=demande;
        this.trajets = new ArrayList<>();
    }

    public Demande getDemande() {
        return demande;
    }

    public ArrayList<Trajet> getTrajets() {
        return this.trajets;
    }

    public int getTotalDistance() {
        int longueur = 0;
        for(Trajet trajet: trajets) {
            longueur+=trajet.getLongueur();
        }
        return longueur;
    }

    public int getTotalDuration() {
        return (int) (trajets.get(trajets.size()-1).getHeureArrivee().getTime()-demande.getHeureDepart().getTime())/(60*1000);
    }

    public Date getHeureArrivee() {
        return trajets.get(trajets.size()-1).getHeureArrivee();
    }

    public void addTrajets(ArrayList<Trajet> trajets){
        for (Trajet trajet: trajets){
            addTrajet(trajet);
        }
    }

    public void addTrajet(Trajet trajet){
        trajets.add(trajet);
    }

    @Override
    public String toString() {
        String txt = "";
        for (Trajet trajet: trajets) {
            txt += "\n" + trajet.toString();
        }
        return "Tournee{" +
                "trajets=" + txt +
                '}';
    }
}
