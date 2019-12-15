package Modele;

import com.sothawo.mapjfx.Coordinate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GrapheTest {

    Graphe graphe;

    @Test
    void getIntersectionMapTest_ShouldReturnIntersectionMapAttribute() {
        //Arrange
        graphe = new Graphe();
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);

        graphe.addIntersection(inter);

        //Act
        HashMap<Long,Intersection> actualI = graphe.getIntersectionMap();

        //Assert
        assertNotNull(actualI);
        assertEquals(inter,actualI.get(inter.getId()));
    }

    @Test
    void addTronconTest_ShouldAddTronconToIntersectionInMap() {
        //Arrange
        graphe = new Graphe();
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);
        Troncon troncon = new Troncon(inter,"",5.43);
        graphe.addIntersection(inter);

        //Act
        graphe.addTroncon(troncon,inter.getId());

        //Assert
        Collection<Troncon> actualT = graphe.getIntersectionMap().get(inter.getId()).getTroncons();
        assertNotNull(actualT);
        assertEquals(troncon,actualT.toArray()[0]);
    }

    @Test
    void addIntersectionTest_ShouldAddIntersectionToMap() {
        graphe = new Graphe();
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);

        //Act
        graphe.addIntersection(inter);

        //Assert
        HashMap<Long,Intersection> actualI = graphe.getIntersectionMap();
        assertNotNull(actualI);
        assertEquals(1,actualI.size());
    }

    @Test
    void clearGraphTest_ShouldEmptyIntersectionMap() {
        //Arrange
        graphe = new Graphe();
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);
        Troncon troncon = new Troncon(inter,"",5.43);
        graphe.addIntersection(inter);
        graphe.addTroncon(troncon,inter.getId());

        //Act
        graphe.clearGraph();

        //Assert
        assertNotNull(graphe);
        assertEquals(0,graphe.getIntersectionMap().size());
    }
}