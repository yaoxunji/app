package com.example.administrator.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.administrator.app.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class phonelogin extends Activity {
    private Context mContext;
    private EditText mphonenumber, mcode;
    private Button mgetcode, mlogin;
    private TimeCount time;
    private CircularProgressButton circularButton1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.phonelogin);
        Bmob.initialize(this, "ee02bb953703b95df7f6df6cb7b11eda");
        mContext = this;
        mphonenumber = (EditText) findViewById(R.id.phonenumber);
        mcode = (EditText) findViewById(R.id.code);
        mgetcode = (Button) findViewById(R.id.getcode);
        circularButton1 = (CircularProgressButton) findViewById(R.id.login);
        circularButton1.setIndeterminateProgressMode(true);
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() == 0) {
                    final String phonenumber = mphonenumber.getText().toString();
                    String code = mcode.getText().toString();
                    BmobUser.loginBySMSCode(phonenumber, code, new LogInListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (bmobUser != null) {
                                circularButton1.setProgress(100);
                                Toast.makeText(phonelogin.this, "登录成功", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(mContext, login.class);
                                intent.putExtra("tag", 3);
                                intent.putExtra("phonenumber", phonenumber);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(phonelogin.this, "注册之后才能使用手机验证码登录", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                } else if (circularButton1.getProgress() == 100) {
                    circularButton1.setProgress(0);
                } else {
                    circularButton1.setProgress(100);
                }
            }
        });
        time = new TimeCount(60000, 1000);

    }

    public void getcode(View view) {
        time.start();
        String phonenumber = mphonenumber.getText().toString();
        BmobSMS.requestSMSCode(phonenumber, "login", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {

                if (e == null) {
                    Toast.makeText(phonelogin.this, "短信验证码已发送", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*public void login(View view) {
        final String phonenumber = mphonenumber.getText().toString();
        String code = mcode.getText().toString();
        BmobUser.loginBySMSCode(phonenumber, code, new LogInListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (bmobUser != null) {
                    Toast.makeText(phonelogin.this, "登录成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mContext, login.class);
                    intent.putExtra("tag",3);
                    intent.putExtra("phonenumber", phonenumber);
                    startActivity(intent);
                }

            }
        });

    }*/

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            mgetcode.setText("获取验证码");
            mgetcode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            mgetcode.setClickable(false);//防止重复点击
            mgetcode.setText(millisUntilFinished / 1000 + "s");
        }
    }
}

