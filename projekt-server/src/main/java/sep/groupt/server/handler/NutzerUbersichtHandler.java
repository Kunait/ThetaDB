package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.dataclass.Film;
import sep.groupt.server.dataclass.Nutzer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class NutzerUbersichtHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if (baseRequest.getMethod().equals("POST") && target.equals("/nutzerubersicht/Freunde")){
            int tempNutzer_id = new Gson().fromJson(request.getReader(), Integer.class);

            Nutzer[] ergebnis;

            try {
                ergebnis = Datenbank.getFreundeslisteForUbersicht(tempNutzer_id);

                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(ergebnis, Nutzer[].class));
                printWriter.close();

                response.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            baseRequest.setHandled(true);
        }

        if (baseRequest.getMethod().equals("POST") && target.equals("/nutzerubersicht/Watchliste")){
            int tempNutzer_id = new Gson().fromJson(request.getReader(), Integer.class);

            Film[] ergebnis;

            try {
                ergebnis = Datenbank.getWatchListeForUbersicht(tempNutzer_id);

                for (Film tempFilm : ergebnis){
                    File filmBanner = new File("banner/" + tempFilm.getFilm_id() + ".jpg");
                    FileInputStream fileInputStream = new FileInputStream(filmBanner);
                    byte[] filmBannerByte = fileInputStream.readAllBytes();
                    fileInputStream.close();
                    tempFilm.setBanner(filmBannerByte);
                }

                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(ergebnis, Film[].class));
                printWriter.close();
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            baseRequest.setHandled(true);
        }

        if (baseRequest.getMethod().equals("POST") && target.equals("/nutzerubersicht/SeenListe")){
            int tempNutzer_id = new Gson().fromJson(request.getReader(), Integer.class);

            Film[] ergebnis;

            try {
                ergebnis = Datenbank.getSeenListeForUbersicht(tempNutzer_id);

                for (Film tempFilm : ergebnis){
                    File filmBanner = new File("banner/" + tempFilm.getFilm_id() + ".jpg");
                    FileInputStream fileInputStream = new FileInputStream(filmBanner);
                    byte[] filmBannerByte = fileInputStream.readAllBytes();
                    fileInputStream.close();
                    tempFilm.setBanner(filmBannerByte);
                }

                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(ergebnis, Film[].class));
                printWriter.close();
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }



            baseRequest.setHandled(true);
        }

        if (baseRequest.getMethod().equals("POST") && target.equals("/nutzerubersicht/filterUser")){


            try{
            String filter = new Gson().fromJson(request.getReader(),String.class);


            Nutzer[] nutzer = Datenbank.nutzerSearch(filter);
            boolean isNull = (nutzer == null);
                System.out.println(isNull);

            PrintWriter printWriter = response.getWriter();
            printWriter.println(new Gson().toJson(nutzer, Nutzer[].class));
            printWriter.close();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

            baseRequest.setHandled(true);
        }
    }
}
