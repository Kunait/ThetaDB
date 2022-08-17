package sep.groupt.server.chat;


import sep.groupt.server.dataclass.Nutzer;

import java.time.LocalDateTime;

public class ChatMessage {


    private Nutzer sender,receiver;

    private String date;

    private String message;

    public ChatMessage(Nutzer sender,Nutzer receiver, String date, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public ChatMessage(String date, String message) {
        this.date = date;
        this.message = message;
    }

    public Nutzer getSender() {
        return sender;
    }

    public void setSender(Nutzer sender) {
        this.sender = sender;
    }

    public Nutzer getReceiver() {
        return receiver;
    }

    public void setReceiver(Nutzer receiver) {
        this.receiver = receiver;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String Nachricht(){


       return "["+sender.getVorname()+" "+sender.getNachname()+"]"+message;

    }
}
