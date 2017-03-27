package com.example.administrator.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bean.dayrecords;
import bean.plans;
import bean.records;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
public class picture_month extends Activity {
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

    private AlertDialog dialog;
    private Button mselect;
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
                    int[] timemount=new int[4];
                    for(int i=0;i<list1.size();i++){
                        if(list1.get(i).getDay()<=7){
                            timemount[0]+=list1.get(i).getEnd_our()-list1.get(i).getStart_sthour();
                        }else if(list1.get(i).getDay()<=14&&list1.get(i).getDay()>=8){
                            timemount[1]+=list1.get(i).getEnd_our()-list1.get(i).getStart_sthour();
                        }else if(list1.get(i).getDay()<=21&&list1.get(i).getDay()>=15){
                            timemount[2]+=list1.get(i).getEnd_our()-list1.get(i).getStart_sthour();
                        }else {
                            timemount[3]+=list1.get(i).getEnd_our()-list1.get(i).getStart_sthour();
                        }

                    }
                    generateDefaultData(timemount);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.picture_month);
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mview=inflater.inflate(R.layout.progressbar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(picture_month.this);
        builder.setView(mview);
        dialog = builder.create();
        dialog.show();
        initFloatButton();
        mColumnChartView = (ColumnChartView) findViewById(R.id.month_chart);
        mColumnChartView.setOnValueTouchListener(new ValueTouchListener());
        //mrecord= (records) getIntent().getSerializableExtra("record");
        selectmain();

    }

    public void  initFloatButton(){

        FloatingActionButton Button1 = (FloatingActionButton) findViewById(R.id.month_action_a);
        FloatingActionButton Button2 = (FloatingActionButton) findViewById(R.id.month_action_b);
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(picture_month.this, record.class);
                startActivity(intent);
            }
        });
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(picture_month.this,future.class);
                startActivity(intent);
            }
        });

        final View actionB = findViewById(R.id.month_action_b);
        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("隐藏/显示更多");
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.month_multiple_actions);
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
        int month = c.get(Calendar.MONTH)+1;
        BmobQuery<dayrecords> query3=new BmobQuery<dayrecords>();
        query3.addWhereGreaterThanOrEqualTo("month", month);
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
                    if(list.size() == 0){
                        Intent intent=new Intent(picture_month.this,nodata.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }else {
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        message.obj = list;
                        handler.sendMessage(message);
                    }

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


    }


    /*private void generateData() {
        switch (dataType) {
            case DEFAULT_DATA:
                generateDefaultData();
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
                generateDefaultData();
                break;
        }
    }
    */
    /*private void reset() {
        hasAxes = true;
        hasAxesNames = true;
        hasLabels = false;
        hasLabelForSelected = false;
        dataType = DEFAULT_DATA;
        mColumnChartView.setValueSelectionEnabled(hasLabelForSelected);
    }*/
    private void generateDefaultData(int[] a) {
        int numSubcolumns = 1;//设置每个柱状图显示的颜色数量(每个柱状图显示多少块)
        int numColumns = 4;//柱状图的数量
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                SubcolumnValue value = new SubcolumnValue(a[i], ChartUtils.pickColor());//第一个值是数值(值>0 方向朝上，值<0，方向朝下)，第二个值是颜色
                //    SubcolumnValue value = new SubcolumnValue((float) Math.random() * 50f + 5, Color.parseColor("#00000000"));//第一个值是数值，第二个值是颜色
                //    values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
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
            value1.setLabel("1-7号");//设置显示的文本，默认为柱状图的位置
            AxisValue value2 = new AxisValue(1.0f);
            value2.setLabel("8-14号");
            AxisValue value3 = new AxisValue(2.0f);
            value3.setLabel("15-21号");
            AxisValue value4 = new AxisValue(3.0f);
            value4.setLabel("22号后");
            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            axisValues.add(value1);
            axisValues.add(value2);
            axisValues.add(value3);
            axisValues.add(value4);
            axisX.setValues(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("一月时间记录");//设置X轴的注释
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
        //generateData();
    }
    private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;
        mColumnChartView.setValueSelectionEnabled(hasLabelForSelected);
        if (hasLabelForSelected) {
            hasLabels = false;
        }
        //generateData();
    }
    private void toggleAxes() {
        hasAxes = !hasAxes;
        //generateData();
    }
    private void toggleAxesNames() {
        hasAxesNames = !hasAxesNames;
        //generateData();
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
            Toast.makeText(picture_month.this, "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
        }
    }
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        //oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        //oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        //oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        //oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //oks.setSiteUrl("http://sharesdk.cn");

        oks.setTitle("分享标题--Title");
        oks.setTitleUrl("http://mob.com");
        oks.setText("分享测试文--Text");
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");

// 启动分享GUI
        oks.show(this);
        Log.e("share", "share");
    }
}
