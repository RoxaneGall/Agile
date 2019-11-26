package Algo;

import Modeles.Graphe;
import Modeles.Intersection;
import Modeles.Trajet;
import Modeles.Troncon;
import javafx.util.Pair;

import java.util.*;

public class Computations {

    //VOYAGEUR DE COMMERCE

    

    //DIJKSTRA

    public static Trajet getMeilleurTrajet(Intersection origine,
                                           Intersection arrivee,
                                           Graphe graphe)
    {
        return dijkstra(origine,arrivee,graphe).get(arrivee.getId());
    }

    public static HashMap<Integer, Trajet> dijkstra(Intersection origine, Intersection arrivee, Graphe graphe) {
        //Dijkstra variables
        HashMap<Integer,Trajet> trajetsPourIntersection = new HashMap<>(); //Liste du trajet optimal trouvé pour chaque interesection
        ArrayList<Integer> alreadyVisitedIntersections = new ArrayList<>(); //Liste des intersection déjà visitées
        //Graphe
        HashMap<Integer,Intersection> plan = graphe.getIntersectionMap(); //Toutes les intersection avec leurs id en key

        //remplissage du graphe avec des couts infinis
        for (Integer id : plan.keySet()){
            //Pour chaque intersection mettre son trajet optimal à null
            trajetsPourIntersection.put(id, null);
        }

        //Pour le point de départ mettre son trajet optimal à lui 0.
        trajetsPourIntersection.replace(origine.getId(),new Trajet(origine));

        while(true) {
            //Trouver la prochaine intersection à visiter (la plus proche du point de départ)
            Intersection intersection = selectNearestIntersection(trajetsPourIntersection, alreadyVisitedIntersections);

            //Vérifier qu'elle existe et que ce n'est pas la destination finale
            if (intersection == null || intersection.getId()==arrivee.getId()) {
                break;
            }

            //Pour chaque troncon de l'intersection, calculer le nouveau trajet optimal de sa destination
            for (Troncon troncon: intersection.getTroncons()) {
                Trajet newTrajet = newTrajetFromAddingTronconToTrajet(troncon, trajetsPourIntersection.get(intersection.getId()));
                trajetsPourIntersection = relacher(newTrajet,trajetsPourIntersection);
            }

            //Ajouter l'intersection aux intersections déjà visitées
            alreadyVisitedIntersections.add(intersection.getId());
        }

        return trajetsPourIntersection;

    }

    public static Intersection selectNearestIntersection(HashMap<Integer, Trajet> trajetsPourIntersection,
                                                            ArrayList<Integer> alreadyVisitedIntersections)
    {
        Trajet nearestIntersectionTrajet = null;

        //Pour prendre le trajet optimal de chaque intersection du graphe
        for (Trajet trajet : trajetsPourIntersection.values()) {
            //Verifier qu'il n'est pas null (intersection jamais atteinte) et qu'il n'a pas deja ete visité
            if (trajet != null && !alreadyVisitedIntersections.contains(trajet.getArrivee().getId())) {
                //Si c'est la plus proche intersection alors on mets son itineraire depuis l'origine dans la variable
                if (nearestIntersectionTrajet == null || nearestIntersectionTrajet.getLongueur() > trajet.getLongueur()) {
                    nearestIntersectionTrajet = trajet;
                }
            }
        }

        //On renvoit la destination encore jamais visité du trajet le plus cours depuis l'origine si elle existe
        return nearestIntersectionTrajet == null ? null : nearestIntersectionTrajet.getArrivee();
    }


    public static HashMap<Integer, Trajet> relacher(Trajet newTrajet, HashMap<Integer, Trajet> trajetsPourIntersection) {
        Trajet trajetActuel = trajetsPourIntersection.get(newTrajet.getArrivee().getId());

        if (trajetActuel==null) {
            //Si l'intersection est atteinte pout la premiere fois alors on enregistre l'itineraire correspondant
            trajetsPourIntersection.put(newTrajet.getArrivee().getId(),newTrajet);
        } else if (trajetActuel.getLongueur() > newTrajet.getLongueur()) {
            //Si le nouveau trajet pour arriver a destination est meilleur que l'ancien
            trajetsPourIntersection.replace(newTrajet.getArrivee().getId(),newTrajet);
        }
        return trajetsPourIntersection;
    }

    public static Trajet newTrajetFromAddingTronconToTrajet(Troncon arc, Trajet previousTrajet)
    {
        Trajet newTrajet = new Trajet(previousTrajet.getOrigine());
        newTrajet.addTroncons(previousTrajet.getTroncons());
        newTrajet.addTroncon(arc);
        return newTrajet;
    }

}
