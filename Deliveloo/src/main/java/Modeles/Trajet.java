package Modeles;

import com.sothawo.mapjfx.Coordinate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.StrictMath.atan2;

/**
 * Classe representant le trajet entre deux points d'une tournee. On garde donc les points et les heures de depart et d'arrivee,
 * les troncons qu'il faut prendre pour faire le trajet, ainsi que le type de l'action effectuee a l'arrivee (pickup/delivery/retour
 * a l'entrepot) et la livraison que cette action concerne.
 */
public class Trajet {
    private ArrayList<Troncon> troncons;
    private Intersection origine;
    private Intersection arrivee;
    private Double longueur;
    private Date heureDepart;
    private Date heureArrivee;
    private Type type;
    private Livraison livraison;

    /**
     * Type d'action realisee a l'arrivee
     */
    public enum Type {
        PICKUP,
        DELIVERY,
        COMEBACKHOME;
    }

    /**
     *
     * @param origine
     */
    public Trajet(Intersection origine) {
        this.origine = origine;
        this.troncons = new ArrayList<Troncon>();
        this.arrivee = origine;
        this.longueur = 0.0;
    }

    /**
     * Constructeur de copie
     * @param trajet
     */
    public Trajet(Trajet trajet) {
        this.origine = trajet.getOrigine();
        this.troncons = trajet.getTroncons();
        this.arrivee = trajet.getArrivee();
        this.longueur = trajet.getLongueur();
        this.type = trajet.getType();
        this.livraison = trajet.getLivraison();
    }


    /**
     *
     * @return l'heure de depart
     */
    public Date getHeureDepart() {
        return heureDepart;
    }

    /**
     * modifie l'heure de depart
     * @param heureDepart
     */
    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }

    /**
     *
     * @return l'heure d'arrivee
     */
    public Date getHeureArrivee() {
        return heureArrivee;
    }

    /**
     * modifie l'heure d'arrivee
     * @param heureArrivee
     */
    public void setHeureArrivee(Date heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    /**
     *
     * @return retourne le type d'action
     */
    public Type getType() {
        return type;
    }

    /**
     * modifie le type d'action
     * @param type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     *
     * @return la livraison concernee par l'action
     */
    public Livraison getLivraison() {
        return livraison;
    }

    /**
     * modifie la livraison concernee
     * @param livraison
     */
    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    }

    /**
     *
     * @return la longueur du trajet
     */
    public Double getLongueur() {
        return longueur;
    }

    /**
     *
     * @return l'intersection d'origine du trajet
     */
    public Intersection getOrigine() {
        return origine;
    }

    /**
     *
     * @return l'intersection d'arrivee du trajet
     */
    public Intersection getArrivee() {
        return arrivee;
    }

    /**
     *
     * @return les troncons du trajet
     */
    public ArrayList<Troncon> getTroncons() {
        return troncons;
    }

    /**
     * ajoute une liste de troncons au trajet
     * @param troncons
     */
    public void addTroncons(ArrayList<Troncon> troncons) {
        for (Troncon troncon: troncons) {
            addTroncon(troncon);
        }
    }

    /**
     * ajoute un troncon au trajet
     * @param troncon
     */
    public void addTroncon(Troncon troncon) {
        this.troncons.add(troncon);
        this.arrivee = troncon.getDestination();
        this.longueur += troncon.getLongueur();
    }

    /**
     * calcul l'angle lors d'un changement de troncon
     * @param P1
     * @param P2
     * @param P3
     * @return l'angle entre les troncons empruntes
     */
    private double computeAngle(Coordinate P1, Coordinate P2, Coordinate P3) {
        return atan2(P3.getLatitude() - P1.getLatitude(), P3.getLongitude() - P1.getLongitude()) -
                atan2(P2.getLatitude() - P1.getLatitude(), P2.getLongitude() - P1.getLongitude());
    }

    /**
     *
     * @return les instructions du trajet
     */
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