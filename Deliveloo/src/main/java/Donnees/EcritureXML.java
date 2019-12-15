package Donnees;

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

    public String genererInstructionsPourTournee(Tournee tournee){
        String instructions="";
        ArrayList<Trajet> trajets = new ArrayList<>();
        trajets = tournee.getTrajets();

        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        instructions+="RECAP TOURNÉE:\n\n";
        instructions += "  Heure de départ : " + simpleDateFormat.format(tournee.getDemande().getHeureDepart()) + "\n";
        instructions += "  Heure d'arrivée : " + simpleDateFormat.format(tournee.getHeureArrivee()) + "\n";
        instructions += "  Durée du trajet : " + (tournee.getTotalDuration()) + " minutes\n";
        instructions += "  Distance totale : " + (tournee.getTotalDistance()/1000) + " km\n\n";
        instructions += "–––––––––––––––––––––––––––\n";
        for (Trajet t : trajets){
            switch (t.getType()) {
                case COMEBACKHOME:
                    instructions += "• TRAJET : RETOUR À L'ENTREPOT \n\n";
                    break;
                case PICKUP:
                    instructions += "• TRAJET : Récupérer le colis de la livraison numéro " + t.getLivraison().getId() + "\n\n";
                    break;
                case DELIVERY:
                    instructions += "• TRAJET : Livrer le colis de la livraison numéro " + t.getLivraison().getId() + "\n\n";
                    break;
            }

            instructions += "  Heure de départ : " + simpleDateFormat.format(t.getHeureDepart()) + "\n";
            instructions += "  Heure d'arrivée : " + simpleDateFormat.format(t.getHeureArrivee()) + "\n";
            instructions += "  Durée du trajet : " + (t.getHeureArrivee().getTime()-t.getHeureDepart().getTime())/(60*1000) + " minutes\n";
            switch (t.getType()) {
                case PICKUP:
                    instructions += "  Temps sur place : " + t.getLivraison().getDureeEnlevement()/(60) + " minutes \n\n";
                    break;
                case DELIVERY:
                    instructions += "  Temps sur place : " + t.getLivraison().getDureeLivraison()/(60) + " minutes \n\n";
                    break;
            }
            instructions += "  Itinéraire : \n" + t.toString() + "\n\n";

            // ajouter aussi des lignes pour les temps d'attente aux intersections
        }
        return instructions+"\n\n";
    }

    public void ecrireFichier(Tournee tournee, String chemin) throws Exception {

        if(chemin=="") return;

        String nomFeuille = tournee.getDemande().getNomDemande();
        String extension = ".txt";
        String fichier = chemin + "/" + nomFeuille + extension;

        FileOutputStream fop =null;
        File file;

        try{
            file = new File(fichier);
            if(!file.exists()){
                file.createNewFile();
            }else {
                throw new Exception("Le fichier "+ chemin + ".txt existe déjà.");
            }
            fop = new FileOutputStream(file);
            byte[] contentInBytes = genererInstructionsPourTournee(tournee).getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

        } catch (FileNotFoundException e) {
            throw new Exception(e);
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                throw new Exception(e);
            }
        }
    }
}
