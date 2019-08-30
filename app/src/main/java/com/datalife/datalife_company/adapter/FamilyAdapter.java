package com.datalife.datalife_company.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.util.DefaultPicEnum;

import java.util.List;

/**
 * Created by LG on 2019/7/17.
 */

public class FamilyAdapter extends BaseAdapter {

    private Context mContext;
    private List<FamilyUserInfo> familyUserInfos ;

    public FamilyAdapter(Context context,List<FamilyUserInfo> familyUserInfos){
        this.mContext = context;
        this.familyUserInfos = familyUserInfos;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_member,null);
            viewHolder = new ViewHolder();

            viewHolder.imMemberFace = (ImageView) convertView.findViewById(R.id.iv_member_face);
            viewHolder.tvMemberName = (TextView) convertView.findViewById(R.id.tv_member_name);
            viewHolder.tvMemberPhoneNum = (TextView) convertView.findViewById(R.id.tv_member_phonenum);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DefaultPicEnum defaultPicEnum = DefaultPicEnum.getPageByValue(familyUserInfos.get(position).getMember_Portrait());
        String date = familyUserInfos.get(position).getMember_DateOfBirth();
        if (date.length() >= 11 ){
            date = date.substring(0,11);
        }

        viewHolder.imMemberFace.setImageResource(defaultPicEnum.getResPic());
        viewHolder.tvMemberName.setText(familyUserInfos.get(position).getMember_Name());
        viewHolder.tvMemberPhoneNum.setText(date);

        return convertView;
    }

    class ViewHolder{
        ImageView imMemberFace;
        TextView tvMemberName;
        TextView tvMemberPhoneNum;
    }
}

