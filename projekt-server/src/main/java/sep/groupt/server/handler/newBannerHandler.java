package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.dataclass.Film;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class newBannerHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(baseRequest.getMethod().equals("POST") && target.equals("/banner")) {

            Film film = new Gson().fromJson(baseRequest.getReader(), Film.class);

            try {
                if (Datenbank.checkMoviebyID(film.getFilm_id())){
                    FileOutputStream fos = new FileOutputStream("banner/" + film.getFilm_id() + ".jpg");
                    fos.write(film.getBanner());
                    fos.close();
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
