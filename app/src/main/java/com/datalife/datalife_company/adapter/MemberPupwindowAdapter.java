package com.datalife.datalife_company.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.bean.MemberListBean;
import com.datalife.datalife_company.util.IDatalifeConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * 绑定成员ａｄａｐｔｅｒ
 * Created by LG on 2019/7/18.
 */
public class MemberPupwindowAdapter extends BaseAdapter {

    private Context mContext;
    private List<MemberListBean> memberListBeans;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private boolean isChoose = false;
    private Handler handler;

    public MemberPupwindowAdapter(Context context, List<MemberListBean> familyUserInfos, Handler handler){
        this.mContext = context;
        this.memberListBeans = familyUserInfos;
        this.handler = handler;
    }


    @Override
    public int getCount() {
        return memberListBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return memberListBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_bindmember,null);
            viewHolder.imgface = (ImageView) convertView.findViewById(R.id.iv_bind_head);
            viewHolder.membername = (TextView) convertView.findViewById(R.id.tv_bind_name);
            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.iv_bind_icon);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.iv_bind_icon_layout);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }

        IDatalifeConstant.GetPIC(mContext,memberListBeans.get(position).getFamilyUserInfo().getMember_Portrait(),viewHolder.imgface);
        viewHolder.membername.setText(memberListBeans.get(position).getFamilyUserInfo().getMember_Name());
        imageViews.add(viewHolder.imgIcon);
        if (memberListBeans.get(position).isSelector()){
            viewHolder.imgIcon.setImageResource(R.mipmap.ic_choose);
        }else{
            viewHolder.imgIcon.setImageResource(R.mipmap.ic_unchoose);
        }


        return convertView;
    }

    class ViewHolder{
        ImageView imgface;
        TextView membername;
        ImageView imgIcon;
        LinearLayout linearLayout;
    }
}

