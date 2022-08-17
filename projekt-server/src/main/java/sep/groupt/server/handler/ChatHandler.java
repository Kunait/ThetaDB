package sep.groupt.server.handler;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sep.groupt.server.Datenbank;
import sep.groupt.server.chat.Chat;
import sep.groupt.server.chat.ChatMessage;
import sep.groupt.server.dataclass.Film;
import sep.groupt.server.dataclass.Nutzer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if(baseRequest.getMethod().equals("POST") && target.contains("sendChat")) {
            ChatMessage message = new Gson().fromJson(request.getReader(),ChatMessage.class);
            int firstID = Math.min(message.getSender().getUserID(),message.getReceiver().getUserID());
            int secondID = Math.max(message.getSender().getUserID(),message.getReceiver().getUserID());
            File chatlog = new File("chatlogs/"+firstID+"-"+secondID+".txt");
            if(!chatlog.exists()){

                Chat chat = new Chat();
                chat.addMessage(message);
                new Gson().newJsonWriter(new FileWriter(chatlog)).jsonValue(new Gson().toJson(chat,Chat.class)).close();
            }else{

                Chat chat = new Gson().fromJson(new FileReader(chatlog),Chat.class);
                message.getReceiver().setProfilbild(null);
                message.getSender().setProfilbild(null);
                chat.addMessage(message);
                new Gson().newJsonWriter(new FileWriter(chatlog)).jsonValue(new Gson().toJson(chat,Chat.class)).close();

            }


           // BufferedWriter writer = new BufferedWriter(new FileWriter(chatlog));
            //writer.write(message.Nachricht());
            //writer.close();

            System.out.println(message.getMessage());


            baseRequest.setHandled(true);
        }

        else if(baseRequest.getMethod().equals("GET") && target.contains("getTestChat")) {

            try {
                Nutzer nutzer = Datenbank.getNutzer(new Nutzer("tas.cuneyt25@gmail.com","1"));
                PrintWriter printWriter = response.getWriter();
                printWriter.println(new Gson().toJson(nutzer, Nutzer.class));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }



            baseRequest.setHandled(true);
        }
        else if(baseRequest.getMethod().equals("POST") && target.contains("getChat")) {

            Chat chat = new Gson().fromJson(request.getReader(),Chat.class);
            int firstID = Math.min(chat.getUser1().getUserID(),chat.getUser2().getUserID());
            int secondID = Math.max(chat.getUser1().getUserID(),chat.getUser2().getUserID());


            File chatlog = new File("chatlogs/"+firstID+"-"+secondID+".txt");

            if(!chatlog.exists()){
                boolean exists = chatlog.exists();

                chat = new Chat();

                new Gson().newJsonWriter(new FileWriter(chatlog)).jsonValue(new Gson().toJson(chat,Chat.class)).close();
            }else{

                chat = new Gson().fromJson(new FileReader(chatlog),Chat.class);
                new Gson().newJsonWriter(new FileWriter(chatlog)).jsonValue(new Gson().toJson(chat,Chat.class)).close();

            }

            PrintWriter printWriter = response.getWriter();
            printWriter.println(new Gson().toJson(chat, Chat.class));
            baseRequest.setHandled(true);
        }

    }
}
