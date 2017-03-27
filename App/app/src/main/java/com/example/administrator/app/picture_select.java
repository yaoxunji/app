package com.example.administrator.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import bean.records;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
public class picture_select extends Activity {
    private Button mweek_zhu,mweek_zhe,mmonth_zhu,mreturn;
    private records mrecord=new records();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.picture_slelct);
        mrecord= (records) getIntent().getSerializableExtra("record");
        mmonth_zhu= (Button) findViewById(R.id.month_zhu);
        mweek_zhe= (Button) findViewById(R.id.week_zhe);
        mweek_zhu= (Button) findViewById(R.id.week_zhu);
        mreturn= (Button) findViewById(R.id.tu_return);
        mreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mweek_zhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(picture_select.this,picture.class);
                //intent.putExtra("record", mrecord);
                startActivity(intent);

            }
        });
        mweek_zhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(picture_select.this,picture_zx.class);
                //intent.putExtra("record", mrecord);
                startActivity(intent);
            }
        });
        mmonth_zhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(picture_select.this,picture_month.class);
                //intent.putExtra("record", mrecord);
                startActivity(intent);
            }
        });


    }
}
