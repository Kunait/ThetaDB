package sep.groupt.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import sep.groupt.client.dataclass.Film;

import java.io.*;

public class ListViewCellController extends ListCell<Film> {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Text titelName;

    @FXML
    private ImageView titelBanner;

    private FXMLLoader fxmlLoader = null;

    private static ByteArrayInputStream byteArrayInputStream;



    private static Image getImage(byte[] imageByte) {
        byteArrayInputStream = new ByteArrayInputStream(imageByte);
        Image image = new Image(byteArrayInputStream, 150, 225, false, true);
        try {
            byteArrayInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    @Override
    protected void updateItem(Film film, boolean empty) {
        super.updateItem(film, empty);
        if(empty || film == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("CustomFilmListCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (film.getName().length() >= 20){
                titelName.setText(film.getName().substring(0, 18) + "...");
            }
            else {
                titelName.setText(film.getName());
            }

            titelBanner.setImage(getImage(film.getBanner()));

            setText(null);
            setGraphic(anchorPane);
        }

    }

}
