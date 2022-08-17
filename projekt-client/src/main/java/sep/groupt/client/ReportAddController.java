package sep.groupt.client;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.Report;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ReportAddController extends SceneController implements Initializable {

    @FXML
    private Text welcomeMessage;
    @FXML
    private Circle profilBild;
    @FXML
    private Button backButton;

    @FXML
    private Button ConfirmButton;

    @FXML
    private TextArea reportFeld;

    @FXML
    private Text filmdrehbuch, filmname, filmcast, filmregie, filmdatum, filmlaenge, filmkategorie, uptxt;

    private Film film;
    private Report report;

    FilmansichtController mang = new FilmansichtController();

    public void importData() {
        film = mang.getFilm(); //TODO: Das muss dann natürlich der ausgewählte Film aus der voherigen Scene sein
        if(filmname.getText().isEmpty()){
            filmname.setText(film.getName());
        }
        if(filmkategorie.getText().isEmpty()) {
            filmkategorie.setText(film.getCategory());
        }
        if(filmlaenge.getText().isEmpty()) {
            filmlaenge.setText(Integer.toString(film.getLength()) + " min");
        }
        if(filmdatum.getText().isEmpty()) {
            filmdatum.setText(film.getDate());
        }
        if(filmregie.getText().isEmpty()) {
            filmregie.setText(film.getRegisseur());
        }
        if(filmcast.getText().isEmpty()) {
            filmcast.setText(film.getCast());
        }
        if(filmdrehbuch.getText().isEmpty()) {
            filmdrehbuch.setText(film.getAuthor());
        }
        // Zeigt Profilbild des Benutzers an
        InputStream inputStream = new ByteArrayInputStream(Client.getCurrentNutzer().getProfilbild());
        Image image = new Image(inputStream);
        profilBild.setFill(new ImagePattern(image));

        // Ändert den Willkommenstext in Hallo, Vorname Nachname
        welcomeMessage.setText("Hallo, " + Client.getCurrentNutzer().getVorname() + " " + Client.getCurrentNutzer().getNachname());
        reportFeld.setWrapText(true);

    }

    public Film getEditFilm(){
        return film;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        importData();
    }

    public void ConfirmButtonPressed() throws IOException {
        String s = getTime();
        report = new Report(s, film, reportFeld.getText(), false);
        try {
            RequestHandler.sendReport(report);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        switchToSceneWithStage("Filmansicht.fxml", "Film ansehen");
    }

    public String getTime(){
        LocalDateTime now= LocalDateTime.now();
        String tempi= "" + now.getYear();
        if(now.getMonthValue()<10){
            tempi = tempi + "-" + "0" + now.getMonthValue();
        }else{tempi = tempi + "-" + now.getMonthValue();}
        if(now.getDayOfMonth()<10){
            tempi = tempi + "-" + "0" + now.getDayOfMonth() + " ";
        }else{tempi = tempi + "-" + now.getDayOfMonth() + " ";}
        if(now.getMinute() > 10)
        tempi = tempi + now.getHour() + ":" + now.getMinute();
        else
        tempi = tempi + now.getHour() + ":0"+now.getMinute();
        System.out.println(tempi);
        return tempi;
    }

    public void backButtonPressed() throws IOException {
        Client.setSelectedFilm(film);
        FilmansichtController.setSeite("watchlist.fxml");
        switchToSceneWithStage("Filmansicht.fxml", "Filmansicht");
    }

    public void abmeldenBoxPressed(){
        try {
            switchToSceneWithStage("login.fxml", "Login");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Login");
        }
    }

    public void einstellungenBoxPressed(){
        try {
            switchToSceneWithStage("editProfile.fxml", "Einstellungen");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "platzhalter");
        }
    }

}
