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
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout) {

        return new IteratorSeq(nonVus, sommetCrt);
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
    protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout) {

        return 0;
    }
}