package sep.groupt.client;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sep.groupt.client.chat.Chat;
import sep.groupt.client.chat.ChatMessage;
import sep.groupt.client.dataclass.Nutzer;
import sep.groupt.client.dataclass.PeriodicPulse;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ChatController extends SceneController implements Initializable {


    private Nutzer receiver;
    private Nutzer sender;
    private PeriodicPulse pulse;
    @FXML
    TextArea messageField,textArea;

    //@FXML
    //ScrollPane messagesPane;

    @FXML
    Button sendButton;

    public ChatController() throws IOException, InterruptedException {


    }

    public void backClicked() throws IOException {
        pulse.stop();
        switchToSceneWithStage("nutzeruebersicht.fxml", "Nutzerübersicht");

    }

    public void sendButtonClicked() throws IOException, InterruptedException {

        if(!messageField.getText().equals("")){


            ChatMessage message = new ChatMessage(sender,receiver, LocalDateTime.now().toString(),messageField.getText());
            RequestHandler.sendMessage(message);
            System.out.println("SENT");
            try {
                textArea.setText("");
                Chat chat = RequestHandler.getChat(Client.getCurrentNutzer(), Client.getChatReceiver());
                for(ChatMessage m : chat.getMessages()){

                    textArea.setText(textArea.getText()+"\n"+m.Nachricht());
                }
                messageField.setText("");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sender = Client.getCurrentNutzer();
        sender = new Nutzer(sender.getUserID(), sender.getEmailAdresse(), sender.getVorname(), sender.getNachname(),sender.getPasswort());

        receiver = Client.getChatReceiver();
        receiver = new Nutzer(receiver.getUserID(), receiver.getEmailAdresse(), receiver.getVorname(), receiver.getNachname(),receiver.getPasswort());

        try {
            Chat chat = RequestHandler.getChat(sender, receiver);
            for(ChatMessage m : chat.getMessages()){

                textArea.setText(textArea.getText()+"\n"+m.Nachricht());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

         pulse = new PeriodicPulse(1) {
            @Override
            public void run(){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            textArea.setText("");
                            Chat chat = RequestHandler.getChat(Client.getCurrentNutzer(), Client.getChatReceiver());
                            for(ChatMessage m : chat.getMessages()){

                                textArea.setText(textArea.getText()+"\n"+m.Nachricht());
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

            }
        };
        pulse.start();

    }




}
