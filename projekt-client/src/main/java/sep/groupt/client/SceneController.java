package sep.groupt.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public abstract class SceneController {

    /*Quelle: Java Arbeitspaket GUI-Aufgabe
    Wechselt von einer Scene zur nächsten Scene mit oder ohne Stage
     */

    /*public void switchToScene(String fxmlPath){
        try {
            Scene newScene = FXMLLoader.load(getClass().getResource(fxmlPath));
            Client.getCurrentStage().setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    //Neue Scene wird erstellt und eine neue FXML Datei wird geladen -> neue GUI
    public void switchToSceneWithStage(String fxmlPath, String title) throws IOException {
        try {
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource(fxmlPath)));
            Client.getCurrentStage().setScene(newScene);
            Client.getCurrentStage().setTitle(title);
            Client.setCurrentPane((AnchorPane) newScene.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
