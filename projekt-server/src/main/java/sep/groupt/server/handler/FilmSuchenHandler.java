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

public class FilmSuchenHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(baseRequest.getMethod().equals("POST") && target.equals("/filter")) {
            String[] data;
            data = new Gson().fromJson(request.getReader(), String[].class);

            Film[] filme;
            try {
                filme = Datenbank.getMoviesFilter(data[0],data[1],data[2], data[3], data[4]);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            PrintWriter printWriter = response.getWriter();
            printWriter.println(new Gson().toJson(filme, Film[].class));
            printWriter.close();

            baseRequest.setHandled(true);
        }



    }
}
