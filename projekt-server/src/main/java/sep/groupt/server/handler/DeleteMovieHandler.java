package sep.groupt.server.handler;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class DeleteMovieHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] strings = target.split("/");

        if (baseRequest.getMethod().equals("DELETE") && strings[1].equals("DeleteMovie")){
            try {
                Datenbank.deleteMovie(Integer.parseInt(strings[2]));
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            baseRequest.setHandled(true);
        }
    }
}
