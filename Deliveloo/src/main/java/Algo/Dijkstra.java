package Algo;

import Modeles.Graphe;
import Modeles.Intersection;
import Modeles.Troncon;


import java.util.*;

import java.util.Collections;

public class Dijkstra {


    private Intersection start;

    private Intersection end;

    public HashMap<Integer, Double> whiteNode; //Long = id des intersections Double = Poids

    public HashMap<Integer, Double> greyNode; //Long = id des intersections Double = Poids

    public HashMap<Integer, Double> cost;

    private HashMap<Double, List<Integer>> greyNodeInv;

    public HashMap<Integer, Intersection> pi;

    private HashMap<Integer,Intersection> plan;


    public Dijkstra(Graphe graphe, Intersection i1, Intersection i2){
        start = i1;
        end = i2;
        whiteNode = new HashMap();
        greyNode = new HashMap();
        cost = new HashMap();
        greyNodeInv = new HashMap();
        plan = new HashMap();
        pi = new HashMap();
        plan = graphe.getIntersectionMap();
        for (Map.Entry<Integer, Intersection> entry : plan.entrySet()){
            whiteNode.put(entry.getKey(), Double.MAX_VALUE);
            cost.put(entry.getKey(), Double.MAX_VALUE);
            pi.put(entry.getKey(), null);
        }
        whiteNode.remove(start.getId());
        greyNode.put(start.getId(), 0.0);
        cost.put(start.getId(), 0.0);
        List<Integer> newListe = new ArrayList<Integer>();
        newListe.add(start.getId());
        greyNodeInv.put(0.0, newListe);
        run();
    }

    public void run(){
        while(greyNode.values().size() != 0){
            Double nodeMin = Collections.min(greyNode.values());

            Integer idNodeMin = greyNodeInv.get(nodeMin).get(0); //Si plusieurs idMin ?
            //Récupérer les voisins du node min en calculant nouvelles distances
            Intersection iCurrent = plan.get(idNodeMin);
            Collection<Troncon> voisins = iCurrent.getTroncons();
            for(Troncon tCurrent : voisins){
                if(greyNode.containsKey(tCurrent.getDestination().getId()) || whiteNode.containsKey(tCurrent.getDestination().getId())){
                    relacherNode(iCurrent, tCurrent.getDestination());
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
            if (greyNodeInv.get(nodeMin).size() != 1) {
                greyNodeInv.get(nodeMin).remove(idNodeMin);
            } else {
                greyNodeInv.remove(nodeMin);
            }
        }
    }

    public void relacherNode(Intersection si, Intersection sj){
        System.out.println(si);
        System.out.println(sj);
        Troncon t = null;
        Collection<Troncon> listTroncon = si.getTroncons();
        for(Troncon tCurrent : listTroncon){
            if(tCurrent.getDestination().getId() == sj.getId()) {
                if (t == null) {
                    t = tCurrent;
                } else if (tCurrent.getLongueur() < t.getLongueur()) {
                    t = tCurrent;
                }
            }
        }
        System.out.println(t.getLongueur());
        System.out.println(cost.get(si.getId()));
        double vTest = cost.get(si.getId()) + t.getLongueur();
        if(cost.get(sj.getId()) == Double.MAX_VALUE){
            cost.put(sj.getId(), vTest);
            pi.put(sj.getId(), si);
        }else if(cost.get(sj.getId()) > vTest){
            cost.put(sj.getId(), vTest);
            pi.put(sj.getId(), si);
        }
        System.out.println(cost);
    }
}
