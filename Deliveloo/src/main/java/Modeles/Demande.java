package Modeles;

import java.util.ArrayList;
import java.util.Date;


public class Demande {
    private ArrayList<Livraison> livraisons;
    private Intersection entrepot;
    private Double longueurTotale;
    private Date heureDepart;

    public Demande(ArrayList<Livraison> livraisons, Intersection entrepot, Date heureDepart ,Double longueurTotale) {
        this.livraisons = livraisons;
        this.entrepot = entrepot;
        this.heureDepart = heureDepart;
        this.longueurTotale = longueurTotale;
    }

    public ArrayList<Livraison> getLivraisons() {
        return livraisons;
    }

    public void setLivraisons(ArrayList<Livraison> livraisons) {
        this.livraisons = livraisons;
    }

    public Intersection getEntrepot() {
        return entrepot;
    }

    public void setEntrepot(Intersection entrepot) {
        this.entrepot = entrepot;
    }

    public Double getLongueurTotale() {
        return longueurTotale;
    }

    public void setLongueurTotale(Double longueurTotale) {
        this.longueurTotale = longueurTotale;
    }

    public Date getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }
}