package sep.groupt.server.dataclass;


import java.util.ArrayList;

public class Nutzer {
    private int userID;
    private String emailAdresse;
    private String vorname;
    private String nachname;
    private String passwort;
    private String einstellungen;
    private String salt;
    private String profilbildURL;
    private byte[] profilbild;

    private ArrayList<Nutzer> freundesListe;
    private ArrayList<Film> watchListe;
    private ArrayList<Film> gesehendeFilmeListe;




    //Constructor

    public Nutzer(){
    }

    // Fürs Anmelden

    public Nutzer(String emailAdresse, String passwort){
        this.emailAdresse = emailAdresse;
        this.passwort = passwort;
    }

    // Bekommt vom Server dieses Objekt zurück

    public Nutzer(String emailAdresse, String vorname, String nachname, String salt, String passwort) {
        this.emailAdresse = emailAdresse;
        this.vorname = vorname;
        this.nachname = nachname;
        this.salt = salt;
        this.passwort = passwort;
    }

    // Für das Registrieren

    public Nutzer(String emailAdresse, String vorname, String nachname, String passwort){
        this.emailAdresse = emailAdresse;
        this.vorname = vorname;
        this.nachname = nachname;
        this.passwort = passwort;
    }

    // KP

    public Nutzer(int userID, String emailAdresse, String vorname, String nachname, String passwort){
        this.userID = userID;
        this.emailAdresse = emailAdresse;
        this.vorname = vorname;
        this.nachname = nachname;
        this.passwort = passwort;
    }

    public Nutzer(int userID, String emailAdresse, String vorname, String nachname, String passwort,byte[] bild){
        this.userID = userID;
        this.emailAdresse = emailAdresse;
        this.vorname = vorname;
        this.nachname = nachname;
        this.passwort = passwort;
        profilbild = bild;
    }

    // Getter & Setter

    public int getUserID() {
        return this.userID;
    }

    public String getEmailAdresse() {
        return this.emailAdresse;
    }

    public String getVorname() {
        return this.vorname;
    }

    public String getNachname() {
        return this.nachname;
    }

    public String getPasswort() {
        return this.passwort;
    }

    public String getSalt() {
         return this.salt;
    }

    public String getProfilbildURL(){
        return this.profilbildURL;
    }

    public byte[] getProfilbild() {
        return profilbild;
    }

    public ArrayList<Nutzer> getFreundesListe() {
        return freundesListe;
    }

    public ArrayList<Film> getWatchListe() {
        return watchListe;
    }

    public String getEinstellungen() {
        return this.einstellungen;
    }

    public ArrayList<Film> getGesehendeFilmeListe() {
        return gesehendeFilmeListe;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setProfilbildURL(String profilbildURL) {
        this.profilbildURL = profilbildURL;
    }

    public void setProfilbild(byte[] profilbild) {
        this.profilbild = profilbild;
    }

    public void setFreundesListe(ArrayList<Nutzer> freundesListe) {
        this.freundesListe = freundesListe;
    }

    public void setWatchListe(ArrayList<Film> watchListe) {
        this.watchListe = watchListe;
    }

    public void setEinstellungen(String einstellungen) {
        this.einstellungen = einstellungen;
    }

    public void setGesehendeFilmeListe(ArrayList<Film> gesehendeFilmeListe) {
        this.gesehendeFilmeListe = gesehendeFilmeListe;
    }
}
