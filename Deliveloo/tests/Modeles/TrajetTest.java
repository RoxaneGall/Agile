package Modeles;

import com.sothawo.mapjfx.Coordinate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TrajetTest {

    Trajet trajet;

    @Test
    void getHeureDepartTest_ShouldReturnHeureDepartAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));

        trajet = new Trajet(i1);

        Date expectedH = new Date();

        trajet.setHeureDepart(expectedH);

        //Act & Assert
        assertEquals(expectedH, trajet.getHeureDepart());
    }

    @Test
    void setHeureDepartTest_ShouldModifyHeureDepartAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));

        trajet = new Trajet(i1);

        Date expectedH = new Date();

        //Act
        trajet.setHeureDepart(expectedH);

        //Assert
        assertEquals(expectedH, trajet.getHeureDepart());
    }

    @Test
    void getHeureArriveeTest_ShouldReturnHeureDepartAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));

        trajet = new Trajet(i1);

        Date expectedH = new Date();

        trajet.setHeureArrivee(expectedH);

        //Act & Assert
        assertEquals(expectedH, trajet.getHeureArrivee());
    }

    @Test
    void setHeureArriveeTest_ShouldModifyHeureDepartAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));

        trajet = new Trajet(i1);

        Date expectedH = new Date();

        trajet.setHeureArrivee(expectedH);

        //Act & Assert
        assertEquals(expectedH, trajet.getHeureArrivee());
    }

    @Test
    void getTypeTest_ShouldReturnTypeAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));

        trajet = new Trajet(i1);

        trajet.setType(Trajet.Type.PICKUP);

        //Act & Assert
        assertEquals(Trajet.Type.PICKUP, trajet.getType());
    }

    @Test
    void setTypeTest_ShouldModifyTypeAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));

        trajet = new Trajet(i1);

        Date expectedH = new Date();

        //Act
        trajet.setType(Trajet.Type.PICKUP);

        //Assert
        assertEquals(Trajet.Type.PICKUP, trajet.getType());
    }

    @Test
    void getLivraisonTest_ShouldReturnLivraisonAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));
        Intersection i2 = new Intersection(2, new Coordinate(0.0, 3.0));
        Livraison l = new Livraison((long) 0, i1, i2, 30, 160);

        trajet = new Trajet(i1);

        trajet.setLivraison(l);

        //Act & Assert
        assertEquals(l, trajet.getLivraison());
    }

    @Test
    void setLivraisonTest_ShouldModifyLivraisonAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));
        Intersection i2 = new Intersection(2, new Coordinate(0.0, 3.0));
        Livraison l = new Livraison((long) 0, i1, i2, 30, 160);

        trajet = new Trajet(i1);

        //Act
        trajet.setLivraison(l);

        //Assert
        assertEquals(l, trajet.getLivraison());
    }

    @Test
    void getLongueurTest_ShouldReturnSumOfTronconLengths() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));
        Intersection i2 = new Intersection(2, new Coordinate(0.0, 3.0));
        Intersection i3 = new Intersection(3, new Coordinate(3.0, 3.0));
        Intersection i4 = new Intersection(4, new Coordinate(3.0, 6.0));
        Intersection i5 = new Intersection(5, new Coordinate(3.0, 12.0));

        Troncon t12 = new Troncon(i2, "", 2.0);
        Troncon t23 = new Troncon(i3, "", 2.0);
        Troncon t34 = new Troncon(i4, "", 2.0);
        Troncon t45 = new Troncon(i5, "", 2.0);
        Troncon t51 = new Troncon(i1, "", 2.0);

        trajet = new Trajet(i1);
        trajet.addTroncon(t12);
        trajet.addTroncon(t23);
        trajet.addTroncon(t34);
        trajet.addTroncon(t45);
        trajet.addTroncon(t51);

        Double expectedLong = t12.getLongueur() + t23.getLongueur() + t34.getLongueur() + t45.getLongueur() + t51.getLongueur();

        //Act
        Double actualLong = trajet.getLongueur();

        //Assert
        assertEquals(expectedLong, actualLong);
    }

    @Test
    void getOrigineTest_ShouldReturnOrigineIntersection() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));
        Intersection i2 = new Intersection(2, new Coordinate(0.0, 3.0));
        Intersection i3 = new Intersection(3, new Coordinate(3.0, 3.0));
        Intersection i4 = new Intersection(4, new Coordinate(3.0, 6.0));
        Intersection i5 = new Intersection(5, new Coordinate(3.0, 12.0));

        Troncon t12 = new Troncon(i2, "", 2.0);
        Troncon t23 = new Troncon(i3, "", 2.0);
        Troncon t34 = new Troncon(i4, "", 2.0);
        Troncon t45 = new Troncon(i5, "", 2.0);
        Troncon t51 = new Troncon(i1, "", 2.0);

        trajet = new Trajet(i1);
        trajet.addTroncon(t12);
        trajet.addTroncon(t23);
        trajet.addTroncon(t34);
        trajet.addTroncon(t45);
        trajet.addTroncon(t51);

        //Act
        Intersection actualO = trajet.getOrigine();

        //Act & Assert
        assertTrue(actualO instanceof Intersection);
        assertEquals(i1, actualO);
    }

    @Test
    void getArriveeTest_ShouldReturnArriveeIntersection() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));
        Intersection i2 = new Intersection(2, new Coordinate(0.0, 3.0));
        Intersection i3 = new Intersection(3, new Coordinate(3.0, 3.0));
        Intersection i4 = new Intersection(4, new Coordinate(3.0, 6.0));
        Intersection i5 = new Intersection(5, new Coordinate(3.0, 12.0));

        Troncon t12 = new Troncon(i2, "", 2.0);
        Troncon t23 = new Troncon(i3, "", 2.0);
        Troncon t34 = new Troncon(i4, "", 2.0);
        Troncon t45 = new Troncon(i5, "", 2.0);
        Troncon t51 = new Troncon(i1, "", 2.0);

        trajet = new Trajet(i1);
        trajet.addTroncon(t12);
        trajet.addTroncon(t23);
        trajet.addTroncon(t34);
        trajet.addTroncon(t45);
        trajet.addTroncon(t51);

        //Act
        Intersection actualO = trajet.getOrigine();

        //Act & Assert
        assertTrue(actualO instanceof Intersection);
        assertEquals(i1, actualO);
    }

    @Test
    void getTronconsTest_ShouldReturnTronconListWithCorrectSize() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));
        Intersection i2 = new Intersection(2, new Coordinate(0.0, 3.0));
        Intersection i3 = new Intersection(3, new Coordinate(3.0, 3.0));
        Intersection i4 = new Intersection(4, new Coordinate(3.0, 6.0));
        Intersection i5 = new Intersection(5, new Coordinate(3.0, 12.0));

        Troncon t12 = new Troncon(i2, "", 2.0);
        Troncon t23 = new Troncon(i3, "", 2.0);
        Troncon t34 = new Troncon(i4, "", 2.0);
        Troncon t45 = new Troncon(i5, "", 2.0);
        Troncon t51 = new Troncon(i1, "", 2.0);

        ArrayList<Troncon> ts = new ArrayList<>();
        ts.add(t12);
        ts.add(t23);
        ts.add(t34);
        ts.add(t45);
        ts.add(t51);

        trajet = new Trajet(i1);
        trajet.addTroncons(ts);

        //Act
        ArrayList<Troncon> actualT = trajet.getTroncons();

        //Assert
        int i = 0;
        for (Troncon t : ts) {
            assertTrue(actualT.toArray()[i] instanceof Troncon);
            assertEquals(t, actualT.toArray()[i]);
            i++;
        }
    }

    @Test
    void addTronconTest_ShouldIncreaseTronconsListSize() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));
        Intersection i2 = new Intersection(2, new Coordinate(0.0, 3.0));
        Intersection i3 = new Intersection(3, new Coordinate(3.0, 3.0));
        Intersection i4 = new Intersection(4, new Coordinate(3.0, 6.0));
        Intersection i5 = new Intersection(5, new Coordinate(3.0, 12.0));

        Troncon t12 = new Troncon(i2, "", 2.0);
        Troncon t23 = new Troncon(i3, "", 2.0);
        Troncon t34 = new Troncon(i4, "", 2.0);
        Troncon t45 = new Troncon(i5, "", 2.0);
        Troncon t51 = new Troncon(i1, "", 2.0);

        ArrayList<Troncon> ts = new ArrayList<>();
        ts.add(t12);
        ts.add(t23);
        ts.add(t34);
        ts.add(t45);
        ts.add(t51);

        trajet = new Trajet(i1);

        //Act
        trajet.addTroncon(t12);

        //Assert
        assertEquals(1, trajet.getTroncons().size());
        assertEquals(t12, trajet.getTroncons().get(0));
    }

    @Test
    void addTronconsTest_ShouldIncreaseTronconsListSize() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));
        Intersection i2 = new Intersection(2, new Coordinate(0.0, 3.0));
        Intersection i3 = new Intersection(3, new Coordinate(3.0, 3.0));
        Intersection i4 = new Intersection(4, new Coordinate(3.0, 6.0));
        Intersection i5 = new Intersection(5, new Coordinate(3.0, 12.0));

        Troncon t12 = new Troncon(i2, "", 2.0);
        Troncon t23 = new Troncon(i3, "", 2.0);
        Troncon t34 = new Troncon(i4, "", 2.0);
        Troncon t45 = new Troncon(i5, "", 2.0);
        Troncon t51 = new Troncon(i1, "", 2.0);

        ArrayList<Troncon> ts = new ArrayList<>();
        ts.add(t12);
        ts.add(t23);
        ts.add(t34);
        ts.add(t45);
        ts.add(t51);

        trajet = new Trajet(i1);

        //Act
        trajet.addTroncons(ts);

        //Assert
        ArrayList<Troncon> actualT = trajet.getTroncons();
        assertEquals(ts.size(), actualT.size());
        int i = 0;
        for (Troncon t : ts) {
            assertTrue(actualT.toArray()[i] instanceof Troncon);
            assertEquals(t, actualT.toArray()[i]);
            i++;
        }
    }

    @Test
    void getInstructionsTest_ShouldReturnCorrectAngleValue() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));
        Intersection i2 = new Intersection(2, new Coordinate(0.0, 3.0));
        Intersection i3 = new Intersection(3, new Coordinate(3.0, 3.0));
        Intersection i4 = new Intersection(4, new Coordinate(3.0, 6.0));
        Intersection i5 = new Intersection(5, new Coordinate(3.0, 12.0));

        Troncon t12 = new Troncon(i2, "", 2.0);
        Troncon t23 = new Troncon(i3, "", 2.0);
        Troncon t34 = new Troncon(i4, "", 2.0);
        Troncon t45 = new Troncon(i5, "", 2.0);
        Troncon t51 = new Troncon(i1, "", 2.0);

        ArrayList<Troncon> ts = new ArrayList<>();
        ts.add(t12);
        ts.add(t23);
        ts.add(t34);
        ts.add(t45);
        ts.add(t51);

        trajet = new Trajet(i1);
        trajet.addTroncons(ts);

        //Act
        ArrayList<InstructionLivraison> instr = trajet.getInstructions();

        //Assert
        assertNotNull(instr);
    }
}