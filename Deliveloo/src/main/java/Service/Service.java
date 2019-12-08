package Service;

import Algo.Computations;
import Donnees.LectureXML;
import Modeles.*;
import com.sothawo.mapjfx.Coordinate;

import java.util.*;

public class Service {

    private LectureXML lec;
    private Demande demandeEnCours;
    private Trajet[][] couts;

    public Service() throws Exception {
        lec = new LectureXML();
    }

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
        Intersection[] intersDemande = getSommetsDemande(demande);

        //Remplissage tableau de couts avec les longueurs des trajets entre les sommets
        couts = getCoutsDemande(intersDemande);
        Computations.runTSP(couts);
    }

    public void recalculerTournee(Tournee tournee, Demande demande) {
        int index = 0;
        //Connaitre les livraisons ajoutées
        //Connaitre les livraisons supprimées
        //Regarder jusqu'ou on peut utiliser l'ancienne tournée

        //En creer une nouvelle a parti de l'ancienne et calculer la suite de la tournée.
    }

    public Tournee recupererTournee() {
        //Verifier que l'on veut recuperer une nouvelle tournée ou une tounrée depuis une ancienne
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
