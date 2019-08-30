package com.datalife.datalife_company.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.bean.MeasureRecordBean;

import java.util.ArrayList;

/**
 * Created by LG on 2019/7/24.
 */
public class EcgRecordAdapter extends BaseAdapter {

    private ArrayList<MeasureRecordBean> ecgs = new ArrayList<>();
    private Context context;

    public EcgRecordAdapter(Context context, ArrayList<MeasureRecordBean> ecgArrayList){
        this.context = context;
        this.ecgs = ecgArrayList;
    }

    public void setValue(ArrayList<MeasureRecordBean> ecgArrayList){
        this.ecgs = ecgArrayList;
    }

    @Override
    public int getCount() {
        return ecgs.size();
    }

    @Override
    public Object getItem(int position) {
        return ecgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.ecg_item_adapter,null);
            viewHolder = new ViewHolder();

            viewHolder.mEcgRRMaxText = convertView.findViewById(R.id.tv_ecg_rr_max);
            viewHolder.mEcgRRMinText = convertView.findViewById(R.id.tv_ecg_rr_min);
            viewHolder.mEcgHrText = convertView.findViewById(R.id.tv_ecg_hr);
            viewHolder.mEcgHrvText = convertView.findViewById(R.id.tv_ecg_hrv);
            viewHolder.mEcgBrText = convertView.findViewById(R.id.tv_ecg_item_br);
            viewHolder.mEcgCreateTimeText = convertView.findViewById(R.id.tv_ecg_createdate);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mEcgRRMaxText.setText(ecgs.get(position).getCheckValue1());
        viewHolder.mEcgRRMinText.setText(ecgs.get(position).getCheckValue2());
        viewHolder.mEcgHrText.setText(ecgs.get(position).getCheckValue3());
        viewHolder.mEcgHrvText.setText(ecgs.get(position).getCheckValue4());
        viewHolder.mEcgBrText.setText(ecgs.get(position).getCheckValue6());
        viewHolder.mEcgCreateTimeText.setText(ecgs.get(position).getCreateDate());


        return convertView;
    }

    class ViewHolder{

        TextView mEcgRRMinText;
        TextView mEcgRRMaxText;
        TextView mEcgHrText;
        TextView mEcgHrvText;
        TextView mEcgBrText;
        TextView mEcgCreateTimeText;

    }
}
