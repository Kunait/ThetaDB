package sep.groupt.client.dataclass;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Film {

    private String name, category, regisseur, author, cast, date;
    private int film_id, length; //in Minuten
    private String bannerLink;

    private byte[] banner;

    private int watchlist_id;

    // Leerer Constructor

    public Film(){

    }

    public Film(int film_id, String name, String category, int length, String date, String regisseur, String author, String cast, byte[] banner) {
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

    public byte[] getBanner() {
        return banner;
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

    public Film(String name, byte[] banner) {
        this.name = name;

        this.banner = banner;
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

    // Constructor sep.groupt.client.dataclass.Film (Film_id, Filmname, Kategorie, Filmlaenge, Erscheinungsdatum, Regisseur, Drehbuchautor, Cast) für Datenbank ohne Banner

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

    // Constructor sep.groupt.client.dataclass.Film (Film_id, Filmname, Kategorie, Filmlaenge, Erscheinungsdatum, Regisseur, Drehbuchautor, Cast, Filmbanner) für Datenbank mit Banner

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

    public Film(int film_id, String name, String date, String author)
    {
        this.film_id=film_id;
        this.name =name;
        this.date = date;
        this.author = author;
    }

    public int getWatchlist_id() {
        return watchlist_id;
    }

    public void setWatchlist_id(int watchlist_id) {
        this.watchlist_id = watchlist_id;
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

    public StringProperty getNameProperty() {
        StringProperty stringProperty = new SimpleStringProperty(this.getName());
        return stringProperty;
    }

    public StringProperty getIdProperty(){
        StringProperty stringProperty2 = new SimpleStringProperty(Integer.toString(this.getFilm_id()));
        return stringProperty2;
    }

    public StringProperty getKategorieProperty() {
        StringProperty stringProperty3 = new SimpleStringProperty(this.getCategory());
        return stringProperty3;
    }

    public StringProperty getCastProperty() {
        StringProperty stringProperty4 = new SimpleStringProperty(this.getCast());
        return stringProperty4;
    }

    public StringProperty getDatumProperty() {
        StringProperty stringProperty5 = new SimpleStringProperty(this.getDate());
        return stringProperty5;
    }

    public ObservableValue<String> getAuthorProperty() {
        StringProperty stringProperty = new SimpleStringProperty(this.getAuthor());
        return  stringProperty;
    }

    public ObservableValue<String> getDateProperty() {
        StringProperty stringProperty = new SimpleStringProperty(this.getDate());
        return  stringProperty;
    }

    public String toString(){
        return name;
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

    public void setBanner(byte[] banner) {
        this.banner = banner;
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

    public String getBannerLink() {
        return this.bannerLink;
    }

}
