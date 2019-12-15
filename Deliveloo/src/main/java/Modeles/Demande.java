package Modeles;

import com.sothawo.mapjfx.Coordinate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe representant une demande de tournee. Contient une liste des livraisons demandees, la localisation de l'entrepot et l'heure de depart.
 */
public class Demande {
    private ArrayList<Livraison> livraisons;
    private Intersection entrepot;
    private Date heureDepart;

    /**
     *
     * @param entrepot
     * @param heureDepart
     */
    public Demande(Intersection entrepot, Date heureDepart) {
        this.livraisons = new ArrayList<>();
        this.entrepot = entrepot;
        this.heureDepart = heureDepart;
    }

    /**
     *
     * @return les livraisons demandees
     */
    public ArrayList<Livraison> getLivraisons() {
        return livraisons;
    }

    /**
     * ajoute une livraison a la demande
     * @param pickup
     * @param delivery
     * @param dureeEnlevement
     * @param dureeLivraison
     * @return
     */
    public Livraison addLivraison(Intersection pickup,  Intersection delivery, int dureeEnlevement, int dureeLivraison) {
        Livraison livraison = new Livraison( ((Integer) livraisons.size()).longValue(), pickup, delivery, dureeEnlevement, dureeLivraison);
        livraisons.add(livraison);
        return livraison;
    }

    /**
     * ajoute une liste de livraisons a la demande
     * @param livraisons
     */
    public void addLivraisons(ArrayList<Livraison> livraisons) {
        for (Livraison livraison: livraisons) {
            addLivraison(livraison.getPickup(), livraison.getDelivery(), livraison.dureeEnlevement, livraison.getDureeLivraison());
        }
    }

    /**
     *
     * @return l'entrepot de la demande
     */
    public Intersection getEntrepot() {
        return entrepot;
    }

    /**
     * modifie l'entrepot de la demande
     * @param entrepot
     */
    public void setEntrepot(Intersection entrepot) {
        this.entrepot = entrepot;
    }

    /**
     *
     * @return l'heure de depart de la tournee qui sera calculee
     */
    public Date getHeureDepart() {
        return heureDepart;
    }

    /**
     * modifie l'heure de depart de la tournee qui sera calculee
     * @param heureDepart
     */
    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }
}