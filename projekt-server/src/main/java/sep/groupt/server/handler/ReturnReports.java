package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.dataclass.Report;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ReturnReports extends AbstractHandler {

    private int id;
    private String name;

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(baseRequest.getMethod().equals("GET") && target.contains("getReportlist")) {

            try {
                Report[] rep = Datenbank.getReports();
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(rep, Report[].class));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            baseRequest.setHandled(true);

        }

    }
}
