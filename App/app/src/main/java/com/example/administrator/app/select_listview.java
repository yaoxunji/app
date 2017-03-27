package com.example.administrator.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import bean.dayrecords;
import bean.show_listview;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dao.MyAdapte;

public class select_listview extends Activity {
    private ListView mListview;
    private List<show_listview> mDatas;
    private MyAdapte myAdapte;
    private Button mchange;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    List<dayrecords> list= (List<dayrecords>) msg.obj;
                    List<show_listview> show_listviews=new ArrayList<show_listview>();
                    for(int i=0;i<list.size();i++){
                        show_listview sl=new show_listview();
                        sl.setXiangmu(list.get(i).getXiangmu());
                        String year= String.valueOf(list.get(i).getYear());
                        String month= String.valueOf(list.get(i).getMonth());
                        String day= String.valueOf(list.get(i).getDay());
                        String date=year+"-"+month+"-"+day;
                        sl.setDate(date);
                        sl.setStart(String.valueOf(list.get(i).getStart_sthour()));
                        sl.setEnd(String.valueOf(list.get(i).getEnd_our()));
                        show_listviews.add(sl);

                    }
                    initDatas(show_listviews);
                    initView();
                    break;
                case 1:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_listview);
        mchange= (Button) findViewById(R.id.listview_change);
        mchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(select_listview.this,list_pic.class);
                startActivity(intent);
            }
        });
        initFloatButton();
        select();

    }

    public void  initFloatButton(){

        FloatingActionButton Button1 = (FloatingActionButton) findViewById(R.id.action_a);
        FloatingActionButton Button2 = (FloatingActionButton) findViewById(R.id.action_b);
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(select_listview.this,record.class);
                startActivity(intent);
            }
        });
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(select_listview.this,future.class);
                startActivity(intent);
            }
        });

        final View actionB = findViewById(R.id.action_b);
        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("隐藏/显示更多");
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        menuMultipleActions.addButton(actionC);
    }
    private void select() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH)+1;
        BmobQuery<dayrecords> query2=new BmobQuery<dayrecords>();
        query2.addWhereEqualTo("name",name);
        BmobQuery<dayrecords> query1=new BmobQuery<dayrecords>();
        query1.addWhereEqualTo("month", month);
        List<BmobQuery<dayrecords>> queries = new ArrayList<BmobQuery<dayrecords>>();
        queries.add(query2);
        queries.add(query1);
        BmobQuery<dayrecords> query=new BmobQuery<dayrecords>();
        query.and(queries);
        query.order("-createdAt");
        query.findObjects(new FindListener<dayrecords>() {
            @Override
            public void done(List<dayrecords> list, BmobException e) {
                Message message = handler.obtainMessage();
                message.what = 0;
                message.obj = list;
                handler.sendMessage(message);

            }
        });

    }

    private void initView() {
        mListview= (ListView) findViewById(R.id.id_listview);
        mListview.setAdapter(myAdapte);

    }
    private void initDatas(List<show_listview> show_listviews) {
        myAdapte=new MyAdapte(this,show_listviews);
    }

}
