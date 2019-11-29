package Modeles;

import java.util.ArrayList;


public class Trajet {
    private ArrayList<Troncon> troncons;
    private Intersection origine;
    private Intersection arrivee;
    private Double longueur;

    public Trajet(Intersection origine) {
        this.origine = origine;
        this.troncons = new ArrayList<Troncon>();
        this.arrivee = origine;
        this.longueur = 0.0;
    }

    public Double getLongueur() {
        return longueur;
    }

    public Intersection getOrigine() {
        return origine;
    }

    public Intersection getArrivee() {
        return arrivee;
    }

    public ArrayList<Troncon> getTroncons() {
        return troncons;
    }

    public void addTroncons(ArrayList<Troncon> troncons) {
        for (Troncon troncon: troncons) {
            addTroncon(troncon);
        }
    }

    public void addTroncon(Troncon troncon) {
        this.troncons.add(troncon);
        this.arrivee = troncon.getDestination();
        this.longueur += troncon.getLongueur();
    }

    @Override
    public String toString() {
        String troncontxt = "";
        for (Troncon troncon: troncons) troncontxt += "\n" + troncon.toString();
        return "Trajet{" +
                "troncons="+ troncontxt +
                "\nlongueur=" + getLongueur() +
                ", origine=" + origine +
                ", arrivee=" + getArrivee() +
                '}'+'\n';
    }
}