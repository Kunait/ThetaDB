package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.ServerClass;
import sep.groupt.server.dataclass.Nutzer;
import sep.groupt.server.dataclass.Systemadministrator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class UserSettingsHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(baseRequest.getMethod().equals("POST") && target.equalsIgnoreCase("/changeSettings")) {
            Nutzer nutzerToRegister = new Gson().fromJson(request.getReader(), Nutzer.class);

            try {
                Nutzer newNutzer = new Nutzer(nutzerToRegister.getEmailAdresse(), nutzerToRegister.getVorname(), nutzerToRegister.getNachname(), nutzerToRegister.getSalt(), nutzerToRegister.getPasswort());
                newNutzer.setEinstellungen(nutzerToRegister.getEinstellungen());
                Datenbank.changeNutzer(newNutzer);
            } catch (SQLException e) {
                System.out.println("Server konnte Einstellungen nicht ändern.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            baseRequest.setHandled(true);
        }
    }
}
