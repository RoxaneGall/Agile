package Algo;

import Modeles.*;

public class VoyageurDeCommerce {

    public static Tournee voyageurDeCommerceALaCon(Demande demande)
    {
        Tournee tourneePasOptimale = new Tournee(demande);
        Intersection lastLocation = demande.getEntrepot();
        for (Livraison livraison: demande.getLivraisons()) {
            Trajet meilleurTrajet = Computations.getMeilleurTrajet(lastLocation,livraison.getPickup());
            tourneePasOptimale.addTrajet(meilleurTrajet);
            meilleurTrajet = Computations.getMeilleurTrajet(livraison.getPickup(),livraison.getDelivery());
            tourneePasOptimale.addTrajet(meilleurTrajet);
            lastLocation = livraison.getDelivery();
        }
        //revenir à l'entrepot
        Trajet meilleurTrajet = Computations.getMeilleurTrajet(lastLocation,demande.getEntrepot());
        tourneePasOptimale.addTrajet(meilleurTrajet);
        //Idée d'amelioration : explorer toutes les possibilités avec des contraintes et choisir la meilleure
        return tourneePasOptimale;
    }
}
