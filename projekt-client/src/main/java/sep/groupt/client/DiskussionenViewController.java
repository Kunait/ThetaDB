package sep.groupt.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sep.groupt.client.dataclass.Diskussionsgruppe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class DiskussionenViewController extends SceneController implements Initializable {


    @FXML
    private Button  backButton;

    @FXML
    private Text welcomeMessage,userName,emailField;

    private Diskussionsgruppe[] gruppen;

    @FXML
    private Rectangle einstellungenBox;

    @FXML
    private Rectangle abmeldenBox;

    @FXML
    private Circle profilBild;

    @FXML
    private ListView<Diskussionsgruppe> resultsList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream inputStream = new ByteArrayInputStream(Client.getCurrentNutzer().getProfilbild());
        Image image = new Image(inputStream, 150, 150, false, true);
        profilBild.setFill(new ImagePattern(image));


        // Ändert den Willkommenstext in Hallo, Vorname Nachname
        welcomeMessage.setText("Hallo, " + Client.getCurrentNutzer().getVorname() + " " + Client.getCurrentNutzer().getNachname());

        try {
            gruppen = RequestHandler.getJoinedDiskussionen(Client.getCurrentNutzer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        resultsList.getItems().addAll(gruppen);
    }

    public void abmeldenBoxPressed(){
        try {
            Client.setFilter("");
            switchToSceneWithStage("login.fxml", "Login");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Login");
        }
    }

    public void einstellungenBoxPressed(){
        try {
            Client.setFilter("");
            switchToSceneWithStage("editProfile.fxml", "Einstellungen");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "platzhalter");
        }
    }

    public void backButtonPressed() throws IOException {
        Client.setFilter("");
        switchToSceneWithStage("nutzeruebersicht.fxml","Nutzerübersicht");
    }

    public void openDiskussion() throws IOException {

        if(resultsList.getSelectionModel().getSelectedItem() != null){

            Client.setSelectedGruppe(resultsList.getSelectionModel().getSelectedItem());
            switchToSceneWithStage("diskussion.fxml","Diskussion "+Client.getSelectedGruppe().getName());


        }


    }
}
