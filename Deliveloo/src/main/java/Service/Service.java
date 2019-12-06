package Service;

import Algo.Computations;
import Donnees.LectureXML;
import Modeles.*;
import com.sothawo.mapjfx.Coordinate;

import java.util.*;

public class Service {
    public Service() throws Exception {
    }

    private LectureXML lec = new LectureXML();

    private Trajet[][] couts;
    private Demande demandeEnCours;

    public ArrayList<Coordinate> chargerPlan( String path) throws Exception {
        lec.chargerPlan(path);
        ArrayList<Coordinate> limites = lec.getLimitesPlan();
        return limites;
    }

    public Demande chargerDemande(String path) throws Exception {
        Demande d = lec.chargerDemande(path);
        return d;
    }

    public void calculerTournee(Demande demande){

        demandeEnCours = demande;
        //METHODE 1 :
        //Tournee t = Computations.getTourneeFromDemande(demande);

        //METHODE 2 :
        //Linearisation de la demande
        Intersection[] intersDemande = getSommetsDemande(demande);

        //Remplissage tableau de couts avec les longueurs des trajets entre les sommets
        couts = getCoutsDemande(intersDemande);
        Computations.runTSP(couts, demande);
    }

    public Tournee recupererTournee() {
        return Computations.getTourneeFromDemande(couts,demandeEnCours);
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

    private static Trajet[][] getCoutsDemande(Intersection[] intersDemande) {
        int nbSommets = intersDemande.length;
        Trajet [][] couts = new Trajet[nbSommets][nbSommets];

        for(int i = 0; i<nbSommets; i++) {
            for(int j = 0; j<nbSommets; j++) {
                if(i==j) {
                    couts[i][j] = new Trajet(intersDemande[i]); //Meme intersection
                } else {
                    couts[i][j] = Computations.getMeilleurTrajet(intersDemande[i],intersDemande[j]); //Calcul du cout
                }
            }
        }

        return couts;
    }


}
