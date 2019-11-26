package Modeles;

import com.sothawo.mapjfx.Coordinate; // OBJET COORDINATE QUI REMPLACE POINT, classe ici :
// https://github.com/sothawo/mapjfx/blob/master/src/main/java/com/sothawo/mapjfx/Coordinate.java

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Intersection {
    private int id;
    private Coordinate coord;
    private HashMap<Integer,Troncon> troncons;

    public Intersection(int id, Coordinate c) {
        this.id = id;
        this.coord = c;
        this.troncons = new HashMap<Integer,Troncon>();
    }

    public int getId() {
        return id;
    }

    public void addTroncon(Troncon t) {
        troncons.put(t.getDestination().getId(),t);
    }

    public Coordinate getCoordinate() {
        return coord;
    }

    public Collection<Troncon> getTroncons() {
        return troncons.values();
    }

    public Troncon getTronconFromDestinationId(Integer id) { return troncons.get(id); }

    @java.lang.Override
    public java.lang.String toString() {
        return "Intersection{" +
                "c=" + coord +
                "nbTroncons="+troncons.size() +
                '}';
    }
}