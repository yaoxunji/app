package com.example.administrator.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/1/24 0024.
 */
public class update_user extends Activity {
    private Button msubmit,mfinish;
    private EditText mphonenumber,memail;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    List<BmobUser> list= (List<BmobUser>) msg.obj;
                    String id=list.get(0).getObjectId();
                    update(id);
                    break;
                case 1:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_user);
        initview();

    }

    private void initview() {
        msubmit= (Button) findViewById(R.id.update_user_submit);
        mfinish= (Button) findViewById(R.id.update_user_finish);
        mphonenumber= (EditText) findViewById(R.id.update_user_phonenumber);
        memail= (EditText) findViewById(R.id.update_user_email);
        msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                String name = sharedPreferences.getString("name", "");
                BmobQuery<BmobUser> query=new BmobQuery<BmobUser>();
                query.addWhereEqualTo("username",name);
                query.findObjects(new FindListener<BmobUser>() {
                    @Override
                    public void done(List<BmobUser> list, BmobException e) {
                        if(e==null) {
                            Message message = handler.obtainMessage();
                            message.what = 0;
                            message.obj = list;
                            handler.sendMessage(message);
                        }else{
                            Log.e("bmob","id");
                        }
                    }
                });
            }
        });
        mfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void update(String id){
        String email=memail.getText().toString();
        String phone=mphonenumber.getText().toString();
        BmobUser bmobUser=new BmobUser();
        bmobUser.setEmail(email);
        bmobUser.setMobilePhoneNumber(phone);
        bmobUser.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(update_user.this, "修改成功", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(update_user.this, "修改失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
