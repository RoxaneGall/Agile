package Modeles;

public class Troncon {

    private int id;
    private Intersection destination;
    private String nom;
    private Double longueur;

    public Troncon(int id, Intersection destination, String nom, Double longueur) {
        this.id = id;
        this.destination = destination;
        this.nom = nom;
        this.longueur = longueur;
    }

    public int getId() {
        return id;
    }

    public Intersection getDestination() {
        return destination;
    }

    public String getNom() {
        return nom;
    }

    public Double getLongueur() {
        return longueur;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Troncon{" +
                "destination=" + destination +
                ", nom='" + nom + '\'' +
                ", longueur=" + longueur +
                '}';
    }
}