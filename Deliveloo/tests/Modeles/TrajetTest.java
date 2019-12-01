package Modeles;

import com.sothawo.mapjfx.Coordinate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrajetTest {

    @Test
    void getInstructions() {

        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));
        Intersection i3 = new Intersection(3,new Coordinate(3.0,3.0));
        Intersection i4 = new Intersection(4,new Coordinate(3.0,6.0));
        Intersection i5 = new Intersection(5,new Coordinate(3.0,12.0));

        Troncon t12 = new Troncon(i2,"",2.0);
        Troncon t23 = new Troncon(i3,"",2.0);
        Troncon t34 = new Troncon(i4,"",2.0);
        Troncon t45 = new Troncon(i5,"",2.0);
        Troncon t51 = new Troncon(i1,"",2.0);

        Trajet trajet = new Trajet(i1);
        trajet.addTroncon(t12);
        trajet.addTroncon(t23);
        trajet.addTroncon(t34);
        trajet.addTroncon(t45);
        trajet.addTroncon(t51);

        Assertions.assertTrue(trajet.getInstructions().size() == 4);
    }

}