package dao;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class dataspace {
    private Context context;
    private SharedPreferences sp;
    private  SharedPreferences.Editor editor;
    public dataspace(Context c,String name){
        context=c;
        sp=context.getSharedPreferences(name,0);
        editor =sp.edit();


    }
    public void putValue(String key,String value){
        editor=sp.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public String getValue(String key){
        return sp.getString(key,null);
    }
}
