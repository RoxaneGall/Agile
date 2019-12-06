package Algo;

import Modeles.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;

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

    public static void runTSP(Trajet[][] couts) {
        tsp1.chercheSolution(Integer.MAX_VALUE, couts.length,couts);
    }

    public static Tournee getTourneeFromDemande(Trajet[][] couts, Demande demande)
    {
        Tournee tournee = new Tournee(demande);
        Integer lastIntersectionId = null;
        Integer[] solution = tsp1.getMeilleureSolution();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(demande.getHeureDepart());

        if (solution == null || solution[0] == null) {
            return null;
        }

        double vitesse = 14 * 1000 / 60; //En m/min

        for(Integer trajetId : solution) {
            if( lastIntersectionId != null) {
                Trajet trajet = couts[lastIntersectionId][trajetId];
                trajet.setHeureDepart(calendar.getTime());

                double cyclingTime = trajet.getLongueur()/vitesse;
                calendar.add(Calendar.MINUTE, (int) cyclingTime);

                trajet.setHeureArrivee(calendar.getTime());
                if (trajetId == 0) {
                    trajet.setType(Trajet.Type.COMEBACKHOME);
                } else if (trajetId%2==0) {
                    trajet.setType(Trajet.Type.DELIVERY);
                    Livraison livraison = demande.getLivraisons().get((int) (trajetId.doubleValue()/2.0+0.6)-1);
                    trajet.setLivraison(livraison);
                    calendar.add(Calendar.SECOND, (int) livraison.getDureeLivraison());
                } else {
                    trajet.setType(Trajet.Type.PICKUP);
                    Livraison livraison = demande.getLivraisons().get((int) (trajetId.doubleValue()/2.0+0.6)-1);
                    trajet.setLivraison(livraison);
                    calendar.add(Calendar.SECOND, (int) livraison.getDureeEnlevement());
                }
                tournee.addTrajet(trajet);
            }
            lastIntersectionId = trajetId;
        }
        //Ajout du dernier trajet jusqu'a l'entrepot
        Trajet trajet = (couts[lastIntersectionId][0]);
        trajet.setHeureDepart(calendar.getTime());

        double cyclingTime = trajet.getLongueur()/vitesse;
        calendar.add(Calendar.MINUTE, (int) cyclingTime);

        trajet.setHeureArrivee(calendar.getTime());
        trajet.setType(Trajet.Type.COMEBACKHOME);
        tournee.addTrajet(trajet);

        return tournee;
    }
    public static Trajet getMeilleurTrajet(Intersection origine,
                                           Intersection arrivee)
    {
        return PlusCourtChemin.dijkstra(origine,arrivee).get(arrivee.getId());
    }

}
