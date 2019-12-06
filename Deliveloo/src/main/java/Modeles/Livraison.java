package Modeles;

import java.util.Date;

public class Livraison {
    private Intersection pickup;
    private Intersection delivery;
    int dureeEnlevement;
    int dureeLivraison;
    Long id;

    public Livraison(Long id, Intersection pickup, Intersection delivery, int dE, int dL) {
        this.pickup = pickup;
        this.delivery = delivery;
        this.dureeEnlevement = dE;
        this.dureeLivraison = dL;
        this.id = id;
    }

    public Intersection getPickup() {
        return pickup;
    }

    public void setPickup(Intersection pickup) {
        this.pickup = pickup;
    }

    public Intersection getDelivery() {
        return delivery;
    }

    public void setDelivery(Intersection delivery) {
        this.delivery = delivery;
    }

    public int getDureeEnlevement() { return dureeEnlevement; }

    public int getDureeLivraison() { return dureeLivraison; }

    public void setDureeEnlevement(int dureeEnlevement) { this.dureeEnlevement = dureeEnlevement; }

    public void setDureeLivraison(int dureeLivraison) { this.dureeLivraison = dureeLivraison; }
}