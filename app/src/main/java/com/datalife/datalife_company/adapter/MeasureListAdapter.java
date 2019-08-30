package com.datalife.datalife_company.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.RecordActivity;
import com.datalife.datalife_company.bean.MeasureErrorListBean;
import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.bean.MeasureRecordListBean;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.util.UIHelper;
import com.datalife.datalife_company.util.UToast;

import java.util.ArrayList;

/**
 * Created by LG on 2019/7/30.
 */
public class MeasureListAdapter extends RecyclerView.Adapter<MeasureListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MeasureErrorListBean> measureRecordListBeans;

    public MeasureListAdapter(Context context,ArrayList<MeasureErrorListBean> measureRecordListBeans){
        this.mContext = context;
        this.measureRecordListBeans = measureRecordListBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_record_list,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MeasureErrorListBean measureRecordListBean = (MeasureErrorListBean) measureRecordListBeans.get(position);
//        UToast.show(mContext,measureRecordListBean.getMeasureName() + "");
        holder.tv_measure_name.setText(measureRecordListBean.getMember_Name());
        holder.tv_measure_phone.setText(measureRecordListBean.getMember_Phone());
        holder.tv_measure_error.setText(measureRecordListBean.getCheck_Result_Name());
        holder.tv_measure_error_detail.setText(measureRecordListBean.getCheck_Result_Name());

        holder.ll_short_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ll_long_error != null && holder.ll_long_error.isShown()) {
                    holder.ll_long_error.setVisibility(View.GONE);
                }else {
                    holder.ll_long_error.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.btn_measure_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.launcher(mContext, RecordActivity.class);
            }
        });

    }

    public void setData(ArrayList<MeasureErrorListBean> measureRecordListBeans){
        this.measureRecordListBeans = measureRecordListBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return measureRecordListBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_measure_name;
        TextView tv_measure_phone;
        TextView tv_measure_error;
        TextView tv_measure_error_detail;
        Button btn_measure_chart;
        LinearLayout ll_short_error;
        LinearLayout ll_long_error;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_measure_name =  itemView.findViewById(R.id.tv_measure_name);
            tv_measure_phone = itemView.findViewById(R.id.tv_measure_phone);
            tv_measure_error = itemView.findViewById(R.id.tv_measure_error);
            tv_measure_error_detail = itemView.findViewById(R.id.tv_measure_error_detail);
            btn_measure_chart =  itemView.findViewById(R.id.btn_measure_chart);
            ll_short_error =  itemView.findViewById(R.id.ll_short_error);
            ll_long_error =  itemView.findViewById(R.id.ll_long_error);
        }
    }

}
