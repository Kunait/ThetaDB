package sep.groupt.server.dataclass;

public class Film {

    private String name, category, regisseur, author, cast, date;
    private int film_id, length; //in Minuten
    private String bannerLink;
    byte[] banner = null;

    // Leerer Constructor

    private int watchlist_id;

    public int getWatchlist_id() {
        return watchlist_id;
    }

    public void setWatchlist_id(int watchlist_id) {
        this.watchlist_id = watchlist_id;
    }

    public Film(){

    }

    public byte[] getBanner() {
        return banner;
    }

    public void setBanner(byte[] banner) {
        this.banner = banner;
    }


    public Film(int watchlist_id, int film_id, String name, String category, int length, String date, String regisseur, String author, String cast, byte[] banner) {
        this.watchlist_id=watchlist_id;
        this.film_id=film_id;
        this.name=name;
        this.category=category;
        this.length=length;
        this.date=date;
        this.regisseur=regisseur;
        this.author=author;
        this.cast=cast;
        this.banner=banner;
    }
    public Film(String name, String category, int length, String date, String regisseur, String author, String cast, byte[] banner) {
        this.name = name;
        this.category = category;
        this.regisseur = regisseur;
        this.author = author;
        this.cast = cast;
        this.date = date;
        this.length = length;
        this.banner = banner;
    }

    // Constructor für den Client ohne Film_id & ohne Banner

    public Film(String name, String category, int length, String date, String regisseur, String author, String cast) {
        this.name = name;
        this.category = category;
        this.regisseur = regisseur;
        this.author = author;
        this.cast = cast;
        this.date = date;
        this.length = length;
    }

    // Constructor für den Client ohne Film_id & mit Banner

    public Film(String name, String category, int length, String date, String regisseur, String author, String cast, String banner) {
        this.name = name;
        this.category = category;
        this.regisseur = regisseur;
        this.author = author;
        this.cast = cast;
        this.date = date;
        this.bannerLink = banner;
        this.length = length;
    }

    // Constructor Film (Film_id, Filmname, Kategorie, Filmlaenge, Erscheinungsdatum, Regisseur, Drehbuchautor, Cast) für Datenbank ohne Banner

    public Film(int film_id, String name, String category, int length, String date, String regisseur, String author, String cast) {
        this.film_id = film_id;
        this.name = name;
        this.category = category;
        this.regisseur = regisseur;
        this.author = author;
        this.cast = cast;
        this.date = date;
        this.length = length;
    }

    // Constructor Film (Film_id, Filmname, Kategorie, Filmlaenge, Erscheinungsdatum, Regisseur, Drehbuchautor, Cast, Filmbanner) für Datenbank mit Banner

    public Film(int film_id, String name, String category, int length, String date, String regisseur, String author, String cast, String banner) {
        this.film_id = film_id;
        this.name = name;
        this.category = category;
        this.regisseur = regisseur;
        this.author = author;
        this.cast = cast;
        this.date = date;
        this.bannerLink = banner;
        this.length = length;
    }

    public Film(int film_id, String name) {
        this.film_id = film_id;
        this.name= name;
    }

    public Film(String name, String date, String author)
    {
        this.name =name;
        this.date = date;
        this.author = author;
    }

    // Ab hier Getter & Setter

    public void setFilm_id(int film_id){
        this.film_id = film_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRegisseur(String regisseur) {
        this.regisseur = regisseur;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
    }

    public int getFilm_id(){
        return this.film_id;
    }

    public String getName() {
        return this.name;
    }

    public String getCategory() {
        return this.category;
    }

    public String getRegisseur() {
        return this.regisseur;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getCast() {
        return this.cast;
    }

    public String getDate() {
        return this.date;
    }

    public int getLength() {
        return this.length;
    }

    public String getBannerLink() {return this.bannerLink;}
}
