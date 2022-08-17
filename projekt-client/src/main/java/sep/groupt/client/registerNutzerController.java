package sep.groupt.client;


import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;
import sep.groupt.client.dataclass.Nutzer;


import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.security.NoSuchAlgorithmException;


public class registerNutzerController extends SceneController implements Initializable {

    @FXML
    private TextField emailFeld, vornameFeld, nachnameFeld;

    @FXML
    private PasswordField passwortFeld, passwortFeld2;

    @FXML
    private Text warning;

    @FXML
    private Button uploadBildButton, registerButton, anmeldenButton;

    @FXML
    private ImageView profilBild;

    private ChangeListener<String> emailListener;
    private ChangeListener<String> vornameListener;
    private ChangeListener<String> nachnameListener;
    private ChangeListener<String> passwortListener;
    private ChangeListener<String> passwort2Listener;

    private byte[] nutzerBild;
    private boolean nutzerBildSelected = false;

    private InputStream is;


    public void anmeldenButtonPressed(){
        try {
            switchToSceneWithStage("login.fxml", "Login");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "login.fxml");
        }
    }

    public void registerButtonPressed(){
        final boolean[] expectedCondition = {true, true, true, true, true};
        boolean[] currentCondition = checkFelder();
        if (Arrays.equals(currentCondition, expectedCondition)){
            emailFeld.textProperty().removeListener(emailListener);
            vornameFeld.textProperty().removeListener(vornameListener);
            nachnameFeld.textProperty().removeListener(nachnameListener);
            passwortFeld.textProperty().removeListener(passwortListener);
            passwortFeld2.textProperty().removeListener(passwort2Listener);

            Nutzer nutzer = createNutzer();

            try {
                boolean[] check = RequestHandler.createNutzer(nutzer);

                final boolean[] condition1 = {true, false};
                final boolean[] condition2 = {false, true};
                final boolean[] condition3 = {false, false};
                if (Arrays.equals(check, condition1)){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Registrierung");
                    alert.setHeaderText("Erfolgreich");
                    alert.setContentText("Die Registrierung war erfolgreich!");
                    alert.showAndWait();
                    switchToSceneWithStage("login.fxml", "Login");
                }
                if (Arrays.equals(check, condition2)){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Registrierung");
                    alert.setHeaderText("Fehler");
                    alert.setContentText("Ein Nutzer mit der E-Mail Adresse existiert bereits!");
                    alert.showAndWait();
                }
                if (Arrays.equals(check, condition3)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Registrierung");
                    alert.setHeaderText("Fehler");
                    alert.setContentText("Beim erstellen des Nutzers ist ein Fehler aufgetreten, bitte wenden Sie sich an den Systemadministrator!");
                    alert.showAndWait();
                }
            } catch (IOException|InterruptedException e) {
                Client.connectionLost("0001");
            }

            warning.setText("");
        }
        else {
            if (!currentCondition[0]){
                emailFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            }
            if (!currentCondition[1]){
                vornameFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            }
            if (!currentCondition[2]){
                nachnameFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            }
            if (!currentCondition[3]){
                passwortFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            }
            if (!currentCondition[4]){
                passwortFeld2.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            }
            warning.setText("Bitte füllen Sie alle Felder richtig aus!");
        }
    }

    private boolean[] checkFelder(){
        boolean[] result = {false, false, false, false, false};
        if (!emailFeld.getText().isEmpty()){
            result[0] = emailValidator(emailFeld.getText());
        }
        if (!vornameFeld.getText().isEmpty()){
            result[1] = stringNurBuchstaben(vornameFeld.getText());
        }
        if (!nachnameFeld.getText().isEmpty()){
            result[2] = stringNurBuchstaben(nachnameFeld.getText());
        }
        if (!passwortFeld.getText().isEmpty()){
            result[3] = passwortFeld.getText().length() >= 8;
        }
        if (!passwortFeld2.getText().isEmpty() && !passwortFeld.getText().isEmpty()){
            result[4] = passwortFeld.getText().equals(passwortFeld2.getText());
        }
        return result;
    }

    // Überprüft, ob E-Mail Regex eingehalten wird oder nicht

    private static boolean emailValidator(String email){
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    // Überprüft, ob der String nur Buchstaben enthält

    private boolean stringNurBuchstaben(String string) {
        return string.matches("[a-zA-Z]+");
    }

    // Wenn der Button Profilbild hochladen ausgewählt wird öffnet sich ein Fileexplorer wo man sein Profilbild auswählen kann
    // nur jpg und png sind erlaubt

    public void uploadBildButtonPressed(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Profilbild auswählen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Alle Bilder", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("jpg", "*.jpg"),
                new FileChooser.ExtensionFilter("png", "*.png")
        );

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Image bild = new Image(file.getAbsolutePath(), 150, 150, false, true);

            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                nutzerBild = fileInputStream.readAllBytes();
                fileInputStream.close();
                profilBild.setImage(bild);
                nutzerBildSelected = true;
            } catch (IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Fehler: 0004");
                alert.setContentText("Bild konnte nicht geladen werden");
                alert.showAndWait();
            }

        }
    }

    // Erstellt eine NutzerKlasse mit Email, vorname, nachname, passwort danach wird ein hashed Passwort generiert mit einem Salt

    private Nutzer createNutzer(){
        String salt = getSalt();
        String hashedPasswort = null;
        try {
            hashedPasswort = getHashedPassword(passwortFeld.getText(), salt);
        }  catch (NoSuchAlgorithmException|NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Fehler: 0002");
            alert.setContentText("geschütztes Passwort konnte nicht generiert werden. Bitte melden Sie sich beim Support");
            alert.showAndWait();
        }

        byte[] profilbild = null;

        if (nutzerBildSelected){
            profilbild = nutzerBild;
        }
        else {
            try {
                is = getClass().getResourceAsStream("nutzerbild/default.jpg");
                profilbild = is.readAllBytes();
                is.close();
            } catch (IOException e) {
                System.out.println("Profilbild konnte nicht geladen werden!");
            }
        }
        Nutzer nutzer = new Nutzer(emailFeld.getText(), vornameFeld.getText(), nachnameFeld.getText(), salt, hashedPasswort, profilbild);
        nutzer.setEinstellungen("1022"); //defaulteinstellungen||||||| watchlist: freunde, history: niemand, freunde und bewertungen: jeder

        return nutzer;
    }


    // Quelle: https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/

    // Erstelle einen 16 Byte langen String mit Randomwerten

    public static String getSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt.toString();
    }

    // Nehme das passwort und Salt und erstelle mit SHA-1 Algorithmus einen hashedPasswort

    public static String getHashedPassword(String passwort, String salt) throws NoSuchAlgorithmException, NullPointerException{
        String hashedPasswort;
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(salt.getBytes());
        byte[] bytes = md.digest(passwort.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        hashedPasswort = sb.toString();

        return hashedPasswort;
    }

    // -----------------------------------------------

    // 1. Methode beim öffnen des Fensters

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setzt das Profilbild auf das Default Bild und zeigt es an
        try {
            is = getClass().getResourceAsStream("nutzerbild/default.jpg");
            Image bild = new Image(is, 150, 150, false, true);
            is.close();
            profilBild.setImage(bild);
        } catch (IOException|NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Fehler: 0003");
            alert.setContentText("Default Profilbild konnte nicht geladen werden, bitte wenden Sie sich an den Support");
            alert.showAndWait();
        }

        // Ein Change-listener für das E-Mail Feld zeigt eine grüne umrandung, falls E-Mail Regex richtig ist
        // oder rot, wenn Regex nicht eingehalten wurde

        emailListener = (observable, oldValue, newValue) -> {
            if (emailValidator(newValue) && !emailFeld.getText().isEmpty()){
                emailFeld.setStyle("-fx-text-box-border: green;-fx-focus-color: green;");
            }
            else if (!emailValidator(newValue) && !emailFeld.getText().isEmpty()){
                emailFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            }
            else {
                emailFeld.setStyle("-fx-text-box-border: lightgrey;-fx-focus-color: lightblue;");
            }
        };

        emailFeld.textProperty().addListener(emailListener);

        // Ein Change-Listener für das Vorname Feld grün, wenn nur Buchstaben vorhanden sind (rot wenn nicht)

        vornameListener = (observable, oldValue, newValue) -> {
            if (stringNurBuchstaben(vornameFeld.getText()) && !vornameFeld.getText().isEmpty()){
                vornameFeld.setStyle("-fx-text-box-border: green;-fx-focus-color: green;");
            }
            else if (!stringNurBuchstaben(vornameFeld.getText()) && !vornameFeld.getText().isEmpty()){
                vornameFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            }
            else {
                vornameFeld.setStyle("-fx-text-box-border: lightgrey;-fx-focus-color: lightblue;");
            }
        };

        vornameFeld.textProperty().addListener(vornameListener);

        // Ein Change-Listener für das Vorname Feld grün, wenn nur Buchstaben vorhanden sind (rot wenn nicht)

        nachnameListener = (observable, oldValue, newValue) -> {
            if (stringNurBuchstaben(nachnameFeld.getText()) && !nachnameFeld.getText().isEmpty()){
                nachnameFeld.setStyle("-fx-text-box-border: green;-fx-focus-color: green;");
            }
            else if (!stringNurBuchstaben(nachnameFeld.getText()) && !nachnameFeld.getText().isEmpty()){
                nachnameFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            }
            else {
                nachnameFeld.setStyle("-fx-text-box-border: lightgrey;-fx-focus-color: lightblue;");
            }
        };

        nachnameFeld.textProperty().addListener(nachnameListener);


        // Ein Change-listener für das Passwort Feld zeigt eine grüne umrandung, falls Passwort mindestens 8 Zeichen lang
        // oder rot für weniger als 8 Zeichen

        passwortListener = (observable, oldValue, newValue) -> {
            if (!passwortFeld.getText().isEmpty() && passwortFeld.getText().length() >= 8) {
                passwortFeld.setStyle("-fx-text-box-border: green;-fx-focus-color: green;");
            } else if (!passwortFeld.getText().isEmpty() && passwortFeld.getText().length() < 8) {
                passwortFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            } else {
                passwortFeld.setStyle("-fx-text-box-border: lightgrey;-fx-focus-color: lightblue;");
            }
        };

        passwortFeld.textProperty().addListener(passwortListener);

        // Ein Change-listener für das 2. Passwort Feld zeigt eine grüne umrandung, falls PasswortFeld1 und Feld2 gleich sind

        passwort2Listener = (observable, oldValue, newValue) -> {
            if (!passwortFeld.getText().isEmpty() && !passwortFeld2.getText().isEmpty() && passwortFeld.getText().equals(passwortFeld2.getText())){
                passwortFeld2.setStyle("-fx-text-box-border: green;-fx-focus-color: green;");
            }
            else if (!passwortFeld.getText().isEmpty() && !passwortFeld2.getText().isEmpty() && !passwortFeld.getText().equals(passwortFeld2.getText())){
                passwortFeld2.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
            }
            else {
                passwortFeld2.setStyle("-fx-text-box-border: lightgrey;-fx-focus-color: lightblue;");
            }
        };

        passwortFeld2.textProperty().addListener(passwort2Listener);
    }
}
