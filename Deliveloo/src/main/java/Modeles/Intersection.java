package Modeles;

import com.sothawo.mapjfx.Coordinate; // OBJET COORDINATE QUI REMPLACE POINT, classe ici :
// https://github.com/sothawo/mapjfx/blob/master/src/main/java/com/sothawo/mapjfx/Coordinate.java

import java.util.ArrayList;
import java.util.List;

public class Intersection {
    private int id;
    private Coordinate p;
    private List<Troncon> troncons;

    public Intersection(int id, Coordinate p) {
        this.id = id;
        this.p = p;
        this.troncons = new ArrayList<Troncon>();
    }

    public int getId() {
        return id;
    }

    public void addTroncon(Troncon t) {
        troncons.add(t);
    }

    public Coordinate getP() {
        return p;
    }

    public void setP(Coordinate p) {
        this.p = p;
    }

    public List<Troncon> getTroncons() {
        return troncons;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Intersection{" +
                "p=" + p +
                "nbTroncons="+troncons.size() +
                '}';
    }
}