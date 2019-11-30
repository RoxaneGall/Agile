package Algo;

import Modeles.Graphe;
import Modeles.Intersection;
import Modeles.Trajet;
import Modeles.Troncon;

import java.util.HashMap;
import java.util.Map;

public class PlusCourtChemin {

    public static HashMap<Long, Trajet> dijkstra(Intersection origine,
                                                 Intersection arrivee,
                                                 Graphe graphe)
    {
        //Dijkstra variables
        HashMap<Long,Trajet> trajetsPourIntersection = new HashMap<>(); //Liste du trajet optimal trouvé pour chaque interesection
        HashMap<Long,Boolean> alreadyVisitedIntersections = new HashMap<>(); //Liste des intersection déjà visitées
        //Graphe
        HashMap<Long,Intersection> plan = graphe.getIntersectionMap(); //Toutes les intersection avec leurs id en key

        //remplissage du graphe avec des couts infinis
        for (Long id : plan.keySet()){
            //Pour chaque intersection mettre son trajet optimal à null
            alreadyVisitedIntersections.put(id, false);
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
            alreadyVisitedIntersections.put(intersection.getId(),true);
        }

        return trajetsPourIntersection;

    }

    public static Intersection selectNearestIntersection(HashMap<Long,Trajet> trajetsPourIntersection,
                                                         HashMap<Long,Boolean> alreadyVisitedIntersections)
    {
        Trajet nearestIntersectionTrajet = null;

        //Pour prendre le trajet optimal de chaque intersection du graphe
        for ( Map.Entry<Long, Trajet> trajet : trajetsPourIntersection.entrySet()) {
            //Verifier qu'il n'est pas null (intersection jamais atteinte) et qu'il n'a pas deja ete visité
            if (trajet.getValue() != null && !alreadyVisitedIntersections.get(trajet.getKey())) {
                //Si c'est la plus proche intersection alors on mets son itineraire depuis l'origine dans la variable
                if (nearestIntersectionTrajet == null || nearestIntersectionTrajet.getLongueur() > trajet.getValue().getLongueur()) {
                    nearestIntersectionTrajet = trajet.getValue();
                }
            }
        }

        //On renvoit la destination encore jamais visité du trajet le plus cours depuis l'origine si elle existe
        return nearestIntersectionTrajet == null ? null : nearestIntersectionTrajet.getArrivee();
    }


    public static HashMap<Long, Trajet> relacher(Trajet newTrajet,
                                                 HashMap<Long, Trajet> trajetsPourIntersection)
    {
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

    public static Trajet newTrajetFromAddingTronconToTrajet(Troncon arc,
                                                            Trajet previousTrajet)
    {
        Trajet newTrajet = new Trajet(previousTrajet.getOrigine());
        newTrajet.addTroncons(previousTrajet.getTroncons());
        newTrajet.addTroncon(arc);
        return newTrajet;
    }

}
