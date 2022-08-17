package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.dataclass.Film;
import sep.groupt.server.Datenbank;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class EditMovieHandler extends AbstractHandler {

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if(baseRequest.getMethod().equals("POST") && target.equals("/editMovie")){
            Film tempFilm = new Gson().fromJson(request.getReader(), Film.class);

            try {
                boolean condition = Datenbank.checkMoviebyID(tempFilm.getFilm_id());
                if (condition){
                    Datenbank.editMovie(tempFilm);
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
    }
}
