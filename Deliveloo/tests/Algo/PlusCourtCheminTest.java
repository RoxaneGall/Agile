package Algo;

import Modeles.Graphe;
import Modeles.Intersection;
import Modeles.Trajet;
import Modeles.Troncon;
import com.sothawo.mapjfx.Coordinate;
import com.sun.tools.corba.se.idl.constExpr.Plus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PlusCourtCheminTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testDijkstra_LengthShouldBe6() {
        Intersection i1 = new Intersection(1,new Coordinate(1.0,2.0));
        Intersection i2 = new Intersection(2,new Coordinate(2.0,2.0));
        Intersection i3 = new Intersection(3,new Coordinate(3.0,2.0));
        Intersection i4 = new Intersection(4,new Coordinate(4.0,2.0));
        Intersection i5 = new Intersection(5,new Coordinate(5.0,2.0));
        Intersection i6 = new Intersection(6,new Coordinate(6.0,2.0));
        Graphe.shared.addIntersection(i1);
        Graphe.shared.addIntersection(i2);
        Graphe.shared.addIntersection(i3);
        Graphe.shared.addIntersection(i4);
        Graphe.shared.addIntersection(i5);
        Graphe.shared.addIntersection(i6);

        Troncon t12 = new Troncon(i2,"",2.0);
        Troncon t13 = new Troncon(i3,"",2.0);
        Troncon t21 = new Troncon(i1,"",2.0);
        Troncon t23 = new Troncon(i3,"",2.0);
        Troncon t24 = new Troncon(i4,"",2.0);
        Troncon t34 = new Troncon(i4,"",2.0);
        Troncon t45 = new Troncon(i5,"",2.0);
        Troncon t43 = new Troncon(i3,"",2.0);
        Troncon t56 = new Troncon(i6,"",2.0);
        Troncon t61 = new Troncon(i1,"",2.0);

        i1.addTroncon(t12);
        i1.addTroncon(t13);

        i2.addTroncon(t21);
        i2.addTroncon(t23);
        i2.addTroncon(t24);

        i3.addTroncon(t34);

        i4.addTroncon(t43);
        i4.addTroncon(t45);

        i5.addTroncon(t56);

        i6.addTroncon(t61);

        System.out.println(Computations.getMeilleurTrajet(i5,i3, Graphe.shared).toString());

        Assertions.assertTrue(PlusCourtChemin.dijkstra(i5,i3,Graphe.shared).get(i3.getId()).getLongueur() == 6.0);

    }

    @Test
    void selectNearestIntersection() {
        //TODO
       /* HashMap<Long,Trajet> trajetsPourIntersection = new HashMap<>(); //Liste du trajet optimal trouvé pour chaque interesection
        HashMap<Long,Boolean> alreadyVisitedIntersections = new HashMap<>(); //Liste des intersection déjà visitées
        //Graphe
        HashMap<Long,Intersection> plan = ; //Toutes les intersection avec leurs id en key

        //remplissage du graphe avec des couts infinis
        for (Long id : plan.keySet()){
            //Pour chaque intersection mettre son trajet optimal à null
            alreadyVisitedIntersections.put(id, false);
            trajetsPourIntersection.put(id, null);
        }

        PlusCourtChemin.selectNearestIntersection(trajetsPourIntersection,
                alreadyVisitedIntersections);*/
    }

    @Test
    void relacher() {
    }

    @Test
    void newTrajetFromAddingTronconToTrajet() {
        Intersection i1 = new Intersection(1,new Coordinate(1.0,2.0));
        Intersection i2 = new Intersection(2,new Coordinate(2.0,2.0));
        Trajet trajet = new Trajet(i1);

        Troncon arc = new Troncon(i2,"",2.0);

        Trajet t = PlusCourtChemin.newTrajetFromAddingTronconToTrajet(arc,trajet);

        Assertions.assertTrue(t.getLongueur()==2 && t.getArrivee()==i2);
    }
}