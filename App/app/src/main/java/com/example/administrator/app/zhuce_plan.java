package com.example.administrator.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import bean.plans;
import bean.records;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/10/4 0004.
 */
public class zhuce_plan extends Activity {
    private Spinner mlevel,mmain,mflesson;
    private String memail,level,main,freelesson,mname;
    private Button mfinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zhuce_plan);
        Bmob.initialize(this, "ee02bb953703b95df7f6df6cb7b11eda");
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        final String email= bundle.getString("email");
        final String name= bundle.getString("name");
        memail=email;
        mname=name;
        mlevel= (Spinner) findViewById(R.id.level);
        mmain= (Spinner) findViewById(R.id.main);
        mflesson= (Spinner) findViewById(R.id.freelessonnum);
        mfinish= (Button) findViewById(R.id.zhuce_submit);
        mlevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView v1=(TextView)view;
                v1.setTextColor(Color.BLACK);
                String[] levels = getResources().getStringArray(R.array.level);
                level=levels[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mmain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView v1=(TextView)view;
                v1.setTextColor(Color.BLACK);
                String[] mains = getResources().getStringArray(R.array.main);
                main=mains[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mflesson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView v1=(TextView)view;
                v1.setTextColor(Color.BLACK);
                String[] frees= getResources().getStringArray(R.array.frlesson);
                freelesson=frees[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mview=inflater.inflate(R.layout.progressbar, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(zhuce_plan.this);
                builder.setView(mview);
                final AlertDialog dialog = builder.create();
                dialog.show();
                String l=level;
                records record=new records();
                String m=main;
                if(l.equals("S")){
                    if(m.equals("学习")) {
                        record.setStudy_per(60);
                        record.setSport_per(15);
                        record.setActivity_per(15);
                        record.setFun_per(10);
                    }else if(m.equals("活动")){
                        record.setStudy_per(15);
                        record.setSport_per(15);
                        record.setActivity_per(60);
                        record.setFun_per(10);

                    }else if(m.equals("娱乐")){
                        record.setStudy_per(15);
                        record.setSport_per(15);
                        record.setActivity_per(10);
                        record.setFun_per(60);

                    }else if(m.equals("运动")){
                        record.setStudy_per(15);
                        record.setSport_per(60);
                        record.setActivity_per(15);
                        record.setFun_per(10);

                    }

                } else if(l.equals("A")){
                    if(m.equals("学习")) {
                        record.setStudy_per(50);
                        record.setSport_per(20);
                        record.setActivity_per(15);
                        record.setFun_per(15);
                    }else if(m.equals("活动")){
                        record.setStudy_per(15);
                        record.setSport_per(15);
                        record.setActivity_per(50);
                        record.setFun_per(20);

                    }else if(m.equals("娱乐")){
                        record.setStudy_per(15);
                        record.setSport_per(15);
                        record.setActivity_per(20);
                        record.setFun_per(50);

                    }else if(m.equals("运动")){
                        record.setStudy_per(15);
                        record.setSport_per(50);
                        record.setActivity_per(15);
                        record.setFun_per(20);

                    }

                }else if(l.equals("B")){
                    if(m.equals("学习")) {
                        record.setStudy_per(40);
                        record.setSport_per(20);
                        record.setActivity_per(20);
                        record.setFun_per(20);
                    }else if(m.equals("活动")){
                        record.setStudy_per(20);
                        record.setSport_per(20);
                        record.setActivity_per(40);
                        record.setFun_per(20);

                    }else if(m.equals("娱乐")){
                        record.setStudy_per(20);
                        record.setSport_per(20);
                        record.setActivity_per(20);
                        record.setFun_per(40);

                    }else if(m.equals("运动")){
                        record.setStudy_per(20);
                        record.setSport_per(40);
                        record.setActivity_per(20);
                        record.setFun_per(20);

                    }

                }else if(l.equals("C")){
                    if(m.equals("学习")) {
                        record.setStudy_per(30);
                        record.setSport_per(25);
                        record.setActivity_per(25);
                        record.setFun_per(20);
                    }else if(m.equals("活动")){
                        record.setStudy_per(25);
                        record.setSport_per(25);
                        record.setActivity_per(30);
                        record.setFun_per(20);

                    }else if(m.equals("娱乐")){
                        record.setStudy_per(25);
                        record.setSport_per(25);
                        record.setActivity_per(20);
                        record.setFun_per(30);

                    }else if(m.equals("运动")){
                        record.setStudy_per(25);
                        record.setSport_per(30);
                        record.setActivity_per(25);
                        record.setFun_per(20);

                    }

                }
                int f=Integer.parseInt(freelesson);
                String e=memail;
                String n=mname;
                record.setName(n);
                record.setEmail(e);
                record.setFreelesson_time(f * 120);
                record.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e!=null){
                            Toast.makeText(zhuce_plan.this,"出错",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                plans plans=new plans();
                plans.setEmail(e);
                plans.setName(n);
                plans.setFreelessonnum(f);
                plans.setLevel(l);
                plans.setMain(m);
                plans.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        Intent intent1=new Intent(zhuce_plan.this,login.class);
                        Bundle bundle1=new Bundle();
                        intent1.putExtras(bundle1);
                        intent1.putExtra("tag", 2);
                        dialog.dismiss();
                        startActivity(intent1);
                        finish();
                    }
                });

            }
        });


    }
}
