package Modeles;

import com.sothawo.mapjfx.Coordinate; // OBJET COORDINATE QUI REMPLACE POINT, classe ici :
// https://github.com/sothawo/mapjfx/blob/master/src/main/java/com/sothawo/mapjfx/Coordinate.java

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Intersection {
    private long id;
    private Coordinate coord;
    private ArrayList<Troncon> troncons;

    public Intersection(long id, Coordinate c) {
        this.id = id;
        this.coord = c;
        this.troncons = new ArrayList<Troncon>();
    }

    public Intersection() {

    }
    public void setIntersection(Intersection i){
        this.coord=i.coord;
        this.id=i.id;
        this.troncons=i.troncons;
    }

    public long getId() {
        return id;
    }

    public void addTroncon(Troncon t) {
        troncons.add(t);
    }

    public Coordinate getCoordinate() {
        return coord;
    }

    public Collection<Troncon> getTroncons() {
        return troncons;
    }

    @Override
    public java.lang.String toString() {
        return "Intersection{" +
                "c=" + coord +
                "nbTroncons="+troncons.size() +
                '}';
    }
}