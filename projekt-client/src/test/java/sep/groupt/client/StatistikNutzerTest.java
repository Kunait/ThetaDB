package sep.groupt.client;

import org.junit.jupiter.api.*;
import sep.groupt.client.dataclass.Film;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatistikNutzerTest {

    Film film;
    int filmid;
    int nutzerid;
    LocalDate datumVon;
    LocalDate datumBis;

    @BeforeAll
    void initFilm(){
        film = new Film();
        film.setName("Das ist ein TestFilm");
        film.setCategory("Action");
        film.setLength(90);
        film.setRegisseur("Gruppe T");
        film.setAuthor("Volkan Tuna");
        film.setCast("Tick, Trick, Track");
        film.setDate("2022-06-08");


        try {
            File file = new File("src/test/resources/sep.groupt.client/Testbild.jpg");
            FileInputStream fileInputStream = new FileInputStream(file);
            film.setBanner(fileInputStream.readAllBytes());
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            RequestHandler.sendMovie(film);

        } catch (IOException|InterruptedException e) {
        }

        try {
            for (Film tempfilm : RequestHandler.getMovies()){
                if (tempfilm.getName().equals(film.getName())){
                    filmid = tempfilm.getFilm_id();
                    break;
                }
            }
        } catch (IOException|InterruptedException e) {
        }

        nutzerid = 1;

        try {
            RequestHandler.sendSeenList(nutzerid, filmid);
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        }
        datumVon = LocalDate.now();
        datumBis = datumVon.plusMonths(2);

    }

    @Order(1)
    @Test
    void testWithGoodConditions(){
        try {
            assertEquals(true, RequestHandler.getNutzerStats(nutzerid, datumVon, datumBis));
        } catch (IOException|InterruptedException e) {
            fail();
        }
    }

    @Order(2)
    @Test
    void testwithBadConditions(){
        try {
            assertEquals(false, RequestHandler.getNutzerStats(nutzerid, datumBis, datumVon));
        } catch (IOException|InterruptedException e) {
            fail();
        }
    }

    @Order(3)
    @Test
    void testSameDay(){
        try {
            assertEquals(true, RequestHandler.getNutzerStats(nutzerid, datumVon, datumVon));
        } catch (IOException|InterruptedException e) {
            fail();
        }
    }

    @AfterAll
    void check(){
        try {
            int filmid = 0;
            for (Film tempfilm :RequestHandler.getMovies()){
                if (tempfilm.getName().equals(film.getName())){
                    filmid = tempfilm.getFilm_id();
                    break;
                }
            }
            RequestHandler.deleteMovie(filmid);

            int seenlistId = RequestHandler.getslID(nutzerid, filmid)       ;
            RequestHandler.deleteSeen(seenlistId);
        } catch (IOException|InterruptedException e) {
            System.out.println("Film and Seenlist did not revert");
        }
    }
}
