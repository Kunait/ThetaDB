package sep.groupt.client.chat;

import sep.groupt.client.dataclass.Nutzer;

import java.util.ArrayList;

public class Chat {

    ArrayList<ChatMessage> messages;
    Nutzer user1,user2;

    public Nutzer getUser1() {
        return user1;
    }

    public void setUser1(Nutzer user1) {
        this.user1 = user1;
    }

    public Nutzer getUser2() {
        return user2;
    }

    public void setUser2(Nutzer user2) {
        this.user2 = user2;
    }

    public Chat(){

        messages = new ArrayList<>();
    }

    public Chat(Nutzer user1,Nutzer user2){
        this.user1 = user1;

        this.user2 = user2;
    }

    public void addMessage(ChatMessage message){


        messages.add(message);
    }

    public ArrayList<ChatMessage> getMessages() {

        return messages;
    }
}
