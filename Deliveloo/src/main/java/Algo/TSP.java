package Algo;

import Modeles.Trajet;

public interface TSP {

    /**
     * @return true si chercheSolution() s'est terminee parce que la limite de temps avait ete atteinte, avant d'avoir pu explorer tout l'espace de recherche,
     */
    public Boolean getTempsLimiteAtteint();

    /**
     * Cherche un circuit de duree minimale passant par chaque sommet (compris entre 0 et nbSommets-1)
     *
     * @param tpsLimite : limite (en millisecondes) sur le temps d'execution de chercheSolution
     * @param nbSommets : nombre de sommets du graphe
     * @param cout      : cout[i][j] = longueur pour aller de i a j, avec {@literal 0 <= i < nbSommets et 0 <= j < nbSommets}
     */
    public void chercheSolution(int tpsLimite, int nbSommets, Trajet[][] cout);

    /**
     *
     * @return le sommet visite en i-eme position dans la solution calculee par chercheSolution
     */
    public Integer[] getMeilleureSolution();

    /**
     * @return la duree de la solution calculee par chercheSolution
     */
    public Double getCoutMeilleureSolution();
}
