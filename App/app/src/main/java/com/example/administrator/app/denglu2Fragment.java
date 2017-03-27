package com.example.administrator.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

import bean.records;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Administrator on 2016/10/4 0004.
 */
public class denglu2Fragment extends Fragment {
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ScrollView mScrollView;


    private PieChartView chart;
    private PieChartData data;

    private boolean hasLabels = false;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean hasCenterText1 = false;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;

    private TextView mSport,mActivity,mFun,mStudy;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    List<records> list= (List<records>) msg.obj;
                    Log.e("list",list.get(0).getName());
                    int a[]=new int[4];
                    int sum=list.get(0).getActivity_time()+list.get(0).getStudy_time()+list.get(0).getSport_time()+list.get(0).getFun_time();
                    if(sum==0){
                        sum++;
                    }
                    a[0]= 100*list.get(0).getActivity_time()/sum;
                    a[1]= 100*list.get(0).getStudy_time()/sum;
                    a[2]= 100*list.get(0).getSport_time()/sum;
                    a[3]= 100*list.get(0).getFun_time()/sum;
                    Log.e("handler", "" + a[0] + "" + a[1] + "" + a[2] + "" + a[3]);
                    generateData(a);
                    new MyAsynctask1().execute(a[0]);
                    new MyAsynctask2().execute(a[1]);
                    new MyAsynctask3().execute(a[2]);
                    new MyAsynctask4().execute(a[3]);

                    break;
            }

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.denglu2, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        mPullRefreshScrollView = (PullToRefreshScrollView) rootView.findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel("lastUpdateLabel");
        mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("PULLLABLE");
        mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("refreshingLabel");
        mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("releaseLabel");
        mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //执行刷新函数
                new GetDataTask().execute();
            }
        });
        mActivity= (TextView) rootView.findViewById(R.id.denglu2_activity);
        mSport= (TextView) rootView.findViewById(R.id.denglu2_sport);
        mFun= (TextView) rootView.findViewById(R.id.denglu2_fun);
        mStudy= (TextView) rootView.findViewById(R.id.denglu2_study);
        chart=(PieChartView)rootView.findViewById(R.id.bing_chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        selectpercent(name);
        return rootView;
    }



    private class GetDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
            mPullRefreshScrollView.onRefreshComplete();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
            String name = sharedPreferences.getString("name", "");
            selectpercent(name);
            return null;
        }
    }
    class MyAsynctask1 extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected void onPostExecute(Integer integer) {
            Log.e("post",""+integer);
            mActivity.setText("活动所占比例为" + integer);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int a=integers[0];
            Log.e("back",""+a);
            return a;
        }
    }
    class MyAsynctask2 extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected void onPostExecute(Integer integer) {
            mStudy.setText("学习所占比例为" + integer);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int a=integers[0];
            return a;
        }
    }
    class MyAsynctask3 extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected void onPostExecute(Integer integer) {
            mSport.setText("运动所占比例为" + integer);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int a=integers[0];
            return a;
        }
    }
    class MyAsynctask4 extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected void onPostExecute(Integer integer) {
            mFun.setText("娱乐所占比例为" + integer);

        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int a=integers[0];
            return a;
        }
    }

    private void selectpercent(String name) {
        BmobQuery<records> query=new BmobQuery<records>();
        query.addWhereEqualTo("name",name);
        query.findObjects(new FindListener<records>() {
            @Override
            public void done(List<records> list, BmobException e) {
                if(e==null){
                    Log.e("size",""+list.size());
                    if(list.size() == 0){
                        Intent intent=new Intent(getActivity(),nodata.class);
                        startActivity(intent);
                    }else {
                        Message message = handler.obtainMessage();
                        message.what = 0;
                        message.obj = list;
                        handler.sendMessage(message);
                    }

                }else {
                    Log.e("Bmob","查询饼图");
                }
            }
        });
    }

    private void generateData(int[] a) {
        int numValues = 4;
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < numValues; ++i) {
            SliceValue sliceValue = new SliceValue(a[i], ChartUtils.nextColor());
            //sliceValue.setLabel(a[i]+"%");
            values.add(sliceValue);
        }

        data = new PieChartData(values);
        data.setHasCenterCircle(true);
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(false);//不用点击显示占的百分比
        data.setHasLabelsOutside(false);//占的百分比是否显示在饼图外面
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        if (isExploded) {
            data.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            data.setCenterText1("Hello!");

            // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Typeface(tf);

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
        }

        if (hasCenterText2) {
            data.setCenterText2("Charts (Roboto Italic)");

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
        }

        chart.setPieChartData(data);
    }

    private void explodeChart() {
        isExploded = !isExploded;
        //generateData();

    }

    private void toggleLabelsOutside() {
        // has labels have to be true:P
        hasLabelsOutside = !hasLabelsOutside;
        if (hasLabelsOutside) {
            hasLabels = true;
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);
        }

        if (hasLabelsOutside) {
            chart.setCircleFillRatio(0.7f);
        } else {
            chart.setCircleFillRatio(1.0f);
        }

        //generateData();

    }

    private void toggleLabels() {
        hasLabels = !hasLabels;

        if (hasLabels) {
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelsOutside) {
                chart.setCircleFillRatio(0.7f);
            } else {
                chart.setCircleFillRatio(1.0f);
            }
        }

        //generateData();
    }

    private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;

        chart.setValueSelectionEnabled(hasLabelForSelected);

        if (hasLabelForSelected) {
            hasLabels = false;
            hasLabelsOutside = false;

            if (hasLabelsOutside) {
                chart.setCircleFillRatio(0.7f);
            } else {
                chart.setCircleFillRatio(1.0f);
            }
        }

        //generateData();
    }


    private void prepareDataAnimation() {
        for (SliceValue value : data.getValues()) {
            value.setTarget((float) Math.random() * 30 + 15);
        }
    }

    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            switch (arcIndex){
                case 0:
                    Toast.makeText(getActivity(), "活动占比例 "+value, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getActivity(), "学习占比例 "+value, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getActivity(), "运动占比例 "+value, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getActivity(), "娱乐占比例 "+value, Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
    /* // MENU
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.pie_chart, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_reset) {
                reset();
                generateData();
                return true;
            }
            if (id == R.id.action_explode) {
                explodeChart();
                return true;
            }
            if (id == R.id.action_center_circle) {
                hasCenterCircle = !hasCenterCircle;
                if (!hasCenterCircle) {
                    hasCenterText1 = false;
                    hasCenterText2 = false;
                }

                generateData();
                return true;
            }
            if (id == R.id.action_center_text1) {
                hasCenterText1 = !hasCenterText1;

                if (hasCenterText1) {
                    hasCenterCircle = true;
                }

                hasCenterText2 = false;

                generateData();
                return true;
            }
            if (id == R.id.action_center_text2) {
                hasCenterText2 = !hasCenterText2;

                if (hasCenterText2) {
                    hasCenterText1 = true;// text 2 need text 1 to by also drawn.
                    hasCenterCircle = true;
                }

                generateData();
                return true;
            }
            if (id == R.id.action_toggle_labels) {
                toggleLabels();
                return true;
            }
            if (id == R.id.action_toggle_labels_outside) {
                toggleLabelsOutside();
                return true;
            }
            if (id == R.id.action_animate) {
                prepareDataAnimation();
                chart.startDataAnimation();
                return true;
            }
            if (id == R.id.action_toggle_selection_mode) {
                toggleLabelForSelected();
                Toast.makeText(getActivity(),
                        "Selection mode set to " + chart.isValueSelectionEnabled() + " select any point.",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }*/

}


