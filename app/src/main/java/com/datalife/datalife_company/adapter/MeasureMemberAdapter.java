package com.datalife.datalife_company.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.util.DefaultPicEnum;
import com.datalife.datalife_company.widget.RoundImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2019/7/24.
 */
public class MeasureMemberAdapter extends BaseAdapter {

    Context mContext;
    List<MachineBindMemberBean> arrayList = new ArrayList<>();
    ArrayList<FamilyUserInfo> familyUserInfos = null;

    public MeasureMemberAdapter(Context context,List<MachineBindMemberBean> list){
        this.mContext = context;
        this.arrayList = list;
        familyUserInfos =  (ArrayList<FamilyUserInfo>) DBManager.getInstance(context).queryFamilyUserInfoList();
    }

    @Override
    public int getCount() {
        return familyUserInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return familyUserInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_fat_gridview,null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_member_item);
            viewHolder.img = (RoundImageView) convertView.findViewById(R.id.iv_member_item);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(familyUserInfos.get(position).getMember_Name());

        if (familyUserInfos.get(position).getMember_Portrait().toString().length() > 1){
            Picasso.with(mContext).load(familyUserInfos.get(position).getMember_Portrait()).into(viewHolder.img);
        }else if (familyUserInfos.get(position).getMember_Portrait().toString().length() == 1) {
            viewHolder.img.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfos.get(position).getMember_Portrait()).getResPic());
        }

        return convertView;
    }

    class ViewHolder{
        TextView textView;
        RoundImageView img;
    }

}