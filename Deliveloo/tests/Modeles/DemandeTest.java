package Modeles;

import com.sothawo.mapjfx.Coordinate;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DemandeTest {

    Demande dmd;

    @Test
    void getLivraisonsTest_ShouldReturnLivraisonsAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));
        Livraison l = new Livraison((long)0,i1,i2,30,160);

        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);
        Date d = new Date();

        dmd = new Demande(inter, d,"nomDemande");
        dmd.addLivraison(i1,i2,30,160);
        //Act
        ArrayList<Livraison> actualL = dmd.getLivraisons();

        //Assert
        assertNotNull(actualL);
        assertEquals(l.toString(),actualL.get(0).toString());
    }

    @Test
    void setLivraisonsTest_ShouldModifyLivraisonsAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));
        ArrayList<Livraison> liv= new ArrayList<>();
        Livraison l = new Livraison((long)0,i1,i2,30,160);
        liv.add(l);

        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);
        Date d = new Date();

        dmd = new Demande(inter, d,"nomDemande");
        dmd.addLivraison(i1,i2,30,160);

        Intersection i3 = new Intersection(3,new Coordinate(3.0,3.0));
        Livraison livraison = new Livraison((long)1,inter,i3,45,450);
        liv.add(livraison);

        //Act
        dmd.addLivraison(inter,i3,45,450);

        //Assert
        ArrayList<Livraison> actualL = dmd.getLivraisons();
        assertNotNull(actualL);
        assertEquals(2,actualL.size());

        int i = 0;
        for (Livraison livr : actualL) {
            assertEquals(liv.toArray()[i].toString(), livr.toString());
            i++;
        }
    }


    @Test
    void getEntrepotTest_ShouldReturnEntrepotAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);
        Date d = new Date();

        dmd = new Demande(inter, d,"nomDemande");
        dmd.addLivraison(i1,i2,30,60);

        //Act
        Intersection actualE = dmd.getEntrepot();

        //Assert
        assertNotNull(actualE);
        assertEquals(inter,actualE);
    }

    @Test
    void setEntrepotTest_ShouldModifyEntrepotAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1,new Coordinate(0.0,0.0));
        Intersection i2 = new Intersection(2,new Coordinate(0.0,3.0));

        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId,p);
        Date d = new Date();

        dmd = new Demande(inter, d,"nomDemande");
        dmd.addLivraison(i1,i2,30,160);
        //Act
        dmd.setEntrepot(i1);

        //Assert
        Intersection actualE = dmd.getEntrepot();
        assertNotNull(actualE);
        assertNotEquals(inter,actualE);
        assertEquals(i1,actualE);
    }

    @Test
    void getHeureDepartTest_ShouldReturnEntrepotAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));
        Intersection i2 = new Intersection(2, new Coordinate(0.0, 3.0));

        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId, p);
        Date d = new Date();

        dmd = new Demande(inter, d,"nomDemande");
        dmd.addLivraison(i1,i2,30,160);

        //Act
        Date actualD = dmd.getHeureDepart();

        //Assert
        assertNotNull(actualD);
        assertEquals(d, actualD);
    }

    @Test
    void setHeureDepartTest_ShouldReturnEntrepotAttribute() {
        //Arrange
        Intersection i1 = new Intersection(1, new Coordinate(0.0, 0.0));
        Intersection i2 = new Intersection(2, new Coordinate(0.0, 3.0));
        Coordinate p = new Coordinate(4.112233, 5.32404);
        long initialId = 34;
        Intersection inter = new Intersection(initialId, p);
        Date d = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, 1);
        Date newD = c.getTime();

        dmd = new Demande(inter, d,"nomDemande");
        dmd.addLivraison(i1,i2,30,160);

        //Act
        dmd.setHeureDepart(newD);

        //Assert
        Date actualD = dmd.getHeureDepart();
        assertNotNull(actualD);
        assertNotEquals(d, actualD);
        assertEquals(newD, actualD);
    }
}