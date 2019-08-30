package com.datalife.datalife_company.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.RecordErrorActivity;
import com.datalife.datalife_company.bean.TypeBean;
import com.datalife.datalife_company.util.MeasureEnum;
import com.datalife.datalife_company.util.MeasureStatus;

import java.util.ArrayList;

/**
 * 点击历史记录中的全部类别
 * Created by LG on 2019/8/7.
 */
public class MeasureItemAdapter extends BaseAdapter {
    private Context mContext;
    private String mName;
    private String nameStr;
    int type = 0;
    private ArrayList<TypeBean> typeBeans = null;

    public MeasureItemAdapter(Context context, String name,int type,ArrayList<TypeBean> typeBean){
        this.mContext = context;
        this.mName = name;
        this.type = type;
        if (typeBean != null){
            this.typeBeans = typeBean;
        }
    }

    @Override
    public int getCount() {
        if (type == RecordErrorActivity.MEASURETYPE) {
            return typeBeans != null ? typeBeans.size() : 0;
        }else if(type == RecordErrorActivity.MEASURESTATUS) {
            return MeasureStatus.values().length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (type == RecordErrorActivity.MEASURETYPE) {
            return MeasureEnum.values()[position];
        }else if(type == RecordErrorActivity.MEASURESTATUS) {
            return MeasureStatus.values()[position];
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(R.layout.listview_member,null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_member_item);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.iv_member_item);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (type == RecordErrorActivity.MEASURESTATUS){
            nameStr = MeasureStatus.values()[position].getMeasureName();
        }else if (type == RecordErrorActivity.MEASURETYPE){
            nameStr = typeBeans.get(position).getName();
        }

        if (nameStr.equals(mName) ){
            viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.ecg_bg));
        }else {
            viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.black_text_bg));
        }

        if (type == RecordErrorActivity.MEASURETYPE) {
            viewHolder.textView.setText(typeBeans.get(position).getName());
        }else if (type == RecordErrorActivity.MEASURESTATUS){
            viewHolder.textView.setText(MeasureEnum.values()[position].getMeasureName());
        }

        return convertView;
    }

    class ViewHolder{
        TextView textView;
        ImageView img;
    }

}

