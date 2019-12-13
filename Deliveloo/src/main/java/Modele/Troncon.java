package Modele;

public class Troncon {

    private Intersection destination;
    private String nom;
    private Double longueur;

    public Troncon(Intersection destination, String nom, Double longueur) {
        this.destination = destination;
        this.nom = nom;
        this.longueur = longueur;
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