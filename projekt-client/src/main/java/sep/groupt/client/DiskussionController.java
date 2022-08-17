package sep.groupt.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import sep.groupt.client.chat.ChatMessage;
import sep.groupt.client.dataclass.DiskussionInhalt;
import sep.groupt.client.dataclass.PeriodicPulse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DiskussionController extends SceneController implements Initializable {


    private PeriodicPulse pulse;
    @FXML
    TextArea messageField, textArea;

    //@FXML
    //ScrollPane messagesPane;

    @FXML
    Button sendButton;

    @FXML
    CheckBox privat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            DiskussionInhalt inhalt = RequestHandler.getDiskussionInhalt(Client.getSelectedGruppe());
            privat.setSelected(Client.getSelectedGruppe().isPrivat());
            if (Client.getSelectedGruppe().getNutzer().getUserID() == Client.getCurrentNutzer().getUserID()) {
                privat.setVisible(true);
            } else {
                privat.setVisible(false);
            }
            for (String m : inhalt.getNachrichten()) {

                textArea.setText(textArea.getText() + "\n" + m);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pulse = new PeriodicPulse(1) {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            textArea.setText("");
                            DiskussionInhalt inhalt = RequestHandler.getDiskussionInhalt(Client.getSelectedGruppe());
                            for (String m : inhalt.getNachrichten()) {

                                textArea.setText(textArea.getText() + "\n" + m);
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

    public void backClicked() throws IOException {

        switchToSceneWithStage("nutzeruebersicht.fxml", "Nutzerübersicht");

    }

    public void sendButtonClicked(ActionEvent event) throws IOException, InterruptedException {
        if (!messageField.getText().equals("")) {

            ChatMessage message = new ChatMessage(Integer.toString(Client.getSelectedGruppe().getId()), Client.getCurrentNutzer().getVorname() + " " + Client.getCurrentNutzer().getNachname() + ":" + messageField.getText());
            RequestHandler.sendDiskussion(message);
            DiskussionInhalt inhalt = RequestHandler.getDiskussionInhalt(Client.getSelectedGruppe());
            textArea.setText("");
            for (String m : inhalt.getNachrichten()) {

                textArea.setText(textArea.getText() + "\n" + m);
            }
            messageField.setText("");

        }

    }

    public void privatClicked(ActionEvent event) throws IOException, InterruptedException {

        if(privat.isVisible()){

            if(privat.isSelected()){


                RequestHandler.setPrivat(Client.getSelectedGruppe());
            }else{


                RequestHandler.setOpen(Client.getSelectedGruppe());
            }




        }


    }

}
