package Modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstructionLivraisonTest {

    InstructionLivraison instrLiv;

    @Test
    void getNomRueTest_ShouldReturnNomRueAttribute() {
        //Arrange
        instrLiv = new InstructionLivraison("rue Ahmed", InstructionLivraison.Direction.LEGERDROIT,12.2);

        //Act
        String actualN = instrLiv.getNomRue();

        //Assert
        assertNotNull(actualN);
        assertTrue(actualN instanceof String);
        assertEquals("rue Ahmed", actualN);
    }

    @Test
    void getDirectionTest_ShouldReturnDirectionInstruction() {
        //Arrange
        instrLiv = new InstructionLivraison("rue Ahmed", InstructionLivraison.Direction.LEGERDROIT,12.2);

        //Act
        InstructionLivraison.Direction actualD = instrLiv.getDirection();

        //Assert
        assertNotNull(actualD);
        assertTrue(actualD instanceof InstructionLivraison.Direction);
        assertEquals(InstructionLivraison.Direction.LEGERDROIT, actualD);
    }

    @Test
    void getDistanceTest_ShouldReturnDistanceAttribute() {
        //Arrange
        instrLiv = new InstructionLivraison("rue Ahmed", InstructionLivraison.Direction.LEGERDROIT,12.2);

        //Act
        double actualD = instrLiv.getDistance();

        //Assert
        assertNotNull(actualD);
        assertEquals(12.2, actualD);
    }

    @Test
    void testToStringTest_ShouldReturnCorrectText() {
        //Arrange
        instrLiv = new InstructionLivraison("rue Ahmed", InstructionLivraison.Direction.LEGERDROIT,12.2);

        //Act
        String actualT = instrLiv.toString();

        //Assert
        assertNotNull(actualT);
        assertEquals("Dans 12 mètres, tournez légèrement à droite sur "+instrLiv.getNomRue()+".", actualT);
    }
}