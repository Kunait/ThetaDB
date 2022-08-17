package sep.groupt.server.dataclass;

public class Bewertung {

    public Bewertung(int bewertungID, int filmID, int punkte, String bewertung) {
        this.bewertungID = bewertungID;
        this.bewertung = bewertung;
        this.punkte = punkte;
        this.filmID = filmID;
    }
    public Bewertung(int filmID, int punkte, String bewertung) {
        this.filmID = filmID;
        this.punkte = punkte;
        this.bewertung=bewertung;
    }

    public Bewertung(int bewertungID, int filmID,int userID, int punkte, String bewertung) {
        this.bewertungID = bewertungID;
        this.userID = userID;
        this.bewertung = bewertung;
        this.punkte = punkte;
        this.filmID = filmID;
    }

    public Bewertung(int punkte, String bewertung) {
        this.punkte=punkte;
        this.bewertung=bewertung;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Bewertung() {

    }

    public int getBewertungID() {
        return bewertungID;
    }

    public void setBewertungID(int bewertungID) {
        this.bewertungID = bewertungID;
    }

    public String getBewertung() {
        return bewertung;
    }

    public void setBewertung(String bewertung) {
        this.bewertung = bewertung;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public int getFilmID() {
        return filmID;
    }

    public void setFilmID(int filmID) {
        this.filmID = filmID;
    }

    private int bewertungID;
    private String bewertung;
    private int punkte ;
    private int filmID;

    private int userID;

}
