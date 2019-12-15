package Modeles;

/**
 * classe representant le lien entre les intersections (donc les rues).
 * Ils sont orientes pour traduire la possibilite de sens uniques.
 */
public class Troncon {

    private Intersection destination;
    private String nom;
    private Double longueur;

    /**
     *
     * @param destination
     *        intersection de destination
     * @param nom
     *        nom du troncon
     * @param longueur
     */
    public Troncon(Intersection destination, String nom, Double longueur) {
        this.destination = destination;
        this.nom = nom;
        this.longueur = longueur;
    }

    /**
     *
     * @return l'intersection de destination
     */
    public Intersection getDestination() {
        return destination;
    }

    /**
     *
     * @return le nom du troncon
     */
    public String getNom() {
        return nom;
    }

    /**
     *
     * @return la longueur du troncon
     */
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