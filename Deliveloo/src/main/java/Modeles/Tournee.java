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

    public double getTotalDistance() {
        Double longueur = 0.0;
        for(Trajet trajet: trajets) {
            longueur+=trajet.getLongueur();
        }
        return longueur;
    }

    public double getTotalDuration() {
        double livraisonsTotalDuration = 0;
        for(Livraison livraison: demande.getLivraisons()) {
            livraisonsTotalDuration+=livraison.getDureeEnlevement()+livraison.getDureeLivraison();
        }
        double bicycleTotalMinutes = 60*getTotalDistance()/14000;
        return bicycleTotalMinutes+livraisonsTotalDuration;
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
