package sep.groupt.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sep.groupt.client.dataclass.Film;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FilmSuchenController extends SceneController implements Initializable {
    @FXML
    public CheckMenuItem actionbox, adventurebox, animationbox, dokumentationbox, dramabox, fantasybox, comedybox, krimibox, lovebox, musicbox, scbox, thrillerbox, westernbox, sonstigesbox, horrorbox;
    @FXML
    public MenuButton kategorie;
    @FXML
    public TextField name;
    @FXML
    public TextField cast;
    @FXML
    public DatePicker von, bis;
    @FXML
    public MenuButton datePick;
    @FXML
    private Button suchen, zurueck;
    @FXML
    private TableView<Film> tabelle;
    @FXML
    private TableColumn<Film, String> bannerColumn, nameColumn, kategorieColumn, castColumn, dateColumn;

    private String SelectedChoices;


    private static Film filmAnzeigen;

    @FXML
    private Text welcomeMessage;

    @FXML
    private Circle profilBild;

    public void zurueckClicked() {
        try {
            switchToSceneWithStage("nutzeruebersicht.fxml", "Nutzerübersicht");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Nutzerübersicht");
        }
    }

    public static Film getFilmAnzeigen() {
        return filmAnzeigen;
    }

    public void pressed(MouseEvent event) {
        try {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY && tabelle.getSelectionModel().getSelectedItem() != null) {
                System.out.println(tabelle.getSelectionModel().getSelectedItem().getName());
                Client.setSelectedFilm(RequestHandler.getMovie(tabelle.getSelectionModel().getSelectedItem().getFilm_id()));
                FilmansichtController.setSeite("Suche.fxml");
                switchToSceneWithStage("Filmansicht.fxml", "Film Ansicht");
            }
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Filmansicht");
        } catch (InterruptedException e1) {
            Client.viewSwitchFailed("0001", "Filmansicht");
        }

    }

    public void suchenClicked() {


        String stringname = "";
        String stringkategorie = "";
        String stringcast = "";
        String stringvon = "";
        String stringbis = "";

        if (!name.getText().isEmpty()) {
            stringname = name.getText();
        }
        if (!getKategorien().equals("")) {
            stringkategorie = getKategorien();
        }
        if (!cast.getText().isEmpty()) {
            stringcast = cast.getText();
        }
        if (von.getValue() != null) {
            stringvon = von.getValue().toString();
        }

        if (bis.getValue() != null) {
            stringbis = bis.getValue().toString();
        }


        Film[] filmListe = null;
        try {
            filmListe = RequestHandler.getFilterFilm(stringname, stringkategorie, stringcast, stringvon, stringbis);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Film> arrayList = new ArrayList<>();


        for (Film x : filmListe) {
            arrayList.add(x);
        }


        ObservableList<Film> list = FXCollections.observableList(arrayList);
        tabelle.setItems(list);
        //bannerColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        kategorieColumn.setCellValueFactory(cellData -> cellData.getValue().getKategorieProperty());
        castColumn.setCellValueFactory(cellData -> cellData.getValue().getCastProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDatumProperty());


    }

    public String getKategorien() {
        SelectedChoices = "";

        if (adventurebox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%Abenteuer%' OR FilmKategorie LIKE '%Adventure%') ";

        }
        if (actionbox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Action%' ";

        }
        if (animationbox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Animation%' ";
        }
        if (dokumentationbox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%Dokumentation%' OR FilmKategorie LIKE '%Documentation%') ";

        }
        if (dramabox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Drama%' ";

        }
        if (sonstigesbox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Sonstiges%' ";
        }
        if (fantasybox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Fantasy%' ";

        }
        if (horrorbox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Horror%' ";

        }
        if (comedybox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%KomÃ¶die%' OR FilmKategorie LIKE '%Comedy%') ";

        }
        if (krimibox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%Krimi%' OR FilmKategorie LIKE '%Crime%') ";

        }
        if (lovebox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%Liebesfilm%' OR FilmKategorie LIKE '%Romance%') ";

        }
        if (musicbox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%Musik%' OR FilmKategorie LIKE '%Music%') ";
        }
        if (scbox.isSelected()) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%Science-Fiction%' OR FilmKategorie LIKE '%Sci-Fi%') ";

        }
        if (thrillerbox.isSelected()) {
            SelectedChoices = SelectedChoices + "%Thriller% ";
        }
        if (westernbox.isSelected()) {
            SelectedChoices = SelectedChoices + "%Western% ";
        }
        return SelectedChoices;
    }

    public void abmeldenBoxPressed() {
        try {
            Client.setFilter("");
            switchToSceneWithStage("login.fxml", "Login");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Login");
        }
    }

    public void einstellungenBoxPressed() {
        try {
            Client.setFilter("");
            switchToSceneWithStage("editProfile.fxml", "Einstellungen");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "platzhalter");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        InputStream inputStream = new ByteArrayInputStream(Client.getCurrentNutzer().getProfilbild());
        Image image1 = new Image(inputStream, 150, 150, false, true);
        profilBild.setFill(new ImagePattern(image1));

        // Ändert den Willkommenstext in Hallo, Vorname Nachname
        welcomeMessage.setText("Hallo, " + Client.getCurrentNutzer().getVorname() + " " + Client.getCurrentNutzer().getNachname());
        if (Client.getSearch().equals("Film")) {
            name.setText(Client.getFilter());

            suchenClicked();
        }


    }
}
