package Modeles;

import java.util.Date;

/**
 * Classe representant les livraisons demandees pour une tournee
 */
public class Livraison {
    private Intersection pickup;
    private Intersection delivery;
    int dureeEnlevement;
    int dureeLivraison;
    Long id;

    /**
     * @param id
     * @param pickup
     * @param delivery
     * @param dE
     * @param dL
     */
    public Livraison(Long id, Intersection pickup, Intersection delivery, int dE, int dL) {
        this.pickup = pickup;
        this.delivery = delivery;
        this.dureeEnlevement = dE;
        this.dureeLivraison = dL;
        this.id = id;
    }

    /**
     * @return id de la livraison
     */
    public Long getId() {
        return id;
    }

    /**
     * @return l'intersection d'enlevement
     */
    public Intersection getPickup() {
        return pickup;
    }

    /**
     * modifie l'intersection d'enlevement
     *
     * @param pickup
     */
    public void setPickup(Intersection pickup) {
        this.pickup = pickup;
    }

    /**
     * @return l'intersection de depot
     */
    public Intersection getDelivery() {
        return delivery;
    }

    /**
     * modifie le point de depot
     *
     * @param delivery
     */
    public void setDelivery(Intersection delivery) {
        this.delivery = delivery;
    }

    /**
     * @return la duree d'enlevement
     */
    public int getDureeEnlevement() {
        return dureeEnlevement;
    }

    /**
     * @return la duree de depot
     */
    public int getDureeLivraison() {
        return dureeLivraison;
    }

    /**
     * modifie la duree d'enlevement
     *
     * @param dureeEnlevement
     */
    public void setDureeEnlevement(int dureeEnlevement) {
        this.dureeEnlevement = dureeEnlevement;
    }

    /**
     * modifie la duree de depot
     *
     * @param dureeLivraison
     */
    public void setDureeLivraison(int dureeLivraison) {
        this.dureeLivraison = dureeLivraison;
    }

    @Override
    public java.lang.String toString() {
        return "Livraison{" +
                pickup + " to " + delivery +
                ", enlevement=" + dureeEnlevement +
                "min(s), livraison=" + dureeLivraison +
                "min(s)}";
    }
}