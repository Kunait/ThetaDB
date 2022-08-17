package sep.groupt.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import org.apache.commons.validator.routines.EmailValidator;
import sep.groupt.client.dataclass.Systemadministrator;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class RegisterAdminController extends SceneController implements Initializable {

    @FXML
    private TextField emailFeld, vorname, nachname;

    @FXML
    private Text warning, vornameText, nachnameText, emailText, passwortText;

    @FXML
    private PasswordField passwortFeld, passwortWiederholen;
    

    private boolean vornameTest = false;
    private boolean nachnameTest = false;
    private boolean emailTest = false;
    private boolean passwortTest = false;
    private boolean passwort2Test = false;


    //Anmelden Button gedrückt -> Login

    public void anmeldenButtonPressed() throws IOException {
        switchToSceneWithStage("login.fxml", "Login");
    }

    //Registrieren Button gedrückt -> Manage Oberfläche

    public void registrierenButtonPressed() throws IOException, InterruptedException{
        boolean felderGefuelt = false;
        //Prüft, ob alle Felder ausgefüllt sind
        if (!emailFeld.getText().isEmpty() && !vorname.getText().isEmpty() && !nachname.getText().isEmpty() && !emailFeld.getText().isEmpty() && !passwortFeld.getText().isEmpty() && !passwortWiederholen.getText().isEmpty()) {
            felderGefuelt = true;
        }

        if (felderGefuelt == false) {
            warning.setText("Bitte fuellen Sie alle Felder aus!");
        }
        if (felderGefuelt && nachnameTest && emailTest && passwortTest && passwort2Test && vornameTest) {
            Systemadministrator admintoAdd = createAdmin();

            try {
                boolean[] check = RequestHandler.createAdmin(admintoAdd);

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
                    alert.setContentText("Ein Admin mit der E-Mail Adresse existiert bereits!");
                    alert.showAndWait();
                }
                if (Arrays.equals(check, condition3)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Registrierung");
                    alert.setHeaderText("Fehler");
                    alert.setContentText("Beim erstellen des Admins ist ein Fehler aufgetreten, bitte wenden Sie sich an den Systemadministrator!");
                    alert.showAndWait();
                }
            } catch (IOException|InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Registrierung");
                alert.setHeaderText("Fehler");
                alert.setContentText("Verbindung zum Server ist fehlgeschlagen!");
                alert.showAndWait();
            }

            warning.setText("");
        }


    }

    // Hilftmethode zum erstellen eines Adminobjekts

    private Systemadministrator createAdmin(){
        Systemadministrator newAdmin = new Systemadministrator();
        newAdmin.setEmailAdresse(emailFeld.getText());
        newAdmin.setVorname(vorname.getText());
        newAdmin.setNachname(nachname.getText());

        String salt = getSalt();
        newAdmin.setSalt(salt);
        String hashedPasswort;

        try {
            hashedPasswort = getHashedPassword(passwortFeld.getText(), salt);
            newAdmin.setPasswort(hashedPasswort);
        } catch (NoSuchAlgorithmException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Fehler: 0002");
            alert.setContentText("geschütztes Passwort konnte nicht generiert werden. Bitte melden Sie sich beim Support");
            alert.showAndWait();
        }

        return newAdmin;
    }

    //Mit der Entertaste sich registrieren können

    public void handleEnter(KeyEvent event) throws IOException, InterruptedException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            registrierenButtonPressed();
        }
    }

    //Validator um die Korrektheit der eingegebenen E-Mail zu prüfen

    public static boolean emailValidator(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    // Quelle: https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/

    // Erstelle einen 16 Byte langen String mit Randomwerten

    private static String getSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt.toString();
    }

    // Nehme das passwort und Salt und erstelle mit SHA-1 Algorithmus einen hashedPasswort

    private static String getHashedPassword(String passwort, String salt) throws NoSuchAlgorithmException, NullPointerException{
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

    //Prüfen, ob die Eingabe in die Textfelder den

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vorname.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!vorname.getText().isEmpty() && vorname.getText().matches("[A-ZÄÖÜ][a-zäöüß]*")) {
                    vorname.setStyle("-fx-text-box-border: green;-fx-focus-color: green;");
                    vornameText.setText("");
                    vornameTest = true;
                } else if (!vorname.getText().isEmpty()) {
                    vorname.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
                    vornameText.setText("Richtige Form nutzen!");
                    vornameTest = false;
                } else {
                    vorname.setStyle("-fx-text-box-border: lightgrey;-fx-focus-color: lightblue;");
                    vornameText.setText("");
                    vornameTest = false;
                }
            }
        });

        nachname.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!nachname.getText().isEmpty() && nachname.getText().matches("[A-ZÄÖÜ][a-zäöüß]*")) {
                    nachname.setStyle("-fx-text-box-border: green;-fx-focus-color: green;");
                    nachnameTest = true;
                    nachnameText.setText("");
                } else if (!nachname.getText().isEmpty()) {
                    nachname.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
                    nachnameText.setText("Richtige Form nutzen!");
                    nachnameTest = false;
                } else {
                    nachname.setStyle("-fx-text-box-border: lightgrey;-fx-focus-color: lightblue;");
                    nachnameText.setText("");
                    nachnameTest = false;
                }
            }
        });
        emailFeld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (emailValidator(newValue) && !emailFeld.getText().equals("")) {
                    emailFeld.setStyle("-fx-text-box-border: green;-fx-focus-color: green;");
                    emailTest = true;
                    emailText.setText("");
                } else if (!emailValidator(newValue) && !emailFeld.getText().equals("")) {
                    emailFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
                    emailText.setText("Keine valide E-Mailadresse");
                    emailTest = false;
                } else {
                    emailFeld.setStyle("-fx-text-box-border: lightgrey;-fx-focus-color: lightblue;");
                    emailText.setText("");
                    emailTest = false;
                }
            }
        });


        passwortFeld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!passwortFeld.getText().equals("") && passwortFeld.getText().length() >= 8) {
                    passwortFeld.setStyle("-fx-text-box-border: green;-fx-focus-color: green;");
                    passwortTest = true;
                    passwortText.setText("");
                } else if (!passwortFeld.getText().equals("") && passwortFeld.getText().length() < 8) {
                    passwortFeld.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
                    passwortText.setText("Mindestens 8 Zeichen!");
                    passwortTest = false;
                } else {
                    passwortFeld.setStyle("-fx-text-box-border: lightgrey;-fx-focus-color: lightblue;");
                    passwortText.setText("");
                    passwortTest = false;
                }
            }
        });

        passwortWiederholen.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!passwortWiederholen.getText().equals("") && passwortWiederholen.getText().equals(passwortFeld.getText()) && passwortWiederholen.getText().length() >= 8) {
                    passwortWiederholen.setStyle("-fx-text-box-border: green;-fx-focus-color: green;");
                    passwort2Test = true;
                    warning.setText("");
                } else if (!passwortWiederholen.getText().equals("")) {
                    passwortWiederholen.setStyle("-fx-text-box-border: red;-fx-focus-color: red;");
                    if (passwortWiederholen.getText().length() >= 8 && !passwortWiederholen.getText().equals(passwortFeld.getText())) {
                        warning.setText("Passwörter sind nicht gleich");
                        passwort2Test = false;
                    } else if (passwortFeld.getText().length() < 8 && !passwortWiederholen.getText().equals(passwortFeld.getText())) {
                        warning.setText("Mindestens 8 Zeichen!");
                        passwort2Test = false;
                    }

                    passwort2Test = false;
                } else {
                    passwortWiederholen.setStyle("-fx-text-box-border: lightgrey;-fx-focus-color: lightblue;");
                    warning.setText("");
                    passwort2Test = false;
                }
            }
        });
    }
}