package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.dataclass.Bewertung;
import sep.groupt.server.dataclass.Film;
import sep.groupt.server.dataclass.Nutzer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class BewertungHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if (baseRequest.getMethod().equals("POST") && target.contains("/getBewertungen")) {

            try {
                int tempint = new Gson().fromJson(request.getReader(), Integer.class);
                Bewertung[] tempBewertung = Datenbank.getBewertungen(tempint);
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(tempBewertung, Bewertung[].class));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            baseRequest.setHandled(true);
        }

        if (baseRequest.getMethod().equals("POST") && target.contains("/getBewertungEinzeln")) {

            try {
                Integer tempint[] = new Gson().fromJson(request.getReader(), Integer[].class);
                Bewertung tempBewertung = Datenbank.getBewertung(tempint[0],tempint[1]);
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(tempBewertung, Bewertung.class));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            baseRequest.setHandled(true);
        }




        if (baseRequest.getMethod().equals("POST") && target.contains("/getDurchschnittsBewertung")) {

            try {
                int tempint = new Gson().fromJson(request.getReader(), Integer.class);
                Bewertung[] tempBewertung = Datenbank.getBewertungen(tempint);
                if (tempBewertung.length != 0) {
                    double durchschnitt = 0;
                    int hilfe = 0;
                    for (Bewertung b : tempBewertung) {
                        if (b.getPunkte() > 0) {
                            durchschnitt = durchschnitt + b.getPunkte();
                        } else {
                            hilfe = hilfe + 1;
                        }
                    }
                    int grose = tempBewertung.length - hilfe;
                    durchschnitt = durchschnitt / grose;
                    PrintWriter printWriter = response.getWriter();
                    printWriter.println(new Gson().toJson(durchschnitt, Integer.class));
                } else {
                    double durchschnitt = 100;
                    PrintWriter printWriter = response.getWriter();
                    printWriter.println(new Gson().toJson(durchschnitt, Integer.class));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            baseRequest.setHandled(true);
        }
        if(baseRequest.getMethod().equals("POST") && target.contains("getListOfBewertung")) {


            try {
                int tempNutzer = new Gson().fromJson(request.getReader(), int.class);
                Bewertung[] bewertungl = Datenbank.getBewertung2(tempNutzer);
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(bewertungl, Bewertung[].class));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            baseRequest.setHandled(true);

        }

        if (baseRequest.getMethod().equals("POST") && target.contains("checkBewertung")) {

            try {
                //nutzerid herausfinden
                int id = new Gson().fromJson(request.getReader(), int.class);

                boolean check = Datenbank.checkBewertung(id);
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(check, boolean.class));
                printWriter.close();

                baseRequest.setHandled(true);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


    }
}