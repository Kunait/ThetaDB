package sep.groupt.client.dataclass;

public class Watchlist {
    private int watchlistID, userID, filmID;

    public Watchlist(int watchlistID, int filmID, int userID) {
        this.watchlistID = watchlistID;
        this.userID = userID;
        this.filmID = filmID;
    }

    public Watchlist(int userID, int filmID) {
        this.userID = userID;
        this.filmID = filmID;
    }

    public Watchlist() {

    }

    public int getWatchlistID() {
        return watchlistID;
    }

    public void setWatchlistID(int watchlistID) {
        this.watchlistID = watchlistID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFilmID() {
        return filmID;
    }

    public void setFilmID(int filmID) {
        this.filmID = filmID;
    }

}
