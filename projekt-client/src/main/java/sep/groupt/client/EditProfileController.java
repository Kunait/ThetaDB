package sep.groupt.client;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import sep.groupt.client.dataclass.Nutzer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController extends SceneController implements Initializable {
    @FXML
    private Button AbbrechenButton;

    @FXML
    private RadioButton watchlistprivat, watchlistfriends, watchlistall;
    @FXML
    private RadioButton historyprivat, historyfriends, historyall;
    @FXML
    private RadioButton friendsprivat, friendsfriends, friendsall;
    @FXML
    private RadioButton bewertungenprivat, bewertungenfriends, bewertungenall;

    @FXML
    private Button SaveButton;

    @FXML
    private TextArea reportFeld;

    @FXML
    private Text name, mail;
    private Nutzer nutzer;

   // nutzerUebersichtController mang = new nutzerUebersichtController();

    public void importData() {
        this.nutzer=Client.getCurrentNutzer();
        String einst = nutzer.getEinstellungen();
        char[] c = einst.toCharArray();
        if(c[0]=='0'){
            watchlistprivat.setSelected(true);
        } else if (c[0]=='1') {
            watchlistfriends.setSelected(true);
        } else{
            watchlistall.setSelected(true);
        }
        if(c[1]=='0'){
            historyprivat.setSelected(true);
        } else if (c[1]=='1') {
            historyfriends.setSelected(true);
        } else{
            historyall.setSelected(true);
        }
        if(c[2]=='0'){
            friendsprivat.setSelected(true);
        } else if (c[2]=='1') {
            friendsfriends.setSelected(true);
        } else{
            friendsall.setSelected(true);
        }
        if(c[3]=='0'){
            bewertungenprivat.setSelected(true);
        } else if (c[3]=='1') {
            bewertungenfriends.setSelected(true);
        } else{
            bewertungenall.setSelected(true);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        importData();
    }

    public void AbbrechenButtonPressed() throws IOException {
        switchToSceneWithStage("nutzeruebersicht.fxml", "Übersicht");
    }

    public void SaveButtonPressed() throws IOException {
        String temp = "";
        if(watchlistprivat.isSelected()){
            temp = temp + "0";
        } else if (watchlistfriends.isSelected()) {
            temp = temp + "1";
        } else{
            temp = temp + "2";
        }
        if(historyprivat.isSelected()){
            temp = temp + "0";
        } else if (historyfriends.isSelected()) {
            temp = temp + "1";
        } else{
            temp = temp + "2";
        }
        if(friendsprivat.isSelected()){
            temp = temp + "0";
        } else if (friendsfriends.isSelected()) {
            temp = temp + "1";
        } else{
            temp = temp + "2";
        }
        if(bewertungenprivat.isSelected()){
            temp = temp + "0";
        } else if (bewertungenfriends.isSelected()) {
            temp = temp + "1";
        } else{
            temp = temp + "2";
        }
        nutzer.setEinstellungen(temp);
        try {
            RequestHandler.changeSettings(nutzer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        switchToSceneWithStage("nutzeruebersicht.fxml", "Übersicht");
    }
}
