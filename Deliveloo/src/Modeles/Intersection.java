package Modeles;

import java.util.ArrayList;
import java.util.List;

public class Intersection {
    private int id;
    private Point p;
    private List<Troncon> troncons;

    public Intersection(int id, Point p) {
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

    public Point getP() {
        return p;
    }

    public void setP(Point p) {
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