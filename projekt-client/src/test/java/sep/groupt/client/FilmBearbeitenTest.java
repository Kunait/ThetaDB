package sep.groupt.client;


import org.junit.jupiter.api.*;
import sep.groupt.client.dataclass.Film;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmBearbeitenTest {

    Film film;
    Film editedFilm;

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
            System.out.println("No Connection to Server!");
        }
    }


    // Film wurde bearbeitet unter editedFilm
    // editMovie sollte true zurückgeben, weil der Film in der Datenbank existiert

    @Test
    @Order(1)
    void editMovie(){
        editedFilm = film;
        editedFilm.setLength(80);
        editedFilm.setCast("Gummibärenbande, Herzog Igzorn");


        int filmid = 0;
        try {
            for (Film tempfilm : RequestHandler.getMovies()){
                if (tempfilm.getName().equals(film.getName())){
                    filmid = tempfilm.getFilm_id();
                    break;
                }
            }
        } catch (IOException|InterruptedException e) {
            System.out.println("No Connection to Server!");
        }

        editedFilm.setFilm_id(filmid);


        try {
            assertEquals(true, RequestHandler.editMovie(editedFilm));
        } catch (IOException|InterruptedException e) {
            System.out.println("No Connection to Server!");
        }
    }


    @Test
    @Order(2)
    void changeMovieBanner(){
        int filmid = 0;
        try {
            for (Film tempfilm :RequestHandler.getMovies()){
                if (tempfilm.getName().equals(film.getName())){
                    filmid = tempfilm.getFilm_id();
                    break;
                }
            }
        } catch (IOException|InterruptedException e) {
            System.out.println("No Connection to Server!");
        }

        editedFilm = film;
        editedFilm.setFilm_id(filmid);

        try {
            File file = new File("src/test/resources/sep.groupt.client/gummibaren.jpg");
            FileInputStream fileInputStream = new FileInputStream(file);
            editedFilm.setBanner(fileInputStream.readAllBytes());
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(true, RequestHandler.sendBanner(editedFilm));
        } catch (IOException|InterruptedException e) {
            System.out.println("No Connection to Server!");
        }
    }


    // Versuch einen editierten Film zu senden, der nicht in der Datenbank vorhanden ist

    @Test
    @Order(3)
    void editMovieWhichIsNotInDatabase(){
        editedFilm = film;
        editedFilm.setLength(80);
        editedFilm.setCast("Gummibärenbande, Herzog Igzorn");

        try {
            int filmid = 0;
            for (Film tempfilm : RequestHandler.getMovies()){
                if (tempfilm.getName().equals(film.getName())){
                    filmid = tempfilm.getFilm_id();
                    break;
                }
            }
            RequestHandler.deleteMovie(filmid);
            assertEquals(false, RequestHandler.editMovie(editedFilm));
        } catch (IOException|InterruptedException e) {
            System.out.println("No Connection to Server!");
        }
    }

    // Test


    @Test
    @Order(4)
    void changeMovieBannerWhichMovieIsNotInDatabase(){
        int filmid = 0;
        try {
            for (Film tempfilm :RequestHandler.getMovies()){
                if (tempfilm.getName().equals(film.getName())){
                    filmid = tempfilm.getFilm_id();
                    break;
                }
            }
        } catch (IOException|InterruptedException e) {
            System.out.println("No Connection to Server!");
        }

        editedFilm = film;
        editedFilm.setFilm_id(filmid);

        try {
            File file = new File("src/test/resources/sep.groupt.client/gummibaren.jpg");
            FileInputStream fileInputStream = new FileInputStream(file);
            editedFilm.setBanner(fileInputStream.readAllBytes());
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(false, RequestHandler.sendBanner(editedFilm));
        } catch (IOException|InterruptedException e) {
            System.out.println("No Connection to Server!");
        }
    }



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
