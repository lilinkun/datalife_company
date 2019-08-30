package com.datalife.datalife_company.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.dao.MachineBean;
import com.datalife.datalife_company.util.IDatalifeConstant;

import java.util.ArrayList;

/**
 * 添加设备中的ａｄａｐｔｅｒ
 * Created by LG on 2019/7/18.
 */
public class MachineAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MachineBean> machineBeans;

    public MachineAdapter(Context context,ArrayList<MachineBean> machineBeans){
        this.mContext = context;
        this.machineBeans = machineBeans;
    }

    public void setMachineBean(ArrayList<MachineBean> machineBeans){
        this.machineBeans = machineBeans;
    }

    @Override
    public int getCount() {
        return machineBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return machineBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_machine,null);
            viewHolder = new ViewHolder();

            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_machinename);
            viewHolder.machineTypeName = (TextView) convertView.findViewById(R.id.tv_machinetype);

            convertView.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (machineBeans.get(position).getMachineName().startsWith("HC02")){
            viewHolder.textView.setText(machineBeans.get(position).getMachineName());
            viewHolder.machineTypeName.setText(R.string.datalife_health_test);
        }else if (machineBeans.get(position).getMachineName().startsWith(IDatalifeConstant.SWAN)){
            viewHolder.textView.setText(machineBeans.get(position).getMachineName());
            viewHolder.machineTypeName.setText(R.string.datalife_body_fat);
        }else if (machineBeans.get(position).getMachineName().startsWith(IDatalifeConstant.ZSONIC)){
            viewHolder.textView.setText(machineBeans.get(position).getMachineName());
            viewHolder.machineTypeName.setText(R.string.datalife_blue_tooth);
        }

        return convertView;
    }

    class ViewHolder{
        TextView textView;
        TextView machineTypeName;
    }
}
