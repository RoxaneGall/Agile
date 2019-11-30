package Modeles;

import com.sothawo.mapjfx.Coordinate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {

    @Test
    void getIdTest_shouldReturnId() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);

        //Act
        long actualId = inter.getId();

        //Assert
        assertEquals(initialId, actualId);
    }

    @Test
    void addTronconTest_shouldIncreaseTronconsSize() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);
        Troncon t = new Troncon(inter, "rue ahmed", 12.243);

        //Act
        inter.addTroncon(t);

        //Assert
        assertEquals(1,inter.getTroncons().size());

    }

    @Test
    void getCoordinateTest_ShouldReturnTronconsMap() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);

        //Act
        Coordinate actualp = inter.getCoordinate();

        //Assert
        assertEquals(p,actualp);
    }

    @Test
    void getTronconsTest_ShouldReturnTronconsMap() {
        //Arrange
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);
        Troncon t = new Troncon(inter, "rue ahmed", 12.243);
        inter.addTroncon(t);

        //Act
        Collection c = inter.getTroncons();

        //Assert
        assertEquals(1,c.size());
    }
}