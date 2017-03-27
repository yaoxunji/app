package com.example.administrator.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class picture extends Activity {
    private ColumnChartView mColumnChartView;
    private ColumnChartData mColumnChartData;
    private static final int DEFAULT_DATA = 0;
    private static final int SUBCOLUMNS_DATA = 1;
    private static final int STACKED_DATA = 2;
    private static final int NEGATIVE_SUBCOLUMNS_DATA = 3;
    private static final int NEGATIVE_STACKED_DATA = 4;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = true;
    private boolean hasLabelForSelected = false;
    private int dataType = DEFAULT_DATA;
    private int selecttag=1;
    private AlertDialog dialog;

    private records mrecord=new records();
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
                    Log.e("size",list1.size()+"");
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
                        generateDefaultData(timemount);
                    }
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.picture);
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mview=inflater.inflate(R.layout.progressbar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(picture.this);
        builder.setView(mview);
        dialog = builder.create();
        dialog.show();
        initFloatButton();
        //mrecord= (records) getIntent().getSerializableExtra("record");
        mColumnChartView = (ColumnChartView) findViewById(R.id.chart);
        mColumnChartView.setOnValueTouchListener(new ValueTouchListener());
        selectmain();



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
                            Intent intent=new Intent(picture.this,nodata.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }else {
                            Message message = handler.obtainMessage();
                            message.what = selecttag;
                            message.obj = list;
                            handler.sendMessage(message);
                            selecttag++;
                            dialog.dismiss();
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
                            Intent intent=new Intent(picture.this,nodata.class);
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

    private void generateData() {
        switch (dataType) {
            case DEFAULT_DATA:
                //generateDefaultData();
                break;
            case SUBCOLUMNS_DATA:
                generateSubcolumnsData();
                break;
            case STACKED_DATA:
                generateStackedData();
                break;
            case NEGATIVE_SUBCOLUMNS_DATA:
                generateNegativeSubcolumnsData();
                break;
            case NEGATIVE_STACKED_DATA:
                generateNegativeStackedData();
                break;
            default:
                //generateDefaultData();
                break;
        }
    }

    private void generateDefaultData(int[] a) {
        int numSubcolumns = 1;//设置每个柱状图显示的颜色数量(每个柱状图显示多少块)
        int numColumns = 7;//柱状图的数量
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                SubcolumnValue value = new SubcolumnValue(a[i], ChartUtils.nextColor());
                values.add(value);
            }
            Column column = new Column(values);//一个柱状图的实例
            column.setHasLabels(hasLabels);//设置是否显示每个柱状图的高度，
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);//点击的时候是否显示柱状图的高度，和setHasLabels()和用的时候，setHasLabels()失效
            columns.add(column);
        }
        mColumnChartData = new ColumnChartData(columns);//表格的数据实例
        if (hasAxes) {
            Axis axisX = new Axis();
            //   axisX.setInside(true);//是否显示在里面，默认为false
            AxisValue value1 = new AxisValue(0f);//值是在哪显示 0 是指 第0个柱状图
            value1.setLabel("周一");//设置显示的文本，默认为柱状图的位置
            AxisValue value2 = new AxisValue(1.0f);
            value2.setLabel("周二");
            AxisValue value3 = new AxisValue(2.0f);
            value3.setLabel("周三");
            AxisValue value4 = new AxisValue(3.0f);
            value4.setLabel("周四");
            AxisValue value5 = new AxisValue(4.0f);
            value5.setLabel("周五");
            AxisValue value6 = new AxisValue(5.0f);
            value6.setLabel("周六");
            AxisValue value7 = new AxisValue(6.0f);
            value7.setLabel("周日");
            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            axisValues.add(value1);
            axisValues.add(value2);
            axisValues.add(value3);
            axisValues.add(value4);
            axisValues.add(value5);
            axisValues.add(value6);
            axisValues.add(value7);
            axisX.setValues(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("一周时间记录");//设置X轴的注释
                axisY.setName("时间");//设置Y轴的注释
            }
            mColumnChartData.setAxisXBottom(axisX);//设置X轴显示的位置
            mColumnChartData.setAxisYLeft(axisY);//设置Y轴显示的位置
        } else {
            mColumnChartData.setAxisXBottom(null);
            mColumnChartData.setAxisYLeft(null);
        }
        mColumnChartView.setColumnChartData(mColumnChartData);//为View设置数据
        dialog.dismiss();

    }
    public void  initFloatButton(){

        FloatingActionButton Button1 = (FloatingActionButton) findViewById(R.id.pic_action_a);
        FloatingActionButton Button2 = (FloatingActionButton) findViewById(R.id.pic_action_b);
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(picture.this,record.class);
                startActivity(intent);
            }
        });
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(picture.this,future.class);
                startActivity(intent);
            }
        });

        final View actionB = findViewById(R.id.pic_action_b);
        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("隐藏/显示更多");
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.pic_multiple_actions);
        menuMultipleActions.addButton(actionC);
    }
    /**
     * Generates columns with subcolumns, columns have larger separation than subcolumns.
     */
    private void generateSubcolumnsData() {
        int numSubcolumns = 4;
        int numColumns = 4;
        // Column can have many subcolumns, here I use 4 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }
        mColumnChartData = new ColumnChartData(columns);
        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            mColumnChartData.setAxisXBottom(axisX);
            mColumnChartData.setAxisYLeft(axisY);
        } else {
            mColumnChartData.setAxisXBottom(null);
            mColumnChartData.setAxisYLeft(null);
        }
        mColumnChartView.setColumnChartData(mColumnChartData);
        dialog.dismiss();
    }
    /**
     * Generates columns with stacked subcolumns.
     */
    private void generateStackedData() {
        int numSubcolumns = 4;
        int numColumns = 8;
        // Column can have many stacked subcolumns, here I use 4 stacke subcolumn in each of 4 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 20f + 5, ChartUtils.pickColor()));
            }
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }
        mColumnChartData = new ColumnChartData(columns);
        // Set stacked flag.
        mColumnChartData.setStacked(true);
        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");//设置X轴的注释
                axisY.setName("Axis Y");//设置Y轴的注释
            }
            mColumnChartData.setAxisXBottom(axisX);//设置X轴显示的位置
            mColumnChartData.setAxisYLeft(axisY);//设置Y轴显示的位置
        } else {
            mColumnChartData.setAxisXBottom(null);
            mColumnChartData.setAxisYLeft(null);
        }
        mColumnChartView.setColumnChartData(mColumnChartData);
    }
    private void generateNegativeSubcolumnsData() {
        int numSubcolumns = 4;
        int numColumns = 4;
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                int sign = getSign();
                values.add(new SubcolumnValue((float) Math.random() * 50f * sign + 5 * sign, ChartUtils.pickColor
                        ()));
            }
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }
        mColumnChartData = new ColumnChartData(columns);
        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            mColumnChartData.setAxisXBottom(axisX);
            mColumnChartData.setAxisYLeft(axisY);
        } else {
            mColumnChartData.setAxisXBottom(null);
            mColumnChartData.setAxisYLeft(null);
        }
        mColumnChartView.setColumnChartData(mColumnChartData);
    }
    private void generateNegativeStackedData() {
        int numSubcolumns = 4;
        int numColumns = 8;
        // Column can have many stacked subcolumns, here I use 4 stacke subcolumn in each of 4 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                int sign = getSign();
                values.add(new SubcolumnValue((float) Math.random() * 20f * sign + 5 * sign, ChartUtils.pickColor()));
            }
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }
        mColumnChartData = new ColumnChartData(columns);
        // Set stacked flag.
        mColumnChartData.setStacked(true);
        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            mColumnChartData.setAxisXBottom(axisX);
            mColumnChartData.setAxisYLeft(axisY);
        } else {
            mColumnChartData.setAxisXBottom(null);
            mColumnChartData.setAxisYLeft(null);
        }
        mColumnChartView.setColumnChartData(mColumnChartData);
    }
    private int getSign() {
        int[] sign = new int[]{-1, 1};
        return sign[Math.round((float) Math.random())];
    }
    private void toggleLabels() {
        hasLabels = !hasLabels;
        if (hasLabels) {
            hasLabelForSelected = false;
            mColumnChartView.setValueSelectionEnabled(hasLabelForSelected);
        }
        generateData();
    }
    private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;
        mColumnChartView.setValueSelectionEnabled(hasLabelForSelected);
        if (hasLabelForSelected) {
            hasLabels = false;
        }
        generateData();
    }
    private void toggleAxes() {
        hasAxes = !hasAxes;
        generateData();
    }
    private void toggleAxesNames() {
        hasAxesNames = !hasAxesNames;
        generateData();
    }

    private void prepareDataAnimation() {
        for (Column column : mColumnChartData.getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                value.setTarget((float) Math.random() * 100);
            }
        }
    }
    private class ValueTouchListener implements ColumnChartOnValueSelectListener {
        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            Toast.makeText(picture.this, "主要方向时间: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
        }
    }

}
