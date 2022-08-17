package sep.groupt.client;

import org.junit.jupiter.api.*;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.StatistikAdmin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatistikAdminTest {
    Film film;

    int fid;

    @BeforeAll
    void initFilmAndStuff() {
        int uid = 1;

        film = new Film();

        film.setName("TestFilm1");
        film.setCategory("Comedy");
        film.setLength(70);
        film.setDate("2022-05-18");
        film.setRegisseur("Gruppe T");
        film.setAuthor("John");
        film.setCast("Ekko, Vi, Jinx");
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        Film[] filmListe = new Film[0];
        try {
            filmListe = RequestHandler.getMovies();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Filmid herausfinden
        for (int i = 0; i < filmListe.length; i++) {
            if (filmListe[i].getName().equals(film.getName()) && filmListe[i].getDate().equals(film.getDate()) && filmListe[i].getRegisseur().equals(film.getRegisseur())) {
                fid = filmListe[i].getFilm_id();
            }
        }
    }

    @Test
    @Order(1)
    void sendStatistik(){
        StatistikAdmin statistikAdmin = new StatistikAdmin(fid,1,1,5);
        try {
            assertEquals(true,RequestHandler.sendFullStat(statistikAdmin));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    void sendGesehen(){
        StatistikAdmin statistikAdmin = new StatistikAdmin(fid,1);
        try {
            RequestHandler.sendOneStat(statistikAdmin);
            assertEquals(2,RequestHandler.getStatistikAdmin(fid).getAnzahlGesehen());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    void getStats(){
        try {
            StatistikAdmin statistikAdmin =RequestHandler.getStatistikAdmin(fid);
            if(statistikAdmin.getGesamtPunkte()==5 && statistikAdmin.getAnzahlBewertung()==1) {
                assertEquals(2, statistikAdmin.getAnzahlGesehen());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    void deleteStats(){
        try {
            assertEquals(true,RequestHandler.deleteStat(fid));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    void delSeen(){
        try {
            RequestHandler.deleteMovie(fid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    }
