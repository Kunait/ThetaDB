package sep.groupt.client;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import sep.groupt.client.dataclass.Film;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmhinzufuegenTest {

    Film film;

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
    }

    // Sendet den Film an den Server und speichert ihn in der Datenbank ab.
    // sendMovie sollte true zurückgeben, weil der Film noch nicht in der Datenbank ist

    @Test
    @Order(1)
    void addMovieToDatabase(){
        try {
            assertEquals(true, RequestHandler.sendMovie(film));
        } catch (IOException|InterruptedException e) {
            System.out.println("No Connection to Server!");
        }
    }

    // Sendet nochmal den Film und versucht ihn abzuspeichern
    // sendMovie sollte false zurückgeben, weil der Film bereits in der Datenbank ist

    @Test
    @Order(2)
    void checkMovieInDatabase(){
        try {
            assertEquals(false, RequestHandler.sendMovie(film));
        } catch (IOException|InterruptedException e) {
            System.out.println("No Connection to Server!");
        }
    }

    /*
    @Test
    @Order(3)
    void deleteMovieFromDatabase(){
        try {
            int filmid = 0;
            for (Film tempfilm : RequestHandler.getMovies()){
                if (tempfilm.getName().equals(film.getName())){
                    filmid = tempfilm.getFilm_id();
                    break;
                }
            }
            assertEquals(true, RequestHandler.deleteMovie(filmid));
        } catch (IOException|InterruptedException e) {
            System.out.println("No Connection to Server!");
        }
    }

    @Test
    @Order(4)
    void checkMovieDeleted(){
        try {
            int filmid = 0;
            for (Film tempfilm :RequestHandler.getMovies()){
                if (tempfilm.getName().equals(film.getName())){
                    filmid = tempfilm.getFilm_id();
                    break;
                }
            }
            assertEquals(0, filmid);
        } catch (IOException|InterruptedException e) {
            System.out.println("No Connection to Server!");
        }
    }

     */

    // Löscht den Film falls er noch existiert

    @AfterAll
    void checkDataBase(){
        try {
            int filmid = 0;
            for (Film tempfilm :RequestHandler.getMovies()){
                if (tempfilm.getName().equals(film.getName())){
                    filmid = tempfilm.getFilm_id();
                    break;
                }
            }
            RequestHandler.deleteMovie(filmid);
        } catch (IOException|InterruptedException e) {
            System.out.println("No Connection to Server!");
        }
    }
}
