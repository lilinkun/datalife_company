package com.datalife.datalife_company.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.dao.MachineBean;

import java.util.List;

/**
 * Created by LG on 2019/7/17.
 */

public class MainMachineAdapter extends RecyclerView.Adapter<MainMachineAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<MachineBean> machineBeans;
    private OnItemClickListener onItemClickListener;

    public MainMachineAdapter(Context context,List<MachineBean> machineBeans){
        this.mContext = context;
        this.machineBeans = machineBeans;
    }

    public void setValues(List<MachineBean> values){
        this.machineBeans = values;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_main,null);

        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setTag(position);

        MachineBean machineBean = machineBeans.get(position);

        if (machineBean.getMachineName().startsWith("HC02")){
            holder.mImageView.setImageResource(R.mipmap.ic_main_health);
            holder.mTvMachineName.setText(R.string.health_testing);
            holder.mTvMachineAddress.setText(machineBean.getMachineName());
        }else if (machineBean.getMachineName().startsWith("SWAN")){
            holder.mImageView.setImageResource(R.mipmap.ic_main_bodyfat);
            holder.mTvMachineName.setText(R.string.home_body_fat);
            holder.mTvMachineAddress.setText(machineBean.getMachineName());
        }else if (machineBean.getMachineName().startsWith("ZSONIC")){
            holder.mImageView.setImageResource(R.mipmap.ic_main_bluetooth);
            holder.mTvMachineName.setText(R.string.health_tooth);
            holder.mTvMachineAddress.setText(machineBean.getMachineName());
        }

    }

    @Override
    public int getItemCount() {
        return machineBeans.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            if (v != null) {
                if (v.getTag() != null)
                    onItemClickListener.onItemClick((Integer) v.getTag());
            }
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mTvMachineName,mTvMachineAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_main_machine);
            mTvMachineAddress = itemView.findViewById(R.id.tv_machine_address);
            mTvMachineName = itemView.findViewById(R.id.tv_machine_name);
        }
    }
}

