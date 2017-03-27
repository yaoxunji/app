package com.example.administrator.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bean.dayrecords;
import bean.records;
import bean.select;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class record extends Activity {
    private Spinner mstart, mend, mxiangmu;
    private String xiangmu;
    private int start, end;
    private List<select> s=new ArrayList<select>();
    private CircularProgressButton circularButton1;
    private int time;
    private AlertDialog dialog;
    private TextView mdate;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<records> list = (List<records>) msg.obj;
                    String id = list.get(0).getObjectId();
                    break;
                case 1:
                    List<records> list1 = (List<records>) msg.obj;
                    String id1 = list1.get(0).getObjectId();
                    records rc=new records();
                    rc.setActivity_per(list1.get(0).getActivity_per());
                    rc.setFun_per(list1.get(0).getFun_per());
                    rc.setSport_per(list1.get(0).getSport_per());
                    rc.setStudy_per(list1.get(0).getStudy_per());
                    rc.setName(list1.get(0).getName());
                    rc.setEmail(list1.get(0).getEmail());
                    rc.setFun_time(list1.get(0).getFun_time());
                    rc.setStudy_time(list1.get(0).getStudy_time());
                    rc.setActivity_time(list1.get(0).getActivity_time());
                    rc.setSport_time(list1.get(0).getSport_time());
                    rc.setFreelesson_time(list1.get(0).getFreelesson_time());
                    update(id1,rc);
                    break;
                case 2:
                    List<dayrecords> list2= (List<dayrecords>) msg.obj;
                    for(int i=0;i<list2.size();i++){
                        select select=new select();
                        select.setStart(list2.get(i).getStart_sthour());
                        select.setEnd(list2.get(i).getEnd_our());
                        s.add(select);
                        Log.e("aaa",list2.get(i).getStart_sthour()+" "+list2.get(i).getEnd_our());
                    }
                    dialog.dismiss();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.record);
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mview=inflater.inflate(R.layout.progressbar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(record.this);
        builder.setView(mview);
        dialog = builder.create();
        dialog.show();
        mstart = (Spinner) findViewById(R.id.start_time);
        mend = (Spinner) findViewById(R.id.end_time);
        mxiangmu = (Spinner) findViewById(R.id.xiangmu);
        circularButton1 = (CircularProgressButton) findViewById(R.id.record_submit);
        mdate = (TextView) findViewById(R.id.record_date);
        mstart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView v1=(TextView)view;
                v1.setTextColor(Color.BLACK);
                selectcheck();
                String[] starts = getResources().getStringArray(R.array.time);
                start = Integer.parseInt(starts[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        mend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView v1=(TextView)view;
                v1.setTextColor(Color.BLACK);
                selectcheck();
                String[] ends = getResources().getStringArray(R.array.time);
                end = Integer.parseInt(ends[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        mxiangmu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView v1 = (TextView) view;
                v1.setTextColor(Color.BLACK);
                String[] xiangmus = getResources().getStringArray(R.array.main);
                xiangmu = xiangmus[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        new dateTask().execute("");
        //final CircularProgressButton circularButton1 = (CircularProgressButton) findViewById(R.id.record_submit);
        circularButton1.setIndeterminateProgressMode(true);
        circularButton1.setProgress(0);
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() == 0) {
                    circularButton1.setProgress(50);
                    SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                    String name = sharedPreferences.getString("name", "");
                    int s = start;
                    int e = end;
                    if (start >= end) {
                        Toast.makeText(record.this, "设置错误", Toast.LENGTH_LONG).show();
                        circularButton1.setProgress(0);
                    }else if(check(s,e)) {
                        Toast.makeText(record.this, "时间段重复", Toast.LENGTH_LONG).show();
                        circularButton1.setProgress(0);
                    } else {
                        selectrecord();
                        time = (e - s) * 60;
                        String x = xiangmu;
                        Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH) + 1;
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int week = c.get(Calendar.DAY_OF_WEEK);
                        int week1 = 0;
                        switch (week) {
                            case 1:
                                week1 = 7;
                                break;
                            case 2:
                                week1 = 1;
                                break;
                            case 3:
                                week1 = 2;
                                break;
                            case 4:
                                week1 = 3;
                                break;
                            case 5:
                                week1 = 4;
                                break;
                            case 6:
                                week1 = 5;
                                break;
                            case 7:
                                week1 = 6;
                                break;

                        }
                        final dayrecords dayrecords = new dayrecords();
                        dayrecords.setYear(year);
                        dayrecords.setMonth(month);
                        dayrecords.setDay(day);
                        dayrecords.setWeek(week1);
                        dayrecords.setStart_sthour(s);
                        dayrecords.setEnd_our(e);
                        dayrecords.setXiangmu(x);
                        dayrecords.setName(name);
                        dayrecords.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    BmobQuery<records> query = new BmobQuery<records>();
                                    query.addWhereEqualTo("name", dayrecords.getName());
                                    query.findObjects(new FindListener<records>() {
                                        @Override
                                        public void done(List<records> list, BmobException e) {
                                            if (e == null) {
                                                Message message = handler.obtainMessage();
                                                message.what = 0;
                                                message.obj = list;
                                                handler.sendMessage(message);
                                            } else {
                                                Log.e("查询id", "");
                                            }
                                        }
                                    });


                                } else {
                                    Toast.makeText(record.this, "保存失败1", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    }else if (circularButton1.getProgress() == 100) {
                        circularButton1.setProgress(0);
                    } else {
                        circularButton1.setProgress(0);
                    }
                }

        });

    }

    public void update(String id,records mrecord) {
        if (xiangmu.equals("活动")) {
            int uptime = time + mrecord.getActivity_time();
            mrecord.setActivity_time(uptime);
            mrecord.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(record.this, "保存成功", Toast.LENGTH_LONG).show();
                        circularButton1.setProgress(100);
                    } else {
                        Toast.makeText(record.this, "保存失败2", Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else if (xiangmu.equals("娱乐")) {
            int uptime = time + mrecord.getFun_time();
            mrecord.setFun_time(uptime);
            mrecord.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(record.this, "保存成功", Toast.LENGTH_LONG).show();
                        circularButton1.setProgress(100);
                    } else {
                        Toast.makeText(record.this, "保存失败2", Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else if (xiangmu.equals("运动")) {
            int uptime = time + mrecord.getSport_time();
            mrecord.setSport_time(uptime);
            mrecord.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(record.this, "保存成功", Toast.LENGTH_LONG).show();
                        circularButton1.setProgress(100);
                    } else {
                        Toast.makeText(record.this, "保存失败2", Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else {
            int uptime = time + mrecord.getStudy_time();
            mrecord.setStudy_time(uptime);
            mrecord.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(record.this, "保存成功", Toast.LENGTH_LONG).show();
                        circularButton1.setProgress(100);
                    } else {
                        Toast.makeText(record.this, "保存失败2", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }
    }
    public void selectrecord(){
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        BmobQuery<records> query=new BmobQuery<records>();
        query.addWhereEqualTo("name",name);
        query.findObjects(new FindListener<records>() {
            @Override
            public void done(List<records> list, BmobException e) {
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = list;
                handler.sendMessage(message);
            }
        });
    }
    class dateTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            mdate.setText(aVoid);
        }

        @Override
        protected String doInBackground(String... strings) {
            Calendar c = Calendar.getInstance();
            String year = Integer.toString(c.get(Calendar.YEAR));
            String month = "0" + Integer.toString(c.get(Calendar.MONTH) + 1);
            String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
            //String week=Integer.toString(c.get(Calendar.DAY_OF_WEEK));
            Log.e("year", year);
            Log.e("month", month);
            String date = year + month + day;
            Log.e("date", date);

            return date;
        }
    }
    public void selectcheck(){
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        BmobQuery<dayrecords> query1=new BmobQuery<dayrecords>();
        query1.addWhereEqualTo("name",name);
        Calendar c = Calendar.getInstance();
        int year =c.get(Calendar.YEAR);
        int  month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        BmobQuery<dayrecords> query2=new BmobQuery<dayrecords>();
        query2.addWhereEqualTo("year",year);
        BmobQuery<dayrecords> query3=new BmobQuery<dayrecords>();
        query3.addWhereEqualTo("month",month);
        BmobQuery<dayrecords> query4=new BmobQuery<dayrecords>();
        query4.addWhereEqualTo("day", day);
        List<BmobQuery<dayrecords>> queries = new ArrayList<BmobQuery<dayrecords>>();
        queries.add(query1);
        queries.add(query2);
        queries.add(query3);
        queries.add(query4);
        BmobQuery<dayrecords> query=new BmobQuery<dayrecords>();
        query.and(queries);
        query.findObjects(new FindListener<dayrecords>() {
            @Override
            public void done(List<dayrecords> list, BmobException e) {
                if(e==null){
                    Message message = handler.obtainMessage();
                    message.what = 2;
                    message.obj = list;
                    handler.sendMessage(message);
                }
            }
        });
    }
    public boolean check(int start1,int end1){
        for(int i=0;i<s.size();i++){
            boolean b=start1<s.get(i).getEnd() && s.get(i).getStart()<start1;
            boolean b2=end1<s.get(i).getEnd() && s.get(i).getStart()<end1;
            Log.e("check1",b+" ");
            Log.e("check2",b2+" ");
            if((start1<s.get(i).getEnd() && s.get(i).getStart()<start1)||(end1<s.get(i).getEnd() && s.get(i).getStart()<end1))
                return true;
        }
        return false;
    }
}
