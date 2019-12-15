package Modeles;

import com.sothawo.mapjfx.Coordinate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Represente la tournee calculee
 */
public class Tournee {

    private ArrayList<Trajet> trajets;
    private Demande demande;

    /**
     * @param demande
     */
    public Tournee(Demande demande) {
        this.demande = demande;
        this.trajets = new ArrayList<>();
    }

    /**
     * @return la demande sur laquelle est basee la tournee
     */
    public Demande getDemande() {
        return demande;
    }

    /**
     * @return les trajets de la tournee
     */
    public ArrayList<Trajet> getTrajets() {
        return this.trajets;
    }

    /**
     * @return la distance totale de la tournee
     */
    public int getTotalDistance() {
        int longueur = 0;
        for (Trajet trajet : trajets) {
            longueur += trajet.getLongueur();
        }
        return longueur;
    }

    /**
     * @return la duree totale de la tournee
     */
    public int getTotalDuration() {
        return (int) (trajets.get(trajets.size() - 1).getHeureArrivee().getTime() - demande.getHeureDepart().getTime()) / (60 * 1000);
    }

    /**
     * @return l'heure d'arrivee
     */
    public Date getHeureArrivee() {
        return trajets.get(trajets.size() - 1).getHeureArrivee();
    }

    /**
     * ajoute des trajets a la tournee
     *
     * @param trajets
     */
    public void addTrajets(ArrayList<Trajet> trajets) {
        for (Trajet trajet : trajets) {
            addTrajet(trajet);
        }
    }

    /**
     * ajoute un trajet a la tournee
     *
     * @param trajet
     */
    public void addTrajet(Trajet trajet) {
        trajets.add(trajet);
    }

    @Override
    public String toString() {
        String txt = "";
        for (Trajet trajet : trajets) {
            txt += "\n" + trajet.toString();
        }
        return "Tournee{" +
                "trajets=" + txt +
                '}';
    }
}
