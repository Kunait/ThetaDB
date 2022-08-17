package sep.groupt.client;

import com.google.gson.Gson;
import sep.groupt.client.chat.Chat;
import sep.groupt.client.chat.ChatMessage;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.Nutzer;
import sep.groupt.client.dataclass.Systemadministrator;
import sep.groupt.client.dataclass.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


public class RequestHandler {


    // -- LOGIN --

    // Client schickt Nutzerdaten an den Server. (Userdata = [Email, Passwort]
    // Als Response bekommt er einen boolean[] = [istNutzer, istAdmin] zurück

    public static boolean[] checkUserdata(String[] userdata) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/login"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(userdata)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        boolean[] ergebnis = {false, false};

        if (response.statusCode() == 200) { // Wenn Userdata mit einem Admin übereinstimmen
            ergebnis[0] = true;
            return ergebnis;
        } else if (response.statusCode() == 201) { // Wenn Userdata mit einem Nutzer übereinstimmen
            ergebnis[1] = true;
            return ergebnis;
        } else if (response.statusCode() == 202) { // Wenn Userdata mit Admin und Nutzer übereinstimmen
            ergebnis[0] = true;
            ergebnis[1] = true;
            return ergebnis;
        } else {  // Es wurde kein Admin oder Nutzer gefunden
            return ergebnis;
        }
    }

    // Client schickt Userdaten als Admin an den Server und erwartet alle Nutzerdaten zurück aus der Datenbank (ohne Salt & Passwort)

    public static Systemadministrator getAdmin(Systemadministrator admin) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/loginAdmin"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(admin)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return null;
        }

        return new Gson().fromJson(response.body(), Systemadministrator.class);
    }

    // Client schickt Userdaten als Nutzer an den Server und erwartet alle Nutzerdaten zurück aus der Datenbank (ohne Salt & Passwort)

    public static Nutzer getNutzer(Nutzer nutzer) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/loginNutzer"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(nutzer)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        /*
        if(response.statusCode() == 200){
            System.out.println("good");
        }
        if(response.statusCode() == 400){
            System.out.println("bad");
        }
        if(response.statusCode() == 500){
            System.out.println("went wrong");
        }
         */

        return new Gson().fromJson(response.body(), Nutzer.class);
    }

    public static String getAuthCode(Nutzer nutzer) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/loginNutzerAuthCode"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(nutzer, Nutzer.class)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), String.class);
    }

    // ---------------

    // -- REGISTER --

    // Schickt dem Server ein Adminobjekt mit den Daten, falls Admin hinzugefügt ist code = 200,
    // code = 400 für Admin existiert bereits, code = 500 Server konnte Admin nicht hinzufügen
    // [true, false] für 200, [false, true] für 400, [false, false] für 500

    public static boolean[] createAdmin(Systemadministrator admin) throws IOException, InterruptedException {
        boolean[] checker = {false, false};

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/registerAdmin"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(admin)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            checker[0] = true;
            return checker;
        } else if (response.statusCode() == 400) {
            checker[1] = true;
            return checker;
        } else {
            return checker;
        }
    }

    // Schickt dem Server ein Nutzerobjekt mit den Daten, falls Nutzer hinzugefügt ist code = 200,
    // code = 400 für Nutzer existiert bereits, code = 500 Server konnte Nutzer nicht hinzufügen
    // [true, false] für 200, [false, true] für 400, [false, false] für 500

    public static boolean[] createNutzer(Nutzer nutzer) throws IOException, InterruptedException {
        boolean[] checker = {false, false};

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/registerNutzer"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(nutzer)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            checker[0] = true;
            return checker;
        } else if (response.statusCode() == 400) {
            checker[1] = true;
            return checker;
        } else {
            return checker;
        }
    }

    // -----------------
    // ---- Nutzerübersicht -----

    public static ArrayList<Nutzer> getFriendList(int nutzerid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/nutzerubersicht/Freunde"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(nutzerid, Integer.class)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ArrayList<Nutzer> ergebnis = new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), Nutzer[].class)));

        return ergebnis;
    }

    public static Nutzer[] getFriendListArray(int id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/nutzerubersicht/Freunde"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(id)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Nutzer[] ergebnis = new Gson().fromJson(response.body(), Nutzer[].class);


        return ergebnis;
    }

    public static Film[] getWatchListeArray(int id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/nutzerubersicht/Watchliste"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(id)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Film[] ergebnis = new Gson().fromJson(response.body(), Film[].class);


        return ergebnis;
    }

    public static Film[] getSeenListeArray(int id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/nutzerubersicht/SeenListe"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(id)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Film[] ergebnis = new Gson().fromJson(response.body(), Film[].class);


        return ergebnis;
    }


    public static ArrayList<Film> getWatchListe(int nutzerid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/nutzerubersicht/Watchliste"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(nutzerid, Integer.class)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ArrayList<Film> ergebnis = new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), Film[].class)));

        return ergebnis;
    }

    public static ArrayList<Film> getSeenListe(int nutzerid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/nutzerubersicht/SeenListe"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(nutzerid, Integer.class)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ArrayList<Film> ergebnis = new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), Film[].class)));

        return ergebnis;
    }

    // ------------------------


    public static boolean sendBanner(Film film) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/banner"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(film)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static byte[] getBanner(Film film) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getBanner"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(film, Film.class)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        byte[] ergebnis = null;
        if (response.statusCode() == 200) {
            ergebnis = new Gson().fromJson(response.body(), byte[].class);
        } else {
            InputStream inputStream = RequestHandler.class.getResourceAsStream("banner/default.jpg");
            ergebnis = inputStream.readAllBytes();
            inputStream.close();
        }
        return ergebnis;
    }

    public static boolean sendScrapingData(String category, String dateVon, String dateBis, int anzahl, boolean banner) throws IOException, InterruptedException {
        String[] data = new String[5];
        data[0] = dateVon;
        data[1] = dateBis;
        data[2] = category;
        data[3] = Integer.toString(anzahl);
        data[4] = Boolean.toString(banner);
        System.out.println(new Gson().toJson(data).toString());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/scrape"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(data)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Boolean.class);
    }

    public static void sendReport(Report rep) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/addReport"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(rep)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void readReport(Report rep) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/readReport"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(rep)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void sendEinladung(Einladung ein) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/addEinladung"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(ein)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static Einladung[] getEinladungen(int nutzerid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getEinladungen"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(nutzerid)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), Einladung[].class);
    }

    public static boolean deleteEinladung(int ein_id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/deleteEinladung/" + ein_id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static void sendResponseMail(Einladung ein) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/sendResponseMail"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(ein)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    public static void changeSettings(Nutzer nutzer) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/changeSettings"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(nutzer)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static Report[] getReports() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getReportlist"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Report[].class);
    }

    public static boolean sendMovie(Film film) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/addMovie"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(film)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean editMovie(Film film) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/editMovie"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(film)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static void deleteMovie(Film film) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/deleteMovie"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static boolean deleteMovie(int film_id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/DeleteMovie/" + film_id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static Film getMovie(int filmid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getMovie/" + filmid))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Film.class);
    }

    public static Film[] getMovies() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getMovielist"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Film[].class);
    }

    public static void sendMessage(ChatMessage message) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/sendChat"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(message)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


    }

    public static Nutzer getTestChat() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getTestChat"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Nutzer nutzer = new Gson().fromJson(response.body(), Nutzer.class);
        return nutzer;
    }


    public static Film[] getFilterFilm(String name, String kategorie, String cast, String von, String bis) throws IOException, InterruptedException {
        String[] data = {name, kategorie, cast, von, bis};

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/filter"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(data)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        return new Gson().fromJson(response.body(), Film[].class);
    }


    public static Film[] getsl(int id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getSeenList"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(id)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Film[].class);

    }

    public static Bewertung getBewertung(int uid, int fid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        int[] data = new int[2];
        data[0] = uid;
        data[1] = fid;
        //System.out.println(new Gson().toJson(data).toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getBewertungEinzeln"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(data)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Bewertung.class);
    }

    public static boolean sendBewertung(Bewertung bewertung) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/sendBewertung"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(bewertung)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return true;
        } else {
            return false;
        }

    }

    public static Bewertung[] getBewertungen(int filmid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getBewertungen"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(filmid)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Bewertung[].class);
    }

    public static double getDurchnittsBewertung(int filmid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getDurchschnittsBewertung"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(filmid)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Double.class);
    }

    public static boolean addToWatchlist(Watchlist list) throws IOException, InterruptedException {
        boolean checker = false;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/addWatchlist"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(list)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            checker = true;
            return checker;
        } else
            return checker;
    }

    public static boolean sendSeenList(int uid, int fid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();


        int[] data = new int[2];
        data[0] = uid;
        data[1] = fid;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/sendSeenList"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(data)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static void deleteBewertung(Bewertung bewertung) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/deleteBewertung"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(bewertung)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static boolean updateBewertung(Bewertung bewertung) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/updateBewertung"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(bewertung)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static Film[] getwl(int id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getWatchList"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(id)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Film[].class);

    }

    public static boolean deleteWatchList(Watchlist watchlist) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/deleteWatchList"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(watchlist)))
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean inWatchlist(Watchlist list) throws IOException, InterruptedException {
        boolean checker = false;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/inWatchlist"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(list)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), boolean.class);
    }


    public static Film[] getMoviesFilter() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getMovieFilter"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), Film[].class);
    }

    /*
    public static Bewertung[] getBewertungen2() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getListOfBewertung"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(fid)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Bewertung[].class);
    }

     */
    public static Chat getChat(Nutzer sender, Nutzer receiver) throws IOException, InterruptedException {

        int firstID = Math.min(sender.getUserID(), receiver.getUserID());
        int secondID = Math.max(sender.getUserID(), receiver.getUserID());

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getChat"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(new Chat(sender, receiver))))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Chat chat = new Gson().fromJson(response.body(), Chat.class);
        return chat;


    }

    public static Nutzer[] searchUser(String filter) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/nutzerubersicht/filterUser"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(filter)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());


        Nutzer[] nutzer = new Gson().fromJson(response.body(), Nutzer[].class);

        return nutzer;

    }

    public static byte[] getProfilbild(int id) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/profilbildNutzer"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(id)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        byte[] bild = new Gson().fromJson(response.body(), byte[].class);

        return bild;

    }

    public static Nutzer getNutzerByID(int userID) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/nutzerByID"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(userID)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Nutzer nutzer = new Gson().fromJson(response.body(), Nutzer.class);

        return nutzer;
    }

    public static void addFriend(int idUser, int idFriend) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String ids = idUser + "-" + idFriend;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/addFriend"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(ids)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("request ging raus");


    }

    public static void addFriendRequest(int idUser, int idFriend) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String ids = idUser + "-" + idFriend;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/addFriendRequest"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(ids)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("request ging raus");


    }

    public static Nutzer[] getAnfragenListe(int nutzerid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getFriendRequests"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(nutzerid)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Nutzer[] n = new Gson().fromJson(response.body(), Nutzer[].class);


        System.out.println("request ging raus");

        return n;
    }


    public static void deleteFriend(Nutzer currentNutzer, Nutzer friend) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        String ids = currentNutzer.getUserID() + "-" + friend.getUserID();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/deleteFriend"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(ids)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    public static void deleteFriendRequest(Nutzer currentNutzer, Nutzer requester) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        String ids = currentNutzer.getUserID() + "-" + requester.getUserID();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/deleteFriendRequest"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(ids)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());


    }

    public static boolean checkBewertung(int filmId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/checkBewertung"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(filmId)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), boolean.class);


    }

    public static boolean checkSeenlist(Watchlist list) throws IOException, InterruptedException {


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/checkSeenlist"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(list)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), boolean.class);
    }


    public static void deleteSeen(int slid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/deleteSeen"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(slid)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    public static int getslID(int uid, int fid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        int[] data = new int[2];
        data[0] = uid;
        data[1] = fid;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getIDofSeenlist"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(data)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int result = new Gson().fromJson(response.body(), int.class);
        return result;
    }

    public static int getWlID(int uid, int fid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        int[] data = new int[2];
        data[0] = uid;
        data[1] = fid;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getIDofWatchList"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(data)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int result = new Gson().fromJson(response.body(), int.class);
        return result;
    }

    public static StatistikAdmin getStatistikAdmin(int fid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getStatistikAdmin"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(fid)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        StatistikAdmin result = new Gson().fromJson(response.body(), StatistikAdmin.class);
        return result;
    }

    public static void sendOneStat(StatistikAdmin statistikAdmin) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/sendOneStat"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(statistikAdmin)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    public static boolean sendFullStat(StatistikAdmin statistikAdmin) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/sendFullStat"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(statistikAdmin)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean deleteStat(int fid) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/deleteStat"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(fid)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static Film[] getFilmvorschlaege(String kategorie) throws IOException, InterruptedException {
        String data = kategorie;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/filmvorschlaege"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(data)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Film[].class);

    }

    public static Film[] getLatestSL(int id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getLatestSL"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(id)))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Film[].class);

    }

    public static Integer[] getFreundeID(int id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getFreundeID"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(id)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Integer[] ergebnis = new Gson().fromJson(response.body(), Integer[].class);

        System.out.println(response.statusCode());

        return ergebnis;
    }


    public static boolean getNutzerStats(int nutzerid, LocalDate datumVon, LocalDate datumBis) throws IOException, InterruptedException {
        if (datumVon.compareTo(datumBis) <= 0) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/NutzerStats/" + nutzerid + "/" + datumVon + "/" + datumBis + "/"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                StatsNutzer statsNutzer = new Gson().fromJson(response.body(), StatsNutzer.class);
                Client.setStatsNutzer(statsNutzer);

                return true;
            }
        }
        return false;

    }

    public static boolean getDiskussionen(Nutzer nutzer) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getDiskussionen"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(nutzer)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Diskussionsgruppe[] gruppen = new Gson().fromJson(response.body(), Diskussionsgruppe[].class);
        Client.setGruppen(gruppen);
        if (response.statusCode() == 200) {


            return true;
        }

        return false;




    }

    public static boolean joinDiskussion(Diskussionsgruppe diskussionsgruppe) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/joinDiskussion"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(diskussionsgruppe)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("request gesendet" + diskussionsgruppe.toString() + " " + diskussionsgruppe.getJoiningUserID());
        if (response.statusCode() == 200) {


            return true;
        }

        return false;

    }
    public static boolean cleanUpTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/cleanUpTest"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {


            return true;
        }

        return false;

    }

    public static boolean addDiskussion(Diskussionsgruppe diskussionsgruppe) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/addDiskussion"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(diskussionsgruppe)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("request gesendet" + diskussionsgruppe.toString() + " " + diskussionsgruppe.getName());
        if (response.statusCode() == 200) {


            return true;
        }

        return false;

    }

    public static Diskussionsgruppe[] getJoinedDiskussionen(Nutzer nutzer) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getJoinedDiskussionen"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(nutzer)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Diskussionsgruppe[] gruppen = new Gson().fromJson(response.body(), Diskussionsgruppe[].class);



        return gruppen;

    }

    public static DiskussionInhalt getDiskussionInhalt(Diskussionsgruppe selectedGruppe) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getDiskussionInhalt"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(selectedGruppe)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

       DiskussionInhalt inhalt = new Gson().fromJson(response.body(),DiskussionInhalt.class);

       return inhalt;

    }

    public static void sendDiskussion(ChatMessage message) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/sendDiskussion"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(message)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void setPrivat(Diskussionsgruppe selectedGruppe) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/setPrivat"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(selectedGruppe)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void setOpen(Diskussionsgruppe selectedGruppe) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/setOpen"))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(selectedGruppe)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}