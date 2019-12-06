package Modeles;

import com.sothawo.mapjfx.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivraisonTest {

    Livraison liv;

    @Test
    void getPickupTest_ShouldReturnPickupAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        liv= new Livraison(i1,i2,30,160);

        //Act
        Intersection ActualI = liv.getPickup();

        //Assert
        assertNotNull(ActualI);
        assertEquals(i1,ActualI);
    }

    @Test
    void setPickupTest_ShouldModifyPickupAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        liv= new Livraison(i1,i2,30,160);

        Intersection i3 = new Intersection(3,new Coordinate(3.0,3.0));

        //Act
        liv.setPickup(i3);

        //Assert
        Intersection ActualI = liv.getPickup();
        assertNotNull(ActualI);
        assertNotEquals(i1,ActualI);
        assertEquals(i3,ActualI);
    }

    @Test
    void getDeliveryTest_ShouldReturnDeliveryAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        liv= new Livraison(i1,i2,30,160);

        //Act
        Intersection ActualI = liv.getDelivery();

        //Assert
        assertNotNull(ActualI);
        assertEquals(i2,ActualI);
    }

    @Test
    void setDeliveryTest_ShouldModifyPickupAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        liv= new Livraison(i1,i2,30,160);

        Intersection i3 = new Intersection(3,new Coordinate(3.0,3.0));

        //Act
        liv.setDelivery(i3);

        //Assert
        Intersection ActualI = liv.getDelivery();
        assertNotNull(ActualI);
        assertNotEquals(i2,ActualI);
        assertEquals(i3,ActualI);
    }

    @Test
    void getDureeEnlevementTest_ShouldReturnDureeEnlevementAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        liv= new Livraison(i1,i2,30,160);

        //Act
        int ActualT = liv.getDureeEnlevement();

        //Assert
        assertNotNull(ActualT);
        assertEquals(30,ActualT);
    }

    @Test
    void getDureeLivraisonTest_ShouldReturnDureeLivraisonAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        liv= new Livraison(i1,i2,30,160);

        //Act
        int ActualT = liv.getDureeLivraison();

        //Assert
        assertNotNull(ActualT);
        assertEquals(160,ActualT);
    }

    @Test
    void setDureeEnlevementTest_ShouldModifyDureeEnlevementAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        liv= new Livraison(i1,i2,30,160);

        //Act
        liv.setDureeEnlevement(10);

        //Assert
        int ActualT = liv.getDureeEnlevement();

        assertNotNull(ActualT);
        assertNotEquals(30,ActualT);
        assertEquals(10,ActualT);
    }

    @Test
    void setDureeLivraisonTest_ShouldModifyDureeLivraisonAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        liv= new Livraison(i1,i2,30,160);

        //Act
        liv.setDureeLivraison(10);

        //Assert
        int ActualT = liv.getDureeLivraison();

        assertNotNull(ActualT);
        assertNotEquals(160,ActualT);
        assertEquals(10,ActualT);
    }
}