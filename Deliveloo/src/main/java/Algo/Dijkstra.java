package Algo;

import Modeles.Graphe;
import Modeles.Intersection;
import Modeles.Troncon;

import java.util.*;

public class Dijkstra {


    private Intersection start;

    private Intersection end;

    private HashMap<Integer, Double> whiteNode; //Long = id des intersections Double = Poids

    private HashMap<Integer, Double> greyNode; //Long = id des intersections Double = Poids

    private HashMap<Integer, Double> cost;

    private HashMap<Double, List<Integer>> greyNodeInv;

    private HashMap<Integer, Intersection> pi;

    private HashMap<Integer,Intersection> plan;


    public Dijkstra(Graphe graphe, Intersection i1, Intersection i2){
        start = i1;
        end = i2;
        HashMap<Integer, Double> whiteNode = new HashMap();
        HashMap<Integer, Double> greyNode = new HashMap();

        HashMap<Integer, Double> cost = new HashMap();
        HashMap<Double, Integer> greyNodeInv = new HashMap();
        HashMap<Integer,Intersection> plan = graphe.getIntersectionMap();
        for (Map.Entry<Integer, Intersection> entry : plan.entrySet()){
            whiteNode.put(entry.getKey(), Double.MAX_VALUE);
            cost.put(entry.getKey(), Double.MAX_VALUE);
            greyNode.put(entry.getKey(), null);
        }
        whiteNode.remove(start.getId());
        greyNode.put(start.getId(), 0.0);
        cost.put(start.getId(), 0.0);
        greyNodeInv.put(0.0, start.getId());
    }

    public void run(){
        while(greyNode.values().size() != 0){
            Double min = Collections.min(greyNode.values());
            Integer idNodeMin = greyNodeInv.get(min).get(0); //Si plusieurs idMin ?
            //Récupérer les voisins du node min en calculant nouvelles distances
            Intersection iCurrent = plan.get(idNodeMin);
            List<Troncon> voisins = iCurrent.getTroncons();
            for(Troncon tCurrent : voisins){
                if(greyNode.containsKey(tCurrent.getDestination()) || whiteNode.containsKey(tCurrent.getDestination())){
                    relacherNode(iCurrent, tCurrent.getDestination());
                    //Mettre à jour les sommets visités
                    //Si un sommet est dans les intersections blanches càd qu'il n'a jamais été visité
                    if(whiteNode.containsKey(tCurrent.getDestination().getId())){
                        greyNode.put(tCurrent.getDestination().getId(), cost.get(tCurrent.getDestination().getId()));
                        //On ajoute aussi à la map inversée
                        if(greyNodeInv.containsKey(cost.get(tCurrent.getDestination().getId()))){
                            greyNodeInv.get(cost.get(tCurrent.getDestination().getId())).add(tCurrent.getDestination().getId());
                        }else{
                            List<Integer> newList = new ArrayList<Integer>();
                            newList.add(tCurrent.getDestination().getId());
                            greyNodeInv.put(cost.get(tCurrent.getDestination().getId()), newList);
                        }
                        whiteNode.remove(tCurrent.getDestination().getId());
                    }else{ //Si il n'est plus dans les sommets blancs càd qu'il est dans les sommets gris
                        double newCost = cost.get(tCurrent.getDestination().getId());
                        double oldCost = greyNode.get(tCurrent.getDestination().getId());
                        if(newCost != oldCost){
                            greyNode.put(tCurrent.getDestination().getId(), newCost);
                            if(greyNodeInv.get(oldCost).size() != 1){
                                greyNodeInv.get(oldCost).remove(tCurrent.getDestination().getId());
                            }else{
                                greyNodeInv.remove(oldCost);
                            }

                            if(greyNodeInv.containsKey(newCost)){
                                greyNodeInv.get(cost.get(tCurrent.getDestination().getId())).add(tCurrent.getDestination().getId());
                            }else{
                                List<Integer> newList = new ArrayList<Integer>();
                                newList.add(tCurrent.getDestination().getId());
                                greyNodeInv.put(newCost, newList);
                            }
                        }
                    }
                }
            }
            greyNode.remove(idNodeMin);
            if (greyNodeInv.get(min).size() != 1) {
                greyNodeInv.get(min).remove(idNodeMin);
            } else {
                greyNodeInv.remove(min);
            }
        }
    }

    public void relacherNode(Intersection si, Intersection sj){
        Troncon t = null;
        List<Troncon> listTroncon = si.getTroncons();
        for(Troncon tCurrent : listTroncon){
            if(t == null) {
                t = tCurrent;
            }
            else if(tCurrent.getLongueur() < t.getLongueur()) {
                t = tCurrent;
            }
        }
        double vTest = cost.get(si.getId()) + t.getLongueur();
        if(cost.get(sj.getId()) == Double.MAX_VALUE){
            cost.put(sj.getId(), vTest);
            pi.put(sj.getId(), sj);
        }else if(cost.get(sj.getId()) > vTest){
            cost.put(sj.getId(), vTest);
            pi.put(sj.getId(), sj);
        }
    }
}
