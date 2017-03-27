package com.example.administrator.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class ud_password extends Activity{
    private Context mcontext;
    private Button mpassord;
    private EditText medittext;
    private CircularProgressButton circularButton1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ud_password);
        Bmob.initialize(this, "ee02bb953703b95df7f6df6cb7b11eda");
        mcontext=this;
        circularButton1= (CircularProgressButton) findViewById(R.id.Button3);
        circularButton1.setIndeterminateProgressMode(true);
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (circularButton1.getProgress() == 0) {
                    circularButton1.setProgress(50);
                    final String email=medittext.getText().toString();
                    BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(mcontext, "重置密码请求成功，请到" + email + "邮箱进行密码重置操作", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(mcontext, login.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(mcontext, "重置密码请求失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    circularButton1.setProgress(100);
                } else if (circularButton1.getProgress() == 100) {
                    circularButton1.setProgress(0);
                } else {
                    circularButton1.setProgress(100);
                }
            }
        });
        medittext=(EditText)findViewById(R.id.editText);
    }
    /*public void submit(View view){
        final String email=medittext.getText().toString();
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(mcontext,"重置密码请求成功，请到" + email + "邮箱进行密码重置操作",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(mcontext,login.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(mcontext,"重置密码请求失败",Toast.LENGTH_LONG).show();
                }
            }
        });
    }*/
}
