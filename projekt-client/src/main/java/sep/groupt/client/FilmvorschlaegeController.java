package sep.groupt.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sep.groupt.client.dataclass.Film;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class FilmvorschlaegeController extends SceneController implements Initializable {

    @FXML
    private Circle profilBild;
    @FXML
    private Text welcomeMessage, userEmailText, userVornameText, userNachnameText;

    @FXML
    private Rectangle einstellungenBox, bell;

    @FXML
    private Rectangle abmeldenBox;

    @FXML
    private TableView<Film> tabelle;
    @FXML
    private TableColumn<Film, ImageView> bannerColumn;
    @FXML
    private TableColumn<Film, String> nameColumn, kategorieColumn, castColumn, dateColumn;
    @FXML
    private Button zurueck;

    int[] kategorien = new int[15];

    Integer[] freunde;
    public int max1 = 1;
    public int max2 = 1;
    public int max3 = 1;

    public void zurueckClicked() throws IOException {
        switchToSceneWithStage("nutzeruebersicht.fxml", "Nutzerübersicht");
    }

    public void abmeldenBoxPressed() {
        try {
            switchToSceneWithStage("login.fxml", "Login");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Login");
        }
    }

    public void einstellungenBoxPressed() {
        try {
            switchToSceneWithStage("editProfile.fxml", "Einstellungen");
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "platzhalter");
        }
    }

    public void maxCategory() {
        for (int i = 0; i < kategorien.length; i++) {
            if (kategorien[max1] <= kategorien[i]) {
                max3 = max2;
                max2 = max1;
                max1 = i;
            }
        }
    }

    public void countCategory(Film[] gesehen) throws IOException, InterruptedException {

        for (Film x : gesehen) {

            if (x.getCategory().contains("Abenteuer") || x.getCategory().contains("Adventure")) {
                kategorien[0] = kategorien[0] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[0] = kategorien[0] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[0] = kategorien[0] - 1;
                }
            }
            if (x.getCategory().contains("Action")) {
                kategorien[1] = kategorien[1] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[1] = kategorien[1] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[1] = kategorien[1] - 1;
                }
            }
            if (x.getCategory().contains("Animation")) {
                kategorien[2] = kategorien[2] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[2] = kategorien[2] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[2] = kategorien[2] - 1;
                }
            }
            if (x.getCategory().contains("Dokumentation") || x.getCategory().contains("Documentation")) {
                kategorien[3] = kategorien[3] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[3] = kategorien[3] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[3] = kategorien[3] - 1;
                }
            }
            if (x.getCategory().contains("Drama")) {
                kategorien[4] = kategorien[4] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[4] = kategorien[4] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[4] = kategorien[4] - 1;
                }
            }
            if (x.getCategory().contains("Sonstiges")) {
                kategorien[5] = kategorien[5] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[5] = kategorien[5] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[5] = kategorien[5] - 1;
                }
            }
            if (x.getCategory().contains("Fantasy")) {
                kategorien[6] = kategorien[6] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[6] = kategorien[6] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[6] = kategorien[6] - 1;
                }
            }
            if (x.getCategory().contains("Horror")) {
                kategorien[7] = kategorien[7] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[7] = kategorien[7] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[7] = kategorien[7] - 1;
                }
            }
            if (x.getCategory().contains("KomÃ¶die") || x.getCategory().contains("Comedy")) {
                kategorien[8] = kategorien[8] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[8] = kategorien[8] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[8] = kategorien[8] - 1;
                }
            }
            if (x.getCategory().contains("Krimi") || x.getCategory().contains("Crime")) {
                kategorien[9] = kategorien[9] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[9] = kategorien[9] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[9] = kategorien[9] - 1;
                }

            }
            if (x.getCategory().contains("Liebesfilm") || x.getCategory().contains("Romance")) {
                kategorien[10] = kategorien[10] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[10] = kategorien[10] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[10] = kategorien[10] - 1;
                }
            }
            if (x.getCategory().contains("Musik") || x.getCategory().contains("Music")) {
                kategorien[11] = kategorien[11] + 1;
            }
            if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                kategorien[11] = kategorien[11] + 1;
            } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                kategorien[11] = kategorien[11] - 1;
            }

            if (x.getCategory().contains("Science-Fiction") || x.getCategory().contains("Sci-Fi")) {
                kategorien[12] = kategorien[12] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[12] = kategorien[12] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[12] = kategorien[12] - 1;
                }
            }
            if (x.getCategory().contains("Thriller")) {
                kategorien[13] = kategorien[13] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[13] = kategorien[13] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[13] = kategorien[13] - 1;
                }
            }

            if (x.getCategory().contains("Western")) {
                kategorien[14] = kategorien[14] + 1;
                if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 3) {
                    kategorien[14] = kategorien[14] + 1;
                } else if (RequestHandler.getDurchnittsBewertung(x.getFilm_id()) < 3 && RequestHandler.getDurchnittsBewertung(x.getFilm_id()) > 0) {
                    kategorien[14] = kategorien[14] - 1;
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream inputStream = new ByteArrayInputStream(Client.getCurrentNutzer().getProfilbild());
        Image image1 = new Image(inputStream, 150, 150, false, true);
        profilBild.setFill(new ImagePattern(image1));

        // Ändert den Willkommenstext in Hallo, Vorname Nachname
        welcomeMessage.setText("Hallo, " + Client.getCurrentNutzer().getVorname() + " " + Client.getCurrentNutzer().getNachname());


    }

    public String getKategorien() {
        String SelectedChoices = "";

        if (max1 == 0 || max2 == 0 || max3 == 0) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%Abenteuer%' OR FilmKategorie LIKE '%Adventure%') ";

        }
        if (max1 == 1 || max2 == 1 || max3 == 1) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Action%' ";

        }
        if (max1 == 2 || max2 == 2 || max3 == 2) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Animation%' ";
        }
        if (max1 == 3 || max2 == 3 || max3 == 3) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%Dokumentation%' OR FilmKategorie LIKE '%Documentation%') ";

        }
        if (max1 == 4 || max2 == 4 || max3 == 4) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Drama%' ";

        }
        if (max1 == 5 || max2 == 5 || max3 == 5) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Sonstiges%' ";
        }
        if (max1 == 6 || max2 == 6 || max3 == 6) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Fantasy%' ";

        }
        if (max1 == 7 || max2 == 7 || max3 == 7) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Horror%' ";

        }
        if (max1 == 8 || max2 == 8 || max3 == 8) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%KomÃ¶die%' OR FilmKategorie LIKE '%Comedy%') ";

        }
        if (max1 == 9 || max2 == 9 || max3 == 9) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%Krimi%' OR FilmKategorie LIKE '%Crime%') ";

        }
        if (max1 == 10 || max2 == 10 || max3 == 10) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%Liebesfilm%' OR FilmKategorie LIKE '%Romance%') ";

        }
        if (max1 == 11 || max2 == 11 || max3 == 11) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%Musik%' OR FilmKategorie LIKE '%Music%') ";
        }
        if (max1 == 12 || max2 == 12 || max3 == 12) {
            SelectedChoices = SelectedChoices + "AND (FilmKategorie LIKE '%Science-Fiction%' OR FilmKategorie LIKE '%Sci-Fi%') ";

        }
        if (max1 == 13 || max2 == 13 || max3 == 13) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Thriller%' ";
        }
        if (max1 == 14 || max2 == 14 || max3 == 14) {
            SelectedChoices = SelectedChoices + "AND FilmKategorie LIKE '%Western%' ";
        }
        return SelectedChoices;
    }


    public void ichClicked() throws IOException, InterruptedException {
        tabelle.getItems().clear();
        Arrays.fill(kategorien, 0);

        Film[] gesehen;
        try {
            gesehen = RequestHandler.getLatestSL(Client.getCurrentNutzer().getUserID());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (gesehen != null) {
            countCategory(gesehen);
            maxCategory();
            System.out.println(max1 + "," + max2 + "," + max3);
            Film[] filmListe = null;
            try {
                System.out.println(getKategorien());
                filmListe = RequestHandler.getFilmvorschlaege(getKategorien());
                //System.out.println(filmListe[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ArrayList<Film> arrayList = new ArrayList<>();

            for (Film x : filmListe) {
                arrayList.add(x);
            }
            if (arrayList.size() < 15) {
                int i = arrayList.size();
                int e = i;
                boolean vorhanden = false;
                Film[] filme = null;
                max3 = max2;
                filme = RequestHandler.getFilmvorschlaege(getKategorien());
                for (Film x : filme) {
                    if (i < 15) {

                        for (int q = 0; q < e; q++) {
                            if (x.getFilm_id() == arrayList.get(q).getFilm_id()) {
                                vorhanden = true;
                            }
                        }
                        if (vorhanden == false) {
                            arrayList.add(x);
                        }
                        vorhanden = false;
                        i++;

                    } else {
                        break;
                    }

                }

            }
            if (arrayList.size() < 15) {
                int i = arrayList.size();
                int e = i;
                boolean vorhanden = false;
                Film[] filme2 = null;
                max3 = max1;
                max2 = max1;
                filme2 = RequestHandler.getFilmvorschlaege(getKategorien());
                for (Film x : filme2) {
                    if (i < 15) {
                        for (int q = 0; q < e; q++) {
                            if (x.getFilm_id() == arrayList.get(q).getFilm_id()) {
                                vorhanden = true;
                            }
                        }
                        if (vorhanden == false) {
                            arrayList.add(x);
                        }
                        vorhanden = false;
                        i++;
                    } else {
                        break;
                    }
                }
            }
            if (arrayList.size() < 15) {
                int i = arrayList.size();
                int e = i;
                boolean vorhanden = false;
                Film[] filme2 = null;
                max3 = max1;
                max2 = max1;
                filme2 = RequestHandler.getFilmvorschlaege("");
                for (Film x : filme2) {
                    if (i < 15) {
                        for (int q = 0; q < e; q++) {
                            if (x.getFilm_id() == arrayList.get(q).getFilm_id()) {
                                vorhanden = true;
                            }
                        }
                        if (vorhanden == false) {
                            arrayList.add(x);
                        }
                        vorhanden = false;
                        i++;
                    } else {
                        break;
                    }
                }
            }
            ObservableList<Film> list = FXCollections.observableList(arrayList);

            tabelle.setItems(list);

            nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            kategorieColumn.setCellValueFactory(cellData -> cellData.getValue().getKategorieProperty());
            castColumn.setCellValueFactory(cellData -> cellData.getValue().getCastProperty());
            dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDatumProperty());

        } else {
            Film[] filmListe2 = null;
            try {

                filmListe2 = RequestHandler.getFilmvorschlaege("");
                //System.out.println(filmListe[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ArrayList<Film> arrayList = new ArrayList<>();


            for (Film x : filmListe2) {
                arrayList.add(x);
            }
            ObservableList<Film> list = FXCollections.observableList(arrayList);


            tabelle.setItems(list);
            nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            kategorieColumn.setCellValueFactory(cellData -> cellData.getValue().getKategorieProperty());
            castColumn.setCellValueFactory(cellData -> cellData.getValue().getCastProperty());
            dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDatumProperty());
        }


        for (int x : kategorien) {
           x = 0;
        }


    }


    public void freundClicked() throws IOException, InterruptedException {
        tabelle.getItems().clear();
        Arrays.fill(kategorien, 0);

        freunde = RequestHandler.getFreundeID(Client.getCurrentNutzer().getUserID());
        if (freunde != null) {
            for (int i = 0; i < freunde.length; i++) {
                Film[] gesehen;
                try {
                    gesehen = RequestHandler.getLatestSL(freunde[i]);
                } catch (IOException e) {
                    System.out.println("Freunde Id failed");
                    throw new RuntimeException(e);

                } catch (InterruptedException e) {
                    System.out.println("Freunde Id failed");
                    throw new RuntimeException(e);
                }
                if (gesehen != null) {
                    countCategory(gesehen);
                } else {
                    int h = (int) ((Math.random()) * 14);
                    kategorien[h] = kategorien[h] + 1;
                }
            }
            maxCategory();
            Film[] filmListe = null;
            try {
                filmListe = RequestHandler.getFilmvorschlaege(getKategorien());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ArrayList<Film> arrayList = new ArrayList<>();

            for (Film x : filmListe) {
                arrayList.add(x);
            }
            if (arrayList.size() < 15) {
                int i = arrayList.size();
                int e = i;
                boolean vorhanden = false;
                Film[] filme = null;
                max3 = max2;
                filme = RequestHandler.getFilmvorschlaege(getKategorien());
                for (Film x : filme) {
                    if (i < 15) {

                        for (int q = 0; q < e; q++) {
                            if (x.getFilm_id() == arrayList.get(q).getFilm_id()) {
                                vorhanden = true;
                            }
                        }
                        if (vorhanden == false) {
                            arrayList.add(x);
                        }
                        vorhanden = false;
                        i++;

                    } else {
                        break;
                    }

                }

            }
            if (arrayList.size() < 15) {
                int i = arrayList.size();
                int e = i;
                boolean vorhanden = false;
                Film[] filme2 = null;
                max3 = max1;
                max2 = max1;
                filme2 = RequestHandler.getFilmvorschlaege(getKategorien());
                for (Film x : filme2) {
                    if (i < 15) {
                        for (int q = 0; q < e; q++) {
                            if (x.getFilm_id() == arrayList.get(q).getFilm_id()) {
                                vorhanden = true;
                            }
                        }
                        if (vorhanden == false) {
                            arrayList.add(x);
                        }
                        vorhanden = false;
                        i++;
                    } else {
                        break;
                    }
                }
            }
            if (arrayList.size() < 15) {
                int i = arrayList.size();
                int e = i;
                boolean vorhanden = false;
                Film[] filme2 = null;
                max3 = max1;
                max2 = max1;
                filme2 = RequestHandler.getFilmvorschlaege("");
                for (Film x : filme2) {
                    if (i < 15) {
                        for (int q = 0; q < e; q++) {
                            if (x.getFilm_id() == arrayList.get(q).getFilm_id()) {
                                vorhanden = true;
                            }
                        }
                        if (vorhanden == false) {
                            arrayList.add(x);
                        }
                        vorhanden = false;
                        i++;
                    } else {
                        break;
                    }
                }
            }
            ObservableList<Film> list = FXCollections.observableList(arrayList);
            tabelle.setItems(list);
            //bannerColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            kategorieColumn.setCellValueFactory(cellData -> cellData.getValue().getKategorieProperty());
            castColumn.setCellValueFactory(cellData -> cellData.getValue().getCastProperty());
            dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDatumProperty());
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Keine Freunde!");
            a.showAndWait();
            tabelle.getItems().clear();
        }
        for (int x : kategorien) {
            x = 0;
        }
    }

    public void pressed(MouseEvent event) {
        try {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY && tabelle.getSelectionModel().getSelectedItem() != null) {
                System.out.println(tabelle.getSelectionModel().getSelectedItem().getName());
                Client.setSelectedFilm(RequestHandler.getMovie(tabelle.getSelectionModel().getSelectedItem().getFilm_id()));
                FilmansichtController.setSeite("Suche.fxml");
                switchToSceneWithStage("Filmansicht.fxml", "Film Ansicht");
            }
        } catch (IOException e) {
            Client.viewSwitchFailed("0001", "Filmansicht");
        } catch (InterruptedException e1) {
            Client.viewSwitchFailed("0001", "Filmansicht");
        }
    }
}
