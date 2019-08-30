package com.datalife.datalife_company.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.datalife.datalife_company.R;

import java.util.ArrayList;

/**
 * ECG中的检测展示adapter
 * Created by LG on 2019/7/23.
 */
public class HealthTestAdapter extends RecyclerView.Adapter<HealthTestAdapter.ViewHolder>{

    private int mResColor;
    private Context mContext;
    private ArrayList<String> mTestList = null;

    public HealthTestAdapter(Context context,int resColor,ArrayList<String> mTestList){
        this.mContext = context;
        this.mResColor = resColor;
        this.mTestList = mTestList;
    }

    public void setArrayList(ArrayList<String> mTestList){
        this.mTestList = mTestList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_healthtest,null);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTestName.setTextColor(mContext.getResources().getColor(mResColor));
        holder.mTestFraction.setTextColor(mContext.getResources().getColor(mResColor));

        if (mTestList != null && mTestList.size() != 0){
            holder.mTestFraction.setText(mTestList.get(position));
        }else {
            holder.mTestFraction.setText("");
        }
        switch (position){
            case 0:
                holder.mTestName.setText(R.string.max_value);
                break;
            case 1:
                holder.mTestName.setText(R.string.min_value);
                break;
            case 2:
                holder.mTestName.setText(R.string.hrv_value);
                break;
            case 3:
                holder.mTestName.setText(R.string.hr_value);
                break;
            case 4:
                holder.mTestName.setText(R.string.mood);
                break;
            case 5:
                holder.mTestName.setText(R.string.br_value);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public void setTextColor(int resColor){
        this.mResColor = resColor;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTestFraction;
        private TextView mTestName;

        public ViewHolder(View itemView) {
            super(itemView);
            mTestFraction = itemView.findViewById(R.id.tv_fraction);
            mTestName = itemView.findViewById(R.id.tv_name);
        }
    }
}
