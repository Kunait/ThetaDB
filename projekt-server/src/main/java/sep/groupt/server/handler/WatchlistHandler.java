package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.dataclass.Film;
import sep.groupt.server.dataclass.Watchlist;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class WatchlistHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (baseRequest.getMethod().equals("POST") && target.contains("addWatchlist")) {
            Watchlist tempList = new Gson().fromJson(request.getReader(), Watchlist.class);
            try {
                boolean checkList = Datenbank.checkWatchlist(tempList);
                boolean checkList2 = Datenbank.checkSeenlist(tempList);
                if (!checkList && !checkList2) {
                    response.setStatus(200);
                    Datenbank.addWatchlist(tempList);
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            baseRequest.setHandled(true);

        }

        if(baseRequest.getMethod().equals("POST") && target.contains("getWatchList")) {

            try {
                //nutzerid herausfinden
                int tempNutzer = new Gson().fromJson(request.getReader(), int.class);
                //String nutzerid=Datenbank.getNutzerID(tempNutzer);
                // String nutzerid =Datenbank.getNutzerID(tempNutzer);
                Film[] watchlist = Datenbank.getMoviesWl(String.valueOf(tempNutzer));
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(watchlist, Watchlist[].class));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            baseRequest.setHandled(true);

        }

        if(baseRequest.getMethod().equals("POST") && target.contains("deleteWatchList")) {


            Watchlist tempWatchList = new Gson().fromJson(request.getReader(), Watchlist.class);
            try {
                if(Datenbank.inWatchlist(tempWatchList.getUserID(), tempWatchList.getFilmID()))
                {

                    Datenbank.deleteWatchList(tempWatchList);
                    System.out.println("DELETE CONFIRMED");
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

        if(baseRequest.getMethod().equals("POST") && target.contains("getIDofWatchList")) {
            int[] data = new int[2];
            data = new Gson().fromJson(request.getReader(),int[].class);
            try {
                int wlid = Datenbank.getWl(data[0], data[1]);
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(wlid, int.class));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            baseRequest.setHandled(true);

        }

        if (baseRequest.getMethod().equals("POST") && target.contains("inWatchlist")) {
            Watchlist tempList = new Gson().fromJson(request.getReader(), Watchlist.class);
            try {
                boolean checkList = Datenbank.inWatchlist(tempList.getFilmID(), tempList.getUserID());

                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(checkList, boolean.class));
                printWriter.close();


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            baseRequest.setHandled(true);

        }



    }
}
