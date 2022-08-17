package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.EmailService;
import sep.groupt.server.dataclass.Report;
import sep.groupt.server.dataclass.Systemadministrator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class AddReportHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        {
            if(baseRequest.getMethod().equals("POST") && target.contains("addReport")) {
                Report tempReport = new Gson().fromJson(request.getReader(), Report.class);
                try {
                   // boolean checkReport = Datenbank.checkReport(tempReport);
                   // if(!checkReport)
                    //{
                        Datenbank.addReport(tempReport);
                        Systemadministrator sys[]=Datenbank.getAdmins();
                        for(Systemadministrator s : sys) {
                            try {
                                EmailService.sendRepMail(tempReport, s);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    // }


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                baseRequest.setHandled(true);
            }
        }   if(baseRequest.getMethod().equals("POST") && target.contains("readReport")) {
            Report tempReport = new Gson().fromJson(request.getReader(), Report.class);
            try {
                // boolean checkReport = Datenbank.checkReport(tempReport);
                // if(!checkReport)
                //{
                Datenbank.readReport(tempReport);
                // }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            baseRequest.setHandled(true);
        }
    }
}