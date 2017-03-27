package com.example.administrator.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bean.dayrecords;
import bean.plans;
import bean.records;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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
 * Created by Administrator on 2016/10/25 0025.
 */
public class picture_zx extends Activity {
    private LineChartView lineChart;
    private int selecttag=1;
    private String[] date = {"周一","周二","周三","周四","周五","周六","周日"};//X轴的标注
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private records mrecord=new records();
    private AlertDialog dialog;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    List<plans> list= (List<plans>) msg.obj;
                    String main=list.get(0).getMain();
                    Log.e("main",""+main);
                    selectday(main);
                    break;
                case 1:
                    List<dayrecords> list1= (List<dayrecords>) msg.obj;
                    int[] timemount=new int[7];
                    for(int i=0;i<list1.size();i++){
                        switch (list1.get(i).getWeek()){
                            case 1:
                                timemount[0]+=list1.get(i).getEnd_our()-list1.get(i).getStart_sthour();
                                break;
                            case 2:
                                timemount[1]+=list1.get(i).getEnd_our()-list1.get(i).getStart_sthour();
                                break;
                            case 3:
                                timemount[2]+=list1.get(i).getEnd_our()-list1.get(i).getStart_sthour();
                                break;
                            case 4:
                                timemount[3]+=list1.get(i).getEnd_our()-list1.get(i).getStart_sthour();
                                break;
                            case 5:
                                timemount[4]+=list1.get(i).getEnd_our()-list1.get(i).getStart_sthour();
                                break;
                            case 6:
                                timemount[5]+=list1.get(i).getEnd_our()-list1.get(i).getStart_sthour();
                                break;
                            case 7:
                                timemount[6]+=list1.get(i).getEnd_our()-list1.get(i).getStart_sthour();
                                break;
                        }
                    }
                    getAxisXLables();
                    getAxisPoints(timemount);
                    initLineChart();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.picture_zx);
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mview=inflater.inflate(R.layout.progressbar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(picture_zx.this);
        builder.setView(mview);
        dialog = builder.create();
        dialog.show();
        initFloatButton();
        //mrecord= (records) getIntent().getSerializableExtra("record");
        lineChart = (LineChartView)findViewById(R.id.line_chart);
        selectmain();
        //getAxisXLables();//获取x轴的标注
        //getAxisPoints();//获取坐标点
        //initLineChart();//初始化
    }

    public void  initFloatButton(){

        FloatingActionButton Button1 = (FloatingActionButton) findViewById(R.id.zx_action_a);
        FloatingActionButton Button2 = (FloatingActionButton) findViewById(R.id.zx_action_b);
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(picture_zx.this, record.class);
                startActivity(intent);
            }
        });
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(picture_zx.this, future.class);
                startActivity(intent);
            }
        });

        final View actionB = findViewById(R.id.zx_action_b);
        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("隐藏/显示更多");
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.zx_multiple_actions);
        menuMultipleActions.addButton(actionC);
    }

    private void selectmain() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
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
                    Log.e("bmob", "图查主要");
                }
            }
        });
    }
    private void selectday(String main){
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        BmobQuery<dayrecords> query1=new BmobQuery<dayrecords>();
        query1.addWhereEqualTo("xiangmu",main);
        BmobQuery<dayrecords> query2=new BmobQuery<dayrecords>();
        query2.addWhereEqualTo("name",name);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
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
        int select_day=day-week1+1;
        if(select_day<1){
            int tag=7+select_day-1;//tag值大1
            month=month-1;
            switch(month){
                case 1:
                    select_day+=31;
                    break;
                case 2:
                    select_day+=28;
                    break;
                case 3:
                    select_day+=31;
                    break;
                case 4:
                    select_day+=30;
                    break;
                case 5:
                    select_day+=31;
                    break;
                case 6:
                    select_day+=30;
                    break;
                case 7:
                    select_day+=31;
                    break;
                case 8:
                    select_day+=31;
                    break;
                case 9:
                    select_day+=30;
                    break;
                case 10:
                    select_day+=31;
                    break;
                case 11:
                    select_day+=30;
                    break;
                case 12:
                    select_day+=31;
                    break;

            }
            Log.e("select_day", "" + select_day);
            BmobQuery<dayrecords> query3 = new BmobQuery<dayrecords>();
            query3.addWhereGreaterThanOrEqualTo("day", select_day);
            BmobQuery<dayrecords> query4 = new BmobQuery<dayrecords>();
            query4.addWhereGreaterThanOrEqualTo("month", month);
            List<BmobQuery<dayrecords>> queries = new ArrayList<BmobQuery<dayrecords>>();
            queries.add(query3);
            queries.add(query4);
            BmobQuery<dayrecords> query5 = new BmobQuery<dayrecords>();
            query5.addWhereLessThanOrEqualTo("day", tag);
            BmobQuery<dayrecords> query6 = new BmobQuery<dayrecords>();
            query6.addWhereGreaterThanOrEqualTo("month", month + 1);
            List<BmobQuery<dayrecords>> queries2 = new ArrayList<BmobQuery<dayrecords>>();
            queries2.add(query5);
            queries2.add(query6);
            BmobQuery<dayrecords> query1and=new BmobQuery<dayrecords>();
            query1and.and(queries);
            BmobQuery<dayrecords> query2and=new BmobQuery<dayrecords>();
            query2and.and(queries2);
            List<BmobQuery<dayrecords>> queries3 = new ArrayList<BmobQuery<dayrecords>>();
            queries3.add(query1and);
            queries3.add(query2and);;
            BmobQuery<dayrecords> query3or=new BmobQuery<dayrecords>();
            query3or.or(queries3);
            List<BmobQuery<dayrecords>> queries4 = new ArrayList<BmobQuery<dayrecords>>();
            queries4.add(query1);
            queries4.add(query2);
            queries4.add(query3or);
            BmobQuery<dayrecords> query = new BmobQuery<dayrecords>();
            query.and(queries4);
            query.findObjects(new FindListener<dayrecords>() {
                @Override
                public void done(List<dayrecords> list, BmobException e) {
                    if (e == null) {
                        if(list.size() == 0){
                            Intent intent=new Intent(picture_zx.this,nodata.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }else {
                            Message message = handler.obtainMessage();
                            message.what = 1;
                            message.obj = list;
                            handler.sendMessage(message);
                            //selecttag++;
                        }

                    } else {
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });

        }else {
            BmobQuery<dayrecords> query3 = new BmobQuery<dayrecords>();
            query3.addWhereGreaterThanOrEqualTo("day", select_day);
            BmobQuery<dayrecords> query4 = new BmobQuery<dayrecords>();
            query4.addWhereEqualTo("month", month);
            List<BmobQuery<dayrecords>> queries = new ArrayList<BmobQuery<dayrecords>>();
            queries.add(query1);
            queries.add(query2);
            queries.add(query3);
            queries.add(query4);
            BmobQuery<dayrecords> query = new BmobQuery<dayrecords>();
            query.and(queries);
            query.findObjects(new FindListener<dayrecords>() {
                @Override
                public void done(List<dayrecords> list, BmobException e) {
                    if (e == null) {
                        if(list.size() == 0){
                            Intent intent=new Intent(picture_zx.this,nodata.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }else {
                            Message message = handler.obtainMessage();
                            message.what = selecttag;
                            message.obj = list;
                            handler.sendMessage(message);
                            selecttag++;
                        }

                    } else {
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }



    }


    private void getAxisXLables(){
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }
    /**
     * 图表的每个点的显示
     */
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
        line.setFilled(false);//是否填充曲线的面积
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
        dialog.dismiss();
    }



    }
