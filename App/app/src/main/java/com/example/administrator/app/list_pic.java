package com.example.administrator.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bean.dayrecords;
import bean.select;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Administrator on 2017/1/29 0029.
 */
public class list_pic extends Activity {
    private Spinner mselect;
    private int[] month=new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
    private LineChartView lineChart;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    List<dayrecords> list= (List<dayrecords>) msg.obj;
                    Log.e("11",list.get(0).getStart_sthour()+"");
                    int i=daysize(list);
                    String [] a=new String[i];
                    a=adddate(a, list);
                    int [] b=new int[i];
                    break;
                case 1:
                    List<select> list1= (List<select>) msg.obj;
                    String[] x=new String[list1.size()];
                    int[] y=new int[list1.size()];
                    for(int j=0;j<list1.size();j++){
                        x[j]=list1.get(j).getDate();
                        y[j]=list1.get(j).getEnd()-list1.get(j).getStart();
                        Log.e("111",list1.get(j).getEnd()-list1.get(j).getStart()+"");
                    }
                    getAxisXLables(x);
                    getAxisPoints(y);
                    initLineChart();


            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_pic);
        lineChart= (LineChartView) findViewById(R.id.list_pic_zhe);
        mselect= (Spinner) findViewById(R.id.list_pic_select);
        mselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPointValues.clear();
                mAxisXValues.clear();
                String[] mains = getResources().getStringArray(R.array.main);
                selectdata(mains[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    //计算时间间隔包含当天
    public int daysize(List<dayrecords> list){
        Calendar aCalendar = Calendar.getInstance();
        String d1="2017-"+list.get(0).getMonth()+"-"+list.get(0).getDay();
        String d2="2017-"+list.get(list.size()-1).getMonth()+"-"+list.get(list.size()-1).getDay();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            aCalendar.setTime(date1);
            int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
            aCalendar.setTime(date2);
            int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
            return day2-day1+1;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }


    }
    public String[] adddate(String[] a,List<dayrecords> list){
        int monthsize=0,day=list.get(0).getDay(),monthday=list.get(0).getMonth();
        for(int j=0;j<a.length;j++){
            switch (list.get(0).getMonth()){
                case 1:monthsize=month[0];
                    break;
                case 2:monthsize=month[1];
                    break;
                case 3:monthsize=month[2];
                    break;
                case 4:monthsize=month[3];
                    break;
                case 5:monthsize=month[4];
                    break;
                case 6:monthsize=month[5];
                    break;
                case 7:monthsize=month[6];
                    break;
                case 8:monthsize=month[7];
                    break;
                case 9:monthsize=month[8];
                    break;
                case 10:monthsize=month[9];
                    break;
                case 11:monthsize=month[10];
                    break;
                case 12:monthsize=month[11];
                    break;
            }
            if(day<=monthsize){
                a[j]=monthday+"月"+day;
                day++;
            }else{
                day=1;
                monthday++;
            }
        }
        return a;
    }
    public void selectdata(String main){
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        BmobQuery<dayrecords> query1=new BmobQuery<dayrecords>();
        query1.addWhereEqualTo("name", name);
        BmobQuery<dayrecords> query2=new BmobQuery<dayrecords>();
        query2.addWhereEqualTo("xiangmu", main);
        List<BmobQuery<dayrecords>> queries = new ArrayList<BmobQuery<dayrecords>>();
        queries.add(query1);
        queries.add(query2);
        BmobQuery<dayrecords> query = new BmobQuery<dayrecords>();
        query.and(queries);
        query.sum(new String[]{"start_sthour","end_our"});
        query.groupby(new String[]{"createdAt"});
        query.order("createdAt");
        query.findStatistics(dayrecords.class, new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if(e==null){
                    Log.e("lenth1",""+ jsonArray.length());
                    if(jsonArray!=null){
                        int length = jsonArray.length();
                        Log.e("lenth",""+length);
                        try {
                            List<select> list=new ArrayList<select>();
                            for (int i = 0; i < length; i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                select s=new select();
                                s.setStart(obj.getInt("_sumStart_sthour"));
                                s.setEnd(obj.getInt("_sumEnd_our"));
                                s.setDate(obj.getString("createdAt"));
                                list.add(i,s);
                            }
                            Message message = handler.obtainMessage();
                            message.what = 1;
                            message.obj = list;
                            handler.sendMessage(message);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Toast.makeText(list_pic.this, "暂无记录", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e("log","查询成功，无数据");
                    }
                }else{
                    Log.e("loge", String.valueOf(e));
                }
            }
        });

    }
    private void getAxisXLables(String[] x){
        for (int i = 0; i < x.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(x[i]));
        }
    }
    private void getAxisPoints(int[] a) {
        for (int i = 0; i < a.length; i++) {
            mPointValues.add(new PointValue(i, a[i]));
        }
    }
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));//折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("日期");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("时间");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }
}
