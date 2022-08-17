package sep.groupt.client;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.util.HttpCookieStore;
import sep.groupt.client.dataclass.*;

import java.io.IOException;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Objects;


public class Client extends Application {

    private static Systemadministrator currentAdmin;

    private static Diskussionsgruppe selectedGruppe;

    private static Diskussionsgruppe[] gruppen;

    public static Diskussionsgruppe[] getGruppen() {
        return gruppen;
    }

    public static void setGruppen(Diskussionsgruppe[] gruppen) {
        Client.gruppen = gruppen;
    }

    public static Diskussionsgruppe getSelectedGruppe() {
        return selectedGruppe;
    }

    public static void setSelectedGruppe(Diskussionsgruppe selectedGruppe) {
        Client.selectedGruppe = selectedGruppe;
    }

    private static Nutzer currentNutzer;

    private static String filmSuche;

    public static String getFilmSuche() {
        return filmSuche;
    }

    public static void setFilmSuche(String filmSuche) {
        Client.filmSuche = filmSuche;
    }

    private static Nutzer profilAnsichtNutzer;

    public static Nutzer getProfilAnsichtNutzer() {
        return profilAnsichtNutzer;
    }

    public static void setProfilAnsichtNutzer(Nutzer profilAnsichtNutzer) {
        Client.profilAnsichtNutzer = profilAnsichtNutzer;
    }

    private static String filter;

    private static String search;

    private static Film selectedFilm;

    public static Film getSelectedFilm(){

        return Client.selectedFilm;
    }

    public static void setSelectedFilm(Film film){

        Client.selectedFilm = film;
    }

    private static StatsNutzer statsNutzer;

    public static StatsNutzer getStatsNutzer() {
        return statsNutzer;
    }

    public static void setStatsNutzer(StatsNutzer statsNutzer) {
        Client.statsNutzer = statsNutzer;
    }

    public static void setSearch(String search){

        Client.search = search;
    }

    public static void setFilter(String filter){

        Client.filter = filter;
    }

    public static Nutzer getChatReceiver() {
        return chatReceiver;
    }

    public static void setChatReceiver(Nutzer chatReceiver) {
        Client.chatReceiver = chatReceiver;
    }

    private static Nutzer chatReceiver;

    public static void setCurrentPane(AnchorPane currentPane) {
        Client.currentPane = currentPane;
    }

    protected static Stage currentStage;

    public static AnchorPane getCurrentPane() {
        return currentPane;
    }

    protected static AnchorPane currentPane;

    HttpClient httpClient;

    public static String getFilter() {

        return filter;
    }

    public static String getSearch() {

        return Client.search;
    }


    // Startet die Anwendung und öffnet das Login-Fenster

    @Override
    public void start(Stage stage) throws IOException {
        Parent login = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        currentStage = stage;
        currentStage.setScene(new Scene(login));
        currentStage.setTitle("Login");
        currentStage.setResizable(false);
        currentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
            }
        });
        currentStage.show();

        // currentUser = new Systemadministrator("Vorname","Nachname","lol","xD",1);
            /*try {

            currentUser = new Systemadministrator(1,"Vorname","Nachname","lol","xD");
            try {

                startHTTP();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

             */
    }
    private void startHTTP() throws Exception {

        httpClient = new HttpClient();

        CookieStore cookieStore = new HttpCookieStore();
        HttpCookie authName = new HttpCookie("username", currentAdmin.getEmailAdresse());
        HttpCookie authPass = new HttpCookie("password",currentAdmin.getPasswort());
        cookieStore.add(URI.create("http://localhost:2525/login"),authName);
        cookieStore.add(URI.create("http://localhost:2525/login"),authPass);
        httpClient.setCookieStore(cookieStore);
        httpClient.start();

        Request request = httpClient.newRequest(URI.create("http://localhost:2525/test"));
        request.method("POST");
        ContentResponse response = request.send();

        System.out.println(response.getContentAsString());

    }

    public static void main(String[] args) {
        launch();
    }


    public static void connectionLost(String fehlerCode){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler: " + fehlerCode);
        alert.setContentText("Es besteht keine Verbindung zum Server.");
        alert.showAndWait();
    }

    public static void viewSwitchFailed(String fehlerCode, String path){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler: " + fehlerCode);
        alert.setContentText("Konnte nicht auf die Seite: " + path + " weitergeleitet werden!");
        alert.showAndWait();
    }



    // Getter und Setter ab hier

    public static  Stage getCurrentStage() {

        return currentStage;

    }

    public static Systemadministrator getCurrentAdmin() {
        return currentAdmin;
    }

    public static void setCurrentAdmin(Systemadministrator admin){
        currentAdmin = admin;
        currentNutzer = null;
    }

    public static Nutzer getCurrentNutzer() {
        return currentNutzer;
    }

    public static void setCurrentNutzer(Nutzer nutzer){
        currentAdmin = null;
        currentNutzer = nutzer;
    }

}



