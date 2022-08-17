package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.dataclass.StatistikAdmin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class StatistikAdminHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (baseRequest.getMethod().equals("POST") && target.contains("getStatistikAdmin")) {
            int fid = new Gson().fromJson(request.getReader(), int.class);
            try {
                StatistikAdmin statistikAdmin = Datenbank.getStats(fid);
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(statistikAdmin, StatistikAdmin.class));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            baseRequest.setHandled(true);
        }

        if (baseRequest.getMethod().equals("POST") && target.contains("sendOneStat")){
            StatistikAdmin statistikAdmin = new Gson().fromJson(request.getReader(), StatistikAdmin.class);
            try {
                Datenbank.addGesehenAdmin(statistikAdmin);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (baseRequest.getMethod().equals("POST") && target.contains("sendFullStat")){
            StatistikAdmin statistikAdmin = new Gson().fromJson(request.getReader(), StatistikAdmin.class);
            try {
                Datenbank.addFullStatsAdmin(statistikAdmin);
                if(Datenbank.getStats(statistikAdmin.getFilmid())!=null)
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

        if (baseRequest.getMethod().equals("POST") && target.contains("deleteStat")){
            int fid = new Gson().fromJson(request.getReader(), int.class);
            try {
                if(Datenbank.inStatsAdmin(fid)) {
                    Datenbank.deleteStatsBewertung(fid);
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
    }
}
