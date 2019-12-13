package Service;

import Algo.Computations;
import Donnees.EcritureXML;
import Donnees.LectureXML;
import Modeles.*;
import com.sothawo.mapjfx.Coordinate;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.*;

public class Service {

    private LectureXML lec;
    private Demande demandeEnCours;
    private Trajet[][] couts;

    public Service() throws Exception {
        lec = new LectureXML();
    }

    public ArrayList<Coordinate> chargerPlan( String path) throws Exception {
        Graphe.shared.clearGraph();
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

    public Tournee recupererTournee() {
        //Verifier que l'on veut recuperer une nouvelle tournée ou une tounrée depuis une ancienne
        return Computations.getTourneeFromDemande(couts,demandeEnCours);
    }

    public Tournee supprimerLivraison(Tournee tournee, Long idLivraison){
        Demande nouvelleDemande = new Demande(tournee.getDemande().getEntrepot(), tournee.getDemande().getHeureDepart());
        for (Livraison livraison: tournee.getDemande().getLivraisons()) {
            if (livraison.getId()!=idLivraison) {
                nouvelleDemande.addLivraison(livraison.getPickup(), livraison.getDelivery(), livraison.getDureeEnlevement(), livraison.getDureeLivraison());
            }
        }
        Tournee nouvelleTournee = new Tournee(nouvelleDemande);
        Intersection lastIntersection = null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nouvelleDemande.getHeureDepart());

        double vitesse = 15 * 1000 / 60; //En m/min

        for (Trajet trajet: tournee.getTrajets()) {

           if ((trajet.getLivraison()!=null )&&(trajet.getLivraison().getId() == idLivraison)&&(lastIntersection==null)) {
                lastIntersection = trajet.getOrigine();
            }  else if (trajet.getLivraison()==null || (trajet.getLivraison().getId() != idLivraison)) {
                Trajet nouveauTrajet = new Trajet(trajet);
                if (lastIntersection!=null) {
                    nouveauTrajet=Computations.getMeilleurTrajet(lastIntersection, trajet.getArrivee());
                    lastIntersection=null;
                }

                nouveauTrajet.setHeureDepart(calendar.getTime());

                double cyclingTime = trajet.getLongueur()/vitesse;
                calendar.add(Calendar.MINUTE, (int) cyclingTime);

                nouveauTrajet.setHeureArrivee(calendar.getTime());
                nouveauTrajet.setLivraison(trajet.getLivraison());
                nouveauTrajet.setType(trajet.getType());

                nouvelleTournee.addTrajet(nouveauTrajet);

                if (trajet.getType()== Trajet.Type.DELIVERY) {
                    calendar.add(Calendar.SECOND, (int) trajet.getLivraison().getDureeLivraison());
                } else if (trajet.getType()== Trajet.Type.PICKUP){
                    calendar.add(Calendar.SECOND, (int) trajet.getLivraison().getDureeEnlevement());
                }
            }
        }
        return nouvelleTournee;
    }

