package com.example.administrator.app;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import bean.plans;
import bean.records;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dao.Bmob_dao;


/**
 * Created by Administrator on 2016/9/24 0024.
 */
public class login extends FragmentActivity implements View.OnClickListener {

    private LinearLayout mTabsetting;
    private LinearLayout mTabfrd;
    private LinearLayout mTabaddress;

    private ImageButton msettingImg;
    private ImageButton mfrdImg;
    private ImageButton maddressImg;

    private Fragment mTab01;
    private Fragment mTab02;
    private Fragment mTab03;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private String femail;
    private records mrecord=new records();

    private TextView mname;

    private Handler  handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:
                    List<plans> list= (List<plans>) msg.obj;
                    femail=list.get(0).getEmail();
                    selectbyemail(femail);
                    break;
                case 1:
                    List<records> list2= (List<records>) msg.obj;
                    mrecord.setEmail(list2.get(0).getEmail());
                    mrecord.setFun_per(list2.get(0).getFun_per());
                    mrecord.setActivity_per(list2.get(0).getActivity_per());
                    mrecord.setSport_per(list2.get(0).getSport_per());
                    mrecord.setStudy_per(list2.get(0).getStudy_per());
                    mrecord.setActivity_time(list2.get(0).getActivity_time());
                    mrecord.setFreelesson_time(list2.get(0).getFreelesson_time());
                    mrecord.setFun_time(list2.get(0).getFun_time());
                    mrecord.setSport_time(list2.get(0).getSport_time());
                    mrecord.setStudy_time(list2.get(0).getStudy_time());
                    mrecord.setName(list2.get(0).getName());
                    FragmentManager manager = getSupportFragmentManager();
                    dengluFragment fragment=new dengluFragment();
                    FragmentTransaction ft = manager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("record", mrecord);
                    fragment.setArguments(bundle);
                    ft.replace(R.id.id_content, fragment, "dengluFragment");
                    ft.commit();
                    break;
               case 2:
                   List<BmobUser> list3= (List<BmobUser>) msg.obj;
                   String email=list3.get(0).getEmail();
                   selectbyemail(email);
                    break;


            }
        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        Bmob.initialize(this, "ee02bb953703b95df7f6df6cb7b11eda");
        mname= (TextView) findViewById(R.id.left_name);
        Intent intent=getIntent();
        int tag=intent.getIntExtra("tag",0);
        if(tag==1) {
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            String name = sharedPreferences.getString("name", "");
            BmobQuery<plans> query = new BmobQuery<plans>();
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
                        Log.w("bmob", "姓名查邮箱");
                    }
                }

            });
        new MyAsynctask().execute("aaa");
        }else if(tag==2){
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            String name = sharedPreferences.getString("name", "");
            Log.e("record2", "" + name);
            new zhuceTask().execute(name);
        }else if(tag==3){
            String phonenumber=intent.getStringExtra("phonenumber");
            BmobQuery<BmobUser> query=new BmobQuery<BmobUser>();
            query.addWhereEqualTo("mobilePhoneNumber",phonenumber);
            query.findObjects(new FindListener<BmobUser>() {
                @Override
                public void done(List<BmobUser> list, BmobException e) {
                    Message message = handler.obtainMessage();
                    message.what = 2;
                    message.obj = list;
                    handler.sendMessage(message);
                }
            });



        }else{
            Log.e("login_error","error");
        }
        initView();
        initEvents();
        select(0);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }
    class MyAsynctask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            dengluFragment df = new dengluFragment();
            String name = sharedPreferences.getString("name", "");
            return name;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            mname.setText("用户:"+aVoid);
        }
    }
    class zhuceTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String name =strings[0];
            return name;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            mname.setText("用户:"+aVoid);
        }
    }

    public void onClick_menu(View view){
        switch (view.getId()){
            case R.id.R_layout1:
                Intent intent=new Intent(login.this,update_plan.class);
                startActivity(intent);
                break;
            case R.id.R_layout2:
                Intent intent2=new Intent(login.this,picture_month.class);
                intent2.putExtra("record", mrecord);
                startActivity(intent2);
                break;
            case R.id.R_layout3:
                Intent intent3=new Intent(login.this,picture_zx.class);
                intent3.putExtra("record", mrecord);
                startActivity(intent3);
                break;
            case R.id.R_layout4:
                Intent intent4=new Intent(login.this,select_listview.class);
                startActivity(intent4);
                break;
            case R.id.R_layout5:
                showShare();
                break;
        }
    }
    public void selectbyemail(String email) {
        BmobQuery<records> quary=new BmobQuery<records>();
        quary.addWhereEqualTo("email", email);
        quary.findObjects(new FindListener<records>() {
            @Override
            public void done(List<records> list, BmobException e) {
                if(e==null) {
                    for (records r : list) {
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        message.obj = list;
                        handler.sendMessage(message);
                    }
                }else{
                    Log.w("bmob", "邮箱查用户");
                }

            }
        });

    }

    public void select(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);

        switch (i) {
            case 0:
                if (mTab01 == null) {
                    mTab01 = new dengluFragment();
                    transaction.add(R.id.id_content, mTab01);
                } else {
                    transaction.show(mTab01);
                }
                maddressImg.setImageResource(R.drawable.tab_address_pressed);
                break;
            case 1:
                if (mTab02 == null) {

                    mTab02 = new denglu2Fragment();
                    transaction.add(R.id.id_content, mTab02);
                } else {
                    transaction.show(mTab02);

                }
                msettingImg.setImageResource(R.drawable.tab_settings_pressed);
                break;
            case 2:
                if (mTab03 == null) {
                    mTab03 = new denglu3Fragment();
                    transaction.add(R.id.id_content, mTab03);
                } else {
                    transaction.show(mTab03);
                }
                mfrdImg.setImageResource(R.drawable.tab_find_frd_pressed);
                break;
            default:
                break;

        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mTab01 != null) {
            transaction.hide(mTab01);
        }
        if (mTab02 != null) {
            transaction.hide(mTab02);
        }
        if (mTab03 != null) {
            transaction.hide(mTab03);
        }

    }

    private void initEvents() {
        mTabaddress.setOnClickListener(this);
        mTabfrd.setOnClickListener(this);
        mTabsetting.setOnClickListener(this);

    }

    private void initView() {
        mTabaddress = (LinearLayout) findViewById(R.id.id_tab_address);
        mTabsetting = (LinearLayout) findViewById(R.id.id_tab_settings);
        mTabfrd = (LinearLayout) findViewById(R.id.id_tab_frd);

        maddressImg = (ImageButton) findViewById(R.id.id_tab_address_img);
        msettingImg = (ImageButton) findViewById(R.id.id_tab_settings_img);
        mfrdImg = (ImageButton) findViewById(R.id.id_tab_frd_img);


    }

    @Override
    public void onClick(View view) {
        ResetImg();
        switch (view.getId()) {
            case R.id.id_tab_address:
                select(0);
                break;
            case R.id.id_tab_settings:
                select(1);
                break;
            case R.id.id_tab_frd:
                select(2);
                break;
        }
    }

    private void ResetImg() {
        maddressImg.setImageResource(R.drawable.tab_address_normal);
        mfrdImg.setImageResource(R.drawable.tab_find_frd_normal);
        msettingImg.setImageResource(R.drawable.tab_settings_normal);

    }
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
        Log.e("share","share");
    }



}
