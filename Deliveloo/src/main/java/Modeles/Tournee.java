package Modeles;

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

    public double getTotalDistance() {
        Double longueur = 0.0;
        for(Trajet trajet: trajets) {
            longueur+=trajet.getLongueur();
        }
        return longueur;
    }

    public double getTotalDuration() {
        double livraisonsTotalDurationSeconds = 0;
        for(Livraison livraison: demande.getLivraisons()) {
            livraisonsTotalDurationSeconds+=livraison.getDureeEnlevement()+livraison.getDureeLivraison();
        }
        double vitesse = 14 * 1000 / 60; //En m/min
        double bicycleTotalMinutes = getTotalDistance()/vitesse;
        return bicycleTotalMinutes+livraisonsTotalDurationSeconds/60;
    }

    public Date getHeureArrivee() {
        Calendar depart = Calendar.getInstance();
        depart.set(Calendar.MINUTE, demande.getHeureDepart().getMinutes());
        depart.set(Calendar.HOUR, demande.getHeureDepart().getHours());
        depart.add(Calendar.MINUTE, (int) getTotalDuration());
        Date dateArrivee = depart.getTime();
        return dateArrivee;
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
