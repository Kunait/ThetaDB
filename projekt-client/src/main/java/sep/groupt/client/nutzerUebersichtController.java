package sep.groupt.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sep.groupt.client.dataclass.Einladung;
import sep.groupt.client.dataclass.Film;
import sep.groupt.client.dataclass.Nutzer;
import sep.groupt.client.dataclass.Watchlist;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class nutzerUebersichtController extends SceneController implements Initializable {

    @FXML
    private Circle profilBild;



    @FXML
    private Text welcomeMessage, userEmailText, userVornameText, userNachnameText, einladungresp;

    @FXML
    private TextField searchBar;

    @FXML
    private Rectangle einstellungenBox,bell,bell2;

    @FXML
    private Rectangle abmeldenBox;

    @FXML
    private ListView<Nutzer> freundesListeView;

    @FXML
    private ListView<Film> watchListeView, seenListeView;

    @FXML
    private ImageView profilBildGross;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private Button suche, filmvorschlaege;

    private ObservableList<Nutzer> freundesListe;
    private ObservableList<Film> watchListe;
    private ObservableList<Film> seenListe;

    private static Nutzer selectedNutzer;
    private static Film selectedFilm;

    private int iii;

    public void filmvorschlägePressed() throws IOException {
        switchToSceneWithStage("Filmvorschläge.fxml", "Filmvorschläge" );
    }


    public void statsWeiterleiten(){
        try {
            switchToSceneWithStage("nutzerstatistik.fxml", "Nutzer Statstik");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sucheClicked(){
        try {
            if (choiceBox.getValue().equals("Film")){
                Client.setFilter(searchBar.getText());
                switchToSceneWithStage("Suche.fxml", "Suche Filme");
            }
            if (choiceBox.getValue().equals("Nutzer")){
                Client.setFilter(searchBar.getText());
                switchToSceneWithStage("nutzerSuche.fxml", "Nutzersuche");

            }
            if (choiceBox.getValue().equals("Diskussionen")){
                Client.setFilter(searchBar.getText());
                switchToSceneWithStage("diskussionsuche.fxml", "Diskussionen");

            }
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Login");
        }
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

    public void WatchlistPressed(){
        try {
            switchToSceneWithStage("watchlist.fxml","Watchlist");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "platzhalter");
        }
    }

    public void SeenlistPressed(){
        try {
            switchToSceneWithStage("SeenList.fxml", "Gesehene Filme");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "platzhalter");
        }
    }



    public void getSelectedFilmFromWatchlist(MouseEvent event){
        try {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY  && watchListeView.getSelectionModel().getSelectedItem() != null) {
                System.out.println(watchListeView.getSelectionModel().getSelectedItem().getName());
                Client.setSelectedFilm(RequestHandler.getMovie(watchListeView.getSelectionModel().getSelectedItem().getFilm_id()));
                FilmansichtController.setSeite("Nutzeruebersicht.fxml");
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
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY  && seenListeView.getSelectionModel().getSelectedItem() != null) {
                System.out.println(seenListeView.getSelectionModel().getSelectedItem().getName());
                Client.setSelectedFilm(RequestHandler.getMovie(seenListeView.getSelectionModel().getSelectedItem().getFilm_id()));
                FilmansichtController.setSeite("nutzeruebersicht.fxml");
                switchToSceneWithStage("Filmansicht.fxml", "Film Ansicht");
            }
        } catch (IOException e){
            Client.viewSwitchFailed("0001", "Filmansicht");
        }catch(InterruptedException e1){
            Client.viewSwitchFailed("0001", "Filmansicht");
        }
    }



    public void getSelectedFriend(MouseEvent event){

        if(event.getClickCount() ==2 && event.getButton().equals(MouseButton.PRIMARY)) {
            try {
                if (freundesListeView.getSelectionModel().getSelectedItem()!= null) {
                    selectedNutzer = freundesListeView.getSelectionModel().getSelectedItem();
                    Client.setProfilAnsichtNutzer(selectedNutzer);
                    switchToSceneWithStage("profilAnzeigen.fxml", "Profil von " + selectedNutzer.getVorname() + " " + selectedNutzer.getNachname());
                }
            } catch (IOException e) {

            }

        }
    }

    public void bell2Clicked(MouseEvent mouseEvent) throws IOException, InterruptedException{
        System.out.println("clicked2");
        Stage thirdStage = new Stage();

        VBox thirdLayout = new VBox();
        thirdLayout.setAlignment(Pos.CENTER);
        thirdLayout.setSpacing(20);

        ListView<Einladung> listView = new ListView<Einladung>();
        try {
            Einladung[] arrayEinladung = RequestHandler.getEinladungen(Client.getCurrentNutzer().getUserID());
            listView.getItems().addAll(arrayEinladung);
        }catch (Exception e){
            e.printStackTrace();
        }


        ContextMenu menu2 = new ContextMenu();
        MenuItem accept = new MenuItem("Anfrage annehmen");

        accept.setOnAction((event -> {
            try {
                int uid = Client.getCurrentNutzer().getUserID();
                Watchlist list = new Watchlist(uid, listView.getSelectionModel().getSelectedItem().getFilm().getFilm_id());
                if (!RequestHandler.inWatchlist(list)) {
                    RequestHandler.addToWatchlist(list);
                }
                Einladung ein = listView.getSelectionModel().getSelectedItem();
                ein.setAngenommen(true);
                ein.setUser1(RequestHandler.getNutzerByID(ein.getUser1().getUserID()));
                RequestHandler.sendResponseMail(ein);
                RequestHandler.deleteEinladung(ein.getId());
                listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
                watchListe = FXCollections.observableList(Client.getCurrentNutzer().getWatchListe());
                watchListeView.setItems(watchListe);
                watchListeView.setCellFactory(param -> new ListViewCellController());
/*                RequestHandler.addFriend(Client.getCurrentNutzer().getUserID(), listView.getSelectionModel().getSelectedItem().getUserID());
                listView.getItems().remove(listView.getSelectionModel().getSelectedItem());

                getCurrentFriendsliste();
                freundesListeView.setDisable(false);
                freundesListe = FXCollections.observableList(Client.getCurrentNutzer().getFreundesListe());
                freundesListeView.setItems(freundesListe);
                freundesListeView.setDisable(true);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Erfolg");
                alert.setHeaderText("Freundschaftsanfrage angenommen");
                alert.setContentText("");
                alert.showAndWait();*/


                //addAsFriend.setDisable(true);

            }catch (Exception e){
                e.printStackTrace();
            }
        }));

        MenuItem deny = new MenuItem("Anfrage ablehnen");
        deny.setOnAction((event -> {

            try{
                Einladung ein = listView.getSelectionModel().getSelectedItem();
                ein.setUser1(RequestHandler.getNutzerByID(ein.getUser1().getUserID()));
                ein.setAngenommen(false);
                RequestHandler.sendResponseMail(ein);
                RequestHandler.deleteEinladung(ein.getId());
                listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
/*                RequestHandler.deleteFriendRequest(Client.getCurrentNutzer(),listView.getSelectionModel().getSelectedItem());
                listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
                getCurrentFriendsliste();
                freundesListeView.setDisable(false);
                freundesListe = FXCollections.observableList(Client.getCurrentNutzer().getFreundesListe());
                freundesListeView.setItems(freundesListe);
                freundesListeView.setDisable(true);*/



            }catch (Exception e){
                e.printStackTrace();
            }

        }));

        menu2.getItems().add(accept);
        menu2.getItems().add(deny);
        listView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                if(listView.getSelectionModel().getSelectedItem() == null){
                    System.out.println("NULL");

                }
            }
        });

        listView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                if(listView.getSelectionModel().getSelectedItem() != null)
                    menu2.show(listView,event.getScreenX(),event.getScreenY());
            }
        });


        thirdLayout.getChildren().add(listView);

        Scene thirdScene = new Scene(thirdLayout, 260, 150);

        thirdStage.setTitle("Filmeinladungen");
        thirdStage.setScene(thirdScene);
        for(Node n : Client.getCurrentPane().getChildren()){

            n.setDisable(true);
        }

        thirdStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                for(Node n : Client.getCurrentPane().getChildren()){

                    n.setDisable(false);
                }
                getEinladungenListe();
            }
        });
        thirdStage.show();
    }


    public void bellClicked(MouseEvent mouseEvent) throws IOException, InterruptedException{
        System.out.println("clicked");
        Stage secondStage = new Stage();

        VBox secondLayout = new VBox();
        secondLayout.setAlignment(Pos.CENTER);
        secondLayout.setSpacing(20);

        ListView<Nutzer> listView = new ListView<Nutzer>();
        try {
            Nutzer[] arrayNutzer = RequestHandler.getAnfragenListe(Client.getCurrentNutzer().getUserID());
            listView.getItems().addAll(arrayNutzer);
        }catch (Exception e){
            e.printStackTrace();
        }


        ContextMenu menu = new ContextMenu();
        MenuItem addFriend = new MenuItem("Anfrage annehmen");

        addFriend.setOnAction((event -> {
            try {
                RequestHandler.addFriend(Client.getCurrentNutzer().getUserID(), listView.getSelectionModel().getSelectedItem().getUserID());
                listView.getItems().remove(listView.getSelectionModel().getSelectedItem());

                getCurrentFriendsliste();
                freundesListeView.setDisable(false);
                freundesListe = FXCollections.observableList(Client.getCurrentNutzer().getFreundesListe());
                freundesListeView.setItems(freundesListe);
                freundesListeView.setDisable(true);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Erfolg");
                alert.setHeaderText("Freundschaftsanfrage angenommen");
                alert.setContentText("");
                alert.showAndWait();


                //addAsFriend.setDisable(true);

            }catch (Exception e){
                e.printStackTrace();
            }
        }));

        MenuItem deleteFriend = new MenuItem("Anfrage ablehnen");
        deleteFriend.setOnAction((event -> {

            try{
                RequestHandler.deleteFriendRequest(Client.getCurrentNutzer(),listView.getSelectionModel().getSelectedItem());
                listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
                getCurrentFriendsliste();
                freundesListeView.setDisable(false);
                freundesListe = FXCollections.observableList(Client.getCurrentNutzer().getFreundesListe());
                freundesListeView.setItems(freundesListe);
                freundesListeView.setDisable(true);



            }catch (Exception e){
                e.printStackTrace();
            }

        }));

        menu.getItems().add(addFriend);
        menu.getItems().add(deleteFriend);
        listView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                if(listView.getSelectionModel().getSelectedItem() == null){
                    System.out.println("NULL");

                }
            }
        });

        listView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                if(listView.getSelectionModel().getSelectedItem() != null)
                    menu.show(listView,event.getScreenX(),event.getScreenY());
            }
        });


        secondLayout.getChildren().add(listView);

        Scene secondScene = new Scene(secondLayout, 260, 150);

        secondStage.setTitle("Freundschaftsanfragen");
        secondStage.setScene(secondScene);
        for(Node n : Client.getCurrentPane().getChildren()){

            n.setDisable(true);
        }

        secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                for(Node n : Client.getCurrentPane().getChildren()){

                    n.setDisable(false);
                }
                getCurrentFriendsliste();
                freundesListe = FXCollections.observableList(Client.getCurrentNutzer().getFreundesListe());
                freundesListeView.setItems(freundesListe);
                getAnfragenListe();

            }
        });
        secondStage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Init Choice Box

        initChoiceBox();


        // Holt sich vom Server die Freundes-/Watch-/SeenListe

        getCurrentFriendsliste();
        getCurrentWatchListe();
        getCurrentSeenListe();


        getAnfragenListe();
        getEinladungenListe();

        try {
            InputStream stream = getClass().getResourceAsStream("nutzerbild/bell.png");
            Image imageBell = new Image(stream,40,40,false,true);
            bell.setFill(new ImagePattern(imageBell));
            bell2.setFill(new ImagePattern(imageBell));
            stream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Zeigt Profilbild des Benutzers an
        InputStream inputStream = new ByteArrayInputStream(Client.getCurrentNutzer().getProfilbild());
        Image image = new Image(inputStream, 150, 150, false, true);
        profilBild.setFill(new ImagePattern(image));
        profilBildGross.setImage(image);

        // Ändert den Willkommenstext in Hallo, Vorname Nachname
        welcomeMessage.setText("Hallo, " + Client.getCurrentNutzer().getVorname() + " " + Client.getCurrentNutzer().getNachname());


        // Nutzer Informationen anzeigen
        userEmailText.setText(Client.getCurrentNutzer().getEmailAdresse());
        userVornameText.setText(Client.getCurrentNutzer().getVorname());
        userNachnameText.setText(Client.getCurrentNutzer().getNachname());


        // Füllt die Freundesliste

        ArrayList<String> test = new ArrayList<>();

        for (Nutzer nutzer : Client.getCurrentNutzer().getFreundesListe()){
            test.add(nutzer.getVorname() + " " + nutzer.getNachname());
        }

        // Freundesliste

        freundesListe = FXCollections.observableList(Client.getCurrentNutzer().getFreundesListe());
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
        MenuItem chat = new MenuItem("Chat");

        chat.setOnAction((event -> {

            Client.setChatReceiver(freundesListeView.getSelectionModel().getSelectedItem());
            try {
                switchToSceneWithStage("chat.fxml","Chat");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }));

        MenuItem deleteFriend = new MenuItem("Freund entfernen");
        deleteFriend.setOnAction((event -> {

            try {
                RequestHandler.deleteFriend(Client.getCurrentNutzer(),freundesListeView.getSelectionModel().getSelectedItem());
                freundesListeView.getItems().remove(freundesListeView.getSelectionModel().getSelectedItem());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }));

        menu.getItems().add(chat);
        menu.getItems().add(deleteFriend);

        freundesListeView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                if(freundesListeView.getSelectionModel().getSelectedItem() != null)
                    menu.show(freundesListeView,event.getScreenX(),event.getScreenY());
            }
        });



        // Watchliste

        watchListe = FXCollections.observableList(Client.getCurrentNutzer().getWatchListe());
        watchListeView.setItems(watchListe);
        watchListeView.setCellFactory(param -> new ListViewCellController());

        // SeenListe

        seenListe = FXCollections.observableList(Client.getCurrentNutzer().getGesehendeFilmeListe());
        seenListeView.setItems(seenListe);
        seenListeView.setCellFactory(param -> new ListViewCellController());

        freundesListeView.setStyle("-fx-text-box-border: red;-fx-border-color: red;");

    }



    private void getCurrentFriendsliste(){
        Nutzer[] tempFriendlist;
        ArrayList<Nutzer> tempArrayList = new ArrayList<Nutzer>();

        try {
            tempFriendlist = RequestHandler.getFriendListArray(Client.getCurrentNutzer().getUserID());
            for (Nutzer tempNutzer : tempFriendlist){
                tempArrayList.add(tempNutzer);
            }
        } catch (IOException| InterruptedException e){
            Client.connectionLost("0001");
        }

        Client.getCurrentNutzer().setFreundesListe(tempArrayList);
    }

    private void getAnfragenListe(){
        Nutzer[] tempFriendlist = new Nutzer[0];


        try {
            Nutzer[] tempArray = RequestHandler.getAnfragenListe(Client.getCurrentNutzer().getUserID());

            if(tempArray != null){

                tempFriendlist = tempArray;
            }
            System.out.println(tempFriendlist.length == 0);

            if(tempFriendlist.length == 0){

                bell.setDisable(true);
                bell.setVisible(false);

            }else{
                bell.setDisable(false);
                bell.setVisible(true);
            }

        } catch (IOException| InterruptedException e){
            Client.connectionLost("0001");
        }


    }

    private void getEinladungenListe(){
        Einladung[] tempein = new Einladung[0];
        iii=0;

        try {
            Einladung[] tempArray = RequestHandler.getEinladungen(Client.getCurrentNutzer().getUserID());

            if(tempArray != null){

                tempein= tempArray;
                for(Einladung u : tempArray){
                    if(u.getDate().equals(String.valueOf(Client.getCurrentNutzer().getUserID()))){
                        einladungresp.setText(u.getText());
                        iii+=1;
                        RequestHandler.deleteEinladung(u.getId());
                    }
                }
            }else{
                System.out.println("keine einladungen");
            }
           // System.out.println(tempFriendlist.length == 0);

            if(tempein.length == iii){

                bell2.setDisable(true);
                bell2.setVisible(false);

            }else{
                bell2.setDisable(false);
                bell2.setVisible(true);
            }

        } catch (Exception e){
            System.out.println("einladungen nicht geklappt");
        }


    }

    private void getCurrentWatchListe(){
        Film[] tempWatchList;
        ArrayList<Film> tempArrayList = new ArrayList<Film>();
        try {
            tempWatchList = RequestHandler.getWatchListeArray(Client.getCurrentNutzer().getUserID());
            tempArrayList.addAll(Arrays.stream(tempWatchList).toList());
        } catch (IOException| InterruptedException e){
            Client.connectionLost("0001");
        }

        Client.getCurrentNutzer().setWatchListe(tempArrayList);
    }



    private void getCurrentSeenListe(){
        Film[] tempSeenList;
        ArrayList<Film> tempArrayList = new ArrayList<Film>();
        try {
            tempSeenList = RequestHandler.getSeenListeArray(Client.getCurrentNutzer().getUserID());
            tempArrayList.addAll(Arrays.stream(tempSeenList).toList());
        } catch (IOException| InterruptedException e){
            Client.connectionLost("0001");
        }

        Client.getCurrentNutzer().setGesehendeFilmeListe(tempArrayList);
    }

    private void initChoiceBox(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Film");
        arrayList.add("Nutzer");
        arrayList.add("Diskussionen");

        ObservableList<String> observableList = FXCollections.observableList(arrayList);
        choiceBox.setItems(observableList);
        choiceBox.setValue("Film");
        Client.setSearch("Film");
    }

    public void profilBildClicked(MouseEvent event) throws IOException, InterruptedException{
        if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() ==2) {
            Nutzer chosenUser = Client.getCurrentNutzer();
            Client.setProfilAnsichtNutzer(RequestHandler.getNutzerByID(chosenUser.getUserID()));
            switchToSceneWithStage("profilAnzeigen.fxml", "Profil von " + chosenUser.getVorname() + " " + chosenUser.getNachname());
        }
    }

    public void diskussionClicked(){

        try {
            switchToSceneWithStage("diskussionenview.fxml", "Diskussionen");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "platzhalter");
        }
    }
}
