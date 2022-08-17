package sep.groupt.client;


import org.junit.jupiter.api.*;
import sep.groupt.client.dataclass.Diskussionsgruppe;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DiskussionenTest {


    Diskussionsgruppe gruppe;
    Diskussionsgruppe join;
    @BeforeAll
    void initDiskussion(){

        try {
            gruppe = new Diskussionsgruppe(RequestHandler.getNutzerByID(1),"Test Diskussion",false);
            join = new Diskussionsgruppe(RequestHandler.getNutzerByID(2),"Zweiter Test",false);
            RequestHandler.addDiskussion(join);
        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            fail();
            throw new RuntimeException(e);
        }


    }

    @Test
    @Order(1)
    void diskussionErstellen(){

        try{

            assertTrue(RequestHandler.addDiskussion(gruppe));

        }catch (IOException|InterruptedException e){

            fail();

        }


    }

    @Test
    @Order(2)
    void diskussionenSuchen(){



        try{

            assertTrue(RequestHandler.getDiskussionen(RequestHandler.getNutzerByID(1)));

        }catch (IOException|InterruptedException e){

            fail();

        }

    }

    @Test
    @Order(3)
    void diskussionBeitreten(){

        try{

            assertTrue(RequestHandler.joinDiskussion(join));

        }catch (IOException|InterruptedException e){

            fail();

        }



    }

    @AfterAll
    void cleanUpTest() throws IOException, InterruptedException {

        RequestHandler.cleanUpTest();

    }





}
