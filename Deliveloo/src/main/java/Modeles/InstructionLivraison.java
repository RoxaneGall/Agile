package Modeles;

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

    public InstructionLivraison(String nomRue, Direction direction, Double distance) {
        this.nomRue = nomRue;
        this.direction = direction;
        this.distance = distance;
    }

    public String getNomRue() {
        return nomRue;
    }

    public Direction getDirection() {
        return direction;
    }

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
