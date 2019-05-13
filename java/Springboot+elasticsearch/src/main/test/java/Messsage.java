
public class Messsage {
    private int id;
    private String belong;
    private String time;

    public Messsage(int id, String belong, String time) {
        this.id = id;
        this.belong = belong;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
