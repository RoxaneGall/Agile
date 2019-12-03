package Modeles;

import com.sothawo.mapjfx.Coordinate;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.StrictMath.atan2;


public class Trajet {
    private ArrayList<Troncon> troncons;
    private Intersection origine;
    private Intersection arrivee;
    private Double longueur;
    private Double heureArrivee;

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

    private double computeAngle(Coordinate P1, Coordinate P2, Coordinate P3) {
        return atan2(P3.getLatitude() - P1.getLatitude(), P3.getLongitude() - P1.getLongitude()) -
                atan2(P2.getLatitude() - P1.getLatitude(), P2.getLongitude() - P1.getLongitude());
    }

    public ArrayList<InstructionLivraison> getInstructions() {
        ArrayList<InstructionLivraison> instructions = new ArrayList<>();
        Intersection lastLocation = origine;
        Troncon lastTroncon = null;
        double distanceSinceLastInstruction = 0;

        for (Troncon troncon : troncons) {

            InstructionLivraison.Direction direction = InstructionLivraison.Direction.TOUTDROIT;

            if (lastTroncon == null) {
                direction = InstructionLivraison.Direction.NONE;
                InstructionLivraison newInstruction = new InstructionLivraison(troncon.getNom(), direction, 0.0);
                instructions.add(newInstruction);
            } else {
                double angle = computeAngle(lastLocation.getCoordinate(), lastTroncon.getDestination().getCoordinate(), troncon.getDestination().getCoordinate());
                distanceSinceLastInstruction += lastTroncon.getLongueur();

                if (angle>0.65) {
                    direction = InstructionLivraison.Direction.GAUCHE;
                } else if (angle<-0.65) {
                    direction = InstructionLivraison.Direction.DROITE;
                } else if (angle>0.1) {
                    direction = InstructionLivraison.Direction.LEGERGAUCHE;
                } else if (angle<-0.1) {
                    direction = InstructionLivraison.Direction.LEGERDROIT;
                }

                if (angle>0.1||angle<-0.1||!lastTroncon.getNom().equals(troncon.getNom())) {
                    InstructionLivraison newInstruction = new InstructionLivraison(troncon.getNom(), direction, distanceSinceLastInstruction);
                    instructions.add(newInstruction);
                    distanceSinceLastInstruction = 0;
                }

                lastLocation = lastTroncon.getDestination();
            }

            lastTroncon = troncon;
        }
        return instructions;
    }

    @Override
    public String toString() {
        String result = "";
        ArrayList<InstructionLivraison> instructions = getInstructions();
        for (InstructionLivraison instruction: instructions) result += instruction.toString() + "\n" ;
        return result;
    }
}