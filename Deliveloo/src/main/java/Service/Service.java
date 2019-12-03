package Service;

import Algo.Computations;
import Donnees.LectureXML;
import Modeles.*;
import com.sothawo.mapjfx.Coordinate;

import java.util.*;

public class Service {
    public Service() {
    }

    private LectureXML lec = new LectureXML();

    public ArrayList<Coordinate> chargerPlan( String path) throws Exception {
        lec.chargerPlan(path);
        ArrayList<Coordinate> limites = lec.getLimitesPlan();
        return limites;
    }

    public Demande chargerDemande(String path) throws Exception {
        Demande d = lec.chargerDemande(path);
        return d;

    }

    public Tournee calculerTournee(Demande demande) throws Exception {
        //METHODE 1 :
        //Tournee t = Computations.getTourneeFromDemande(demande);

        //METHODE 2 :
        //Linearisation de la demande
        Intersection[] intersDemande = getSommetsDemande(demande);

        //Remplissage tableau sommets avec les id d'intersection
        //long[] sommets = getIdSommetsDemande(intersDemande);

        //Remplissage tableau de couts avec les longueurs des trajets entre les sommets
        double[][] couts = getCoutsDemande(intersDemande);

        Tournee t = Computations.getTourneeFromDemande(couts);
        return t;
    }

    private static Intersection[] getSommetsDemande(Demande demande) {
        int nbSommets = 2*demande.getLivraisons().size()+1;
        Intersection[] intersDemande = new Intersection[nbSommets];

        intersDemande[0] = demande.getEntrepot();

        Iterator<Livraison> iter = demande.getLivraisons().iterator();
        int i = 1; //indice du premier pickup
        Livraison l;
        while (iter.hasNext()) {
            l = iter.next();
            intersDemande[i] =l.getPickup();
            i++;
            intersDemande[i] = l.getDelivery();
            i++;
        }
        return intersDemande;
    }

    /*private static long[] getIdSommetsDemande(Intersection[] intersDemande) {
        long[] sommets = new long[intersDemande.length];

        for(int i = 0; i<intersDemande.length; i++ ) {
            sommets[i] = intersDemande[i].getId();
        }

        return sommets;
    }*/

    private static double[][] getCoutsDemande(Intersection[] intersDemande) {
        int nbSommets = intersDemande.length;
        double [][] couts = new double[nbSommets][nbSommets];

        for(int i = 0; i<nbSommets; i++) {
            for(int j = 0; j<nbSommets; j++) {
                if(i==j) {
                    couts[i][j] = 0.0; //Meme intersection
                } else {
                    couts[i][j] = Computations.getMeilleurTrajet(intersDemande[i],intersDemande[j]).getLongueur(); //Calcul du cout
                }
            }
        }

        return couts;
    }


}
