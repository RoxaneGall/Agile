package Modeles;

import java.util.Date;

public class Livraison {
    private Intersection depart;
    private Intersection arrivee;
    int dureeEnlevement;
    int dureeLivraison;

    public Livraison(Intersection depart, Intersection arrivee, int dE, int dL) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.dureeEnlevement = dE;
        this.dureeLivraison = dL;
    }

    public Intersection getDepart() {
        return depart;
    }

    public Intersection getArrivee() {
        return arrivee;
    }

    public int getDureeEnlevement() { return dureeEnlevement; }

    public int getDureeLivraison() { return dureeLivraison; }

    public void setDepart(Intersection depart) {
        this.depart = depart;
    }

    public void setArrivee(Intersection arrivee) {
        this.arrivee = arrivee;
    }

    public void setDureeEnlevement(int dureeEnlevement) { this.dureeEnlevement = dureeEnlevement; }

    public void setDureeLivraison(int dureeLivraison) { this.dureeLivraison = dureeLivraison; }
}