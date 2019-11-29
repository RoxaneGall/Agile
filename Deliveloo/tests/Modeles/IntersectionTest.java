package Modeles;

import com.sothawo.mapjfx.Coordinate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

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
    }

    @Test
    void getP() {
    }

    @Test
    void setP() {
    }

    @Test
    void getTroncons() {
    }

    @Test
    void testToString() {
    }
}