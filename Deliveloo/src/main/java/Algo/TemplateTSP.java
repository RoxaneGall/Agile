package Algo;

import Modeles.Trajet;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class TemplateTSP implements TSP {

    private Integer[] meilleureSolution;
    private Double coutMeilleureSolution = 0.0;
    private Boolean tempsLimiteAtteint;
    private Boolean end;
    private int limitDiscrepancy = 10;

    public void setEnd(Boolean end) {
        this.end = end;
    }

    public Boolean getTempsLimiteAtteint() {
        return tempsLimiteAtteint;
    }

    public void chercheSolution(int tpsLimite, int nbSommets, Trajet[][] cout) {
        tempsLimiteAtteint = false;
        end = false;
        coutMeilleureSolution = Double.MAX_VALUE;
        limitDiscrepancy = nbSommets/2+4;
        meilleureSolution = new Integer[nbSommets];
        ArrayList<Integer> nonVus = new ArrayList<Integer>();
        for (int i = 1; i < nbSommets; i++) nonVus.add(i);
        ArrayList<Integer> vus = new ArrayList<Integer>(nbSommets);
        vus.add(0); // le premier sommet visite est 0
        branchAndBound(0,0, nonVus, vus, 0.0, cout, System.currentTimeMillis(), tpsLimite);
        Computations.lastResultIsBestResult();
    }

    public Integer[] getMeilleureSolution() {
        return meilleureSolution;
    }

    public Double getCoutMeilleureSolution() {
        return coutMeilleureSolution;
    }

    /**
     * Methode devant etre redefinie par les sous-classes de TemplateTSP
     *
     * @param sommetCourant
     * @param nonVus        : tableau des sommets restant a visiter
     * @param cout          : cout[i][j] = longueur pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
     * @return une borne inferieure du cout des permutations commencant par sommetCourant,
     * contenant chaque sommet de nonVus exactement une fois et terminant par le sommet 0
     */
    protected abstract Double bound(Integer sommetCourant, ArrayList<Integer> nonVus, Trajet[][] cout);

    /**
     * Methode devant etre redefinie par les sous-classes de TemplateTSP
     *
     * @param sommetCrt
     * @param nonVus    : tableau des sommets restant a visiter
     * @param cout      : cout[i][j] = longueur pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
     * @return un iterateur permettant d'iterer sur tous les sommets de nonVus
     */
    //Prendre en compte la notion de précédence
    protected abstract Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, Trajet[][] cout);

    /**
     * Methode definissant le patron (template) d'une resolution par separation et evaluation (branch and bound) du TSP
     *
     * @param sommetCrt le dernier sommet visite
     * @param nonVus    la liste des sommets qui n'ont pas encore ete visites
     * @param vus       la liste des sommets visites (y compris sommetCrt)
     * @param coutVus   la somme des couts des arcs du chemin passant par tous les sommets de vus + la somme des duree des sommets de vus
     * @param cout      : cout[i][j] = longueur pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
     * @param tpsDebut  : moment ou la resolution a commence
     * @param tpsLimite : limite de temps pour la resolution
     */
    void branchAndBound(int sommetCrt, int discrepancy, ArrayList<Integer> nonVus, ArrayList<Integer> vus, Double coutVus, Trajet[][] cout, long tpsDebut, int tpsLimite) {
        if (end || System.currentTimeMillis() - tpsDebut > tpsLimite) {
            tempsLimiteAtteint = true;
            return;
        }
        if (nonVus.size() == 0) { // tous les sommets ont ete visites
            coutVus += cout[sommetCrt][0].getLongueur();
            if (coutVus < coutMeilleureSolution) { // on a trouve une solution meilleure que meilleureSolution
                vus.toArray(meilleureSolution);
                coutMeilleureSolution = coutVus;
                Computations.betterResultFound();
            }
        } else if ((discrepancy<limitDiscrepancy)&&(coutVus + bound(sommetCrt, nonVus, cout) < coutMeilleureSolution)) {
            Iterator<Integer> it = iterator(sommetCrt, nonVus, cout);
            int d = 0;
            while (it.hasNext()) {
                Integer prochainSommet = it.next();
                vus.add(prochainSommet);
                nonVus.remove(prochainSommet);
                branchAndBound(prochainSommet,discrepancy+d, nonVus, vus, coutVus + cout[sommetCrt][prochainSommet].getLongueur(), cout, tpsDebut, tpsLimite);
                vus.remove(prochainSommet);
                nonVus.add(prochainSommet);
                d++;
            }
        }
    }
}