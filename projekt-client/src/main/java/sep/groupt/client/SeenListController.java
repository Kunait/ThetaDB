package sep.groupt.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sep.groupt.client.dataclass.Bewertung;
import sep.groupt.client.dataclass.Film;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SeenListController extends SceneController implements Initializable {


    @FXML
    private Text welcomeMessage;
    @FXML
    private Rectangle einstellungenBox, abmeldenBox;
    @FXML
    private Circle profilBild;
    @FXML
    private TableColumn<Film, String> filmcolumn;

    @FXML
    private TableColumn<Film, String> datecolumn;

    @FXML
    private TableColumn<Film, String> authorcolumn;
    @FXML
    private MenuItem einstern, zweistern, dreistern, vierstern, fünfstern;
    @FXML
    private TableView<Film> Table;

    @FXML
    private Button ConfirmChanges, filmdaten, Back;

    @FXML
    private TextArea Rezensionfeld;

    @FXML
    private MenuButton Sterne;

    private int Sternbewertung;

    private int hiddenfID;


    private int hiddenbID;


    private static Film film;

    public Film getFilm(){
        return film;
    }

    public void FilmdatenPressed() throws IOException {
        Client.setSelectedFilm(film);
        FilmansichtController.setSeite("SeenList.fxml");
        switchToSceneWithStage("Filmansicht.fxml", "Film");
    }


    public void BackPressed() throws IOException {
        //zurück zur nutzerübersicht
        switchToSceneWithStage("nutzeruebersicht.fxml", "Nutzerübersicht");
    }

    public void ConfirmChangesPressed() throws IOException, InterruptedException {
        if(Sternbewertung == 0 && !Rezensionfeld.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warnung");
            alert.setHeaderText("Warnung: 1000");
            alert.setContentText("Eine Rezension darf nicht ohne Sterne abgegeben werden!");
            alert.showAndWait();
        }
        else {
            int uid = Client.getCurrentNutzer().getUserID();
            Bewertung updateBert = new Bewertung(hiddenbID, hiddenfID, uid, Sternbewertung, Rezensionfeld.getText());
            RequestHandler.updateBewertung(updateBert);
            aktualisieren();
        }

    }


    //Wenn man auf eine Sternbewertung drückt
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

    public void addZero() {
        Sternbewertung=0;
        Sterne.setText("Sternbewertung");
        Rezensionfeld.setText("");
    }

    public void FilmWahl() throws IOException, InterruptedException {
        Film sid= Table.getSelectionModel().getSelectedItem();
        //Wenn man nichts in der Tabelle auswählt
        if (sid == null)
        {

        }
        else {
            //Wenn man einen Film auswählt
            int fid = sid.getFilm_id();

            int uid = Client.getCurrentNutzer().getUserID();

            Bewertung bert = RequestHandler.getBewertung(uid, fid);

            Rezensionfeld.setText(bert.getBewertung());
            int punkt = bert.getPunkte();
            getSterne(punkt);
            hiddenfID = fid;
            hiddenbID = bert.getBewertungID();

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

    //Sternbewertung bekommen
    public void getSterne(int wert) {
        if (wert == 1) {
            Sterne.setText("1 Stern");
            Sternbewertung = 1;
        }
        if (wert == 2) {
            Sterne.setText("2 Sterne");
            Sternbewertung = 2;
        }
        if (wert == 3) {
            Sterne.setText("3 Sterne");
            Sternbewertung = 3;
        }
        if (wert == 4) {
            Sterne.setText("4 Sterne");
            Sternbewertung = 4;
        }
        if (wert == 5) {
            Sterne.setText("5 Sterne");
            Sternbewertung = 5;
        }
        if(wert ==0)
        {
            Sterne.setText("Sternbewertung");
            Sternbewertung = 0;
        }
    }

    public void aktualisieren(){
        Film[] SeenListe = new Film[0];
        int id =Client.getCurrentNutzer().getUserID();
        try {
            SeenListe = RequestHandler.getsl(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Film> arrayList = new ArrayList<>();

        for (Film x : SeenListe) {
            arrayList.add(x);
        }

        //Tabelle initialisieren
        ObservableList<Film> list = FXCollections.observableList(arrayList);
        Table.setItems(list);
        filmcolumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        datecolumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
        authorcolumn.setCellValueFactory(cellData -> cellData.getValue().getAuthorProperty());



        //Filmdaten und Änderungen sind nur aktiviert, wenn ein Film ausgewählt wird
        ConfirmChanges.setDisable(true);
        filmdaten.setDisable(true);
        Rezensionfeld.setDisable(true);
        Sterne.setDisable(true);
        Table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Film>() {
            @Override
            public void changed(ObservableValue<? extends Film> observable,Film oldValue, Film newValue) {

                if (newValue != null) {
                    ConfirmChanges.setDisable(false);
                    filmdaten.setDisable(false);
                    Rezensionfeld.setDisable(false);
                    Sterne.setDisable(false);
                } else {
                    ConfirmChanges.setDisable(true);
                    filmdaten.setDisable(true);
                    Rezensionfeld.setDisable(true);
                    Sterne.setDisable(true);
                }

            }
        });
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
