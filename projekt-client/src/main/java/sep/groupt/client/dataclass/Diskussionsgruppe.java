package sep.groupt.client.dataclass;

public class Diskussionsgruppe {



    private Nutzer nutzer;
    private String name;
    private boolean privat;
    private int id,joiningUserID;


    public Diskussionsgruppe(Nutzer nutzer, String name, boolean privat, int id) {
        this.nutzer = nutzer;
        this.name = name;
        this.privat = privat;
        this.id = id;
    }

    public Diskussionsgruppe(Nutzer nutzer, String name, boolean privat) {
        this.nutzer = nutzer;
        this.name = name;
        this.privat = privat;

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

    public int getJoiningUserID() {
        return joiningUserID;
    }

    public void setJoiningUserID(int joiningUserID) {
        this.joiningUserID = joiningUserID;
    }

    @Override
    public String toString(){

        if(privat){
            return name+" (Privat)";
        }
        else{
            return name+ " (Öffentlich)";
        }
    }
}
