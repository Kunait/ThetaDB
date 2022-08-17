package sep.groupt.server;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import sep.groupt.server.dataclass.Film;
import sep.groupt.server.handler.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Scanner;

public class ServerClass {


    Server server;
    private static final Scanner input = new Scanner(System.in);

    public void start() throws Exception {
        // Starte Server

        startServer();

        inputDatabaseData();
        //inputAPIKeys();
        checkDirectorys();
        connectToDatabase();
    }

    // Datenbank username und passwort abfragen

    private void inputDatabaseData() {
        System.out.println("Bitte Datenbank Benutzername eintragen!");
        String username = input.nextLine();
        Datenbank.setUser(username);
        System.out.println("Bitte Passwort fuer die Datenbank eintragen!");
        String password = input.nextLine();
        Datenbank.setPassword(password);
    }

    // Email Adresse und API Keys abfragen

    private void inputAPIKeys() {
        System.out.println("Bitte Email-Adresse eingeben!");
        String email = input.nextLine();
        EmailService.setEmail(email);
        System.out.println("Sendgrid API-CODE für SMTP eintragen!");
        String SMTPAPIKEY = input.nextLine();
        EmailService.setPasswort(SMTPAPIKEY);
        System.out.println("Sendgrid API-CODE für das Senden von E-mails eintragen!");
        String emailApi = input.nextLine();
        EmailService.setSendgridEmailAPIKEY(emailApi);
        input.close();
    }

    //Server wird gestartet auf dem Port 2525
    //Die Handler werden hinzugefügt
    private void startServer() throws Exception {
        int port = 8080;
        server = new Server(port);

        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(new ReturnMovie());
        handlers.addHandler(new ReturnMovies());
        handlers.addHandler(new ReturnReports());
        handlers.addHandler(new LoginHandler());
        handlers.addHandler(new AddMovieHandler());
        handlers.addHandler(new EinladungHandler());
        handlers.addHandler(new AddReportHandler());
        handlers.addHandler(new UserSettingsHandler());
        handlers.addHandler(new EditMovieHandler());
        handlers.addHandler(new RegisterHandler());
        handlers.addHandler(new newBannerHandler());
        handlers.addHandler(new SeenListHandler());
        handlers.addHandler(new WatchlistHandler());
        handlers.addHandler(new ChatHandler());
        handlers.addHandler(new FilmSuchenHandler());
        handlers.addHandler(new BewertungHandler());
        handlers.addHandler(new NutzerUbersichtHandler());
        handlers.addHandler(new ProfilbildHandler());
        handlers.addHandler(new userSearchHandler());
        handlers.addHandler(new getBannerHandler());
        handlers.addHandler(new DeleteMovieHandler());
        handlers.addHandler(new StatistikAdminHandler());
        handlers.addHandler(new FilmvorschlaegeHandler());
        handlers.addHandler(new NutzerStatsHandler());
        handlers.addHandler(new DiskussionHandler());
        server.setHandler(handlers);

        server.start();
    }

    // Erstellt die Datenbankstruktur (Comment bei der Methode selbst)

    private void connectToDatabase() throws SQLException {
        Datenbank.setupDatabase();
    }

    private void checkDirectorys() throws IOException {
        File banner = new File("banner/");
        if (!banner.exists()) {
            banner.mkdir();
        }


        File profilbilder = new File("profilbilder/");
        if (!profilbilder.exists()) {
            profilbilder.mkdir();
        }
    }

    private void renameMovieBanner() {
        File file = new File("banner/");


        try {
            for (Film film : Datenbank.getMovies()) {
                File filmFile = new File("banner/" + film.getName().replaceAll(":", "").replaceAll("\\*", "").replaceAll("/", "").replaceAll("[^a-zA-Z0-9]", " ") + ".jpg");
                if (filmFile.exists()) {
                    File filmFileNew = new File("banner/" + film.getFilm_id() + ".jpg");
                    filmFile.renameTo(filmFileNew);
                    Datenbank.setBannerForMovie(file.getAbsolutePath().replaceAll("\\\\", "/"), film.getFilm_id());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Hier wird versucht den Server vernünftig herunterzufahren.
    public void downRight() {
        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            close();
        } catch (IOException e) {
            System.err.println("Server konnte nicht heruntergefahren werden! Fehler!");
            e.printStackTrace();
        }
    }

    //Falls dies nicht funktioniert wird der Server force shutdowned
    public void close() throws IOException {
        System.exit(1);
    }
}
