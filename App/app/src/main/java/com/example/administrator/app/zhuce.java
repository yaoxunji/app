package com.example.administrator.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;


/**
 * Created by Administrator on 2016/8/20 0020.
 */
public class zhuce extends Activity  {
    private EditText mname,mpassword,memail,mtelnumber;
    private Context mcontext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zhuce);
        Bmob.initialize(this, "ee02bb953703b95df7f6df6cb7b11eda");
        mcontext=this;

        mname=(EditText)findViewById(R.id.name);
        mpassword=(EditText)findViewById(R.id.password);
        memail=(EditText)findViewById(R.id.email);
        mtelnumber=(EditText)findViewById(R.id.telnumber);

    }


    public void chakan(View view){
        Intent intent=new Intent(mcontext,shuoming.class);
        startActivity(intent);
    }
    public void submit(View view) {

        final String name = mname.getText().toString();
        String password = mpassword.getText().toString();
        if (name.length() < 6 || password.length() < 6) {
            Toast.makeText(zhuce.this, "请输入正确的用户名或密码", Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.commit();
            final String email = memail.getText().toString();
            String telphone = mtelnumber.getText().toString();
            final BmobUser user = new BmobUser();
            user.setUsername(name);
            user.setPassword(password);
            user.setEmail(email);
            user.setMobilePhoneNumber(telphone);
            user.signUp(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if (e == null) {

                        user.requestEmailVerify(email, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(zhuce.this, "注册成功,已发送邮件到邮箱", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(mcontext, zhuce_plan.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("email", email);
                                    bundle.putString("name", name);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(zhuce.this, "注册失败", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(zhuce.this, "请填写正确的手机号和邮箱", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }


}
