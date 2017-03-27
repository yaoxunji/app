package bean;

/**
 * Created by Administrator on 2017/1/8 0008.
 */
public class show_listview {
    private String xiangmu;
    private String start;
    private String end;
    private String date;

    public show_listview() {
    }

    public show_listview(String xiangmu,String start, String end, String date) {
        this.xiangmu = xiangmu;
        this.start = start;
        this.end = end;
        this.date = date;
    }

    public String getXiangmu() {
        return xiangmu;
    }

    public void setXiangmu(String xiangmu) {
        this.xiangmu = xiangmu;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
