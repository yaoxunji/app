package com.example.administrator.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import java.util.List;

import bean.plans;
import bean.records;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Administrator on 2017/1/24 0024.
 */
public class update_plan extends Activity {
    private Spinner mlevel,mmain,mflesson;
    private String memail,level,main,freelesson,mname;
    private Button mfinish,msubmit;
    private records mrecord;
    private CircularProgressButton circularButton1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    List<plans> list= (List<plans>) msg.obj;
                    String id=list.get(0).getObjectId();
                    update(id);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_plan);
        initview();
    }

    private void initview() {
        mflesson= (Spinner) findViewById(R.id.up_freelessonnum);
        mlevel= (Spinner) findViewById(R.id.up_level);
        mmain= (Spinner) findViewById(R.id.up_main);
        mfinish= (Button) findViewById(R.id.update_plan_finish);
        circularButton1= (CircularProgressButton) findViewById(R.id.update_plan_submit);
        mrecord= (records) getIntent().getSerializableExtra("record");
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
                String[] frees = getResources().getStringArray(R.array.frlesson);
                freelesson = frees[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        circularButton1.setIndeterminateProgressMode(true);
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() == 0) {
                    circularButton1.setProgress(50);
                    BmobQuery<plans> query = new BmobQuery<plans>();
                    query.addWhereEqualTo("name", mrecord.getName());
                    query.findObjects(new FindListener<plans>() {
                        @Override
                        public void done(List<plans> list, BmobException e) {
                            Message message = handler.obtainMessage();
                            message.what = 0;
                            message.obj = list;
                            handler.sendMessage(message);

                        }
                    });
                } else if (circularButton1.getProgress() == 100) {
                    circularButton1.setProgress(0);
                } else {
                    circularButton1.setProgress(100);
                }
            }
        });
    }
    public void update(String id){
        plans plan=new plans();
        plan.setFreelessonnum(Integer.parseInt(freelesson));
        plan.setLevel(level);
        plan.setMain(main);
        plan.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(update_plan.this, "修改成功", Toast.LENGTH_LONG).show();
                    circularButton1.setProgress(100);
                }else {
                    Toast.makeText(update_plan.this, "修改失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
