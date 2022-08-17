package sep.groupt.client.dataclass;

public class StatsNutzer {

    private int nutzer_id;
    private FavouriteStats[] lieblingsSchauspieler;
    private FavouriteStats[] lieblingsKategorie;
    private byte[] lieblingsFilmBanner;
    private int geschauteFilmMinuten;
    private int gesamteFilmMinuten;

    public StatsNutzer() {

    }

    public StatsNutzer(int nutzer_id, FavouriteStats[] lieblingsSchauspieler, FavouriteStats[] lieblingsKategorie, byte[] lieblingsFilmBanner, int geschauteFilmMinuten, int gesamteFilmMinuten){
        this.nutzer_id = nutzer_id;
        this.lieblingsSchauspieler = lieblingsSchauspieler;
        this.lieblingsKategorie = lieblingsKategorie;
        this.lieblingsFilmBanner = lieblingsFilmBanner;
        this.geschauteFilmMinuten = geschauteFilmMinuten;
        this.gesamteFilmMinuten = gesamteFilmMinuten;
    }

    public int getNutzer_id() {
        return nutzer_id;
    }

    public FavouriteStats[] getLieblingsSchauspieler() {
        return lieblingsSchauspieler;
    }

    public FavouriteStats[] getLieblingsKategorie() {
        return lieblingsKategorie;
    }

    public byte[] getLieblingsFilm() {
        return lieblingsFilmBanner;
    }

    public int getGeschauteFilmMinuten() {
        return geschauteFilmMinuten;
    }

    public int getGesamteFilmMinuten() {
        return gesamteFilmMinuten;
    }

    public void setNutzer_id(int nutzer_id) {
        this.nutzer_id = nutzer_id;
    }

    public void setLieblingsSchauspieler(FavouriteStats[] lieblingsSchauspieler) {
        this.lieblingsSchauspieler = lieblingsSchauspieler;
    }

    public void setLieblingsKategorie(FavouriteStats[] lieblingsKategorie) {
        this.lieblingsKategorie = lieblingsKategorie;
    }

    public void setLieblingsFilm(byte[] lieblingsFilmBanner) {
        this.lieblingsFilmBanner = lieblingsFilmBanner;
    }

    public void setGeschauteFilmMinuten(int geschauteFilmMinuten) {
        this.geschauteFilmMinuten = geschauteFilmMinuten;
    }

    public void setGesamteFilmMinuten(int gesamteFilmMinuten) {
        this.gesamteFilmMinuten = gesamteFilmMinuten;
    }
}