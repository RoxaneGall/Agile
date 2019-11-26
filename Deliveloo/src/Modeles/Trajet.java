package Modeles;

import java.util.ArrayList;


public class Trajet {
    private ArrayList<Troncon> troncons;
    private Double longueur;
    private Intersection origine;
    private Intersection arrivee;

    public Trajet(Double longueur,Intersection origine, Intersection arrivee) {
        this.longueur=longueur;
        this.origine=origine;
        this.arrivee=arrivee;
        this.troncons = new ArrayList<Troncon>();
    }

    public Double getLongueur() {
        return longueur;
    }

    public void setLongueur(Double longueur) {
        this.longueur = longueur;
    }

    public Intersection getOrigine() {
        return origine;
    }

    public void setOrigine(Intersection origine) {
        this.origine = origine;
    }

    public Intersection getArrivee() {
        return arrivee;
    }

    public void setArrivee(Intersection arrivee) {
        this.arrivee = arrivee;
    }

    public ArrayList<Troncon> getTroncons() {
        return troncons;
    }

    public void setTroncons(ArrayList<Troncon> troncons) {
        this.troncons = troncons;
    }

    public void addMeilleurTroncon (Troncon t){
        this.troncons.add(t);
    }
}