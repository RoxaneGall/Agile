package Donnees;

import Modeles.InstructionLivraison;
import Modeles.Tournee;
import Modeles.Trajet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EcritureXML {

    public EcritureXML(){

    }

    public String genererNomFichierDeTournee(Tournee tournee, String chemin) throws Exception {
        // methode a un parametre string chemin si jamais je change plus tard et que je connais le chemin du fichier
        if(chemin.equals("")) {
            int nbDeliv = tournee.getDemande().getLivraisons().size();
            String dateDemande = tournee.getDemande().getHeureDepart().toString();
            if (nbDeliv == 0) {
                throw new Exception("Cette tournée est réalisée à partir d'une demande composée d'aucune livraison.");
            } else if (nbDeliv > 0 && nbDeliv < 3) {
                chemin = "petiteTournee" + nbDeliv;
            } else if (nbDeliv >= 3 && nbDeliv < 7) {
                chemin = "moyenneTournee" + nbDeliv;
            } else if (nbDeliv > 7 && nbDeliv < 10) {
                chemin = "grandeTournee" + nbDeliv;
            } else {
                chemin = "tresGrandeTournee" + nbDeliv;
            }
        }

        if(chemin.equals("")) {
            throw new Exception("Le fichier doit avoir un nom pour être créé.");
        }else return chemin;
    }

    public String genererInstructionsPourTournee(Tournee tournee){
        String instructions="";
        ArrayList<Trajet> trajets = new ArrayList<>();
        trajets = tournee.getTrajets();
        for (Trajet t : trajets){
            switch (t.getType()) {
                case COMEBACKHOME:
                    instructions += "• TRAJET : RETOUR À L'ENTREPOT \n";
                    break;
                case PICKUP:
                    instructions += "• TRAJET : Recupérer le colis de la livraison numéro " + t.getLivraison().getId() + "\n";
                    break;
                case DELIVERY:
                    instructions += "• TRAJET : Livrer le colis de la livraison numéro " + t.getLivraison().getId() + "\n";
                    break;
            }
            String pattern = "HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            instructions += "  Heure de départ : " + simpleDateFormat.format(t.getHeureDepart()) + "\n";
            instructions += "  Heure d'arrivée : " + simpleDateFormat.format(t.getHeureArrivee()) + "\n";
            instructions += "  Durée du trajet : " + (t.getHeureArrivee().getTime()-t.getHeureDepart().getTime())/(60*1000) + " minutes\n";
            switch (t.getType()) {
                case PICKUP:
                    instructions += "  Temps sur place : " + t.getLivraison().getDureeEnlevement()/(60) + " minutes \n";
                    break;
                case DELIVERY:
                    instructions += "  Temps sur place : " + t.getLivraison().getDureeLivraison()/(60) + " minutes \n";
                    break;
            }
            instructions += "  Itineraire : \n" + t.toString() + "\n";

            // ajouter aussi des lignes pour les temps d'attente aux intersections
        }
        return instructions+"\n";
    }

    public void ecrireFichier(Tournee tournee) throws Exception {

        String chemin = genererNomFichierDeTournee(tournee,"");
        FileOutputStream fop = null;
        File file;

        try{
            file = new File(chemin);
            fop = new FileOutputStream(file);

            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] contentInBytes = genererInstructionsPourTournee(tournee).getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
