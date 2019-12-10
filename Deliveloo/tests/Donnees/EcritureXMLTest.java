package Donnees;

import Modeles.Tournee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EcritureXMLTest {
    EcritureXML ecritureXML;

    @BeforeEach
    void setUp() throws Exception {
        ecritureXML = new EcritureXML();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void ecrireFichier_shouldCreateANewFile(){
        //Tournee ;
    }

    @Test
    void ecrireFichier_shouldThrowException(){

    }
}
