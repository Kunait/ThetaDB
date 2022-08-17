package sep.groupt.client;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import sep.groupt.client.RequestHandler;
import sep.groupt.client.dataclass.Bewertung;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.Watchlist;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmBewertenTest {

    Film film;

    int fid;


    Bewertung bewertung = new Bewertung();
    Watchlist list2 = new Watchlist();

    int WID;
    int slid;
    int bid;
    Bewertung bewertung2 = new Bewertung();


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
        for(int i = 0; i < filmListe.length;i++)
        {
            if(filmListe[i].getName().equals(film.getName())&&filmListe[i].getDate().equals(film.getDate())&&filmListe[i].getRegisseur().equals(film.getRegisseur()))
            {
                fid = filmListe[i].getFilm_id();
            }
        }

        Watchlist list = new Watchlist(uid, fid);

        try {
            RequestHandler.addToWatchlist(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        int wlid = 0;
        try {
            wlid= RequestHandler.getWlID(1,fid);
            WID =wlid;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



    }

    @Test
    @Order(1)
    void Bewertung(){
        bewertung = new Bewertung(fid,1,1,"Okay");


        try {
            assertEquals(true,RequestHandler.sendBewertung(bewertung));
            Bewertung[] bert = RequestHandler.getBewertungen(fid);
            for(int i = 0; i < bert.length;i++)
            {
                if(bert[i].getUserID()==1)
                {
                    //System.out.println(bert[i].getBewertungID());
                    bewertung2.setBewertungID(bert[i].getBewertungID());
                    bewertung2.setFilmID(bert[i].getFilmID());
                    bewertung2.setUserID(bert[i].getUserID());

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    //list2 = new Watchlist(WID,1,fid);
    @Test
    @Order(2)
    void Seenlist(){
        try {
            assertEquals(true,RequestHandler.sendSeenList(1,fid));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
           slid= RequestHandler.getslID(1,fid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    void del(){
        try {
            Watchlist watchlist = new Watchlist(WID,fid,1);
            //System.out.println(watchlist.getWatchlistID());
            //System.out.println(watchlist.getFilmID());
            assertEquals(true,RequestHandler.deleteWatchList(watchlist));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    void updateBewertung(){
        Bewertung[] bert = new Bewertung[0];
        Bewertung bewertung3 = new Bewertung();
        try {
            bert = RequestHandler.getBewertungen(fid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0; i < bert.length;i++)
        {
            if(bert[i].getUserID()==1) {
                bewertung3.setBewertungID(bert[i].getBewertungID());
                bewertung3.setFilmID(bert[i].getFilmID());
                bewertung3.setUserID(bert[i].getUserID());
                bewertung3.setPunkte(3);
                bewertung3.setBewertung("Super");
                //System.out.println(bewertung3.getBewertungID());
            }
        }
        try {
            assertEquals(true,RequestHandler.updateBewertung(bewertung3));
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
            RequestHandler.deleteBewertung(bewertung2);
            RequestHandler.deleteSeen(slid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
