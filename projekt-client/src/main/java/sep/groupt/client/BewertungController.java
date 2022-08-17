package sep.groupt.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sep.groupt.client.dataclass.Bewertung;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.Nutzer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BewertungController extends SceneController implements Initializable {

    @FXML
    private Text welcomeMessage;
    @FXML
    private Circle profilBild;
    @FXML
    private Button zurueck;
    @FXML
    private Text name;
    @FXML
    private Text punkte;
    @FXML
    private TableView<Bewertung> tabelle;
    @FXML
    private TableColumn<Bewertung, String> punkteTabelle, bewertungTabelle;

    private Film film;

    public void zurueckClicked() throws IOException {
        switchToSceneWithStage("Filmansicht.fxml", "Film");

    }

    public void bewertungClicked(MouseEvent mouseEvent) {
    }

    public void initialize(URL location, ResourceBundle resources) {
        FilmansichtController ansicht = new FilmansichtController();
        film = ansicht.getFilm();
        name.setText(film.getName());

        Bewertung[] bewertung=null;
        try {
            bewertung = RequestHandler.getBewertungen(film.getFilm_id());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Bewertung> arrayList = new ArrayList<>();

        for (Bewertung x : bewertung) {
            if(x.getPunkte()!=0) {
                String s;
                Nutzer u;
                try {
                    u = RequestHandler.getNutzerByID(x.getUserID());
                    s= u.getEinstellungen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(s!=null) {
                    char[] c = s.toCharArray();
                    if (c[3] == '1'){
                        Nutzer[] uu = new Nutzer[0];
                        try {
                            uu = RequestHandler.getFriendListArray(u.getUserID());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        for (Nutzer tempNutzer : uu){
                            if(tempNutzer.getUserID()==Client.getCurrentNutzer().getUserID()){
                                c[3]='2';
                            }
                        }
                    }
                    if(Client.getCurrentNutzer().getUserID()==u.getUserID()){
                        c[3]='2';
                    }
                    if (c[3] == '2') {
                        arrayList.add(x);
                    }
                }

            }
        }


        ObservableList<Bewertung> list = FXCollections.observableList(arrayList);
        tabelle.setItems(list);
        punkteTabelle.setCellValueFactory(cellData -> cellData.getValue().getPunkteProperty());
        bewertungTabelle.setCellValueFactory(cellData -> cellData.getValue().getBewertungProperty());

        // Zeigt Profilbild des Benutzers an
        InputStream inputStream = new ByteArrayInputStream(Client.getCurrentNutzer().getProfilbild());
        Image image = new Image(inputStream, 150, 150, false, true);
        profilBild.setFill(new ImagePattern(image));

        // Ändert den Willkommenstext in Hallo, Vorname Nachname
        welcomeMessage.setText("Hallo, " + Client.getCurrentNutzer().getVorname() + " " + Client.getCurrentNutzer().getNachname());

        double hilfe= 0;
        try {
            hilfe = RequestHandler.getDurchnittsBewertung(film.getFilm_id());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        punkte.setText(String.valueOf(hilfe+"/5"));


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
