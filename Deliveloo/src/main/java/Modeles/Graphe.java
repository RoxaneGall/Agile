package Modeles;

import java.util.HashMap;

public class Graphe {

    public static Graphe shared = new Graphe();

    private HashMap<Long,Intersection> intersectionMap;

    public Graphe() {
        this.intersectionMap = new HashMap<Long, Intersection>();
    }

    public HashMap<Long,Intersection> getIntersectionMap() {
        return intersectionMap;
    }

    public void addTroncon(Troncon troncon, long origineId) {
        if (intersectionMap.containsKey(origineId)) {
            intersectionMap.get(origineId).addTroncon(troncon);
        }
    }

    public void addIntersection(Intersection intersection) {
        if (!intersectionMap.containsKey(intersection.getId())) {
            intersectionMap.put(intersection.getId(),intersection);
        }
    }
}
