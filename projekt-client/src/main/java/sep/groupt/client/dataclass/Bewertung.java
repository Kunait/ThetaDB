package sep.groupt.client.dataclass;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Bewertung {


    private int bewertungID;

    private int filmID;

    private int userID;

    private String bewertung;

    private int punkte;

    //    public Bewertung(int filmID, String bewertung, int punkte) {
//        this.filmID =filmID;
//        this.bewertung = bewertung;
//        this.punkte= punkte;
//    }
//
//    public Bewertung(int filmID, int punkte) {
//        this.filmID =filmID;
//        this.punkte= punkte;
//    }
//
//    public Bewertung(int punkte, String bewertung)
//    {
//        this.punkte = punkte;
//        this.bewertung=bewertung;
//    }
//
    public Bewertung(int filmID, int userid ,int punkte, String bewertung)
    {
        this.filmID=filmID;
        this.userID = userid;
        this.punkte = punkte;
        this.bewertung=bewertung;
    }

    public Bewertung(int bewertungID,int filmID, int userid, int punkte, String bewertung) {
        this.bewertungID = bewertungID;
        this.filmID=filmID;
        this.userID=userid;
        this.punkte=punkte;
        this.bewertung=bewertung;
    }

    public Bewertung(int punkte, String bewertung) {
        this.punkte=punkte;
        this.bewertung=bewertung;
    }

    public Bewertung() {

    }

    public StringProperty getPunkteProperty() {
        StringProperty stringProperty = new SimpleStringProperty(Integer.toString(this.getPunkte()));
        return stringProperty;
    }

    public StringProperty getBewertungProperty() {
        StringProperty stringProperty2 = new SimpleStringProperty(this.getBewertung());
        return stringProperty2;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }


    public String getBewertung() {
        return bewertung;
    }

    public void setBewertung(String bewertung) {
        this.bewertung = bewertung;
    }

    public int getFilmID() {
        return filmID;
    }

    public void setFilmID(int filmID) {
        this.filmID = filmID;
    }

    public int getBewertungID() {
        return bewertungID;
    }

    public void setBewertungID(int bewertungID) {
        this.bewertungID = bewertungID;
    }


}
