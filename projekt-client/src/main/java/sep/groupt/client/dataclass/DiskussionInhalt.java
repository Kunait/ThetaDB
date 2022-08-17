package sep.groupt.client.dataclass;

import java.util.ArrayList;

public class DiskussionInhalt {

    ArrayList<String> nachrichten;


    public DiskussionInhalt() {

        nachrichten = new ArrayList<>();
    }

    public void addMessage(String nachricht){

        nachrichten.add(nachricht);
    }

    public ArrayList<String> getNachrichten(){

        return nachrichten;
    }
}
