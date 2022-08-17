package sep.groupt.server.dataclass;

public class Diskussionsgruppe {



    private Nutzer nutzer;
    private String name;
    private boolean privat;
    private int id;

    private int joiningUserID;


    public Diskussionsgruppe(Nutzer nutzer, String name, boolean privat, int id) {
        this.nutzer = nutzer;
        this.name = name;
        this.privat = privat;
        this.id = id;
    }

    public Diskussionsgruppe(int id, int joiningUserID) {
        this.id = id;
        this.joiningUserID = joiningUserID;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public String getName() {
        return name;
    }

    public boolean isPrivat() {
        return privat;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString(){

        return id+" "+name+" "+nutzer.getUserID()+" "+Boolean.toString(privat);
    }

    public int getJoiningID() {
        return joiningUserID;
    }
}
