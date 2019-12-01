package Algo;

import Modeles.Graphe;
import Modeles.Intersection;
import Modeles.Trajet;
import Modeles.Troncon;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlusCourtChemin {

    public static HashMap<Long, Trajet> dijkstra(Intersection origine,
                                                 Intersection arrivee)
    {
        //Dijkstra variables
        HashMap<Long,Trajet> trajetsPourIntersection = new HashMap<>(); //Liste du trajet optimal trouvé pour chaque interesection
        ArrayList<Long> consideredIntersections = new ArrayList<>(); //Liste des intersection non visités ayant un trajet depuis l'origine

        //Pour le point de départ mettre son trajet optimal à un trajet partant de lui meme sans aucun troncon.
        trajetsPourIntersection.put(origine.getId(),new Trajet(origine));
        consideredIntersections.add(origine.getId());

        while(true) {
            //Trouver la prochaine intersection à visiter (la plus proche du point de départ)
            Intersection intersection = selectNearestIntersection(trajetsPourIntersection, consideredIntersections);

            //Vérifier qu'elle existe et que ce n'est pas la destination finale
            if (intersection == null || intersection.getId()==arrivee.getId()) {
                break;
            }

            //Pour chaque troncon de l'intersection, calculer le nouveau trajet optimal de sa destination
            for (Troncon troncon: intersection.getTroncons()) {
                Trajet newTrajet = newTrajetFromAddingTronconToTrajet(troncon, trajetsPourIntersection.get(intersection.getId()));
                relacher(newTrajet,trajetsPourIntersection,consideredIntersections);
            }

            //Ajouter l'intersection aux intersections déjà visitées
            consideredIntersections.remove(intersection.getId());
        }

        return trajetsPourIntersection;

    }

    public static Intersection selectNearestIntersection(HashMap<Long,Trajet> trajetsPourIntersection,
                                                         ArrayList<Long> consideredIntersections)
    {
        Trajet nearestIntersectionTrajet = null;

        //Pour prendre le trajet optimal de chaque intersection du graphe
        for (Long intersectionId : consideredIntersections) {
            //Verifier qu'il n'est pas null (intersection jamais atteinte) et qu'il n'a pas deja ete visité
            Trajet trajet = trajetsPourIntersection.get(intersectionId);
            if (trajet != null) {
                //Si c'est la plus proche intersection alors on mets son itineraire depuis l'origine dans la variable
                if (nearestIntersectionTrajet == null || nearestIntersectionTrajet.getLongueur() > trajet.getLongueur()) {
                    nearestIntersectionTrajet = trajet;
                }
            }
        }

        //On renvoit la destination encore jamais visité du trajet le plus cours depuis l'origine si elle existe
        return nearestIntersectionTrajet == null ? null : nearestIntersectionTrajet.getArrivee();
    }


    public static void relacher(Trajet newTrajet,
                                                 HashMap<Long, Trajet> trajetsPourIntersection,
                                                 ArrayList<Long> consideredIntersections)
    {
        Trajet trajetActuel = trajetsPourIntersection.get(newTrajet.getArrivee().getId());

        if (trajetActuel==null) {
            //Si l'intersection est atteinte pout la premiere fois alors on enregistre l'itineraire correspondant
            trajetsPourIntersection.put(newTrajet.getArrivee().getId(),newTrajet);
            //On ajoute l'intersection aux intersections considérés pour la prochaine iteration
            consideredIntersections.add(newTrajet.getArrivee().getId());
        } else if (trajetActuel.getLongueur() > newTrajet.getLongueur()) {
            //Si le nouveau trajet pour arriver a destination est meilleur que l'ancien
            trajetsPourIntersection.replace(newTrajet.getArrivee().getId(),newTrajet);
        }
    }

    public static Trajet newTrajetFromAddingTronconToTrajet(Troncon arc,
                                                            Trajet previousTrajet)
    {
        Trajet newTrajet = new Trajet(previousTrajet.getOrigine());
        newTrajet.addTroncons(previousTrajet.getTroncons());
        newTrajet.addTroncon(arc);
        return newTrajet;
    }

}
