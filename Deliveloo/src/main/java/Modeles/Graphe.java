package Modeles;

import java.util.HashMap;

public class Graphe {

    public static Graphe shared = new Graphe();

    private HashMap<Integer,Intersection> intersectionMap;

    public Graphe() {
        this.intersectionMap = new HashMap<Integer, Intersection>();
    }

    public HashMap<Integer,Intersection> getIntersectionMap() {
        return intersectionMap;
    }

    public void addTroncon(Troncon troncon, Intersection origine) {
        if (!intersectionMap.containsKey(origine.getId())) {
            intersectionMap.get(origine.getId()).addTroncon(troncon);
        }
    }

    public void addIntersection(Intersection intersection) {
        if (!intersectionMap.containsKey(intersection.getId())) {
            intersectionMap.put(intersection.getId(),intersection);
        }
    }
}
