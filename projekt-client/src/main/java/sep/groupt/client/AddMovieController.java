package sep.groupt.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import sep.groupt.client.dataclass.Film;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddMovieController extends SceneController implements Initializable {


    @FXML
    private MenuButton kategoriemenu;

    String bannerPath = "";
    boolean bannerSelected;

    @FXML
    private CheckMenuItem adventurebox, actionbox, animationbox, dokumentationbox, dramabox, sonstigesbox, fantasybox, horrorbox, comedybox, krimibox, lovebox, musicbox, scbox, thrillerbox, westernbox;

    @FXML
    private TextField name, fl, regi, cast, da;

    @FXML
    private DatePicker release;

    @FXML
    private Button banner;

    @FXML
    private Button addFilm;

    @FXML
    private Button AbbrechenButton;

    @FXML
    private Text warning, best, best1,best2,best3,best4,best5,best6,best7;

    @FXML
    private AnchorPane pane;

    @FXML
    private ImageView imageView;


    File bannerFile;
    private String SelectedChoices;
    private String Fehlermeldung;
    private String releaseString;


    //Kategorien werden nach angetickten Box zum String hinzugefügt
    public void getKategorien(){
        SelectedChoices= "";
        if(adventurebox.isSelected())
        {
            SelectedChoices = SelectedChoices + "Abenteuer ";
        }
        if (actionbox.isSelected())
        {
            SelectedChoices = SelectedChoices + "Action ";
        }
        if (animationbox.isSelected())
        {
            SelectedChoices = SelectedChoices + "Animation ";
        }
        if (dokumentationbox.isSelected())
        {
            SelectedChoices = SelectedChoices + "Dokumentation ";
        }
        if(dramabox.isSelected())
        {
            SelectedChoices = SelectedChoices + "Drama ";
        }
        if(sonstigesbox.isSelected())
        {
            SelectedChoices = SelectedChoices + "Sonstiges ";
        }
        if(fantasybox.isSelected()){
            SelectedChoices = SelectedChoices + "Fantasy ";
        }
        if(horrorbox.isSelected())
        {
            SelectedChoices = SelectedChoices + "Horror ";
        }
        if(comedybox.isSelected()){
            SelectedChoices = SelectedChoices + "Komödie ";
        }
        if(krimibox.isSelected()){
            SelectedChoices = SelectedChoices + "Krimi ";
        }
        if(lovebox.isSelected()){
            SelectedChoices = SelectedChoices + "Liebesfilm ";
        }
        if(musicbox.isSelected()){
            SelectedChoices = SelectedChoices + "Musik ";
        }
        if(scbox.isSelected()){
            SelectedChoices = SelectedChoices + "Science-Fiction ";
        }
        if(thrillerbox.isSelected()){
            SelectedChoices = SelectedChoices + "Thriller ";
        }
        if(westernbox.isSelected()){
            SelectedChoices = SelectedChoices + "Western ";
        }

    }




    public void addFilmPressed() throws IOException, InterruptedException {
        deletewarning();
        getKategorien();
        if(SelectedChoices.equals("")){
            best2.setText("Kategorien fehlen!");
        }
        if (name.getText().isEmpty())
        {
            best1.setText("Name fehlt!");
        }
        //Constraints werden abgerufen
        Constraintcast();
        Constraintda();
        Constraintregi();
        Constraintfl();
        Constraintrelease();

        if (name.getText().isEmpty()||SelectedChoices.equals("")||releaseString==null||fl.getText().isEmpty()||regi.getText().isEmpty()||cast.getText().isEmpty()||da.getText().isEmpty()||regi.getText().equals("")||cast.getText().equals("")||da.getText().equals(""))
        {
            //Fehlermeldung, falls Informationen fehlen
            best.setText("");
            warning.setText("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fehler");
            alert.setHeaderText("Fehler: 0010");
            alert.setContentText("Pflichtinformationen fehlen");
            alert.showAndWait();
        }

        else
        {
            Integer fl2 = Integer.valueOf(fl.getText());
            Film fooo = new Film(name.getText(), SelectedChoices, fl2, releaseString, regi.getText(), da.getText(), cast.getText());

            if (bannerSelected){
                FileInputStream fileInputStream = new FileInputStream(bannerPath);
                fooo.setBanner(fileInputStream.readAllBytes());
                fileInputStream.close();
            }
            else {
                InputStream inputStream = getClass().getResourceAsStream("banner/default.jpg");
                fooo.setBanner(inputStream.readAllBytes());
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(containsMovie(fooo))
            {
                //Fehlermeldung wird angezeigt
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Fehler");
                alert.setHeaderText("Fehler: 0011");
                alert.setContentText("sep.groupt.client.dataclass.Film ist schon vorhanden");
                alert.showAndWait();
                warning.setText("");
            }
            else {
                //Bestätigung wird angezeigt
                RequestHandler.sendMovie(fooo);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Bestätigung");
                alert.setHeaderText("Bestätigung: 0001");
                alert.setContentText("sep.groupt.client.dataclass.Film wurde eingefügt");
                alert.showAndWait();
                warning.setText("");
            }
        }

    }



    //Man kommt so zu mange movie zurück
    public void returnPressed() throws IOException{
        switchToSceneWithStage("manageMovie.fxml", "Manage Movies");
    }

    //Überprüft, ob Regisseur gültige Zeichen enthält
    public void Constraintregi() {
        if(!regi.getText().matches("[a-zA-Z ,äöüß.-]+"))
        {
            best5.setText("Nur Buchstaben sind erlaubt/ Regisseur fehlt");
            regi.setText("");
        }
    }

    //Überprüft, ob Cast gültige Zeichen enthält
    public void Constraintcast() {
        if(!cast.getText().matches("[a-zA-Z ,äöüß.-]+"))
        {
            best6.setText("Nur Buchstaben sind erlaubt/ Cast fehlt");
            cast.setText("");

        }

    }

    //Überprüft, ob Drehbuchautor gültige Zeichen enthält
    public void Constraintda()
    {
        if(!da.getText().matches("[a-zA-Z ,äöüß.-]+")){
            best7.setText("Nur Buchstaben sind erlaubt/ Drehbuchautor fehlt");
            da.setText("");

        }

    }

    //Überprüft, ob bei Filmlänge nur Zahlen vorkommen
    public void Constraintfl(){
        if(!fl.getText().matches("[1-9]\\d*"))
        {
                best3.setText("");
                best3.setText("Nur Zahlen sind erlaubt/ 0 darf nicht an den Anfang");
                fl.setText("");
        }
    }

    public void Constraintrelease(){
        //Überprüft, ob etwas im Feld des Datums steht
        if (release.getValue() == null)
        {
            best4.setText("Datum fehlt");
        }
        else if (release.getValue() != null){
            //Datum darf nicht mit Werten unter 19 anfangen
            if(release.getValue().toString().startsWith("20")||release.getValue().toString().startsWith("19"))
            {
                best4.setText("");
                releaseString =  release.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            else {
                best4.setText("Bitte ein gueltiges Datum eingeben");
            }
        }
    }

    //Button öffnet den file explorer, um ein Bild auszuwählen
    public void bannerPressed() throws IOException {
        if(!name.getText().equals("")){
            FileChooser chooser = new FileChooser();
            //chooser.setE(new FileChooser.ExtensionFilter("Images","jpg"));
             bannerFile = chooser.showOpenDialog(Client.getCurrentStage());
             if(bannerFile!=null) {
                 bannerPath = bannerFile.getCanonicalPath();

                 bannerSelected = true;


                 FileInputStream fileInputStream = new FileInputStream(bannerFile);
                 Image image = new Image(fileInputStream, 150, 225, false, true);
                 fileInputStream.close();
                 imageView.setImage(image);
             }
        }
    }

    //Warnungen werden gelöscht
    public void deletewarning(){
        best1.setText("");
        best2.setText("");
        best3.setText("");
        best4.setText("");
        best5.setText("");
        best6.setText("");
        best7.setText("");

    }

    //Vergleicht den sep.groupt.client.dataclass.Film mit den Filmen in der Datenbank, um zu checken ob er schon vorhanden ist
    public boolean containsMovie(Film film) throws IOException, InterruptedException {
        Film[] filmListe = new Film[0];
        filmListe = RequestHandler.getMovies();
        for(int i = 0; i < filmListe.length;i++)
        {
            if(filmListe[i].getName().equals(film.getName())&&filmListe[i].getDate().equals(film.getDate())&&filmListe[i].getRegisseur().equals(film.getRegisseur()))
            {
                return true;
            }
        }
        return  false;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream inputStream = getClass().getResourceAsStream("banner/default.jpg");
        Image image = new Image(inputStream, 150, 225, false, true);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImage(image);
    }
}
