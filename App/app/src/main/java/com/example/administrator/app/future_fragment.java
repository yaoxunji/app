package com.example.administrator.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import bean.dayrecords;
import bean.plans;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.BubbleChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Administrator on 2017/2/19 0019.
 */
public class future_fragment extends Fragment {
    private String mlevel;
    private int mlesson;
    private LineChartView lineChart;
    private BubbleChartView bubbleChart;
    private TextView mtextview;
    private String[] date = {"周一","周二","周三","周四","周五","周六","周日"};
    private int[] day=new int[]{0,0,0,0,0,0,0};
    private int[] checkday=new int[7];
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<PointValue> mPointValues2 = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private List<BubbleValue> pointValues = new ArrayList<BubbleValue>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    List<plans> list= (List<plans>) msg.obj;
                    mlevel=list.get(0).getLevel();
                    mlesson=list.get(0).getFreelessonnum();
                    break;
                case 1:
                    List<dayrecords> list2= (List<dayrecords>) msg.obj;
                    for(int i=0;i<list2.size();i++) {
                        switch (list2.get(i).getWeek()){
                            case 1:
                                day[0]+=(list2.get(i).getEnd_our()-list2.get(i).getStart_sthour());
                                break;
                            case 2:
                                day[1]+=(list2.get(i).getEnd_our()-list2.get(i).getStart_sthour());
                                break;
                            case 3:
                                day[2]+=(list2.get(i).getEnd_our()-list2.get(i).getStart_sthour());
                                break;
                            case 4:
                                day[3]+=(list2.get(i).getEnd_our()-list2.get(i).getStart_sthour());
                                break;
                            case 5:
                                day[4]+=(list2.get(i).getEnd_our()-list2.get(i).getStart_sthour());
                                break;
                            case 6:
                                day[5]+=(list2.get(i).getEnd_our()-list2.get(i).getStart_sthour());
                                break;
                            case 7:
                                day[6]+=(list2.get(i).getEnd_our()-list2.get(i).getStart_sthour());
                                break;

                        }
                    }

