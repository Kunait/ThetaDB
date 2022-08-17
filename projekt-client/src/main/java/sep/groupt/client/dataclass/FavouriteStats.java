package sep.groupt.client.dataclass;

public class FavouriteStats {

    private String key;
    private int value;

    public FavouriteStats(String key, int value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void addValue(int value){
        this.value += value;
    }

    public boolean check(String key) {
        return this.key.equals(key);
    }
}
