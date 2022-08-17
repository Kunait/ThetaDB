package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.dataclass.Film;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;


public class getBannerHandler extends AbstractHandler{
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if (baseRequest.getMethod().equals("POST") && target.equals("/getBanner")){
            Film film = new Gson().fromJson(request.getReader(), Film.class);
            File filmBanner = new File("banner/" + film.getFilm_id() + ".jpg");

            if(filmBanner.exists()){
                FileInputStream fileInputStream = new FileInputStream(filmBanner);
                byte[] filmBannerByte = fileInputStream.readAllBytes();
                fileInputStream.close();

                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(filmBannerByte, byte[].class));
                printWriter.close();

                response.setStatus(HttpServletResponse.SC_OK);

            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            baseRequest.setHandled(true);
        }
    }
}

