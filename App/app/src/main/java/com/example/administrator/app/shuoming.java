package com.example.administrator.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class shuoming extends Activity {
    private Context mcontext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shuoming);
        mcontext=this;

    }
    public void submit(View view){
        Intent intent=new Intent(mcontext,zhuce.class);
        startActivity(intent);
        finish();

    }
}
