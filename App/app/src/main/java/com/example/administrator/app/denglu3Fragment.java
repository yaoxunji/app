package com.example.administrator.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.Toast;

import bean.records;
import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2016/10/4 0004.
 */
public class denglu3Fragment extends Fragment {
    private Context mcontext;
    private TableRow mpassord,mupdateplan,mupdateuser,mhelp,mabout;
    private records mrecord=new records();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.denglu3, container, false);
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){
            mrecord= (records) bundle.getSerializable("record");
        }
        mpassord= (TableRow) view.findViewById(R.id.more_page_row1);
        mupdateplan= (TableRow) view.findViewById(R.id.more_page_row2);
        mupdateuser= (TableRow) view.findViewById(R.id.more_page_row3);
        mhelp= (TableRow) view.findViewById(R.id.more_page_row4);
        mabout= (TableRow) view.findViewById(R.id.more_page_row5);
        mpassord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ud_password.class);
                startActivity(intent);
            }
        });
        mupdateplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),update_plan.class);
                intent.putExtra("record", mrecord);
                startActivity(intent);

            }
        });
        mupdateuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),update_user.class);
                startActivity(intent);
            }
        });
        mhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        mabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),guanyu.class);
                startActivity(intent);

            }
        });
        return view;


    }
}
