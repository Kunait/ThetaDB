package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.dataclass.Film;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class FilmvorschlaegeHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (baseRequest.getMethod().equals("POST") && target.equals("/filmvorschlaege")) {
            String data;
            data = new Gson().fromJson(request.getReader(), String.class);

            Film[] filme;
            try {
                filme = Datenbank.getFilmvorschläge(data);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            PrintWriter printWriter = response.getWriter();
            printWriter.println(new Gson().toJson(filme, Film[].class));
            printWriter.close();

            baseRequest.setHandled(true);
        }


        if (baseRequest.getMethod().equals("POST") && target.contains("getLatestSL")) {

            try {
                //nutzerid herausfinden
                int tempNutzer = new Gson().fromJson(request.getReader(), int.class);

                Film[] seenl = Datenbank.getLatestSL(String.valueOf(tempNutzer));
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(seenl, Film[].class));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            baseRequest.setHandled(true);

        }

        if (baseRequest.getMethod().equals("POST") && target.equals("/getFreundeID")){
            int tempNutzer_id = new Gson().fromJson(request.getReader(), Integer.class);

            Integer[] ergebnis;

            try {
                ergebnis = Datenbank.getFreundeID(tempNutzer_id);

                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(ergebnis, Integer[].class));
                printWriter.close();

                response.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            baseRequest.setHandled(true);
        }

    }
}
