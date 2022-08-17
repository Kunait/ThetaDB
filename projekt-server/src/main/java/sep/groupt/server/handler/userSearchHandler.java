package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.dataclass.Nutzer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class userSearchHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (baseRequest.getMethod().equals("POST") && target.equals("/addFriend")) {

            String ids = new Gson().fromJson(request.getReader(), String.class);
            int idUser = Integer.valueOf(ids.split("-")[0]);
            int idFriend = Integer.valueOf(ids.split("-")[1]);

            System.out.println(idUser + " " + idFriend);

            try {
                Datenbank.addFriend(Datenbank.getUserByID(idUser), Datenbank.getUserByID(idFriend));
                Datenbank.addFriend(Datenbank.getUserByID(idFriend), Datenbank.getUserByID(idUser));
                response.setStatus(HttpServletResponse.SC_OK);


            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new RuntimeException(e);
            }
        }
        if (baseRequest.getMethod().equals("POST") && target.equals("/deleteFriendRequest")) {

            String ids = new Gson().fromJson(request.getReader(), String.class);
            int idUser = Integer.valueOf(ids.split("-")[0]);
            int idFriend = Integer.valueOf(ids.split("-")[1]);

            System.out.println(idUser + " " + idFriend);

            try {
                Datenbank.deleteFriendRequest(Datenbank.getUserByID(idUser), Datenbank.getUserByID(idFriend));
                response.setStatus(HttpServletResponse.SC_OK);


            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new RuntimeException(e);
            }


            baseRequest.setHandled(true);
        }

        if (baseRequest.getMethod().equals("POST") && target.equals("/addFriendRequest")) {

            String ids = new Gson().fromJson(request.getReader(), String.class);
            int idUser = Integer.valueOf(ids.split("-")[0]);
            int idFriend = Integer.valueOf(ids.split("-")[1]);

            System.out.println(idUser + " " + idFriend);

            try {
                Datenbank.addFriendRequest(Datenbank.getUserByID(idUser), Datenbank.getUserByID(idFriend));
                response.setStatus(HttpServletResponse.SC_OK);


            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new RuntimeException(e);
            }


            baseRequest.setHandled(true);
        }


            if (baseRequest.getMethod().equals("POST") && target.equals("/getFriendRequests")) {

                int n = new Gson().fromJson(request.getReader(), Integer.class);


                try {
                    Nutzer[] nutzer = Datenbank.getAnfragen(Datenbank.getUserByID(n));
                    PrintWriter printWriter = response.getWriter();
                    printWriter.println(new Gson().toJson(nutzer, Nutzer[].class));
                    printWriter.close();
                    response.setStatus(HttpServletResponse.SC_OK);


                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    throw new RuntimeException(e);
                }


                baseRequest.setHandled(true);
            }
            if (baseRequest.getMethod().equals("POST") && target.equals("/deleteFriend")) {

                String ids = new Gson().fromJson(request.getReader(), String.class);
                int idUser = Integer.valueOf(ids.split("-")[0]);
                int idFriend = Integer.valueOf(ids.split("-")[1]);

                System.out.println(idUser + " " + idFriend);

                try {
                    Datenbank.deleteFriend(Datenbank.getUserByID(idUser), Datenbank.getUserByID(idFriend));
                    response.setStatus(HttpServletResponse.SC_OK);


                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    throw new RuntimeException(e);
                }


                baseRequest.setHandled(true);
            }
        }

    }
