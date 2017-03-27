package dao;

import android.util.Log;

import java.util.List;

import bean.plans;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class Bmob_dao {
    private String memailbyname;
    public void SelectEmailByName(String name){
        BmobQuery<plans> query = new BmobQuery<plans>();
        query.addWhereEqualTo("name", name);
        query.findObjects(new FindListener<plans>() {
            @Override
            public void done(List<plans> list, BmobException e) {
                if (e == null) {
                    list.get(0).getEmail();
                } else {
                    Log.e("bmob", "姓名查邮箱");

                }

            }

        });

    }
}
