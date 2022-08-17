package sep.groupt.client.chat;

import sep.groupt.client.dataclass.Nutzer;

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

    public ChatMessage(String date, String message) {
        this.date = date;
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public String Nachricht(){


       return "["+sender.getVorname()+" "+sender.getNachname()+"]"+message;

    }
}
