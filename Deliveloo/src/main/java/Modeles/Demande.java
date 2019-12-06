package Modeles;

import com.sothawo.mapjfx.Coordinate;

import java.util.ArrayList;
import java.util.Date;


public class Demande {
    private ArrayList<Livraison> livraisons;
    private Intersection entrepot;
    private Date heureDepart;

    public Demande(ArrayList<Livraison> livraisons, Intersection entrepot, Date heureDepart) {
        this.livraisons = livraisons;
        this.entrepot = entrepot;
        this.heureDepart = heureDepart;
    }

    public ArrayList<Livraison> getLivraisons() {
        return livraisons;
    }


    public void removeLivraison(Coordinate coord) {
        System.out.println(coord);
        System.out.println(livraisons);
        for (Livraison l : livraisons) {
            // recherche de la livraison qui contient la coordinate
            if (l.getDelivery().getCoordinate().equals(coord) || l.getPickup().getCoordinate().equals(coord)) {
                // suppression de cette livraison
                livraisons.remove(l);
                break;
            }
        }
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

    public Date getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }
}