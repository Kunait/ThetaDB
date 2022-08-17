package sep.groupt.server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;

import sep.groupt.server.Datenbank;
import sep.groupt.server.DiskussionInhalt;
import sep.groupt.server.chat.Chat;
import sep.groupt.server.chat.ChatMessage;
import sep.groupt.server.dataclass.Diskussionsgruppe;
import sep.groupt.server.dataclass.Nutzer;

public class DiskussionHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(baseRequest.getMethod().equals("POST") && target.contains("getDiskussionen")) {

            try {
                Nutzer nutzer = new Gson().fromJson(request.getReader(), Nutzer.class);

                Diskussionsgruppe[] gruppen = Datenbank.getVisibleDiskussionsgruppen(nutzer);


                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(gruppen, Diskussionsgruppe[].class));


            }catch (SQLException e){
                e.printStackTrace();
                response.setStatus(404);
            }
            baseRequest.setHandled(true);
        }

        if(baseRequest.getMethod().equals("POST") && target.contains("joinDiskussion")) {

            try {
                Diskussionsgruppe gruppe  = new Gson().fromJson(request.getReader(), Diskussionsgruppe.class);

                Datenbank.joinDiskussion(gruppe,Datenbank.getUserByID(gruppe.getJoiningID()));





            }catch (SQLException e){
                e.printStackTrace();
                response.setStatus(404);
            }
            baseRequest.setHandled(true);
        }

        if(baseRequest.getMethod().equals("POST") && target.contains("addDiskussion")) {

            try {
                Diskussionsgruppe gruppe  = new Gson().fromJson(request.getReader(), Diskussionsgruppe.class);

                Datenbank.addDiskussion(gruppe.getName(),gruppe.getNutzer(),gruppe.isPrivat());





            }catch (SQLException e){
                e.printStackTrace();
                response.setStatus(404);
            }
            baseRequest.setHandled(true);
        }

        if(baseRequest.getMethod().equals("POST") && target.contains("setPrivat")) {


                Diskussionsgruppe gruppe  = new Gson().fromJson(request.getReader(), Diskussionsgruppe.class);

            try {
                Datenbank.setPrivat(gruppe);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            baseRequest.setHandled(true);
        }
        if(baseRequest.getMethod().equals("POST") && target.contains("setOpen")) {


            Diskussionsgruppe gruppe  = new Gson().fromJson(request.getReader(), Diskussionsgruppe.class);


            try {
                Datenbank.setOpen(gruppe);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            baseRequest.setHandled(true);
        }

        if(baseRequest.getMethod().equals("POST") && target.contains("getJoinedDiskussionen")) {


            try {
                Nutzer nutzer = new Gson().fromJson(baseRequest.getReader(),Nutzer.class);

                Diskussionsgruppe[] gruppen = Datenbank.getDiskussionen(nutzer);

                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(gruppen, Diskussionsgruppe[].class));



            }catch (SQLException e){
                e.printStackTrace();
                response.setStatus(404);
            }
        baseRequest.setHandled(true);
    }

        if(baseRequest.getMethod().equals("POST") && target.contains("getDiskussionInhalt")) {
            Diskussionsgruppe gruppe = new Gson().fromJson(baseRequest.getReader(),Diskussionsgruppe.class);
            int id = gruppe.getId();

            File chatlog = new File("diskussionen/"+id+".txt");
            DiskussionInhalt chat;
            if(!chatlog.exists()){
                boolean exists = chatlog.exists();

                 chat = new DiskussionInhalt();

                new Gson().newJsonWriter(new FileWriter(chatlog)).jsonValue(new Gson().toJson(chat,DiskussionInhalt.class)).close();
            }else{

                 chat = new Gson().fromJson(new FileReader(chatlog),DiskussionInhalt.class);
                new Gson().newJsonWriter(new FileWriter(chatlog)).jsonValue(new Gson().toJson(chat,DiskussionInhalt.class)).close();

            }
            PrintWriter printWriter = response.getWriter();
            printWriter.println(new Gson().toJson(chat, DiskussionInhalt.class));



            baseRequest.setHandled(true);

        }

        if(baseRequest.getMethod().equals("POST") && target.contains("sendDiskussion")) {
            ChatMessage message = new Gson().fromJson(request.getReader(),ChatMessage.class);

            File chatlog = new File("diskussionen/"+message.getDate()+".txt");
            if(!chatlog.exists()){

                DiskussionInhalt chat = new DiskussionInhalt();
                chat.addMessage(message.getMessage());
                new Gson().newJsonWriter(new FileWriter(chatlog)).jsonValue(new Gson().toJson(chat,DiskussionInhalt.class)).close();
            }else{

                DiskussionInhalt chat = new Gson().fromJson(new FileReader(chatlog),DiskussionInhalt.class);

                chat.addMessage(message.getMessage());
                new Gson().newJsonWriter(new FileWriter(chatlog)).jsonValue(new Gson().toJson(chat,DiskussionInhalt.class)).close();

            }


            // BufferedWriter writer = new BufferedWriter(new FileWriter(chatlog));
            //writer.write(message.Nachricht());
            //writer.close();

            System.out.println(message.getMessage());


            baseRequest.setHandled(true);
        }

        if(baseRequest.getMethod().equals("GET") && target.contains("cleanUpTest")) {
            try {
                Datenbank.cleanUpTest();
            }catch (Exception e){
                e.printStackTrace();
                response.setStatus(404);
            }
            baseRequest.setHandled(true);


        }
    }}

