package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.dataclass.FavouriteStats;
import sep.groupt.server.dataclass.StatsNutzer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

public class NutzerStatsHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] string = target.split("/");

        if (request.getMethod().equals("GET") && string[1].equals("NutzerStats")){
            StatsNutzer statsNutzer = new StatsNutzer();

            // Typkonvertierung

            int nutzer_id = Integer.parseInt(string[2]);
            LocalDate datumVonDate = LocalDate.parse(string[3]);
            LocalDate datumBisDate = LocalDate.parse(string[4]);

            Date datumVon = Date.valueOf(datumVonDate);
            Date datumBis = Date.valueOf(datumBisDate);

            // Filmminuten = [geschaute Filmminuten, gesamt geschaute Filmminuten & Top 5 Schauspieler und Kategorie nach
            // Häufigkeiten

            int[] filmMinuten;
            FavouriteStats[] lieblingsSchauspieler;
            FavouriteStats[] lieblingsKategorie;
            String bannerSpeicherURL = "";


            try {
                lieblingsSchauspieler = Datenbank.getNutzerStatsSchauspieler(nutzer_id, datumVon, datumBis);
                lieblingsKategorie = Datenbank.getNutzerStatsKategorie(nutzer_id, datumVon, datumBis);

                statsNutzer.setLieblingsSchauspieler(lieblingsSchauspieler);
                statsNutzer.setLieblingsKategorie(lieblingsKategorie);

                // Finde den Lieblingsfilm heraus

                String[] topSchauspieler = new String[2];
                String[] topKategorie = new String[2];

                if (lieblingsSchauspieler.length >= 2 && lieblingsKategorie.length >= 2){
                    for (int x = 0; x < topSchauspieler.length; x++){
                        if (lieblingsSchauspieler[x] != null){
                            topSchauspieler[x] = lieblingsSchauspieler[x].getKey();
                        }
                        if (lieblingsKategorie[x] != null){
                            topKategorie[x] = lieblingsKategorie[x].getKey();
                        }
                    }

                }

                bannerSpeicherURL = Datenbank.getNutzerStatsFavouriteMovie(nutzer_id, datumVon, datumBis, topSchauspieler, topKategorie);

                if (bannerSpeicherURL.isEmpty()){
                    bannerSpeicherURL = Datenbank.getNutzerStatsFavouriteMovieAlternative(nutzer_id, datumVon, datumBis, topSchauspieler[0], topKategorie[0]);
                }

                if (!bannerSpeicherURL.isEmpty()){
                    File file = new File(bannerSpeicherURL);
                    FileInputStream fileInputStream = new FileInputStream(file);
                    statsNutzer.setLieblingsFilm(fileInputStream.readAllBytes());
                    fileInputStream.close();
                }

                // geschaute Filmminuten

                filmMinuten = Datenbank.getNutzerStatsForWatchedTime(nutzer_id, datumVon, datumBis);
                statsNutzer.setGeschauteFilmMinuten(filmMinuten[0]);
                statsNutzer.setGesamteFilmMinuten(filmMinuten[1]);


                response.setStatus(200);
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }


            PrintWriter printWriter = response.getWriter();
            printWriter.println(new Gson().toJson(statsNutzer, StatsNutzer.class));


            baseRequest.setHandled(true);
        }

    }
}
