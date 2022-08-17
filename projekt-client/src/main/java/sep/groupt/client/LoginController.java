package sep.groupt.client;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import org.apache.commons.validator.routines.EmailValidator;
import sep.groupt.client.dataclass.Nutzer;
import sep.groupt.client.dataclass.Systemadministrator;


import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class LoginController extends SceneController implements Initializable {

    @FXML
    private TextField emailFeld;

    @FXML
    private PasswordField passwortFeld;

    @FXML
    private TextField authCodeFeld;

    @FXML
    private Text warning, authCodeWarning, sicherheitsCodeText;

    @FXML
    private Button anmeldenButton, registerAdminButton, registerNutzerButton;

    @FXML
    private Button anmeldenAlsAdmin, anmeldenAlsNutzer;


    private ChangeListener<String> emailListener;
    private ChangeListener<String> passwortListener;
    private ChangeListener<String> authCodeListener;

    private String authCode;

    // Reaktion, wenn der Anmelden Button gedrückt wird

    public void anmeldenButtonPressed() {
        if (alleFelderAusgefuellt()) {
            String[] userdata = {emailFeld.getText(), passwortFeld.getText()};
            boolean[] check = {false, false};
            try {
                check = RequestHandler.checkUserdata(userdata);

            } catch (IOException | InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Fehler");
                alert.setHeaderText("Fehler: 0003");
                alert.setContentText("Es besteht keine Verbindung zum Server.");
                alert.showAndWait();
            }

            final boolean[] isAdmin = {true, false};
            final boolean[] isNutzer = {false, true};
            final boolean[] isBoth = {true, true};
            final boolean[] isNothing = {false, false};

            if (Arrays.equals(check, isAdmin)) {
                loginAsAdmin();
            }
            if (Arrays.equals(check, isNutzer)){
                viewAsNutzer();
            }
            if (Arrays.equals(check, isBoth)){
                anmeldenAlsAdmin.setVisible(true);
                anmeldenAlsAdmin.setOnAction(event -> loginAsAdmin());
                anmeldenButton.setText("Anmelden als Nutzer");
                anmeldenButton.setOnAction(event -> viewAsNutzer());
            }
            if (Arrays.equals(check, isNothing)){
                warning.setText("E-Mail oder Passwort sind falsch!");
            }
        } else {
            markFelder();
        }
    }


    // Loggt sich als Admin ein dh. Client -> setCurrentadmin und wechseln des Fensters auf Manage Movie

    private void loginAsAdmin(){
        Systemadministrator admin = new Systemadministrator(emailFeld.getText(), passwortFeld.getText());
        try {
            Client.setCurrentAdmin(RequestHandler.getAdmin(admin));
        } catch (IOException|InterruptedException e){
            Client.connectionLost("0002");
        }

        try {
            switchToSceneWithStage("manageMovie.fxml", "Manage Movie");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "ManageMovie");
        }
    }

    // Loggt sich als Nutzer ein dh. Client -> setcurrentNutzer und wechseln des Fensters auf Nutzerübersicht

    private void loginAsNutzer(){
        Nutzer nutzer = new Nutzer(emailFeld.getText(), passwortFeld.getText());
        try {
            Client.setCurrentNutzer(RequestHandler.getNutzer(nutzer));
        } catch (IOException|InterruptedException e){
            Client.connectionLost("0002");
        }

        try {
            switchToSceneWithStage("nutzeruebersicht.fxml", "Nutzerübersicht");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Nutzerübersicht");
        }
    }

    // View für Nutzer anpassen

    private void viewAsNutzer(){
        sendAuthPass(new Nutzer(emailFeld.getText(), passwortFeld.getText()));
        showSecurityView();
        anmeldenButton.setOnAction(event -> checkAuthCode());

        if (anmeldenAlsAdmin.isVisible()){
            anmeldenAlsAdmin.setVisible(false);
        }
    }

    // Check Conditions for Nutzer

    public void checkAuthCode(){
        if (authCode.equals(authCodeFeld.getText())){
            loginAsNutzer();
        }
        else {
            authCodeWarning.setText("Sicherheitscode ist falsch!");
        }
    }

    // Sicherheitspasswort wird vom Client angefordert (Server schickt E-Mail an Client EmailAdresse und übergibt hier den Sicherheitscode

    public void sendAuthPass(Nutzer nutzer){
        try {
            authCode = RequestHandler.getAuthCode(nutzer);
        } catch (IOException|InterruptedException e) {
            Client.connectionLost("0001");
        }
    }

    // Felder für Sicherheitscode werden angezeigt

    private void showSecurityView(){
        emailFeld.setDisable(true);
        passwortFeld.setDisable(true);
        authCodeFeld.setVisible(true);
        authCodeWarning.setVisible(true);
        sicherheitsCodeText.setVisible(true);
    }


    //

    private void loginAsAdminAndNutzer(){
        emailFeld.setDisable(true);
        passwortFeld.setDisable(true);
        anmeldenAlsAdmin.setVisible(true);
        anmeldenAlsAdmin.setOnAction(event -> loginAsAdmin());

    }


    // Gibt einen Booleanarray ob Contraints erfüllt sind [emailfeld][passwortfeld]

    private boolean[] checkFelder(){
        boolean[] result = {false, false};
        if (!emailFeld.getText().isEmpty()) {
            result[0] = emailValidator(emailFeld.getText());
        }
        if (!passwortFeld.getText().isEmpty()) {
            result[1] = passwortFeld.getText().length() >= 8;
        }
        return result;
    }

    // Wenn Felder leer sind, dann werden sie rot umrandet

    private void markFelder(){
        final boolean[] expectedCondition = {true, true};
        boolean[] currentCondition = checkFelder();
        if (!Arrays.equals(currentCondition, expectedCondition)) {
            if (!currentCondition[0]) {
                emailFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            }
            if (!currentCondition[1]) {
                passwortFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            }
            warning.setText("Bitte füllen Sie die Felder richtig aus!");
        }
    }

    // Überprüft, ob EMail Feld und Passwort Feld ausgefüllt sind.

    private boolean alleFelderAusgefuellt(){
        boolean[] expectedCondition = {true, true};
        return Arrays.equals(expectedCondition, checkFelder());
    }

    // Wechselt zum Registrierung-fenster

    public void registerAdminButtonPressed() {
        try {
            switchToSceneWithStage("registerAdmin.fxml", "Registrieren als Admin");
        } catch (IOException e){
            Client.viewSwitchFailed("0001", "RegisterAdmin");
        }
    }

    public void registerNutzerButtonPressed() {
        try {
            switchToSceneWithStage("registerNutzer.fxml", "Registrieren als Nutzer");
        } catch (IOException e){
            Client.viewSwitchFailed("0001", "RegisterNutzer");
        }
    }

    // Wenn Enter gedrückt wird, meldet man sich an

    public void handleEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            anmeldenButtonPressed();
        }
    }

    // Überprüft, ob E-Mail Regex eingehalten wird oder nicht

    private static boolean emailValidator(String email){
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }


    // Methode wird als erstes ausgeführt

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // SicherheitscodeFeld & Texte die dazu gehören werden als erstes Deaktiviert

        anmeldenAlsAdmin.setVisible(false);
        authCodeFeld.setVisible(false);
        authCodeWarning.setVisible(false);
        sicherheitsCodeText.setVisible(false);



        // Ein Change-listener für das E-Mail Feld zeigt eine grüne umrandung, falls E-Mail Regex richtig ist
        // oder rot, wenn Regex nicht eingehalten wurde

        emailListener = (observable, oldValue, newValue) -> {
            if (emailValidator(newValue) && !emailFeld.getText().isEmpty()) {
                emailFeld.setStyle("-fx-text-box-border: green;-fx-focus-color: green;");
            } else if (!emailValidator(newValue) && !emailFeld.getText().isBlank()) {
                emailFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            } else {
                emailFeld.setStyle("-fx-text-box-border: lightgrey;-fx-focus-color: lightblue;");
            }
        };

        emailFeld.textProperty().addListener(emailListener);

        // Ein Change-listener für das Passwort Feld zeigt eine grüne umrandung, falls Passwort mindestens 8 Zeichen lang
        // oder rot für weniger als 8 Zeichen

        passwortListener = (observable, oldValue, newValue) -> {
            if (!passwortFeld.getText().isBlank() && passwortFeld.getText().length() >= 8) {
                passwortFeld.setStyle("-fx-text-box-border: green;-fx-focus-color: green;");
            } else if (!passwortFeld.getText().isBlank() && passwortFeld.getText().length() < 8) {
                passwortFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            } else {
                passwortFeld.setStyle("-fx-text-box-border: lightgrey;-fx-focus-color: lightblue;");
            }
        };

        passwortFeld.textProperty().addListener(passwortListener);

    }
}