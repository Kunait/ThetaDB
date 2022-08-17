package sep.groupt.server.dataclass;

public class Systemadministrator{
    private int adminID;
    private String emailAdresse;
    private String vorname;
    private String nachname;
    private String salt;
    private String passwort;


    // Constructor

    public Systemadministrator(){

    }

    public Systemadministrator(String emailAdresse, String passwort){
        this.emailAdresse = emailAdresse;
        this.passwort = passwort;
    }

    public Systemadministrator(String emailAdresse, String vorname, String nachname, String salt, String passwort){
        this.emailAdresse = emailAdresse;
        this.vorname = vorname;
        this.nachname = nachname;
        this.salt = salt;
        this.passwort = passwort;
    }

    public Systemadministrator(int adminID, String vorname, String nachname, String emailAdresse, String passwort){
        this.adminID = adminID;
        this.emailAdresse = emailAdresse;
        this.vorname = vorname;
        this.nachname = nachname;
        this.passwort = passwort;
    }


    // Getter & Setter
    public int getAdminID(){
        return this.adminID;
    }

    public void setAdminID(int adminID){
        this.adminID = adminID;
    }

    // Getter & Setter ab hier

    public String getVorname(){
        return this.vorname;
    }

    public void setVorname(String vorname){
        this.vorname = vorname;
    }

    public String getNachname(){
        return this.nachname;
    }

    public void setNachname(String nachname){
        this.nachname = nachname;
    }

    public String getEmailAdresse(){
        return this.emailAdresse;
    }

    public void setEmailAdresse(String emailAdresse){
        this.emailAdresse = emailAdresse;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPasswort(){
        return this.passwort;
    }

    public void setPasswort(String passwort){
        this.passwort = passwort;
    }


}
