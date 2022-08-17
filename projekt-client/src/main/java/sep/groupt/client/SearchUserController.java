package sep.groupt.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.Nutzer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchUserController extends SceneController implements Initializable {

    @FXML
    private Button suche, backButton;

    @FXML
    private ImageView userBild;

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
    private ListView<Nutzer> resultsList, friendlist;

    @FXML
    private ListView<Film> watchlist, seenlist;

    @FXML
    private Button addAsFriend;

    private String filter;

    private Nutzer[] nutzer;

    private Nutzer chosenUser;

    public void setFilter(String filter){

        this.filter = filter;
    }


    public void sucheClicked() throws IOException, InterruptedException {

        filter = searchBar.getText();
        nutzer = RequestHandler.searchUser(filter);
        if(nutzer != null) {
            Nutzer[] finalArray = new Nutzer[nutzer.length-1];
            int i = 0;
            for(Nutzer n: nutzer){
                if(n.getUserID() != Client.getCurrentNutzer().getUserID()){
                    finalArray[i] = n;
                    i++;
                }

            }

            resultsList.getItems().removeAll(resultsList.getItems());
            resultsList.getItems().addAll(finalArray);

        }

    }

    public void addAsFriendClicked() throws IOException, InterruptedException {

        if(chosenUser != null) {
            RequestHandler.addFriendRequest(Client.getCurrentNutzer().getUserID(), chosenUser.getUserID());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Erfolg");
            alert.setHeaderText("Freundschaftsanfrage geschickt");
            alert.setContentText("");
            alert.showAndWait();
            addAsFriend.setDisable(true);
        }
    }

    public void friendListClicked(MouseEvent event) throws IOException, InterruptedException {

        if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() ==2){
            if(friendlist.getSelectionModel().getSelectedItem() != null) {
                chosenUser = friendlist.getSelectionModel().getSelectedItem();
                Client.setProfilAnsichtNutzer(RequestHandler.getNutzerByID(chosenUser.getUserID()));
                switchToSceneWithStage("profilAnzeigen.fxml", "Profil von "+chosenUser.getVorname()+ " "+chosenUser.getNachname());

            }

        }


    }

    private void checkFreundesliste() {
        Nutzer[] tempFriendlist;


        try {
            tempFriendlist = RequestHandler.getFriendListArray(Client.getCurrentNutzer().getUserID());

            for (Nutzer n : tempFriendlist) {

                if (n.getUserID() == chosenUser.getUserID() || addAsFriend.isDisabled() || chosenUser.getUserID() == Client.getCurrentNutzer().getUserID()) {
                    addAsFriend.setDisable(true);
                    System.out.println("Freundesliste");

                } else {
                    addAsFriend.setDisable(false);
                }

            }


        } catch (IOException | InterruptedException e) {
            Client.connectionLost("0001");
        }


    }

    private void checkAnfragenListe() throws Exception{
        Nutzer[] tempFriendlist = RequestHandler.getAnfragenListe(chosenUser.getUserID());


        for (Nutzer n : tempFriendlist) {

            if (n.getUserID() == Client.getCurrentNutzer().getUserID() || addAsFriend.isDisabled() || chosenUser.getUserID() == Client.getCurrentNutzer().getUserID()) {
                addAsFriend.setDisable(true);
                System.out.println("Anfragenliste");

            } else {
                addAsFriend.setDisable(false);
            }

        }


    }

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

        userBild.setDisable(true);
        emailField.setDisable(true);
        userName.setDisable(true);
        userBild.setVisible(false);
        emailField.setVisible(false);
        userName.setVisible(false);
        addAsFriend.setDisable(true);

        try {
            nutzer = RequestHandler.searchUser(filter);
            Nutzer[] finalArray = new Nutzer[nutzer.length-1];
            int i = 0;
            for(Nutzer n: nutzer){
                if(n.getUserID() != Client.getCurrentNutzer().getUserID()){
                    finalArray[i] = n;
                    i++;
                }

            }

            if(nutzer != null)
                resultsList.getItems().addAll(finalArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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

    public void listClicked(MouseEvent event) throws Exception {

        if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() ==2){

            chosenUser = resultsList.getSelectionModel().getSelectedItem();
            //chosenUser = RequestHandler.getNutzerByID(chosenUser.getUserID());

            System.out.println(chosenUser.toString());
            char[] c = chosenUser.getEinstellungen().toCharArray();
            userBild.setDisable(false);
            emailField.setDisable(false);
            userName.setDisable(false);
            userBild.setVisible(true);
            emailField.setVisible(true);
            userName.setVisible(true);
            addAsFriend.setDisable(false);


            byte[] bild = RequestHandler.getProfilbild(chosenUser.getUserID());
            Image image = new Image(new ByteArrayInputStream(bild), 150, 150, false, true);

            userBild.setImage(image);
            emailField.setText(chosenUser.getEmailAdresse());
            System.out.println(chosenUser.getEmailAdresse());
            userName.setText(chosenUser.getVorname() + " " + chosenUser.getNachname());
            //RequestHandler.getFriendList(chosenUser())
            if(c[2]=='1'){
                Nutzer[] u = RequestHandler.getFriendListArray(chosenUser.getUserID());
                for (Nutzer tempNutzer : u){
                    if(tempNutzer.getUserID()==Client.getCurrentNutzer().getUserID()){
                        c[2]='2';
                        if(c[0]=='1'){
                            c[0]='2';
                        }
                        if(c[1]=='1'){
                            c[1]='2';
                        }
                    }
                }
            }
            if(c[2]=='2') {
                friendlist.setVisible(true);
                friendlisttxt.setVisible(true);
                friendlist.getItems().removeAll(friendlist.getItems());
                friendlist.getItems().addAll(RequestHandler.getFriendList(chosenUser.getUserID()));
            }else{
                friendlist.setVisible(false);
                friendlisttxt.setVisible(false);
            }
            if(c[0]=='1'){
                Nutzer[] u = RequestHandler.getFriendListArray(chosenUser.getUserID());
                for (Nutzer tempNutzer : u){
                    if(tempNutzer.getUserID()==Client.getCurrentNutzer().getUserID()){
                        c[0]='2';
                        if(c[1]=='1'){
                            c[1]='2';
                        }
                    }
                }
            }
            if(c[0]=='2') {
                watchlist.setVisible(true);
                watchlisttxt.setVisible(true);
                watchlist.getItems().removeAll(watchlist.getItems());
                watchlist.getItems().addAll(RequestHandler.getWatchListe(chosenUser.getUserID()));
            }else{
                watchlist.setVisible(false);
                watchlisttxt.setVisible(false);
            }
            if(c[1]=='1'){
                Nutzer[] u = RequestHandler.getFriendListArray(chosenUser.getUserID());
                for (Nutzer tempNutzer : u){
                    if(tempNutzer.getUserID()==Client.getCurrentNutzer().getUserID()){
                        c[1]='2';
                    }
                }
            }
            if(c[1]=='2') {
                seenlist.setVisible(true);
                seenlisttxt.setVisible(true);
                seenlist.getItems().removeAll(seenlist.getItems());
                seenlist.getItems().addAll(RequestHandler.getSeenListe(chosenUser.getUserID()));
            }else{
                seenlist.setVisible(false);
                seenlisttxt.setVisible(false);
            }

            addAsFriend.setDisable(false);
            checkAnfragenListe();
            checkFreundesliste();
        }


    }



    public void getSelectedFilmFromWatchlist(MouseEvent event){
        try {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY  && watchlist.getSelectionModel().getSelectedItem() != null) {
                System.out.println(watchlist.getSelectionModel().getSelectedItem().getName());
                Client.setSelectedFilm(RequestHandler.getMovie(watchlist.getSelectionModel().getSelectedItem().getFilm_id()));
                FilmansichtController.setSeite("nutzerSuche.fxml");
                switchToSceneWithStage("Filmansicht.fxml", "Film Ansicht");
            }
        } catch (IOException e){
            Client.viewSwitchFailed("0001", "Filmansicht");
        }catch(InterruptedException e1){
            Client.viewSwitchFailed("0001", "Filmansicht");
        }

    }

    public void getSelectedFilmFromSeenlist(MouseEvent event){
        try {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY  && seenlist.getSelectionModel().getSelectedItem() != null) {
                System.out.println(seenlist.getSelectionModel().getSelectedItem().getName());
                Client.setSelectedFilm(RequestHandler.getMovie(seenlist.getSelectionModel().getSelectedItem().getFilm_id()));
                FilmansichtController.setSeite("nutzerSuche.fxml");
                switchToSceneWithStage("Filmansicht.fxml", "Film Ansicht");
            }
        } catch (IOException e){
            Client.viewSwitchFailed("0001", "Filmansicht");
        }catch(InterruptedException e1){
            Client.viewSwitchFailed("0001", "Filmansicht");
        }
    }
}
