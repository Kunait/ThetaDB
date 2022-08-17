package sep.groupt.client.dataclass;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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

    public Nutzer(String emailAdresse, String vorname, String nachname, String salt, String passwort, byte[] profilbild) {
        this.emailAdresse = emailAdresse;
        this.vorname = vorname;
        this.nachname = nachname;
        this.salt = salt;
        this.passwort = passwort;
        this.profilbild = profilbild;
    }

    // Für das Registrieren

    public Nutzer(String emailAdresse, String vorname, String nachname, String passwort){
        this.emailAdresse = emailAdresse;
        this.vorname = vorname;
        this.nachname = nachname;
        this.passwort = passwort;
    }

    public Nutzer(String emailAdresse, String vorname, String nachname, String passwort, byte[] profilbild){
        this.emailAdresse = emailAdresse;
        this.vorname = vorname;
        this.nachname = nachname;
        this.passwort = passwort;
        this.profilbild = profilbild;
    }

    public Nutzer(String emailAdresse, String vorname, String nachname, String passwort, byte[] profilbild, ArrayList<Nutzer> freundesListe, ArrayList<Film> watchListe, ArrayList<Film> gesehendeFilmeListe){
        this.emailAdresse = emailAdresse;
        this.vorname = vorname;
        this.nachname = nachname;
        this.passwort = passwort;
        this.profilbild = profilbild;
        this.freundesListe = freundesListe;
        this.watchListe = watchListe;
        this.gesehendeFilmeListe = gesehendeFilmeListe;
    }




    // KP

    public Nutzer(int userID, String emailAdresse, String vorname, String nachname, String passwort){
        this.userID = userID;
        this.emailAdresse = emailAdresse;
        this.vorname = vorname;
        this.nachname = nachname;
        this.passwort = passwort;
    }

    // Getter & Setter

    @Override
    public String toString(){

        return vorname+ " "+ nachname;
    }

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

    public StringProperty getNameProperty() {
        StringProperty stringProperty = new SimpleStringProperty(this.vorname + " " + this.nachname);
        return stringProperty;
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

    public byte[] getProfilbild() {
        return profilbild;
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
