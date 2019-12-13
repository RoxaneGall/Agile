package Donnees;

import Modeles.InstructionLivraison;
import Modeles.Tournee;
import Modeles.Trajet;

import java.io.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Scanner;

public class EcritureXML {

    public EcritureXML(){
    }

    /*public String genererNomFichierDeTournee(Tournee tournee) throws Exception {

        String nomFichier="";
        int nbDeliv = tournee.getDemande().getLivraisons().size();
        String dateDemande = tournee.getDemande().getHeureDepart().toString();
        if (nbDeliv == 0) {
            throw new Exception("Cette tournée est réalisée à partir d'une demande composée d'aucune livraison.");
        } else if (nbDeliv > 0 && nbDeliv < 3) {
            nomFichier = "petiteTournee" + nbDeliv+"_"+dateDemande;
        } else if (nbDeliv >= 3 && nbDeliv < 7) {
            nomFichier = "moyenneTournee" + nbDeliv+"_"+dateDemande;
        } else if (nbDeliv > 7 && nbDeliv < 10) {
            nomFichier = "grandeTournee" + nbDeliv+"_"+dateDemande;
        } else {
            nomFichier = "tresGrandeTournee" + nbDeliv+"_"+dateDemande;
        }

        if(nomFichier.equals("")) {
            throw new Exception("Le fichier doit avoir un nom pour être créé.");
        }else return nomFichier;
    }*/

    public String genererInstructionsPourTournee(Tournee tournee){
        String instructions="";
        ArrayList<Trajet> trajets = new ArrayList<>();
        trajets = tournee.getTrajets();
        for (Trajet t : trajets){
            switch (t.getType()) {
                case COMEBACKHOME:
                    instructions += "• TRAJET : RETOUR À L'ENTREPOT \n\n";
                    break;
                case PICKUP:
                    instructions += "• TRAJET : Recupérer le colis de la livraison numéro " + t.getLivraison().getId() + "\n\n";
                    break;
                case DELIVERY:
                    instructions += "• TRAJET : Livrer le colis de la livraison numéro " + t.getLivraison().getId() + "\n\n";
                    break;
            }
            String pattern = "HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

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
            instructions += "  Itineraire : \n" + t.toString() + "\n\n";

            // ajouter aussi des lignes pour les temps d'attente aux intersections
        }
        return instructions+"\n\n";
    }

    public void ecrireFichier(Tournee tournee, String chemin) throws Exception {

        if(chemin=="") return;

        String nomFeuille = tournee.getDemande().getNomDemande();
        String extension = ".txt";
        String fichier = chemin + nomFeuille + extension;

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
