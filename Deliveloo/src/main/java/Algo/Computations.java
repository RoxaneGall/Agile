package Algo;

import Modeles.*;
import javafx.util.Pair;

import java.util.*;

public class Computations {

    public static Tournee getTourneeFromDemande(Demande demande)
    {
        return VoyageurDeCommerce.voyageurDeCommerceALaCon(demande);
    }

    public static Trajet getMeilleurTrajet(Intersection origine,
                                           Intersection arrivee)
    {
        return PlusCourtChemin.dijkstra(origine,arrivee).get(arrivee.getId());
    }

}
