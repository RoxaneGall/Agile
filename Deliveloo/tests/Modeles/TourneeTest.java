package Modeles;

import com.sothawo.mapjfx.Coordinate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TourneeTest {

    Tournee tournee;

    @Test
    void getDemandeTest_ShouldReturnDemandeAttribute() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Date d = new Date();

        Intersection inter = new Intersection(initialId,p);
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));
        Intersection i3 = new Intersection(3,new Coordinate(3.0,3.0));
        Livraison livraison = new Livraison(inter,i3,45,450);
        ArrayList<Livraison> liv= new ArrayList<>();
        Livraison l = new Livraison(i1,i2,30,160);
        liv.add(l);
        liv.add(livraison);

        Demande dmd = new Demande(liv, inter, d);

        tournee = new Tournee(dmd);

        //Act
        Demande actualD = tournee.getDemande();

        //Assert
        assertNotNull(actualD);
        assertEquals(dmd,actualD);
    }

    @Test
    void getTrajetsTest_ShouldReturnTrajetsAttribute() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Date d = new Date();

        Intersection inter = new Intersection(initialId,p);
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));
        Intersection i3 = new Intersection(3,new Coordinate(3.0,3.0));
        Livraison livraison = new Livraison(inter,i3,45,450);
        ArrayList<Livraison> liv= new ArrayList<>();
        Livraison l = new Livraison(i1,i2,30,160);
        liv.add(l);
        liv.add(livraison);

        Demande dmd = new Demande(liv, inter, d);

        Trajet t1 = new Trajet(i1);
        Trajet t2 = new Trajet(i2);
        Trajet t3 = new Trajet(i3);

        tournee = new Tournee(dmd);
        tournee.addTrajet(t1);
        tournee.addTrajet(t2);
        tournee.addTrajet(t3);

        //Act
        ArrayList<Trajet> actualT = tournee.getTrajets();

        //Assert
        assertNotNull(actualT);
        assertEquals(t1,actualT.toArray()[0]);
        assertEquals(t2,actualT.toArray()[1]);
        assertEquals(t3,actualT.toArray()[2]);
    }

    @Test
    void getTotalDistanceTest_ShouldComputeCorrectDistance() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Date d = new Date();

        Intersection inter = new Intersection(initialId,p);
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));
        Intersection i3 = new Intersection(3,new Coordinate(3.0,3.0));
        Livraison livraison = new Livraison(inter,i3,45,450);
        ArrayList<Livraison> liv= new ArrayList<>();
        Livraison l = new Livraison(i1,i2,30,160);
        liv.add(l);
        liv.add(livraison);

        Demande dmd = new Demande(liv, inter, d);

        Trajet t1 = new Trajet(i1);

        tournee = new Tournee(dmd);
        tournee.addTrajet(t1);

        //Act
        double actualL = tournee.getTotalDistance();

        //Assert
        assertNotNull(actualL);
        assertEquals(0,actualL);
    }

    @Test
    void getTotalDurationTest_ShouldComputeCorrectDuration() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Date d = new Date();

        Intersection inter = new Intersection(initialId,p);
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));
        Intersection i3 = new Intersection(3,new Coordinate(3.0,3.0));

        Livraison livraison = new Livraison(inter,i3,45,450);
        Livraison l = new Livraison(i1,i2,30,160);

        ArrayList<Livraison> liv= new ArrayList<>();
        liv.add(l);
        liv.add(livraison);

        Demande dmd = new Demande(liv, inter, d);

        Trajet t1 = new Trajet(i1);

        tournee = new Tournee(dmd);
        tournee.addTrajet(t1);

        //Act
        double actualD = tournee.getTotalDuration();

        //Assert
        assertNotNull(actualD);
        assertEquals((45.0+450.0+30.0+160.0)/60.0,actualD);
    }

    @Test
    void addTrajetTest_ShouldIncreaseTrajetsMapSize() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Date d = new Date();

        Intersection inter = new Intersection(initialId,p);
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));
        Intersection i3 = new Intersection(3,new Coordinate(3.0,3.0));

        Livraison livraison = new Livraison(inter,i3,45,450);
        Livraison l = new Livraison(i1,i2,30,160);

        ArrayList<Livraison> liv= new ArrayList<>();
        liv.add(l);
        liv.add(livraison);

        Demande dmd = new Demande(liv, inter, d);

        Trajet t1 = new Trajet(i1);

        tournee = new Tournee(dmd);

        //Act
        tournee.addTrajet(t1);

        //Assert
        ArrayList<Trajet> actualT = tournee.getTrajets();
        assertNotNull(actualT);
        assertEquals(1,actualT.size());
        assertEquals(t1,actualT.toArray()[0]);
    }

    @Test
    void addTrajetsTest_ShouldIncreaseTrajetsMapSize() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Date d = new Date();

        Intersection inter = new Intersection(initialId,p);
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));
        Intersection i3 = new Intersection(3,new Coordinate(3.0,3.0));

        Livraison livraison = new Livraison(inter,i3,45,450);
        Livraison l = new Livraison(i1,i2,30,160);
        ArrayList<Livraison> liv= new ArrayList<>();
        liv.add(l);
        liv.add(livraison);

        Demande dmd = new Demande(liv, inter, d);

        Trajet t1 = new Trajet(i1);
        Trajet t2 = new Trajet(i2);
        Trajet t3 = new Trajet(i3);
        ArrayList<Trajet> ts= new ArrayList<>();
        ts.add(t1);
        ts.add(t2);
        ts.add(t3);

        tournee = new Tournee(dmd);

        //Act
        tournee.addTrajets(ts);

        //Assert
        ArrayList<Trajet> actualT = tournee.getTrajets();
        assertNotNull(actualT);
        assertEquals(ts.size(),actualT.size());

        int i = 0;
        for (Trajet t: ts) {
            assertEquals(t,actualT.toArray()[i]);
            i++;
        }
    }

    @Test
    void toStringTest_ShouldReturnCorrectText() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Date d = new Date();

        Intersection inter = new Intersection(initialId,p);
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));
        Intersection i3 = new Intersection(3,new Coordinate(3.0,3.0));

        Livraison livraison = new Livraison(inter,i3,45,450);
        Livraison l = new Livraison(i1,i2,30,160);
        ArrayList<Livraison> liv= new ArrayList<>();
        liv.add(l);
        liv.add(livraison);

        Demande dmd = new Demande(liv, inter, d);

        Trajet t1 = new Trajet(i1);
        Trajet t2 = new Trajet(i2);
        Trajet t3 = new Trajet(i3);
        ArrayList<Trajet> ts= new ArrayList<>();
        ts.add(t1);
        ts.add(t2);
        ts.add(t3);

        tournee = new Tournee(dmd);
        tournee.addTrajets(ts);

        //Act
        String actualT = tournee.toString();

        //Assert
        String txt = "";
        for (Trajet trajet: ts) {
            txt += "\n" + trajet.toString();
        }
        String expectedT =  "Tournee{" + "trajets=" + txt + '}';

        assertNotNull(actualT);
        assertEquals(expectedT,actualT);
    }
}