package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.EmailService;
import sep.groupt.server.dataclass.Einladung;
import sep.groupt.server.dataclass.Nutzer;
import sep.groupt.server.dataclass.Report;
import sep.groupt.server.dataclass.Systemadministrator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;

public class EinladungHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        {
            if (baseRequest.getMethod().equals("POST") && target.contains("addEinladung")) {
                Einladung tempEinladung = new Gson().fromJson(request.getReader(), Einladung.class);
                try {
                    EmailService.sendEinMail(tempEinladung);
                    Datenbank.addEinladung(tempEinladung);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                baseRequest.setHandled(true);
            }
        } if (baseRequest.getMethod().equals("POST") && target.contains("getEinladungen")) {
            try {
                int curruser = new Gson().fromJson(request.getReader(), Integer.class);
                Einladung[] ein = Datenbank.getEinladungen(curruser);
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(ein, Einladung[].class));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            baseRequest.setHandled(true);
        }  if (baseRequest.getMethod().equals("POST") && target.contains("sendResponseMail")) {
            try {
                Einladung tempEinladung = new Gson().fromJson(request.getReader(), Einladung.class);
                EmailService.sendEinResponseMail(tempEinladung);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            baseRequest.setHandled(true);
        }
        if (baseRequest.getMethod().equals("DELETE") && target.contains("deleteEinladung")){
            String[] strings = target.split("/");
            try {
                System.out.println(strings[2]);
                Datenbank.deleteEinladung(Integer.parseInt(strings[2]));
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            baseRequest.setHandled(true);
        }
     }

}
