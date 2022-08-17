package sep.groupt.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import sep.groupt.client.dataclass.Film;

import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class editMovieController extends SceneController implements Initializable {


    @FXML private MenuButton kategoriemenu;
    @FXML private CheckMenuItem adventurebox, actionbox,animationbox,dokumentationbox,dramabox,sonstigesbox,fantasybox,horrorbox,comedybox,krimibox,lovebox,musicbox,scbox,thrillerbox,westernbox;
    @FXML private TextField NameFeld;
    @FXML private TextField LaengeFeld;
    // @FXML private TextField KategorieFeld;
    @FXML private DatePicker ErscheinungsdatumFeld;
    @FXML private TextField RegisseurFeld;
    @FXML private TextArea CastFeld;
    @FXML private TextField DrehbuchautorFeld;
    @FXML private Button bannerButton;

    @FXML
    private ImageView imageView;


    @FXML
    private Button ConfirmButton, AbbrechenButton, ImportButton;
    private Film film;
    @FXML private Text msgtext;

    byte[] pic = null;

    private String SelectedChoices;

    private String string;

    boolean bannerInput = false;

    ManageMovieController mang = new ManageMovieController();

    ReportViewController rep = new ReportViewController();

    @FXML
    private Text editText;

    public void AbbrechenButtonPressed() throws IOException{
        rep.noReport();
        switchToSceneWithStage("manageMovie.fxml", "Manage Movies");
    }

    public void bannerButtonPressed() throws IOException, InterruptedException {


        FileChooser chooser = new FileChooser();
        File bannerFile = chooser.showOpenDialog(Client.getCurrentStage());
        if(bannerFile!=null) {
            String bannerPath = null;
            try {
                bannerPath = bannerFile.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Path path = Paths.get(bannerPath);
            try {
                pic = Files.readAllBytes(path);
                bannerInput = true;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pic);
            Image image = new Image(byteArrayInputStream, 150, 225, false, true);
            imageView.setImage(image);
        }

    }

    public void ConfirmButtonPressed() throws IOException{
        msgtext.setText("");
        this.importData(); //für den fall, dass ein feld leer ist, werden die alten daten verwendet
        int ik = 0;
        try {
            ik = Integer.parseInt(LaengeFeld.getText());//filmlänge zu int
        }
        catch (Exception e) {
            msgtext.setText("Bitte eine ganze Zahl als Filmlaenge(in Minuten) angeben");
            return;
        }
        //prüft, ob eingaben gültig sind
        Constraintcast();
        Constraintda();
        Constraintregi();
        if(msgtext.getText().contains("i"))
        {
            return;
        }
        Film endfilm;
        if(rep.isReport()){
            endfilm = new Film(rep.getEditFilm().getFilm_id(), NameFeld.getText());
        }else {
            endfilm = new Film(mang.getMovieToEdit().getFilm_id(), NameFeld.getText());
        }
        endfilm.setCategory(SelectedChoices);
        endfilm.setDate(ErscheinungsdatumFeld.getValue().toString());
        if(ik!=0){endfilm.setLength(ik);}
        endfilm.setRegisseur(RegisseurFeld.getText());
        endfilm.setAuthor(DrehbuchautorFeld.getText());
        endfilm.setCast(CastFeld.getText());

        try {
            RequestHandler.editMovie(endfilm);
            if(bannerInput && pic!=null){
                endfilm.setBanner(pic);
                RequestHandler.sendBanner(endfilm);
            }
        } catch (InterruptedException e) {
            System.out.println("Hat nicht geklappt");
        }
        rep.noReport();
        switchToSceneWithStage("manageMovie.fxml", "Manage Movies");
        //Zurück zu verwaltungsfenster, Änderungen übernehmen
    }

    public void importData() {
        getKategorien();
        if(rep.isReport()){
            film = rep.getEditFilm();
        }else {
            film = mang.getMovieToEdit();
        }
        if(NameFeld.getText().isEmpty()){
            NameFeld.setText(film.getName());
        }
        //todo choicemenu genre
        if(SelectedChoices.equals("")) {
            checkGenre();
        }
        if(LaengeFeld.getText().isEmpty()) {
            LaengeFeld.setText(Integer.toString(film.getLength()));
        }
        if(ErscheinungsdatumFeld.getValue()==null) {
            ErscheinungsdatumFeld.setValue(LocalDate.parse(film.getDate()));
        }
        if(RegisseurFeld.getText().isEmpty()) {
            RegisseurFeld.setText(film.getRegisseur());
        }
        if(CastFeld.getText().isEmpty()) {
            CastFeld.setText(film.getCast());
        }
        if(DrehbuchautorFeld.getText().isEmpty()) {
            DrehbuchautorFeld.setText(film.getAuthor());
        }

        // Banner

        try {
            film.setBanner(RequestHandler.getBanner(film));
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(film.getBanner());
            Image image = new Image(byteArrayInputStream, 150, 225, false, true);
            imageView.setImage(image);
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        }
    }

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

    public void checkGenre(){
        if(rep.isReport()){
            film = rep.getEditFilm();
        }else {
            film = mang.getMovieToEdit();
        }
        //Filme werden oft auf englisch gescraped, deswegen kategorien auf deutsch und englisch
        if(film.getCategory().contains("Abenteuer") || film.getCategory().contains("Adventure")){
            adventurebox.setSelected(true);
        }
        if(film.getCategory().contains("Action"))
        {
            actionbox.setSelected(true);
        }
        if(film.getCategory().contains("Animation"))
        {
            animationbox.setSelected(true);
        }
        if(film.getCategory().contains("Dokumentation") || film.getCategory().contains("Documentation"))
        {
            dokumentationbox.setSelected(true);
        }
        if(film.getCategory().contains("Drama"))
        {
            dramabox.setSelected(true);
        }
        if(film.getCategory().contains("Sonstiges"))//wird nicht gescraped, deswegen nur deutsch
        {
            sonstigesbox.setSelected(true);
        }
        if(film.getCategory().contains("Fantasy"))
        {
            fantasybox.setSelected(true);
        }
        if(film.getCategory().contains("Horror"))
        {
            horrorbox.setSelected(true);
        }
        if(film.getCategory().contains("Komödie") || film.getCategory().contains("Comedy"))
        {
            comedybox.setSelected(true);
        }
        if(film.getCategory().contains("Krimi") || film.getCategory().contains("Crime"))
        {
            krimibox.setSelected(true);
        }
        if(film.getCategory().contains("Liebesfilm") || film.getCategory().contains("Romance"))
        {
            lovebox.setSelected(true);
        }
        if(film.getCategory().contains("Musik") || film.getCategory().contains("Music"))
        {
            musicbox.setSelected(true);
        }
        if(film.getCategory().contains("Science-Fiction") || film.getCategory().contains("Sci-Fi"))
        {
            scbox.setSelected(true);
        }
        if(film.getCategory().contains("Thriller"))
        {
            thrillerbox.setSelected(true);
        }
        if(film.getCategory().contains("Western"))
        {
            westernbox.setSelected(true);
        }
    }

    public void Constraintregi() {
        if(!RegisseurFeld.getText().matches("[a-zA-Z ,äöüß.-]+"))
        {
            msgtext.setText("Bitte gueltigen Regisseur angeben");

        }
    }

    public void Constraintcast() {
        if(!CastFeld.getText().matches("[a-zA-Z ,äöüß.-]+"))
        {
            msgtext.setText("Bitte gueltigen Cast angeben");

        }

    }

    public void Constraintda()
    {
        if(!DrehbuchautorFeld.getText().matches("[a-zA-Z ,äöüß.-]+")){
            msgtext.setText("Bitte gueltigen Drehbuchautor angeben");


        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        importData();
    }
}