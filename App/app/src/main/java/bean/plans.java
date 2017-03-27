package bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/10/4 0004.
 */
public class plans extends BmobObject{
    private String name;
    private String level;
    private String email;
    private String main;
    private int freelessonnum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public int getFreelessonnum() {
        return freelessonnum;
    }

    public void setFreelessonnum(int freelessonnum) {
        this.freelessonnum = freelessonnum;
    }
}
