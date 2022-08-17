package sep.groupt.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sep.groupt.client.dataclass.Bewertung;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.StatistikAdmin;
import sep.groupt.client.dataclass.Watchlist;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class WatchlistController extends SceneController implements Initializable {

    @FXML
    private Text welcomeMessage;
    @FXML
    private Circle profilBild;
    @FXML
    private TableColumn<Film, String> filmcolumn, datecolumn, authorcolumn;
    @FXML
    private MenuButton Sterne;
    @FXML
    private Button Gelesen, Filmdaten, Back,Filmentfernen;
    @FXML
    private TableView<Film> Table;

    @FXML
    private TextArea rezensionfeld;


    @FXML
    private MenuItem einstern,zweistern,dreistern,vierstern,fuenfstern;

    private int Sternbewertung;

    private static Film movieToRate;

    private int hiddenfID;

    private int hiddenwID;

    private static Film film;
    private Film[]watchlistNutzer;

    public Film[] getWatchlistNutzer(){
        return watchlistNutzer;
    }

    public Film getFilm(){
        return film;
    }

    public void BackPressed() throws IOException {
        switchToSceneWithStage("nutzeruebersicht.fxml", "Nutzerübersicht");
    }

    public void FilmdatenAnzeigen() throws IOException {
        Client.setSelectedFilm(film);
        FilmansichtController.setSeite("watchlist.fxml");
        switchToSceneWithStage("Filmansicht.fxml", "Film");
    }

    public void GelesenPressed() throws IOException, InterruptedException {
        if(Sternbewertung == 0 && !rezensionfeld.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warnung");
            alert.setHeaderText("Warnung: 1000");
            alert.setContentText("Eine Rezension darf nicht ohne Sterne abgegeben werden!");
            alert.showAndWait();
        }
        else {
            int uid = Client.getCurrentNutzer().getUserID();

            Bewertung bert = new Bewertung(hiddenfID, uid, Sternbewertung, rezensionfeld.getText());
            RequestHandler.sendBewertung(bert);

            Watchlist watchlist = new Watchlist(hiddenwID, uid, hiddenfID);
            RequestHandler.deleteWatchList(watchlist);

            //an die Statistik vom Admin
            if(Sternbewertung==0)
            {
                StatistikAdmin statistikAdmin = new StatistikAdmin(hiddenfID,1);
                RequestHandler.sendOneStat(statistikAdmin);
            }
            else {
                StatistikAdmin statistikAdmin = new StatistikAdmin(hiddenfID,1,1,Sternbewertung);
                RequestHandler.sendFullStat(statistikAdmin);

            }
            aktualisieren();
            RequestHandler.sendSeenList(uid, hiddenfID);

            Sterne.setText("Sternbewertung");
            Sternbewertung = 0;
            rezensionfeld.setText("");
        }
    }



    public void addOneStar() {
        Sternbewertung = 1;
        Sterne.setText("1 Stern");
    }

    public void addTwoStar() {
        Sternbewertung = 2;
        Sterne.setText("2 Sterne");
    }
    public void addThreeStar() {
        Sternbewertung = 3;
        Sterne.setText("3 Sterne");
    }
    public void addFourStar() {
        Sternbewertung = 4;
        Sterne.setText("4 Sterne");
    }
    public void addFiveStar() {
        Sternbewertung = 5;
        Sterne.setText("5 Sterne");
    }


    public void FilmWahl() throws IOException, InterruptedException {
        Film sid= Table.getSelectionModel().getSelectedItem();
        //Wenn man nichts in der Tabelle auswählt
        if (sid == null)
        {

        }
        else {
            hiddenfID = sid.getFilm_id();
            hiddenwID=sid.getWatchlist_id();
            film = new Film(sid.getFilm_id(), sid.getName(), sid.getCategory(), sid.getLength(), sid.getDate(), sid.getRegisseur(),sid.getAuthor(),sid.getCast(), sid.getBanner());
        }



    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Zeigt Profilbild des Benutzers an
        InputStream inputStream = new ByteArrayInputStream(Client.getCurrentNutzer().getProfilbild());
        Image image = new Image(inputStream);
        profilBild.setFill(new ImagePattern(image));

        // Ändert den Willkommenstext in Hallo, Vorname Nachname
        welcomeMessage.setText("Hallo, " + Client.getCurrentNutzer().getVorname() + " " + Client.getCurrentNutzer().getNachname());
        aktualisieren();
    }

    public void aktualisieren(){
        Film[] WatchListe = new Film[0];
        int id = Client.getCurrentNutzer().getUserID();
        try {
            WatchListe = RequestHandler.getwl(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ArrayList<Film> arrayList = new ArrayList<>();


        for (Film x : WatchListe) {
            arrayList.add(x);
        }
        watchlistNutzer = new Film[arrayList.size()];
        watchlistNutzer = arrayList.toArray(watchlistNutzer);


        ObservableList<Film> list = FXCollections.observableList(arrayList);
        Table.setItems(list);
        filmcolumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        datecolumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
        authorcolumn.setCellValueFactory(cellData -> cellData.getValue().getAuthorProperty());


        // Man muss zuerst einen Film auswählen, bevor man mit diesem weiter arbeiten kann

        Gelesen.setDisable(true);
        Filmdaten.setDisable(true);
        Filmentfernen.setDisable(true);
        Sterne.setDisable(true);
        rezensionfeld.setDisable(true);
        Table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Film>() {
            @Override
            public void changed(ObservableValue<? extends Film> observable, Film oldValue, Film newValue) {

                if (newValue != null) {
                    Gelesen.setDisable(false);
                    Filmdaten.setDisable(false);
                    Filmentfernen.setDisable(false);
                    Sterne.setDisable(false);
                    rezensionfeld.setDisable(false);
                } else {
                    Gelesen.setDisable(true);
                    Filmdaten.setDisable(true);
                    Filmentfernen.setDisable(true);
                    Sterne.setDisable(true);
                    rezensionfeld.setDisable(true);
                }

            }
        });
    }

    public void DeleteFilm() throws IOException, InterruptedException {
        int uid = Client.getCurrentNutzer().getUserID();
        Watchlist watchlist = new Watchlist(hiddenwID,uid, hiddenfID);
        RequestHandler.deleteWatchList(watchlist);
        aktualisieren();
    }

    public void einstellungenBoxPressed(){
        try {
            switchToSceneWithStage("editProfile.fxml", "Einstellungen");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "platzhalter");
        }
    }

    public void abmeldenBoxPressed(){
        try {
            switchToSceneWithStage("login.fxml", "Login");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Login");
        }
    }
}
