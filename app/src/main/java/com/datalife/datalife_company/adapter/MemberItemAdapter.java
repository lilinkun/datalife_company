package com.datalife.datalife_company.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.MeasureFamilyUserInfo;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 点击成员名弹出来的pop
 * Created by LG on 2019/7/19.
 */
public class MemberItemAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<MeasureFamilyUserInfo> familyUserInfoArrayList = null;
    String mName;

    public MemberItemAdapter(Context context,List<MachineBindMemberBean> list,String name){
        this.mContext = context;
        this.mName = name;
        familyUserInfoArrayList = (ArrayList<MeasureFamilyUserInfo>) DBManager.getInstance(context).queryMeasureFamilyUserInfoList();
    }

    @Override
    public int getCount() {
        return familyUserInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return familyUserInfoArrayList.get(position);
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

        if (familyUserInfoArrayList.get(position).getMember_Name().equals(mName)){
            viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.ecg_bg));
        }else {
            viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.black_text_bg));
        }
        viewHolder.textView.setText(familyUserInfoArrayList.get(position).getMember_Name());

        return convertView;
    }

    class ViewHolder{
        TextView textView;
        ImageView img;
    }

}
