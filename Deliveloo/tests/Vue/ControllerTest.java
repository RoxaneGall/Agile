package Vue;

import com.sothawo.mapjfx.Coordinate;
import com.sun.javafx.application.ParametersImpl;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import javafx.application.Application;

import java.io.File;
import java.util.ArrayList;

class ControllerTest {
    @Test
    public void mapExtentTest() throws Exception {
        // ArrayList<Coordinate> col = new ArrayList<>();
        Coordinate c1 = new Coordinate(45.778579, 4.852096);
        Coordinate c2 = new Coordinate(45.781901, 4.791063);
        Coordinate c3 = new Coordinate(45.730995, 4.859773);
        Coordinate c4 = new Coordinate(45.714939, 4.901873);
        Controller contr = new Controller();
        contr.chargerPlan("../datas/grandPlan.xml");
        // min latitude
        Assertions.assertEquals(contr.mapExtent.getMin().getLatitude(), c4.getLatitude());
        // min longitude
        Assertions.assertEquals(contr.mapExtent.getMin().getLongitude(), c2.getLongitude());
        // max latitude
        Assertions.assertEquals(contr.mapExtent.getMax().getLatitude(), c2.getLatitude());
        // max longitude
        Assertions.assertEquals(contr.mapExtent.getMax().getLongitude(), c4.getLongitude());
        System.out.println(" Extent Test : PASSED ");
    }


}