package com.example.administrator.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;


/**
 * Created by Administrator on 2017/2/5 0005.
 */
public class loading extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading);
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 3000);
    }
    class splashhandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(),MainActivity.class));// 这个线程的作用3秒后就是进入主界面
            loading.this.finish();// 把当前的Activity结束掉
        }
    }
}
