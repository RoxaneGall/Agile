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

    /**
     * @param al
     * Action Listener sur lequel Computations va pouvoir declencher des actions
     * Comme la notification de nouveau meilleur trajet calculé
     */
    public static void setDelegate(ActionListener al) {
        delegate = al;
    }

    /**
     * Arreter les calculs en cours
    */
    public static void endComputations() {
        tsp1.setEnd(true);
    }

    /**
     * Declenche la notification "Meilleur Trajet trouvé"
     */
    public static void betterResultFound() {
        ActionEvent action = new ActionEvent(tsp1,1,"newResultFound");
        if (delegate!=null)
            delegate.actionPerformed(action);
    }

    /**
     * Declenche la notification calcul terminé, le dernier trajet est le meilleur
     */
    public static void lastResultIsBestResult() {
        ActionEvent action = new ActionEvent(tsp1,0,"ended");
        if (delegate!=null)
            delegate.actionPerformed(action);
    }


    /**
     * Declenche le calcul de la meilleure tournée
     *
     * @param couts
     * Tableau des plus court chemin entre tous les sommets du graphe
     */
    public static void runTSP(Trajet[][] couts) {
        tsp1.chercheSolution(Integer.MAX_VALUE, couts.length,couts);
    }


    /**
     * @param couts
     * Tableau des plus court chemin entre tous les sommets du graphe
     *
     * @param demande
     * Demande associé à la tournée en calcul
     *
     * @return la meilleure tournée actuellement calculée
     */
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

        double vitesse = 15 * 1000 / 60; //En m/min

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

    /**
     * @param origine Intersection d'origine
     * @param arrivee Intersection d'arrivée
     * @return le plus court chemin entre l'origine et l'arrivée
     */
    public static Trajet getMeilleurTrajet(Intersection origine,
                                           Intersection arrivee)
    {
        return PlusCourtChemin.dijkstra(origine,arrivee).get(arrivee.getId());
    }

}
