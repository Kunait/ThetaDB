package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.EmailService;
import sep.groupt.server.dataclass.Nutzer;
import sep.groupt.server.dataclass.Systemadministrator;
import sep.groupt.server.Datenbank;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;

public class LoginHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Client sendet Logindaten an den Server mit Userdata = [Email, Passwort], überprüfe ob es Nutzer bzw. einen Admin mit der EMail existiert.
        // Setzt Statuscode auf 202, wenn es Admin und Nutzer gibt mit demselben Passwort, wenn ungleiche Passwörter, dann wird geguckt welches Passwort zu wem passt
        // Setzt Statuscode auf 202, wenn es ein Nutzer ist (incl. Passwortcheck)
        // Setzt Statuscode auf 200, wenn es ein admin ist. (incl. Passwortcheck)
        // Setzt Statuscode auf 400, wenn es keinen Nutzer mit der Email und PW gibt

        if (baseRequest.getMethod().equals("POST") && target.equals("/login")){
            String[] userdata = new Gson().fromJson(request.getReader(), String[].class);

            Nutzer dummyNutzer = new Nutzer(userdata[0], userdata[1]);
            Systemadministrator dummyAdmin = new Systemadministrator(userdata[0], userdata[1]);

            Nutzer nutzer = null;
            Systemadministrator admin = null;

            try {
                // Überprüft, ob es einen Admin und Nutzer mit der E-Mailadresse in der Datenbank existiert

                boolean checkAdmin = Datenbank.checkAdmin(dummyAdmin);
                boolean checkNutzer = Datenbank.checkNutzer(dummyNutzer);

                // Erstelle Referenz zu den Objekten, falls Sie in der Datenbank existieren

                if (checkAdmin){
                    admin = Datenbank.getAdmin(dummyAdmin);
                }
                if (checkNutzer){
                    nutzer = Datenbank.getNutzer(dummyNutzer);
                }

                // Wenn, es einen Nutzer und Admin mit der gleichen E-Mailadresse gibt:

                if (checkAdmin && checkNutzer){
                    String hashedPasswortForAdmin = getHashedPassword(userdata[1], admin.getSalt());
                    String hashedPasswortForNutzer = getHashedPassword(userdata[1], nutzer.getSalt());

                    boolean passwortGleichAdminPasswort = checkPasswords(hashedPasswortForAdmin, admin.getPasswort());
                    boolean passwortGleichNutzerPasswort = checkPasswords(hashedPasswortForNutzer, nutzer.getPasswort());

                    // Falls, Admin & Nutzer das gleiche Passwort haben
                    if (passwortGleichAdminPasswort && passwortGleichNutzerPasswort){
                        response.setStatus(HttpServletResponse.SC_ACCEPTED);
                    }
                    // Falls, Adminpasswort übereinstimmt, aber das Nutzerpasswort nicht übereinstimmt
                    else if (passwortGleichAdminPasswort && !passwortGleichNutzerPasswort){
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                    // Falls, Adminpasswort nicht übereinstimmt, aber das Nutzerpasswort übereinstimmt
                    else if (!passwortGleichAdminPasswort && passwortGleichNutzerPasswort){
                        response.setStatus(201);
                    }
                    else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }

                }
                else if (checkAdmin && !checkNutzer){
                    String hashedPasswortForAdmin = getHashedPassword(userdata[1], admin.getSalt());
                    boolean passwortGleichAdminPasswort = checkPasswords(hashedPasswortForAdmin, admin.getPasswort());

                    // Überprüfe, ob Passwort gleich ist
                    if (passwortGleichAdminPasswort){
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                    else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
                else if (!checkAdmin && checkNutzer){
                    String hashedPasswortForNutzer = getHashedPassword(userdata[1], nutzer.getSalt());
                    boolean passwortGleichNutzerPasswort = checkPasswords(hashedPasswortForNutzer, nutzer.getPasswort());

                    if (passwortGleichNutzerPasswort){
                        response.setStatus(201);
                    }
                    else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } catch (SQLException|NullPointerException|NoSuchAlgorithmException e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            baseRequest.setHandled(true);
        }

        // Client sendet Admin mit Email und PW an Server. Hier wird geprüft, ob Email und Passwort übereinstimmen, wenn ja bekommt Client seine Daten geliefert

        if (baseRequest.getMethod().equals("POST") && target.equals("/loginAdmin")){
            Systemadministrator dummyAdmin  = new Gson().fromJson(request.getReader(), Systemadministrator.class);

            try {
                if (Datenbank.checkAdmin(dummyAdmin)){
                    Systemadministrator systemadministrator = Datenbank.getAdmin(dummyAdmin);
                    PrintWriter printWriter = response.getWriter();
                    printWriter.println(new Gson().toJson(systemadministrator, Systemadministrator.class));
                    printWriter.close();
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

        // Client sendet Nutzer mit Email und PW an Server. Hier wird geprüft, ob Email und Passwort übereinstimmen, wenn ja bekommt Client seine Daten geliefert

        if (baseRequest.getMethod().equals("POST") && target.equals("/loginNutzer")){
            Nutzer dummyNutzer = new Gson().fromJson(request.getReader(), Nutzer.class);

            try {
                if (Datenbank.checkNutzer(dummyNutzer)){
                    Nutzer nutzer = Datenbank.getNutzer(dummyNutzer);
                    dummyNutzer.setPasswort(getHashedPassword(dummyNutzer.getPasswort(), nutzer.getSalt()));

                    if (checkPasswords(dummyNutzer.getPasswort(), nutzer.getPasswort())){

                        dummyNutzer.setUserID(nutzer.getUserID());
                        dummyNutzer.setVorname(nutzer.getVorname());
                        dummyNutzer.setNachname(nutzer.getNachname());
                        dummyNutzer.setProfilbildURL(nutzer.getProfilbildURL());

                        File picture = new File(dummyNutzer.getProfilbildURL());

                        if (picture.exists()){
                            FileInputStream fileInputStream = new FileInputStream(picture);
                            dummyNutzer.setProfilbild(fileInputStream.readAllBytes());
                            fileInputStream.close();
                            dummyNutzer.setProfilbildURL(null);
                        }


                        PrintWriter printWriter = response.getWriter();
                        printWriter.println(new Gson().toJson(dummyNutzer, Nutzer.class));
                        printWriter.close();
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            } catch (NoSuchAlgorithmException|NullPointerException f) {
                f.printStackTrace();
            }
            baseRequest.setHandled(true);
        }

        // Email mit Code schicken und an Nutzer weiterleiten

        if (baseRequest.getMethod().equals("POST") && target.equals("/loginNutzerAuthCode")){
            Nutzer nutzer = new Gson().fromJson(request.getReader(), Nutzer.class);

            try {
                if (Datenbank.checkNutzer(nutzer)){
                    String securityCode = getAuthCode();

                    EmailService.sendSecurityPass(nutzer, securityCode);

                    PrintWriter printWriter = response.getWriter();
                    printWriter.println(new Gson().toJson(securityCode));
                    printWriter.close();
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } catch (IOException|SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            }
        }


    }


    // Quelle: https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/

    // Nehme das passwort und Salt und erstelle mit SHA-1 Algorithmus einen hashedPasswort

    private static String getHashedPassword(String passwort, String salt) throws NoSuchAlgorithmException, NullPointerException{
        String hashedPasswort;
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(salt.getBytes());
        byte[] bytes = md.digest(passwort.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        hashedPasswort = sb.toString();

        return hashedPasswort;
    }

    private static boolean checkPasswords(String passwort1, String passwort2) {
        return passwort1.equals(passwort2);
    }

    private static String getAuthCode(){
        Random zahl = new Random();
        int oberGrenze = 9;

        String result = "";

        for (int x = 0; x < 8; x++){
            result += Integer.toString(zahl.nextInt(oberGrenze));
        }

        return result;
    }
}
