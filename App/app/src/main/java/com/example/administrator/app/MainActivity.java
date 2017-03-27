package com.example.administrator.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class MainActivity extends Activity {
    private EditText mname,mpassword;
    private TextView mudpassword,mphone;
    private Button denglu,zhuce;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "ee02bb953703b95df7f6df6cb7b11eda");
        mContext=this;
        mname=(EditText)findViewById(R.id.name);
        mpassword=(EditText)findViewById(R.id.password);
        mudpassword=(TextView)findViewById(R.id.udpassword);
        mphone=(TextView)findViewById(R.id.phonedenglu);
        mphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, phonelogin.class);
                startActivity(intent);
                finish();

            }
        });
        mudpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ud_password.class);
                startActivity(intent);
                finish();
            }
        });
        denglu=(Button)findViewById(R.id.denglu);
        zhuce=(Button)findViewById(R.id.zhuce);
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, zhuce.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void denglu(View view){
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mview=inflater.inflate(R.layout.progressbar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final String name=mname.getText().toString();
        final String password=mpassword.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.commit();
        if(name.equals("")||password.equals("")){
            dialog.dismiss();
            Toast.makeText(MainActivity.this,"用户名或密码不能为空",Toast.LENGTH_LONG).show();
        }else {
            BmobUser.loginByAccount(name, password, new LogInListener<BmobUser>() {

                @Override
                public void done(BmobUser user, BmobException e) {
                    if(user !=null){
                        Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(mContext,login.class);
                        intent.putExtra("tag",1);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                    } else{
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,"密码不正确",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }

}