                    int a=mlesson*2/7;
                    int[] freetime=new int[]{a,a,a,a,a,a,a};
                    getAxisXLables();
                    getAxisPoints(freetime);
                    getAxisPoints2(day);
                    getAxisPoints3(day, freetime);
                    initLineChart();
                    initBubbleChart();
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.future, container, false);
        lineChart = (LineChartView)rootView.findViewById(R.id.future_line_chart);
        bubbleChart = (BubbleChartView)rootView.findViewById(R.id.bubblechart);
        mtextview= (TextView)rootView.findViewById(R.id.future_suggest);
        select_paln();
        getdata();
        new MyAsynctask().execute();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    class MyAsynctask extends AsyncTask<String,Void,String> {
        @Override
        protected void onPostExecute(String s) {
            String str1="",str2="",str3="";
            for(int i=0;i<checkday.length;i++){
                if(checkday[i]==1){
                    str1+=String.valueOf(i);
                }else if(checkday[i]==0){
                    str2+=String.valueOf(i);
                }else {
                    str3+=String.valueOf(i);
                }
            }
            int sum=0;
            if (str2.equals("")) {
                mtextview.setText("本周时间分配完成情况非常好，下周可灵活分配课余时间");
            } else if (str1.equals("")) {
                mtextview.setText("本周时间分配完成情况很不好，没有合理分配时间/n建议下周紧凑分配时间，合理安排琐碎时间");

            } else{
                mtextview.setText("本周时间分配完成情况较好/n，在周"+str1+"课余时间安排充足，在周"+str2+"课余时间分配较少/n建议下周在周"+str2+"合理安排课余时间，可适当减少在周"+str1+"的活动，均匀分配时间");
            }



        }

        @Override
        protected String doInBackground(String... strings) {
            for(int i=0;i<day.length;i++){
                if(day[i]<mlesson*2/7){
                    checkday[i]=0;
                }else if(day[i]>=mlesson*2/7){
                    checkday[i]=1;
                }
            }
            return null;
        }
    }
    private void getdata() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int week=c.get(Calendar.DAY_OF_WEEK);
        int week1=0;
        switch(week) {
            case 1:week1=7;
                break;
            case 2:week1=1;
                break;
            case 3:week1=2;
                break;
            case 4:week1=3;
                break;
            case 5:week1=4;
                break;
            case 6:week1=5;
                break;
            case 7:week1=6;
                break;
        }
        BmobQuery<dayrecords> query1=new BmobQuery<dayrecords>();
        query1.addWhereEqualTo("name",name);
        BmobQuery<dayrecords> query2=new BmobQuery<dayrecords>();
        query2.addWhereEqualTo("month",month);
        int select_day=day-week1+1;
        BmobQuery<dayrecords> query3=new BmobQuery<dayrecords>();
        query3.addWhereGreaterThanOrEqualTo("day", select_day);
        List<BmobQuery<dayrecords>> queries = new ArrayList<BmobQuery<dayrecords>>();
        queries.add(query1);
        queries.add(query2);
        queries.add(query3);
        BmobQuery<dayrecords> query = new BmobQuery<dayrecords>();
        query.and(queries);
        query.findObjects(new FindListener<dayrecords>() {
            @Override
            public void done(List<dayrecords> list, BmobException e) {
                if(e==null){
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.obj = list;
                    handler.sendMessage(message);
                }else{

                }
            }
        });
    }
    private void select_paln() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        BmobQuery<plans> query=new BmobQuery<plans>();
        query.addWhereEqualTo("name", name);
        query.findObjects(new FindListener<plans>() {
            @Override
            public void done(List<plans> list, BmobException e) {
                if (e == null) {
                    Message message = handler.obtainMessage();
                    message.what = 0;
                    message.obj = list;
                    handler.sendMessage(message);

                } else {

                }
            }
        });
    }
    private void getAxisXLables(){
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }
    private void getAxisPoints(int[] a) {
        for (int i = 0; i < a.length; i++) {
            mPointValues.add(new PointValue(i, a[i]));
        }
    }
    private void getAxisPoints2(int[] a) {
        for (int i = 0; i < a.length; i++) {
            mPointValues2.add(new PointValue(i, a[i]));
        }
    }
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));//折线的颜色（橙色）
        Line line2 = new Line(mPointValues2).setColor(Color.parseColor("#FF306F91"));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）

        line2.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line2.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line2.setFilled(false);//是否填充曲线的面积
        line2.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line2.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line2.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）

        lines.add(line);
        lines.add(line2);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("每周时间");  //表格名称
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
    private void getAxisPoints3(int[] a,int[] b){
        for (int i = 0; i < a.length; i++) {
            Random random = new Random();
            switch(i){
                case 0:
                    if(a[i] == 0) {
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (1 + b[i]) / 2, Color.YELLOW));
                    }else{
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (a[i] + b[i]) / 2, Color.YELLOW));
                    }
                    break;
                case 1:
                    if(a[i] == 0) {
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (1 + b[i]) / 2, Color.RED));
                    }else{
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (a[i] + b[i]) / 2, Color.RED));
                    }
                    break;
                case 2:
                    if(a[i] == 0) {
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (1 + b[i]) / 2, Color.BLUE));
                    }else{
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (a[i] + b[i]) / 2, Color.BLUE));
                    }
                    break;
                case 3:
                    if(a[i] == 0) {
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (1 + b[i]) / 2, Color.DKGRAY));
                    }else{
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (a[i] + b[i]) / 2, Color.DKGRAY));
                    }
                    break;
                case 4:
                    if(a[i] == 0) {
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (1 + b[i]) / 2, Color.CYAN));
                    }else{
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (a[i] + b[i]) / 2, Color.CYAN));
                    }
                    break;
                case 5:
                    if(a[i] == 0) {
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (1 + b[i]) / 2, Color.BLACK));
                    }else{
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (a[i] + b[i]) / 2, Color.BLACK));
                    }
                    break;
                case 6:
                    if(a[i] == 0) {
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (1 + b[i]) / 2, Color.YELLOW));
                    }else{
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (a[i] + b[i]) / 2, Color.YELLOW));
                    }
                    break;
                case 7:
                    if(a[i] == 0) {
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (1 + b[i]) / 2, Color.YELLOW));
                    }else{
                        pointValues.add(new BubbleValue(i, random.nextInt(10) * i, (a[i] + b[i]) / 2, Color.YELLOW));
                    }
                    break;
            }

        }
    }
    private void initBubbleChart(){
        Axis axisY = new Axis().setHasLines(true);// Y轴属性
        Axis axisX = new Axis();// X轴属性
        axisY.setName("时间");//设置Y轴显示名称
        axisX.setValues(mAxisXValues);//设置X轴显示名称
        ArrayList<AxisValue> axisValuesX = new ArrayList<AxisValue>();//定义X轴刻度值的数据集合
        ArrayList<AxisValue> axisValuesY = new ArrayList<AxisValue>();//定义Y轴刻度值的数据集合
        axisX.setValues(axisValuesX);//为X轴显示的刻度值设置数据集合
        axisX.setLineColor(Color.BLACK);// 设置X轴轴线颜色
        axisY.setLineColor(Color.BLACK);// 设置Y轴轴线颜色
        axisX.setTextColor(Color.BLACK);// 设置X轴文字颜色
        axisY.setTextColor(Color.BLACK);// 设置Y轴文字颜色
        axisX.setTextSize(14);// 设置X轴文字大小
        axisX.setTypeface(Typeface.DEFAULT);// 设置文字样式，此处为默认
        //axisX.setHasTiltedLabels(bolean isHasTit);// 设置X轴文字向左旋转45度
        axisX.setHasLines(false);// 是否显示X轴网格线
        axisY.setHasLines(false);// 是否显示Y轴网格线
        axisX.setHasSeparationLine(false);// 设置是否有分割线
        axisX.setInside(false);// 设置X轴文字是否在X轴内部
        /*BubbleValue v=new BubbleValue();//定义气泡
        v.set(float x,float y,float z);//设置气泡的横纵坐标x、y，z为气泡的半径
        v.setColor(int color);//设置气泡的颜色
        v.setLabel(String label);//设置气泡中显示的文本
        v.setShape(ValueShape shape);//设置气泡的形状*/
        BubbleChartData bubbleDate=new BubbleChartData(pointValues);//定义气泡图的数据对象
        bubbleDate.setBubbleScale((float) 1);//设置气泡的比例大小
        bubbleDate.setHasLabelsOnlyForSelected(true);//设置文本只有当点击时显示
        bubbleDate.setMinBubbleRadius(0);//设置气泡的最小半径
        bubbleDate.setValueLabelsTextColor(Color.BLACK);// 设置数据文字颜色
        bubbleDate.setValueLabelTextSize(15);// 设置数据文字大小
        bubbleDate.setValueLabelTypeface(Typeface.MONOSPACE);// 设置数据文字样式
        bubbleDate.setAxisYLeft(axisY);// 将Y轴属性设置到左边
        bubbleDate.setAxisXBottom(axisX);// 将X轴属性设置到底部
        //bubbleDate.setAxisYRight(axisYRight);//设置右边显示的轴
        //bubbleDate.setAxisXTop(axisXTop);//设置顶部显示的轴
        bubbleChart.setBubbleChartData(bubbleDate);//将数据设置给气泡图

    }
}
