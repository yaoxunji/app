package com.example.administrator.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import bean.records;

/**
 * Created by Administrator on 2016/10/4 0004.
 */
public class dengluFragment extends Fragment {
    private Button mtime,mtu,mwenzi,mlesson;
    private records mrecord=new records();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.denglu1, container, false);
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){
            mrecord= (records) bundle.getSerializable("record");
        }
        mtime= (Button) view.findViewById(R.id.time);
        mtu= (Button) view.findViewById(R.id.tu);
        mwenzi= (Button) view.findViewById(R.id.wenzi);
        mlesson= (Button) view.findViewById(R.id.lesson);
        mlesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),select_listview.class);
                startActivity(intent);
            }
        });
        mtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),record.class);
                intent.putExtra("record", mrecord);
                startActivity(intent);

            }
        });
        mwenzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),future.class);
                startActivity(intent);
            }
        });
        mtu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),picture_select.class);
                intent.putExtra("record", mrecord);
                startActivity(intent);
            }
        });



        return view;


    }
}
