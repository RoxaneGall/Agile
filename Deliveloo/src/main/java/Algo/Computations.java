package Algo;

import Modeles.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class Computations {

    private static ActionListener delegate = null;
    private static TSP1 tsp1 = new TSP1();

    public static void setDelegate(ActionListener al) {
        delegate = al;
    }

    public static void endComputations() {
        tsp1.setEnd(true);
    }

    public static void betterResultFound() {
        ActionEvent action = new ActionEvent(tsp1,1,"newResultFound");
        if (delegate!=null)
            delegate.actionPerformed(action);
    }

    public static void lastResultIsBestResult() {
        ActionEvent action = new ActionEvent(tsp1,0,"ended");
        if (delegate!=null)
            delegate.actionPerformed(action);
    }

    public static void runTSP(Trajet[][] couts, Demande demande) {
        tsp1.chercheSolution(Integer.MAX_VALUE, couts.length,couts);
    }

    public static Tournee getTourneeFromDemande(Trajet[][] couts, Demande demande)
    {
        Tournee tournee = new Tournee(demande);
        Integer lastIntersectionId = null;
        Integer[] solution = tsp1.getMeilleureSolution();

        if (solution == null || solution[0] == null) {
            return null;
        }

        for(Integer trajetId : solution) {
            if( lastIntersectionId != null) {
                tournee.addTrajet(couts[lastIntersectionId][trajetId]);
            }
            lastIntersectionId = trajetId;
        }
        tournee.addTrajet((couts[lastIntersectionId][0]));

        return tournee;
    }
    public static Trajet getMeilleurTrajet(Intersection origine,
                                           Intersection arrivee)
    {
        return PlusCourtChemin.dijkstra(origine,arrivee).get(arrivee.getId());
    }

}
