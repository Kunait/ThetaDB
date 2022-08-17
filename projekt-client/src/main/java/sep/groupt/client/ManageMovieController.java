package sep.groupt.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.Report;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManageMovieController extends SceneController implements Initializable {
    @FXML
    private Button Moviestat;
    @FXML
    private Button addMovie;

    @FXML
    private Button editMovie;

    @FXML
    private Button downloadMovie;

    @FXML
    private Button refresh;

    @FXML
    private Button abmeldenButton;

    @FXML
    private Button reportButton;

    @FXML
    private SplitPane replist;

    @FXML
    private Label uptext;

    @FXML
    private TableView<Film> table;

    @FXML
    private TableView<Report> retable;
    @FXML
    private TableColumn<Report, String> repname;
    @FXML
    private TableColumn<Report, String> reptxt;
    @FXML
    private TableColumn<Report, String> gelesen;
    @FXML
    private TableColumn<Film, String> filmidt;

    @FXML
    private TableColumn<Film, String> filmnamet;

    private static Film movieToEdit;
    private static boolean bol=false;
    private static Report reportToEdit;




    public void addMoviePressed() throws IOException {

        switchToSceneWithStage("addMovie.fxml", "Add Movie");
    }

    public void editMoviePressed() throws IOException {
        movieToEdit = table.getSelectionModel().getSelectedItem();
        switchToSceneWithStage("editmovie.fxml", "Edit Movie");
    }

    public void StatsMoviePressed() throws IOException {
        movieToEdit = table.getSelectionModel().getSelectedItem();
        switchToSceneWithStage("StatistikAdmin.fxml", "Statistik zum Film");
    }



    public Film getMovieToEdit(){
        return movieToEdit;
    }

    public Report getReportToEdit(){
        return reportToEdit;
    }

    public void abmeldenButtonPressed() {
        try {
            switchToSceneWithStage("login.fxml", "Login");
            Client.setCurrentAdmin(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //wenn man doppelklick auf nen report in reportansicht macht
    public void dingsPressed(){
        if(retable.getSelectionModel().getSelectedItem().equals(reportToEdit)){
            try {
                bol=true;
                switchToSceneWithStage("errorview.fxml", "View Report");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            reportToEdit = retable.getSelectionModel().getSelectedItem();
        }
    }

    public void reportButtonPressed() {
        if(replist.isVisible()){
            uptext.setText("Filmübersicht");
            replist.setVisible(false);
            reportButton.setText(("Fehlerberichte"));
            if(null!=table.getSelectionModel().getSelectedItem()) {
                editMovie.setDisable(false);
            }
        } else{
            uptext.setText("Reportübersicht");
            reportButton.setText(("Filmübersicht"));
            replist.setVisible(true);
            editMovie.setDisable(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Füllt Table mit den Daten die in der Datenbank abgespeichert sind, falls diese keine Einträge hat wird eine leer Liste ausgegeben

        Film[] filmListe = new Film[0];
        try {
            filmListe = RequestHandler.getMovies();
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
        table.setItems(list);
        filmidt.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
        filmnamet.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
//
        Report[] reportListe = new Report[0];
        try {
            reportListe = RequestHandler.getReports();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Report> arrayyList = new ArrayList<>();

        for (Report x : reportListe) {
            arrayyList.add(x);
        }


        ObservableList<Report> listt = FXCollections.observableList(arrayyList);
        retable.setItems(listt);
        repname.setCellValueFactory(cellData -> cellData.getValue().getNamePropertyy());
        reptxt.setCellValueFactory(cellData -> cellData.getValue().getTextPropertyy());
        gelesen.setCellValueFactory(cellData -> cellData.getValue().getIdPropertyyy());

//
        // sep.groupt.client.dataclass.Film Bearbeiten Button ist deaktiviert und aktiviert sich nur, wenn der Admin einen sep.groupt.client.dataclass.Film ausgewählt hat

        editMovie.setDisable(true);
        Moviestat.setDisable(true);
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Film>() {
            @Override
            public void changed(ObservableValue<? extends Film> observable, Film oldValue, Film newValue) {
                if (newValue != null) {
                    editMovie.setDisable(false);
                    Moviestat.setDisable(false);
                }
                else {
                    editMovie.setDisable(true);
                    Moviestat.setDisable(true);
                }
            }
        });
        if(bol){
            this.reportButtonPressed();
            bol=false;
        }
    }
}
