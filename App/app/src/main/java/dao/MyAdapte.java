package dao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.app.R;

import java.util.List;

import bean.show_listview;

/**
 * Created by Administrator on 2017/1/8 0008.
 */
public class MyAdapte extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<show_listview> mdatas;
    public MyAdapte(Context context,List<show_listview> datas) {

        mInflater=LayoutInflater.from(context);
        mdatas=datas;

    }

    @Override
    public int getCount() {
        return mdatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mdatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if(view==null){
            view=mInflater.inflate(R.layout.item_listview,viewGroup,false);
            holder=new ViewHolder();
            holder.mxiangmu= (TextView) view.findViewById(R.id.item_xiangmu);
            holder.mstart= (TextView) view.findViewById(R.id.item_start);
            holder.mend= (TextView) view.findViewById(R.id.item_end);
            holder.mdate= (TextView) view.findViewById(R.id.item_time);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        show_listview sl=mdatas.get(i);
        holder.mxiangmu.setText(sl.getXiangmu());
        holder.mstart.setText(sl.getStart());
        holder.mend.setText(sl.getEnd());
        holder.mdate.setText(sl.getDate());
        return view;
    }
    private class ViewHolder {
        TextView mxiangmu;
        TextView mstart;
        TextView mend;
        TextView mdate;
    }
}
