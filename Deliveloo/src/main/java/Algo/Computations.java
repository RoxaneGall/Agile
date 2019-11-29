package Algo;

import Modeles.*;
import javafx.util.Pair;

import java.util.*;

public class Computations {

    public static Tournee getTourneeFromDemande(Demande demande,
                                                Graphe graphe)
    {
        return VoyageurDeCommerce.voyageurDeCommerceALaCon(demande,graphe);
    }

    public static Trajet getMeilleurTrajet(Intersection origine,
                                           Intersection arrivee,
                                           Graphe graphe)
    {
        return PlusCourtChemin.dijkstra(origine,arrivee,graphe).get(arrivee.getId());
    }

}
