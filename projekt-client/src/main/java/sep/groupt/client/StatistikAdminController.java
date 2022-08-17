package sep.groupt.client;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.StatistikAdmin;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class StatistikAdminController extends SceneController implements Initializable {


    @FXML
    private NumberAxis y;
    @FXML
    private CategoryAxis x;
    @FXML
    private BarChart<String, Integer> barchart;
    @FXML
    private Text Filmname, wieoftgesehen, anzahlBewertung, durchschnitt;
    @FXML
    private ImageView imageview;

    Film film;

    private int fid;




    ManageMovieController mang = new ManageMovieController();

    public void downloadButton() throws IOException,RuntimeException{
        FileChooser chooser = new FileChooser();

        //Datum wann es heruntergeladen wurde
        LocalDateTime jetzt = LocalDateTime.now();
        String datum =  jetzt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //Nur textfiles
        //Quelle: https://stackoverflow.com/questions/13634576/javafx-filechooser-how-to-set-file-filters
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text file", "*.txt");
        chooser.getExtensionFilters().add(filter);

        //text wird geschrieben
        File file = chooser.showSaveDialog(Client.getCurrentStage());
        if(file!=null) {
                FileWriter writer = new FileWriter(file);
                writer.write("FilmID: " + fid + "\n" + "Heruntergeladen am: " + datum + "\n" + "Statistik zum Film: " + Filmname.getText() + "\n" + "Anzahl gesehen: " + wieoftgesehen.getText() + "\n" + "Anzahl Bewertungen: " + anzahlBewertung.getText() + "\n" + "Durchschnitt: " + durchschnitt.getText());
                writer.close();
        }
    }

    public void deleteButton() {
        Alert abfrage = new Alert(Alert.AlertType.CONFIRMATION,"Soll die Statistik zu diesem Film zurückgesetzt werden?", ButtonType.YES, ButtonType.NO);
        abfrage.setHeaderText("Bestätigung zum Zurücksetzen");
        abfrage.showAndWait();
        if(abfrage.getResult()==ButtonType.YES) {
            try {
                RequestHandler.deleteStat(fid);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            aktualisieren();
        }
    }

    public void aktualisieren(){
        StatistikAdmin statistikAdmin;

        try {
            statistikAdmin = RequestHandler.getStatistikAdmin(fid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(statistikAdmin!= null) {

            wieoftgesehen.setText(String.valueOf(statistikAdmin.getAnzahlGesehen()));
            anzahlBewertung.setText(String.valueOf(statistikAdmin.getAnzahlBewertung()));

            //Graphen anpassen
            if(Integer.parseInt(wieoftgesehen.getText())<10)
            {
               y.setUpperBound(Integer.parseInt(wieoftgesehen.getText())+1);
            }
            else
            {
                y.setUpperBound(Integer.parseInt(wieoftgesehen.getText())+1);
                y.setTickUnit(5);
            }

            //Quelle zu den Graphen: https://jenkov.com/tutorials/javafx/barchart.html
            XYChart.Series barchart1  = new XYChart.Series<>();
            barchart1.getData().add(new XYChart.Data("Wie oft gesehen", Integer.parseInt(wieoftgesehen.getText())));
            barchart1.getData().add(new XYChart.Data("Anzahl der Bewertungen", Integer.parseInt(anzahlBewertung.getText())));


            barchart.getData().add(barchart1);
            //Durchschnittsbewertung herausfinden
            double punkte = statistikAdmin.getGesamtPunkte();
            if(punkte!=0)
            {
                double alle = statistikAdmin.getAnzahlBewertung();
                double d = punkte/alle;
                double rounddaufzwei = Math.round(d*100)/100.0;
                durchschnitt.setText(String.valueOf(rounddaufzwei));
            }
            else{
                durchschnitt.setText("Keine Bewertungen");
            }

        }
        else{
            wieoftgesehen.setText("0");
            anzahlBewertung.setText("0");
            durchschnitt.setText("Keine Bewertungen");

            //Falls man einen bestehenden Graphen zurücksetzt
            barchart.getData().clear();
            //Falls es keine stats zum Film gibt
            //Quelle zu den Graphen: https://jenkov.com/tutorials/javafx/barchart.html
            XYChart.Series barchart1  = new XYChart.Series<>();
            barchart1.getData().add(new XYChart.Data("Wie oft gesehen", 0));
            barchart1.getData().add(new XYChart.Data("Anzahl der Bewertungen", 0));

            y.setUpperBound(1);
            barchart.getData().addAll(barchart1);
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ausgewählten Film holen
        film = mang.getMovieToEdit();
        Filmname.setText(film.getName());
        fid = film.getFilm_id();
        aktualisieren();
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(RequestHandler.getBanner(film));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Image image = new Image(byteArrayInputStream, 150, 225, false, true);
        imageview.setImage(image);
    }

    public void backPressed() throws IOException {
        switchToSceneWithStage("manageMovie.fxml", "Manage Movies");
    }
}
