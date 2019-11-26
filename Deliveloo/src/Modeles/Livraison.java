package Modeles;

import java.util.Date;

public class Livraison {
    private Date date;
    private Intersection depart;
    private Intersection arrivee;

    public Livraison(Date date, Intersection depart, Intersection arrivee) {
        this.date = date;
        this.depart = depart;
        this.arrivee = arrivee;
    }

    public Date getDate() {
        return date;
    }

    public Intersection getDepart() {
        return depart;
    }

    public Intersection getArrivee() {
        return arrivee;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDepart(Intersection depart) {
        this.depart = depart;
    }

    public void setArrivee(Intersection arrivee) {
        this.arrivee = arrivee;
    }
}