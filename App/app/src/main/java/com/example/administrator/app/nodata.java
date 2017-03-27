package com.example.administrator.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by Administrator on 2017/2/22 0022.
 */
public class nodata extends Activity {
    private Button mgorecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.nodata);
        mgorecord= (Button) findViewById(R.id.go_record);
        mgorecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(nodata.this,record.class);
                startActivity(intent);
            }
        });
    }
}
