package Modeles;

/**
 * Classe representant les instructions pour effectuer une livraison. Ex : "dans 200m prenez à gauche sur ..."
 */
public class InstructionLivraison {

    enum Direction {
        GAUCHE,
        DROITE,
        TOUTDROIT,
        LEGERGAUCHE,
        LEGERDROIT,
        NONE;

        @Override
        public String toString() {
            switch (this) {
                case DROITE: return "prenez à droite";
                case GAUCHE: return "prenez à gauche";
                case LEGERDROIT: return "tournez légèrement à droite";
                case LEGERGAUCHE: return "tournez légèrement à gauche";
                case TOUTDROIT: return "continuez tout droit";
            }
            return "";
        }
    }

    private String nomRue;
    private Direction direction;
    private double distance;

    /**
     *
     * @param nomRue
     * @param direction
     * @param distance
     */
    public InstructionLivraison(String nomRue, Direction direction, Double distance) {
        this.nomRue = nomRue;
        this.direction = direction;
        this.distance = distance;
    }

    /**
     *
     * @return le nom de la rue sur laquelle on se trouve
     */
    public String getNomRue() {
        return nomRue;
    }

    /**
     *
     * @return la direction a prendre pour emprunter le troncon suivant
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     *
     * @return la distance sur laquelle on parcourt le troncon
     */
    public Double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        String rueTxt = "";
        if (nomRue != "") {
            rueTxt = " sur "+nomRue;
        }
        if (direction == Direction.NONE)
            return "Continuez"+rueTxt+".";
        return "Dans "+((int) distance)+" mètres, "+direction.toString()+rueTxt+".";
    }
}
