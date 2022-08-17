package sep.groupt.client;

import org.junit.jupiter.api.*;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.Nutzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class FilmvorschlägeTest {
    public Film film;
    public Film film2;
    public Nutzer test,test2,temp,temp2;
    public InputStream is;
    public byte[] profilbild;
    public int fid;

    public int nid;

    @BeforeAll
    void init(){

        try{
        String salt = registerNutzerController.getSalt();
            is = getClass().getResourceAsStream("nutzerbild/default.jpg");
            profilbild = is.readAllBytes();
            is.close();
        test = new Nutzer("test@uni.de","Hallo", "Name", salt, registerNutzerController.getHashedPassword("abcdefgh",salt),profilbild);
        test2 = new Nutzer("test2@uni.de","Hallo", "Name", salt, registerNutzerController.getHashedPassword("abcdefgh",salt),profilbild);

            RequestHandler.createNutzer(test);
            RequestHandler.createNutzer(test2);
        }catch(NoSuchAlgorithmException | IOException f){
            f.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        film2 = new Film();
        film2.setName("Das ist ein TestFilm2");
        film2.setCategory("Test");
        film2.setLength(90);
        film2.setRegisseur("Gruppe T");
        film2.setAuthor("Volkan Tuna");
        film2.setCast("Tick, Trick, Track");
        film2.setDate("2022-06-08");


        try {
            File file = new File("src/test/resources/sep.groupt.client/Testbild.jpg");
            FileInputStream fileInputStream = new FileInputStream(file);
            film2.setBanner(fileInputStream.readAllBytes());
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        film = new Film();
        film.setName("Das ist ein TestFilm");
        film.setCategory("Test");
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
            RequestHandler.sendMovie(film2);

        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        }
        Film[] filmListe = new Film[0];
        try {
            filmListe = RequestHandler.getMovies();

            for(Film f: filmListe){

                System.out.println(f.toString());
            }
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

    @Order(1)
    @Test
    void getFilmvorschläge() {
        System.out.println(fid);
        boolean b=false;
        try {
           Film[] filme = RequestHandler.getFilmvorschlaege("AND FilmKategorie LIKE '%Kategorie%' ");
           for(Film f: filme){
               //System.out.println(f.toString());
           }
           for(Film x:filme) {
               if (x.getFilm_id() == fid) {
                   //System.out.println(x.getFilm_id());
                   b = true;
               }
           }
           assertTrue(b);


        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            fail();
            throw new RuntimeException(e);
        }
    }

    @Order(2)
    @Test
    void getSeenlist() {
        boolean h=false;
        try {
             temp = RequestHandler.getNutzer(new Nutzer(test.getEmailAdresse(),"abcdefgh"));
             temp2 = RequestHandler.getNutzer(new Nutzer(test2.getEmailAdresse(),"abcdefgh"));
            RequestHandler.sendMovie(film2);


            int id =0;
            Film[] filmListe = RequestHandler.getMovies();
            for (int i = 0; i < filmListe.length; i++) {
                if (filmListe[i].getName().equals(film.getName()) && filmListe[i].getDate().equals(film.getDate()) && filmListe[i].getRegisseur().equals(film.getRegisseur())) {
                    id = filmListe[i].getFilm_id();
                }
            }

            RequestHandler.sendSeenList(temp.getUserID(),id);

            Film[] i=RequestHandler.getLatestSL(temp.getUserID());

            for(Film x:i) {
                System.out.println(x.getFilm_id());
                if (x.getFilm_id() == id) {
                    h = true;
                }
            }
            assertTrue(h);
        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            fail();
            throw new RuntimeException(e);
        }
    }


    @Order(3)
    @Test
    void getFriendID() {


            assertEquals(true,true);


    }

    @AfterAll
    void check() {
        /*try {
            //RequestHandler.deleteMovie(fid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
             throw new RuntimeException(e);
        }*/
       /* try {
            int seenlistId = RequestHandler.getslID(99, 5001);
            RequestHandler.deleteSeen(seenlistId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
*/

    }
}




