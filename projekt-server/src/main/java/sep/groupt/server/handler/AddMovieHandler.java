package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.dataclass.Film;
import sep.groupt.server.Datenbank;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class AddMovieHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        {
            if(baseRequest.getMethod().equals("POST") && target.contains("addMovie")) {
                Film tempFilm = new Gson().fromJson(request.getReader(), Film.class);

                // Fügt Film in die Datenbank hinzu ohne BannerURL
                try {
                    if (!Datenbank.checkMovie(tempFilm)){
                        System.out.println("Film adden");
                        Datenbank.addMovie(tempFilm);

                        // Erhalte von der Datenbank die Filmid und speicher das Bild unter banner/filmid.jpg ab
                        // Trage in die Datenbank die URL ein

                        int filmid = Datenbank.getFilm_id(tempFilm);
                        File file = new File ("banner/" + filmid + ".jpg");
                        FileOutputStream fos = new FileOutputStream("banner/" + filmid + ".jpg");
                        fos.write(tempFilm.getBanner());
                        fos.close();
                        Datenbank.setBannerForMovie(file.getAbsolutePath().replaceAll("\\\\", "/"), filmid);
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
}
