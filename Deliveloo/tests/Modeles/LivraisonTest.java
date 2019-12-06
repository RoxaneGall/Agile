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
        Intersection actualI = liv.getPickup();

        //Assert
        assertNotNull(actualI);
        assertEquals(i1,actualI);
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
        Intersection actualI = liv.getPickup();
        assertNotNull(actualI);
        assertNotEquals(i1,actualI);
        assertEquals(i3,actualI);
    }

    @Test
    void getDeliveryTest_ShouldReturnDeliveryAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        liv= new Livraison(i1,i2,30,160);

        //Act
        Intersection actualI = liv.getDelivery();

        //Assert
        assertNotNull(actualI);
        assertEquals(i2,actualI);
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
        Intersection actualI = liv.getDelivery();
        assertNotNull(actualI);
        assertNotEquals(i2,actualI);
        assertEquals(i3,actualI);
    }

    @Test
    void getDureeEnlevementTest_ShouldReturnDureeEnlevementAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        liv= new Livraison(i1,i2,30,160);

        //Act
        int actualT = liv.getDureeEnlevement();

        //Assert
        assertNotNull(actualT);
        assertEquals(30,actualT);
    }

    @Test
    void getDureeLivraisonTest_ShouldReturnDureeLivraisonAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        liv= new Livraison(i1,i2,30,160);

        //Act
        int actualT = liv.getDureeLivraison();

        //Assert
        assertNotNull(actualT);
        assertEquals(160,actualT);
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
        int actualT = liv.getDureeEnlevement();

        assertNotNull(actualT);
        assertNotEquals(30,actualT);
        assertEquals(10,actualT);
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
        int actualT = liv.getDureeLivraison();

        assertNotNull(actualT);
        assertNotEquals(160,actualT);
        assertEquals(10,actualT);
    }
}