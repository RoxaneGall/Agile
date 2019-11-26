package Modeles;

import java.util.ArrayList;


public class Tournee {
    private ArrayList<Livraison> livraisons;
    private  Graphe graphe;
    private Double longueurTotale;

    public Tournee(ArrayList<Livraison> livraisons, Graphe graphe, Double longueurTotale) {
        this.livraisons = livraisons;
        this.graphe = graphe;
        this.longueurTotale = longueurTotale;
    }

    public ArrayList<Livraison> getLivraisons() {
        return livraisons;
    }

    public void setLivraisons(ArrayList<Livraison> livraisons) {
        this.livraisons = livraisons;
    }

    public Graphe getGraphe() {
        return graphe;
    }

    public void setGraphe(Graphe graphe) {
        this.graphe = graphe;
    }

    public Double getLongueurTotale() {
        return longueurTotale;
    }

    public void setLongueurTotale(Double longueurTotale) {
        this.longueurTotale = longueurTotale;
    }
}