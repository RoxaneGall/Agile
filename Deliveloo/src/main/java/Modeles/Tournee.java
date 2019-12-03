package Modeles;

import com.sothawo.mapjfx.Coordinate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Tournee {

    private ArrayList<Trajet> trajets;
    private Demande demande;
    private ArrayList<Coordinate> coordTournee;
    private double vitesse = 15.0/3600.0; //En m/millis

    public Tournee(Demande demande) {
        this.demande=demande;
        this.trajets = new ArrayList<>();
    }

    public Demande getDemande() {
        return demande;
    }

    public ArrayList<Trajet> getTrajets() {
        return this.trajets;
    }

    public double getTotalDistance() {
        Double longueur = 0.0;
        for(Trajet trajet: trajets) {
            System.out.println("Trajet de longueur : "+trajet.getLongueur());
            longueur+=trajet.getLongueur();
        }
        return longueur;
    }

    public double getTotalDuration() {
        double livraisonsTotalDurationSeconds = 0;
        for(Livraison livraison: demande.getLivraisons()) {
            livraisonsTotalDurationSeconds+=livraison.getDureeEnlevement()+livraison.getDureeLivraison();
        }
        double bicycleTotalMinutes = getTotalDistance()/vitesse;
        return bicycleTotalMinutes+livraisonsTotalDurationSeconds/60;
    }

    public Date getHeureArrivee() {
        Calendar depart = Calendar.getInstance();
        depart.set(Calendar.MINUTE, demande.getHeureDepart().getMinutes());
        depart.set(Calendar.HOUR, demande.getHeureDepart().getHours());
        depart.add(Calendar.MINUTE, (int) getTotalDuration());
        Date dateArrivee = depart.getTime();
        return dateArrivee;
    }

    public ArrayList<String> getHeuresLivraisons() {
        ArrayList<String>  horairesLivr = new ArrayList<String>();

        double millis = System.currentTimeMillis();
        horairesLivr.add("Entrepôt"+"\nDépart à : "+getHourArrival(millis)); // le premier horaire de livraison

        for (int i=0; i<trajets.size(); i++) {
            millis += Math.round(trajets.get(i).getLongueur()/vitesse);
            horairesLivr.add("Etape "+i+"\n Arrivée à :"+getHourArrival(millis)+"\n Temps nécessaire :");
        }

        return horairesLivr;
    }

    public String getHourArrival(double millis) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        Date today = new Date((long) millis);
        calendar.setTime(today);  // assigns calendar to given date
        int heure = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        int minutes = calendar.get(Calendar.MINUTE);

        return heure+"h"+minutes+"min";
    }

    public void addTrajets(ArrayList<Trajet> trajets){
        for (Trajet trajet: trajets){
            addTrajet(trajet);
        }
    }

    public void addTrajet(Trajet trajet){
        trajets.add(trajet);
    }

    @Override
    public String toString() {
        String txt = "";
        for (Trajet trajet: trajets) {
            txt += "\n" + trajet.toString();
        }
        return "Tournee{" +
                "trajets=" + txt +
                '}';
    }
}
