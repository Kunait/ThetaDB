package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.ServerClass;
import sep.groupt.server.dataclass.Nutzer;
import sep.groupt.server.dataclass.Systemadministrator;
import sep.groupt.server.Datenbank;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class RegisterHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(baseRequest.getMethod().equals("POST") && target.equals("/registerAdmin")) {
            Systemadministrator tempAdmin = new Gson().fromJson(request.getReader(), Systemadministrator.class);

            try {
                if (!Datenbank.checkAdmin(tempAdmin)){
                    Datenbank.addAdmin(tempAdmin);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } catch (SQLException e) {
                System.out.println("Server konnte Admin nicht in die Datenbank eintragen!");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            baseRequest.setHandled(true);
        }

        if(baseRequest.getMethod().equals("POST") && target.equalsIgnoreCase("/registerNutzer")) {
            Nutzer nutzerToRegister = new Gson().fromJson(request.getReader(), Nutzer.class);

            try {
                if (!Datenbank.checkNutzer(nutzerToRegister)){ // Wenn Nutzer in der Datenbank nicht vorhanden ist

                    // Nutzer in die Datenbank eintragen
                    Nutzer newNutzer = new Nutzer(nutzerToRegister.getEmailAdresse(), nutzerToRegister.getVorname(), nutzerToRegister.getNachname(), nutzerToRegister.getSalt(), nutzerToRegister.getPasswort());
                    newNutzer.setEinstellungen(nutzerToRegister.getEinstellungen());
                    Datenbank.addNutzer(newNutzer);

                    // Nimm von der Datenbank die zugewiesene ID und speicher das Profilbild im Filesystem ab
                    String getNutzerID = Datenbank.getNutzerID(newNutzer);

                    File directory = new File("profilbilder");

                    if (!directory.exists()){
                        directory.mkdir();
                    }

                    File picture = new File("profilbilder/" + getNutzerID + ".jpg");
                    FileOutputStream fileOutputStream = new FileOutputStream(picture);
                    fileOutputStream.write(nutzerToRegister.getProfilbild());
                    fileOutputStream.close();

                    // Speicher die Bild URL in die Datenbank ab
                    newNutzer.setUserID(Integer.valueOf(getNutzerID));
                    String bildPath = picture.getPath().replaceAll("\\\\", "/");

                    newNutzer.setProfilbildURL(bildPath);
                    Datenbank.setProfilBildNutzer(newNutzer);

                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Server konnte Nutzer nicht in die Datenbank einfügen");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            baseRequest.setHandled(true);
        }
    }
}
