package Algo;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

    @Override
    /**
     * Methode devant etre redefinie par les sous-classes de TemplateTSP
     * @param sommetCrt
     * @param nonVus : tableau des sommets restant a visiter
     * @param cout : cout[i][j] = longueur pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
     * @return un iterateur permettant d'iterer sur tous les sommets de nonVus
     */
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, Double[][] cout) {
        ArrayList<Integer> sommetsPossibles = new ArrayList<Integer>();
        for (Integer sommet : nonVus) {
            if(sommet!=sommetCrt) {
                switch (sommet % 2) {
                    case 0:
                        if (!nonVus.contains(sommet - 1))
                            sommetsPossibles.add(sommet);
                        break;
                    case 1:
                        sommetsPossibles.add(sommet);
                        break;
                }
            }
        }
        return sommetsPossibles.iterator();
    }

    @Override
    /**
     * Methode devant etre redefinie par les sous-classes de TemplateTSP
     * @param sommetCourant
     * @param nonVus : tableau des sommets restant a visiter
     * @param cout : cout[i][j] = longueur pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
     * @return une borne inferieure du cout des permutations commencant par sommetCourant,
     * contenant chaque sommet de nonVus exactement une fois et terminant par le sommet 0
     */
    protected Double bound(Integer sommetCourant, ArrayList<Integer> nonVus, Double[][] cout) {

        Double cout_min = Double.MAX_VALUE;
        Double sommetLePlusProcheDeLEntrepotDesNonsVus = Double.MAX_VALUE;
        for(int i = 0; i < nonVus.size(); i ++){
            for(int j = 0; j < nonVus.size(); j++){
                if (j!=i) {
                    double cout_act = cout[nonVus.get(i)][nonVus.get(j)];
                    if (cout_act < cout_min)
                        cout_min = cout_act;
                }
            }
            if (cout[nonVus.get(i)][0] < sommetLePlusProcheDeLEntrepotDesNonsVus)
                sommetLePlusProcheDeLEntrepotDesNonsVus = cout[nonVus.get(i)][0];
        }
        return cout_min*nonVus.size() + sommetLePlusProcheDeLEntrepotDesNonsVus;
    }
}