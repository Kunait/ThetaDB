package sep.groupt.client;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.Report;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportViewController extends SceneController implements Initializable {
    @FXML
    private Button backButton;

    @FXML
    private Button zumFilm;

    @FXML
    private CheckBox readBox;

    @FXML
    private TextArea reportFeld;

    @FXML
    private Text filmdrehbuch, filmname, filmcast, filmregie, filmdatum, filmlaenge, filmkategorie, uptxt;

    private static Film film;
    private static Boolean repedit=false;
    private Report report;

    ManageMovieController mang = new ManageMovieController();

    public void importData() {
        report = mang.getReportToEdit();
        film = report.getFilm();
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
        uptxt.setText("Report vom " + report.getDate());
        reportFeld.setWrapText(true);
        reportFeld.setText(report.getRepText());
//        if(report.isGelesen()){
//            readBox.setSelected(true);
//        }

    }

    public Film getEditFilm(){
        return film;
    }

    public void noReport(){
        repedit=false;
    }

    public boolean isReport(){
        return repedit;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        importData();
    }

    public void zumFilmPressed() throws IOException {
        repedit=true;
        if(readBox.isSelected()){
            report.setGelesen(true);
            try {
                RequestHandler.readReport(report);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        switchToSceneWithStage("editmovie.fxml", "Edit Movie");
    }

    public void backButtonPressed() throws IOException {
        if(readBox.isSelected()){
            report.setGelesen(true);
            try {
                RequestHandler.readReport(report);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        switchToSceneWithStage("manageMovie.fxml", "Manage Movies");
    }
}
