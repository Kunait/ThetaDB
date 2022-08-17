package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.dataclass.Bewertung;
import sep.groupt.server.dataclass.Film;
import sep.groupt.server.dataclass.Watchlist;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class SeenListHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(baseRequest.getMethod().equals("POST") && target.contains("getSeenList")) {

            try {
                //nutzerid herausfinden
                int tempNutzer = new Gson().fromJson(request.getReader(), int.class);

                Film[] seenl = Datenbank.getMoviesSl(String.valueOf(tempNutzer));
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(seenl, Film[].class));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            baseRequest.setHandled(true);

        }



        if (baseRequest.getMethod().equals("POST") && target.contains("sendSeenList")) {
            int[] data = new int[2];
            data = new Gson().fromJson(request.getReader(),int[].class);

           // SeenList tempseen = new Gson().fromJson(request.getReader(), SeenList.class);
            try {
                Datenbank.addSeenList(data[0], data[1]);
                if(Datenbank.checkSeenlist(data[1],data[0]))
                {
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else{
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            baseRequest.setHandled(true);

        }

        if (baseRequest.getMethod().equals("POST") && target.contains("sendBewertung"))
        {
            Bewertung tempbert =  new Gson().fromJson(request.getReader(), Bewertung.class);
            try {
                Datenbank.addBewertung(tempbert);
                if(Datenbank.checkBewertungUser(tempbert.getUserID(),tempbert.getFilmID()))
                {
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            baseRequest.setHandled(true);
        }

        if (baseRequest.getMethod().equals("POST") && target.contains("deleteBewertung"))
        {
            Bewertung tempbert =  new Gson().fromJson(request.getReader(), Bewertung.class);
            try {
                Datenbank.deleteBewertung(tempbert);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            baseRequest.setHandled(true);
        }

        if (baseRequest.getMethod().equals("POST") && target.contains("updateBewertung"))
        {
            Bewertung tempbert =  new Gson().fromJson(request.getReader(), Bewertung.class);
            try {
                Datenbank.updateBewertung(tempbert);
                if(Datenbank.checkBewertungUpdate(tempbert.getPunkte(),tempbert.getBewertung()))
                {
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }

            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            baseRequest.setHandled(true);
        }

        if (baseRequest.getMethod().equals("POST") && target.contains("checkSeenlist")) {
            Watchlist tempList = new Gson().fromJson(request.getReader(), Watchlist.class);
            try {
                boolean checkList = Datenbank.checkSeenlist(tempList.getFilmID(), tempList.getUserID());

                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(checkList, boolean.class));
                printWriter.close();


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            baseRequest.setHandled(true);

        }
        if (baseRequest.getMethod().equals("POST") && target.contains("deleteSeen"))
        {

            int data = new Gson().fromJson(request.getReader(),int.class);

            try {
                Datenbank.deleteSeenList(data);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            baseRequest.setHandled(true);
        }

        if(baseRequest.getMethod().equals("POST") && target.contains("getIDofSeenlist")) {

            int[] data = new int[2];
            data = new Gson().fromJson(request.getReader(),int[].class);
            try {

                int result = Datenbank.getSeenlistID(data[0],data[1]);
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(result, int.class));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            baseRequest.setHandled(true);

        }




    }



}
