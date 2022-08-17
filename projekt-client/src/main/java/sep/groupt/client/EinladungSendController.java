package sep.groupt.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.apache.commons.io.output.ByteArrayOutputStream;
import sep.groupt.client.dataclass.Einladung;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.Nutzer;
import sep.groupt.client.dataclass.Watchlist;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EinladungSendController extends SceneController implements Initializable {

    FilmansichtController mang = new FilmansichtController();
    @FXML
    Text filmtxt, filmlangetxt, errortxt;
    @FXML
    private Text welcomeMessage;
    @FXML
    private Circle profilBild;
    @FXML
    private ListView<Nutzer> freundesListeView, eingeladenView;
    @FXML
    private Spinner<Integer> hour, minute;
    @FXML
    private DatePicker date;
    private static int wichtig=0;
    @FXML
    private TextArea reportFeld;
    private ObservableList<Nutzer> freundesListe, eingeladen;
    Film film;
    int i =0;
    public void einstellungenBoxPressed(){
        try {
            switchToSceneWithStage("editProfile.fxml", "Einstellungen");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "platzhalter");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Zeigt Profilbild des Benutzers an
        InputStream inputStream = new ByteArrayInputStream(Client.getCurrentNutzer().getProfilbild());
        Image image1 = new Image(inputStream, 150, 150, false, true);
        profilBild.setFill(new ImagePattern(image1));


        // Ändert den Willkommenstext in Hallo, Vorname Nachname
        welcomeMessage.setText("Hallo, " + Client.getCurrentNutzer().getVorname() + " " + Client.getCurrentNutzer().getNachname());
        if (Client.getSelectedFilm() != null){
            try {
                film = RequestHandler.getMovie(Client.getSelectedFilm().getFilm_id());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        film=mang.getFilm();
        filmtxt.setText(film.getName());
        filmlangetxt.setText(film.getLength() + " Minuten");

        SpinnerValueFactory<Integer> hourfactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);
        hour.setValueFactory(hourfactory);
        SpinnerValueFactory<Integer> minutefactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minute.setValueFactory(minutefactory);

        ArrayList<String> test = new ArrayList<>();

        for (Nutzer nutzer : Client.getCurrentNutzer().getFreundesListe()){
            test.add(nutzer.getVorname() + " " + nutzer.getNachname());
        }
        ArrayList<Nutzer> arr = new ArrayList<Nutzer>();
        arr.add(Client.getCurrentNutzer());
        eingeladen = FXCollections.observableList(arr);
        freundesListe = FXCollections.observableList(Client.getCurrentNutzer().getFreundesListe());
        for(Nutzer i : Client.getCurrentNutzer().getFreundesListe()){
            System.out.println(i.getVorname());
        }
        freundesListeView.setItems(freundesListe);

        freundesListeView.setCellFactory(lv ->
                new ListCell<>() {

                    @Override
                    public void updateItem(Nutzer nutzer, boolean empty) {
                        super.updateItem(nutzer, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(nutzer.getVorname() + " " + nutzer.getNachname());


                        }
                    }
                }
        );

        ContextMenu menu = new ContextMenu();
        ContextMenu menu1 = new ContextMenu();
        MenuItem invite = new MenuItem("Einladen");

        invite.setOnAction((event -> {

            if(eingeladen.size()>0) {
                for (Nutzer u : eingeladen) {
                    if (u.getUserID() == freundesListeView.getSelectionModel().getSelectedItem().getUserID()) {
                        i += 1;
                    }
                    if(u.getUserID() == Client.getCurrentNutzer().getUserID()){
                        eingeladen.remove(u);
                    }
                }
            }
            if(i==0){
                eingeladen.add(freundesListeView.getSelectionModel().getSelectedItem());
                System.out.println(freundesListeView.getSelectionModel().getSelectedItem().getVorname());
            }
            i=0;
            eingeladenView.setItems(eingeladen);
            wichtig+=1;

        }));

        MenuItem ausvite = new MenuItem("Ausladen");

        ausvite.setOnAction((event -> {
            eingeladen.remove(eingeladenView.getSelectionModel().getSelectedItem());
            eingeladenView.setItems(eingeladen);
        }));

        menu.getItems().add(invite);
        menu1.getItems().add(ausvite);

        freundesListeView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                if(freundesListeView.getSelectionModel().getSelectedItem() != null)
                    menu.show(freundesListeView,event.getScreenX(),event.getScreenY());
            }
        });

        eingeladenView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                if(eingeladenView.getSelectionModel().getSelectedItem() != null)
                    menu1.show(eingeladenView,event.getScreenX(),event.getScreenY());
            }
        });
    }

    public void abmeldenBoxPressed(){
        try {
            switchToSceneWithStage("login.fxml", "Login");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Login");
        }
    }

    public void send() {
        try {
       LocalDateTime now = LocalDateTime.now();
       if(now.getYear()>date.getValue().getYear() || now.getYear()==date.getValue().getYear() && now.getDayOfYear()>date.getValue().getDayOfYear() || date.getValue().equals(null)) {
           System.out.println("ungültiges datum");
           errortxt.setText("Ungültiges Datum");
           return;
        }
       }
       catch(Exception e){
            errortxt.setText("Ungültiges Datum");
            return;
        }
       if(wichtig<1 || eingeladen.size()==0){
           System.out.println("Keine User eingeladen");
           errortxt.setText("Keine User eingeladen");
           return;
       }
       Nutzer eee;
        for (Nutzer i : eingeladen) {
            System.out.println(Client.getCurrentNutzer().getUserID());
            Einladung ein;
            try {
                eee = RequestHandler.getNutzerByID(i.getUserID());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (minute.getValue() < 10) {
                ein = new Einladung(Client.getCurrentNutzer(), eee, film, date.getValue().toString() + " " + hour.getValue().toString() + ":0" + minute.getValue().toString(), reportFeld.getText());
                System.out.println(ein.getUser2().getEmailAdresse() + " 1");
                System.out.println(ein.getUser1().getEmailAdresse() + " 2");
            } else {
                ein = new Einladung(Client.getCurrentNutzer(), eee, film, date.getValue().toString() + " " + hour.getValue().toString() + ":" + minute.getValue().toString(), reportFeld.getText());
            }
                System.out.println(ein.getUser1().getUserID() + "  " + ein.getUser2().getUserID() + "  " + ein.getFilm().getFilm_id() + "  " + ein.getDate() + "   " + ein.getText());
            try {
                RequestHandler.sendEinladung(ein);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            switchToSceneWithStage("Filmansicht.fxml", "Filmansicht");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void zurueck() throws IOException {
        switchToSceneWithStage("Filmansicht.fxml", "Filmansicht");
    }

    public void getInvitedFriend(MouseEvent mouseEvent) {
    }

    public void getSelectedFriend(MouseEvent mouseEvent) {
    }
}