    public Tournee ajouterLivraison(Tournee tournee, Intersection pickup, Intersection delivery, int dE, int dL){
        Demande nouvelleDemande = new Demande(tournee.getDemande().getEntrepot(), tournee.getDemande().getHeureDepart());
        nouvelleDemande.addLivraisons(tournee.getDemande().getLivraisons());
        Livraison livraison = nouvelleDemande.addLivraison(pickup, delivery, dE, dL);
        Tournee nouvelleTournee = new Tournee(nouvelleDemande);

        //Recuperer le meilleur emplacement pour pickup
        Double cout_min = Double.MAX_VALUE;
        int pickupInsertionId = 0;
        int currentPickupInsertionId = 0;
        for (Trajet trajet: tournee.getTrajets()) {
            Trajet trajet1=Computations.getMeilleurTrajet(trajet.getOrigine(), pickup);
            Trajet trajet2=Computations.getMeilleurTrajet(pickup, trajet.getArrivee());
            Double cout = trajet1.getLongueur()+trajet2.getLongueur();
            if (cout<cout_min) {
                cout_min = cout;
                pickupInsertionId = currentPickupInsertionId;
            }
            currentPickupInsertionId++;
        }
        //Recuperer le meilleur emplacement pour delivery
        cout_min = Double.MAX_VALUE;
        int deliveryInsertionId = 0;
        int currentDeliveryInsertionId = 0;

        for (Trajet trajet: tournee.getTrajets()) {
            if (currentDeliveryInsertionId>pickupInsertionId) {
                Trajet trajet1=Computations.getMeilleurTrajet(trajet.getOrigine(), delivery);
                Trajet trajet2=Computations.getMeilleurTrajet(delivery, trajet.getArrivee());
                Double cout = trajet1.getLongueur()+trajet2.getLongueur();
                if (cout<cout_min) {
                    cout_min = cout;
                    deliveryInsertionId = currentDeliveryInsertionId;
                }
            }
            currentDeliveryInsertionId++;
        }
        
        //Creation des trajets correspondant
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nouvelleDemande.getHeureDepart());

        double vitesse = 15 * 1000 / 60; //En m/min


        int currentInsertionId = 0;
        for (Trajet t: tournee.getTrajets()) {

            Trajet trajet = new Trajet(t);

            if (currentInsertionId==pickupInsertionId||currentInsertionId==deliveryInsertionId) {
                Trajet nouveauTrajet = null;
                if (currentInsertionId==pickupInsertionId) {
                    nouveauTrajet = Computations.getMeilleurTrajet(t.getOrigine(), pickup);
                    nouveauTrajet.setType(Trajet.Type.PICKUP);
                    nouveauTrajet.setLivraison(livraison);
                    trajet = Computations.getMeilleurTrajet(pickup, t.getArrivee());
                } else {
                    nouveauTrajet = Computations.getMeilleurTrajet(t.getOrigine(), delivery);
                    nouveauTrajet.setType(Trajet.Type.DELIVERY);
                    nouveauTrajet.setLivraison(livraison);
                    trajet = Computations.getMeilleurTrajet(delivery, t.getArrivee());
                }
                trajet.setLivraison(t.getLivraison());
                trajet.setType(t.getType());

                nouveauTrajet.setHeureDepart(calendar.getTime());

                double cyclingTime = nouveauTrajet.getLongueur()/vitesse;
                calendar.add(Calendar.MINUTE, (int) cyclingTime);

                nouveauTrajet.setHeureArrivee(calendar.getTime());

                nouvelleTournee.addTrajet(nouveauTrajet);

                if (nouveauTrajet.getType()== Trajet.Type.DELIVERY) {
                    calendar.add(Calendar.SECOND, (int) nouveauTrajet.getLivraison().getDureeLivraison());
                } else if (nouveauTrajet.getType()== Trajet.Type.PICKUP){
                    calendar.add(Calendar.SECOND, (int) nouveauTrajet.getLivraison().getDureeEnlevement());
                }

            }
            trajet.setHeureDepart(calendar.getTime());

            double cyclingTime = trajet.getLongueur()/vitesse;
            calendar.add(Calendar.MINUTE, (int) cyclingTime);

            trajet.setHeureArrivee(calendar.getTime());

            nouvelleTournee.addTrajet(trajet);

            if (trajet.getType()== Trajet.Type.DELIVERY) {
                calendar.add(Calendar.SECOND, (int) trajet.getLivraison().getDureeLivraison());
            } else if (trajet.getType()== Trajet.Type.PICKUP){
                calendar.add(Calendar.SECOND, (int) trajet.getLivraison().getDureeEnlevement());
            }

            currentInsertionId++;
        }
        return nouvelleTournee;
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

    public Intersection intersectionPlusProche(Coordinate coord){
        Intersection inter = lec.getIntersectionPlusProche(coord);
        return inter;
    }


}
