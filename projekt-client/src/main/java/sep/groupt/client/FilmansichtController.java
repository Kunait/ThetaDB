package sep.groupt.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.apache.commons.io.output.ByteArrayOutputStream;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.Watchlist;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class FilmansichtController extends SceneController implements Initializable {

    public Button fehler;

    @FXML
    private Text welcomeMessage;

    @FXML
    private Circle profilBild;
    @FXML
    private ImageView imageView;
    @FXML
    private Button zurueck, watchlist, bewertung;

    @FXML
    private Text titel, kategorie, regisseur, drehbuchautor, cast, erscheinungsdatum, filmlaenge;


    private static Film film;

    private static String seite;


    public String getSeite() {
        return seite;
    }

    public static void setSeite(String string) {
        seite = string;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public void fehlerClicked() throws IOException {
        switchToSceneWithStage("errorreport.fxml", "Fehler melden");
    }

    public void einladenButtonPressed() throws IOException {
        switchToSceneWithStage("einladen.fxml", "User einladen");
    }

    public void zurueckClicked() {
        try {

            switchToSceneWithStage("nutzeruebersicht.fxml","Nutzerübersicht");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void bewertungClicked() {
        try {
            switchToSceneWithStage("Bewertungen.fxml", "Bewertungen");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Keine Bewertungen");
        }

    }

    public void watchlistClicked() throws IOException, InterruptedException {
        int uid = Client.getCurrentNutzer().getUserID();
        Watchlist list = new Watchlist(uid, film.getFilm_id());
        if (!RequestHandler.inWatchlist(list)) {
            RequestHandler.addToWatchlist(list);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Watchlist");
            alert.setHeaderText("Erfolgreich");
            alert.setContentText("Der Film wurde zur Watchlist hinzugefügt!");
            alert.showAndWait();
            watchlist.setText("delete from Watchlist");
        } else {
            Watchlist entfernen = null;
            Film[] hilfe =RequestHandler.getwl(Client.getCurrentNutzer().getUserID());
            for (Film x:hilfe) {
                if(x.getFilm_id()==film.getFilm_id()){
                    entfernen=new Watchlist(x.getWatchlist_id(), uid , x.getFilm_id());
                }
                
            }
            RequestHandler.deleteWatchList(entfernen);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Watchlist");
            alert.setHeaderText("Erfolgreich");
            alert.setContentText("Der Film wurde aus der Watchlist entfernt!");
            alert.showAndWait();
            watchlist.setText("add to Watchlist");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        // Zeigt Profilbild des Benutzers an
        InputStream inputStream = new ByteArrayInputStream(Client.getCurrentNutzer().getProfilbild());
        Image image1 = new Image(inputStream, 150, 150, false, true);
        profilBild.setFill(new ImagePattern(image1));


        // Ändert den Willkommenstext in Hallo, Vorname Nachname
        welcomeMessage.setText("Hallo, " + Client.getCurrentNutzer().getVorname() + " " + Client.getCurrentNutzer().getNachname());
        if (Client.getSelectedFilm() != null){
            try {
                film = RequestHandler.getMovie(Client.getSelectedFilm().getFilm_id());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        titel.setText(film.getName());
        kategorie.setText(film.getCategory());
        regisseur.setText(film.getRegisseur());
        drehbuchautor.setText(film.getAuthor());
        cast.setText(film.getCast());
        erscheinungsdatum.setText(film.getDate());
        filmlaenge.setText(Integer.toString(film.getLength()));

        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(RequestHandler.getBanner(film));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Image image = new Image(byteArrayInputStream, 150, 225, false, true);
        imageView.setImage(image);

        Watchlist list2 = new Watchlist(Client.getCurrentNutzer().getUserID(), film.getFilm_id());

        try {
            if (RequestHandler.inWatchlist(list2)) {
                watchlist.setText("delete from Watchlist");
            } else {
                watchlist.setText("add to Watchlist");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            if (RequestHandler.checkSeenlist(list2)) {
                watchlist.setDisable(true);
                watchlist.setText("GESEHEN");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            if (RequestHandler.checkBewertung(film.getFilm_id())) {
                double hilfe=RequestHandler.getDurchnittsBewertung(film.getFilm_id());
                bewertung.setText(String.valueOf(hilfe+"/5"));
            } else {
                bewertung.setText("Keine Bewertungen!");
                bewertung.setDisable(true);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


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

