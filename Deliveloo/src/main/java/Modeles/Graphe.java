package Modeles;

import java.util.HashMap;

/**
 * Classe representant le graphe de la ville de lyon
 */
public class Graphe {

    public static Graphe shared = new Graphe();
    private HashMap<Long,Intersection> intersectionMap;

    public Graphe() {this.intersectionMap = new HashMap<Long, Intersection>();}

    /**
     *
     * @return les intersections de la carte
     */
    public HashMap<Long,Intersection> getIntersectionMap() {return intersectionMap;}

    /**
     * ajoute un troncon au graphe de la ville
     * @param troncon
     * @param origineId
     */
    public void addTroncon(Troncon troncon, long origineId) {
        if (intersectionMap.containsKey(origineId)) {
            intersectionMap.get(origineId).addTroncon(troncon);
        }
    }

    /**
     * ajoute une intersection au graphe de la ville
     * @param intersection
     */
    public void addIntersection(Intersection intersection) {
        if (!intersectionMap.containsKey(intersection.getId())) {
            intersectionMap.put(intersection.getId(),intersection);
        }
    }

    public void clearGraph() {this.intersectionMap.clear();}
}
