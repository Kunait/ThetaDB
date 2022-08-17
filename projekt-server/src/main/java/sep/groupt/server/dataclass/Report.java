package sep.groupt.server.dataclass;

public class Report {

    private String repText, date;
    private int id; //in Minuten
    private boolean gelesen;
    private Film film;

    // Leerer Constructor

    public Report(){

    }

// Constructor für den Client ohne Film_id & ohne Banner

    public Report(int idi, String date, Film filmi, String repText, boolean gelesen) {
        this.id = idi;
        this.film = filmi;
        this.date = date;
        this.repText = repText;
        this.gelesen = gelesen;
    }

    public Report(String date, Film filmi, String repText, boolean gelesen) {
        this.film = filmi;
        this.date = date;
        this.repText = repText;
        this.gelesen = gelesen;
    }

    // Ab hier Getter & Setter

    public void setid(int id){
        this.id = id;
    }

    public void setGelesen(boolean gelesen) {
        this.gelesen = gelesen;
    }

    public void setRepText(String text) {
        this.repText = text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public int getid(){
        return this.id;
    }

    public boolean isGelesen() {
        return gelesen;
    }

    public String getRepText() {
        return this.repText;
    }

    public String getDate() {
        return this.date;
    }

    public Film getFilm() {
        return this.film;
    }

}
