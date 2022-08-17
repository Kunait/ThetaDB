package sep.groupt.client.dataclass;

public class StatistikAdmin {
    private int filmid, anzahlBewertung, AnzahlGesehen, gesamtPunkte;

    public StatistikAdmin(int anzahlBewertung, int AnzahlGesehen, int gesamtPunkte)
    {
        this.anzahlBewertung=anzahlBewertung;
        this.AnzahlGesehen=AnzahlGesehen;
        this.gesamtPunkte=gesamtPunkte;
    }

    public StatistikAdmin(int filmid,int anzahlBewertung, int AnzahlGesehen, int gesamtPunkte)
    {
        this.filmid=filmid;
        this.anzahlBewertung=anzahlBewertung;
        this.AnzahlGesehen=AnzahlGesehen;
        this.gesamtPunkte=gesamtPunkte;
    }

    public StatistikAdmin(int filmid, int AnzahlGesehen) {
        this.filmid=filmid;
        this.AnzahlGesehen=AnzahlGesehen;
    }

    public int getFilmid() {
        return filmid;
    }

    public void setFilmid(int filmid) {
        this.filmid = filmid;
    }

    public int getAnzahlBewertung() {
        return anzahlBewertung;
    }

    public void setAnzahlBewertung(int anzahlBewertung) {
        this.anzahlBewertung = anzahlBewertung;
    }

    public int getAnzahlGesehen() {
        return AnzahlGesehen;
    }

    public void setAnzahlGesehen(int anzahlGesehen) {
        AnzahlGesehen = anzahlGesehen;
    }

    public int getGesamtPunkte() {
        return gesamtPunkte;
    }

    public void setGesamtPunkte(int gesamtPunkte) {
        this.gesamtPunkte = gesamtPunkte;
    }
}
