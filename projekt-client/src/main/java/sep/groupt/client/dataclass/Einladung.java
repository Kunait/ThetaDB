package sep.groupt.client.dataclass;


public class Einladung {

    private String  date, text;
    private int id, userID1, userID2, filmID;
    private Film film;
    private boolean angenommen;
    private Nutzer user1, user2;


    //Constructor

    public Einladung(){
    }

    public Einladung(int id, Nutzer user1, Nutzer user2, Film film, String date){
        this.id=id;
        this.user1=user1;
        this.user2=user2;
        this.film=film;
        this.date=date;
    }

    public Einladung(Nutzer user1, Nutzer user2, Film film, String date){
        this.user1=user1;
        this.user2=user2;
        this.film=film;
        this.date=date;
    }

    public Einladung(Nutzer user1, Nutzer user2, Film film, String date, String text){
        this.user1=user1;
        this.user2=user2;
        this.film=film;
        this.date=date;
        this.text=text;
    }

    public String toString(){
        if(text.equals(null) || text.equals("")){
            return film.getName()+ " mit "+ user1.getVorname() + "(" + date + ")";
        } else{
            return film.getName()+ " mit "+ user1.getVorname() + "(" + date + ")        '" + text + "'";
        }

    }

    // Getter & Setter

    public Nutzer getUser1() {
        return this.user1;
    }

    public int getId() {
        return this.id;
    }

    public Nutzer getUser2() {
        return this.user2;
    }

    public Film getFilm() {
        return this.film;
    }

    public String getDate() {
        return this.date;
    }

    public String getText() {
        return this.text;
    }

    public boolean isAngenommen() {
        return this.angenommen;
    }

    public void setUser1(Nutzer user) {
        this.user1 = user;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setUser2(Nutzer user) {
        this.user2 = user;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public void setDate(String dat) {
        this.date = dat;
    }

    public void setText(String dat) {
        this.text = dat;
    }

    public void setAngenommen(boolean ang) {
        this.angenommen = ang;
    }
}
