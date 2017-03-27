package bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/10/5 0005.
 */
public class records extends BmobObject implements Serializable{
    private String email;
    private String name;
    private int study_per;
    private int sport_per;
    private int fun_per;
    private int activity_per;
    private int study_time;
    private int sport_time;
    private int fun_time;
    private int activity_time;
    private int freelesson_time;

    @Override
    public String toString() {
        return "records{}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFreelesson_time() {
        return freelesson_time;
    }

    public void setFreelesson_time(int freelesson_time) {
        this.freelesson_time = freelesson_time;
    }

    public int getActivity_time() {
        return activity_time;
    }

    public void setActivity_time(int activity_time) {
        this.activity_time = activity_time;
    }

    public int getFun_time() {
        return fun_time;
    }

    public void setFun_time(int fun_time) {
        this.fun_time = fun_time;
    }

    public int getSport_time() {
        return sport_time;
    }

    public void setSport_time(int sport_time) {
        this.sport_time = sport_time;
    }

    public int getStudy_per() {
        return study_per;
    }

    public void setStudy_per(int study_per) {
        this.study_per = study_per;
    }

    public int getSport_per() {
        return sport_per;
    }

    public void setSport_per(int sport_per) {
        this.sport_per = sport_per;
    }

    public int getFun_per() {
        return fun_per;
    }

    public void setFun_per(int fun_per) {
        this.fun_per = fun_per;
    }

    public int getActivity_per() {
        return activity_per;
    }

    public void setActivity_per(int activity_per) {
        this.activity_per = activity_per;
    }

    public int getStudy_time() {
        return study_time;
    }

    public void setStudy_time(int study_time) {
        this.study_time = study_time;
    }


}
