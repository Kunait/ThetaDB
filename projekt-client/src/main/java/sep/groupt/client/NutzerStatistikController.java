package sep.groupt.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sep.groupt.client.dataclass.FavouriteStats;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NutzerStatistikController extends SceneController implements Initializable {

    @FXML
    private Circle profilBild;

    @FXML
    private Text welcomeMessage;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private TextField searchBar;

    @FXML
    private Rectangle einstellungenBox;

    @FXML
    private Rectangle abmeldenBox;

    @FXML
    private DatePicker datumVon, datumBis;

    @FXML
    private Button getStatsButton;

    @FXML
    private PieChart castChart, categoryChart, seenTimeChart;

    @FXML
    private ImageView lieblingsFilm;


    // Importiert die Statistiken und zeigt sie dem Nutzer an

    private void importStats(){
        importStatsForSeenMinutes();
        importStatsForSchauspieler();
        importStatsForKategorie();
        importBanner();
    }

    private void importBanner() {
        if (Client.getStatsNutzer().getLieblingsFilm() != null){
            InputStream inputStream = new ByteArrayInputStream(Client.getStatsNutzer().getLieblingsFilm());
            Image image = new Image(inputStream, 150, 225, false, true);
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            lieblingsFilm.setImage(image);
        }
    }

    private void importStatsForKategorie(){
        ArrayList<PieChart.Data> dataArrayList = new ArrayList<>();

        for (FavouriteStats item : Client.getStatsNutzer().getLieblingsKategorie()){
            dataArrayList.add(new PieChart.Data(item.getKey(), item.getValue()));
        }

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(dataArrayList);

        categoryChart.setData(data);
        categoryChart.setLabelsVisible(true);
        categoryChart.setLabelLineLength(10);
        categoryChart.setLegendSide(Side.LEFT);
    }

    private void importStatsForSchauspieler(){
        ArrayList<PieChart.Data> dataArrayList = new ArrayList<>();

        for (FavouriteStats item : Client.getStatsNutzer().getLieblingsSchauspieler()){
            dataArrayList.add(new PieChart.Data(item.getKey(), item.getValue()));
        }

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(dataArrayList);

        castChart.setData(data);
        castChart.setLabelsVisible(true);
        castChart.setLabelLineLength(10);
        castChart.setLegendSide(Side.LEFT);
    }

    private void importStatsForSeenMinutes(){
        ObservableList<PieChart.Data> minutes = FXCollections.observableArrayList(
                new PieChart.Data("Gesamt: " + Client.getStatsNutzer().getGesamteFilmMinuten(), Client.getStatsNutzer().getGesamteFilmMinuten() - Client.getStatsNutzer().getGeschauteFilmMinuten()),
                new PieChart.Data("Zeitraum: " + Client.getStatsNutzer().getGeschauteFilmMinuten(), Client.getStatsNutzer().getGeschauteFilmMinuten())
        );

        seenTimeChart.setData(minutes);
        seenTimeChart.setLabelsVisible(true);
        seenTimeChart.setLabelLineLength(10);
        seenTimeChart.setLegendSide(Side.LEFT);
    }



    // Stats anfordern

    public void statsAnfordern(){
        if (checkDates()){
            try {
                boolean check = RequestHandler.getNutzerStats(Client.getCurrentNutzer().getUserID(), datumVon.getValue(), datumBis.getValue());

                if (check){
                    lieblingsFilm.setImage(null);
                    importStats();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Datum");
                    alert.setHeaderText("Datumsangaben falsch!");
                    alert.setContentText("Geben Sie einen richtigen Zeitraum ein!");
                    alert.showAndWait();
                }

            } catch (IOException|InterruptedException e) {
                Client.connectionLost("0001");
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Datum");
            alert.setHeaderText("Ungültiger Zeitraum!");
            alert.setContentText("Wählen Sie einen Zeitraum!");
            alert.showAndWait();
        }
    }

    // Überprüft, ob das Datum von < bis

    private boolean checkDates(){
        if (datumVon.getValue() != null && datumBis.getValue() != null){
            return true;
        }
        else {
            return false;
        }
    }


    // Zurück

    public void nutzerUbersichtWeiterleiten() {
        try {
            switchToSceneWithStage("nutzeruebersicht.fxml", "Nutzerübersicht");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Nutzerübersicht");
        }
    }

    public void sucheClicked(){
        try {
            if (choiceBox.getValue().equals("Film")){
                Client.setFilter(searchBar.getText());
                switchToSceneWithStage("Suche.fxml", "Suche Filme");
            }
            if (choiceBox.getValue().equals("Nutzer")){
                Client.setFilter(searchBar.getText());
                switchToSceneWithStage("nutzerSuche.fxml", "Nutzersuche");

            }
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Login");
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


    private void initChoiceBox(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Film");
        arrayList.add("Nutzer");

        ObservableList<String> observableList = FXCollections.observableList(arrayList);
        choiceBox.setItems(observableList);
        choiceBox.setValue("Film");
        Client.setSearch("Film");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBox();

        // Zeigt Profilbild des Benutzers an
        InputStream inputStream = new ByteArrayInputStream(Client.getCurrentNutzer().getProfilbild());
        Image image = new Image(inputStream, 150, 150, false, true);
        profilBild.setFill(new ImagePattern(image));

        // Ändert den Willkommenstext in Hallo, Vorname Nachname
        welcomeMessage.setText("Hallo, " + Client.getCurrentNutzer().getVorname() + " " + Client.getCurrentNutzer().getNachname());
    }


}
