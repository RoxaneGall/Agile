package Algo;

import Modeles.*;

public class Computations {

    public static Tournee getTourneeFromDemande(Demande demande)
    {
        //return tsp.tempsSolution();
        return VoyageurDeCommerce.voyageurDeCommerceALaCon(demande);
    }

    public static Tournee getTourneeFromDemande(Trajet[][] couts, Demande demande)
    {
        //return tsp.tempsSolution(couts);
        TSP1 tsp1 = new TSP1();

        tsp1.chercheSolution(Integer.MAX_VALUE, couts.length,couts);

        Tournee tournee = new Tournee(demande);
        Integer lastIntersectionId = null;

        for(Integer trajetId : tsp1.getMeilleureSolution()) {
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
