package Modeles;

import com.sothawo.mapjfx.Coordinate; // OBJET COORDINATE QUI REMPLACE POINT, classe ici :
// https://github.com/sothawo/mapjfx/blob/master/src/main/java/com/sothawo/mapjfx/Coordinate.java

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Classe representant les sommets du graphe de la ville. Possede une liste de troncons lies a l'intersection
 */
public class Intersection {
    private long id;
    private Coordinate coord;
    private ArrayList<Troncon> troncons;

    /**
     * @param id
     * @param c
     */
    public Intersection(long id, Coordinate c) {
        this.id = id;
        this.coord = c;
        this.troncons = new ArrayList<Troncon>();
    }

    public Intersection() {

    }

    /**
     * @param i
     */
    public Intersection(Intersection i) {
        this.coord = i.coord;
        this.id = i.id;
        this.troncons = i.troncons;
    }

    /**
     * @return l'identifiant de l'intersection
     */
    public long getId() {
        return id;
    }

    /**
     * lie un troncon a l'intersection
     *
     * @param t
     */
    public void addTroncon(Troncon t) {
        troncons.add(t);
    }

    /**
     * @return les coordonnees de l'intersection
     */
    public Coordinate getCoordinate() {
        return coord;
    }

    /**
     * @return les troncons lies a l'intersection
     */
    public Collection<Troncon> getTroncons() {
        return troncons;
    }

    @Override
    public java.lang.String toString() {
        return "Intersection{" +
                "c=" + coord +
                "nbTroncons=" + troncons.size() +
                '}';
    }
}