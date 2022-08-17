package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.dataclass.Film;
import sep.groupt.server.dataclass.Nutzer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ProfilbildHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (baseRequest.getMethod().equals("POST") && target.equals("/profilbildNutzer")){

            int id = new Gson().fromJson(request.getReader(),Integer.class);
            byte[] bild;
            try {
                Nutzer nutzer = Datenbank.getUserByID(id);
                FileInputStream fileInputStream = new FileInputStream(nutzer.getProfilbildURL());
                 bild = fileInputStream.readAllBytes();

                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(bild, byte[].class));
                printWriter.close();
                response.setStatus(HttpServletResponse.SC_OK);


            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new RuntimeException(e);
            }


            baseRequest.setHandled(true);
        }

        if (baseRequest.getMethod().equals("POST") && target.equals("/nutzerByID")){

            int id = new Gson().fromJson(request.getReader(),Integer.class);

            try {
                Nutzer nutzer = Datenbank.getUserByID(id);


                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(nutzer, Nutzer.class));
                printWriter.close();
                response.setStatus(HttpServletResponse.SC_OK);


            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new RuntimeException(e);
            }


            baseRequest.setHandled(true);
        }
    }
}
