package Modeles;

import com.sothawo.mapjfx.Coordinate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Demande {
    private ArrayList<Livraison> livraisons;
    private Intersection entrepot;
    private Date heureDepart;
    private String nomDemande;

    public Demande(Intersection entrepot, Date heureDepart, String nomDemande) {
        this.livraisons = new ArrayList<>();
        this.entrepot = entrepot;
        this.heureDepart = heureDepart;
        this.nomDemande = nomDemande;
    }

    public ArrayList<Livraison> getLivraisons() {
        return livraisons;
    }

    public Livraison addLivraison(Intersection pickup,  Intersection delivery, int dureeEnlevement, int dureeLivraison) {
        Livraison livraison = new Livraison( ((Integer) livraisons.size()).longValue(), pickup, delivery, dureeEnlevement, dureeLivraison);
        livraisons.add(livraison);
        return livraison;
    }

    public void addLivraisons(ArrayList<Livraison> livraisons) {
        for (Livraison livraison: livraisons) {
            addLivraison(livraison.getPickup(), livraison.getDelivery(), livraison.dureeEnlevement, livraison.getDureeLivraison());
        }
    }

    public Intersection getEntrepot() {
        return entrepot;
    }

    public void setEntrepot(Intersection entrepot) {
        this.entrepot = entrepot;
    }

    public Date getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }

    public String getNomDemande() {return nomDemande;}

    public void setNomDemande(String nomDemande){ this.nomDemande = nomDemande;}
}