package Modeles;

import java.util.ArrayList;


public class Trajet {
    private ArrayList<Troncon> troncons;
    private Intersection origine;

    public Trajet(Intersection origine) {
        this.origine = origine;
        this.troncons = new ArrayList<Troncon>();
    }

    public Double getLongueur() {
        Double longueur = 0.0;
        for(Troncon troncon: troncons) {
            longueur+=troncon.getLongueur();
        }
        return longueur;
    }

    public Intersection getOrigine() {
        return origine;
    }

    public Intersection getArrivee() {
        return troncons.size() > 0 ? troncons.get(troncons.size()-1).getDestination() : origine;
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