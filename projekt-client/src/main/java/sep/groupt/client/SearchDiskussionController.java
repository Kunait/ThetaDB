package sep.groupt.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

public class SearchDiskussionController extends SceneController implements Initializable {

    @FXML
    private Button suche, backButton;

    @FXML
    private Text welcomeMessage,userName,emailField, friendlisttxt, watchlisttxt, seenlisttxt;

    @FXML
    private TextField searchBar;

    @FXML
    private Rectangle einstellungenBox;

    @FXML
    private Rectangle abmeldenBox;

    @FXML
    private Circle profilBild;

    @FXML
    private ListView<Diskussionsgruppe> resultsList;

    @FXML
    private CheckBox privatBox;

    @FXML
    private TextField name;


    String filter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream inputStream = new ByteArrayInputStream(Client.getCurrentNutzer().getProfilbild());
        Image image = new Image(inputStream, 150, 150, false, true);
        profilBild.setFill(new ImagePattern(image));


        // Ändert den Willkommenstext in Hallo, Vorname Nachname
        welcomeMessage.setText("Hallo, " + Client.getCurrentNutzer().getVorname() + " " + Client.getCurrentNutzer().getNachname());


        // Nutzer Informationen anzeigen

        filter = Client.getFilter();
        searchBar.setText(filter);

        try {
            sucheClicked();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinPressed() throws IOException, InterruptedException {

        if(resultsList.getSelectionModel().getSelectedItem() != null){

            Diskussionsgruppe gruppe = resultsList.getSelectionModel().getSelectedItem();
            gruppe.setJoiningUserID(Client.getCurrentNutzer().getUserID());
            RequestHandler.joinDiskussion(gruppe);
            resultsList.getItems().remove(resultsList.getSelectionModel().getSelectedItem());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Beitritt");
            alert.setHeaderText("Erfolgreich");
            alert.setContentText("Der Beitritt war erfolgreich!");
            alert.showAndWait();

        }


    }

    public void create() throws IOException, InterruptedException {

        if(!name.getText().equals("")){

            Diskussionsgruppe gruppe = new Diskussionsgruppe(Client.getCurrentNutzer(),name.getText().replaceAll("[^a-zA-Z0-9\\s]", ""),privatBox.isSelected());
            RequestHandler.addDiskussion(gruppe);
            name.setText("");
            privatBox.setSelected(false);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Erstellung");
            alert.setHeaderText("Erfolgreich");
            alert.setContentText("Die Gruppe wurde erstellt!");
            alert.showAndWait();
            sucheClicked();

        }

    }

    public void sucheClicked() throws IOException, InterruptedException{
            RequestHandler.getDiskussionen(Client.getCurrentNutzer());
           Diskussionsgruppe[] gruppen = Client.getGruppen();
           filter = searchBar.getText();

        resultsList.getItems().removeAll(resultsList.getItems());
        for(Diskussionsgruppe g: gruppen){
            System.out.println(g.toString());
            if(g.getName().toLowerCase().contains(filter.toLowerCase())){
                resultsList.getItems().add(g);
            }
        }

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
}
