package bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class dayrecords extends BmobObject {
    private int year;
    private int month;
    private int day;
    private int week;
    private String xiangmu;
    private String email;
    private int start_sthour;
    private int end_our;
    private String name;

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXiangmu() {
        return xiangmu;
    }

    public void setXiangmu(String xiangmu) {
        this.xiangmu = xiangmu;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStart_sthour() {
        return start_sthour;
    }

    public void setStart_sthour(int start_sthour) {
        this.start_sthour = start_sthour;
    }

    public int getEnd_our() {
        return end_our;
    }

    public void setEnd_our(int end_our) {
        this.end_our = end_our;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
