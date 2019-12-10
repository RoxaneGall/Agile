package Modeles;

import Donnees.LectureXML;
import com.sothawo.mapjfx.Coordinate;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;


class TronconTest {
    Troncon troncon;

    @Test
    void getDestinationTest_ShouldReturnDestinationIntersection() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);
        troncon = new Troncon(inter,"",5.43);

        //Act
        Intersection actualD = troncon.getDestination();

        //Assert
        assertNotNull(actualD);
        assertTrue(actualD instanceof Intersection);
        assertEquals(inter, actualD);
    }

    @Test
    void getNomTest_ShouldReturnNomAttribute() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);
        troncon = new Troncon(inter,"rue Ahmed",5.43);

        //Act
        String actualN = troncon.getNom();

        //Assert
        assertNotNull(actualN);
        assertTrue(actualN instanceof String);
        assertEquals("rue Ahmed",actualN);
    }

    @Test
    void getLongueurTest_ShouldReturnLongueurAttribute() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);
        troncon = new Troncon(inter,"rue Ahmed",5.43);

        //Act
        double actualL = troncon.getLongueur();

        //Assert
        assertNotNull(actualL);
        assertEquals(5.43,actualL);
    }

    @Test
    void toStringTest_ShouldReturnCorrectText() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);
        troncon = new Troncon(inter,"rue Ahmed",5.43);

        //Act
        String actualT = troncon.toString();

        //Assert
        assertNotNull(actualT);
        assertEquals("Troncon{destination="+troncon.getDestination()+", nom='"+troncon.getNom()+"', longueur="+troncon.getLongueur()+"}",actualT);
    }
}