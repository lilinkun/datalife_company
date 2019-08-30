package com.datalife.datalife_company.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.SimplebackActivity;
import com.datalife.datalife_company.bean.SimpleBackPage;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UIHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by LG on 2019/7/17.
 */

public class FamilyRecyclerAdapter extends RecyclerView.Adapter<FamilyRecyclerAdapter.ViewHolder> {

    private List<FamilyUserInfo> familyUserInfos;
    private Handler handler;
    private Fragment context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_member,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {

        FamilyUserInfo familyUserInfo = familyUserInfos.get(position);

        String date = familyUserInfos.get(position).getMember_DateOfBirth();
        if (date.length() >= 11 ){
            date = date.substring(0,11);
        }

        if (familyUserInfo.getMember_Portrait().toString().trim().length() > 1) {
            Picasso.with(context.getActivity()).load(familyUserInfo.getMember_Portrait()).into(holder.imMemberFace);
        }else if (familyUserInfo.getMember_Portrait().toString().length() == 1) {
            IDatalifeConstant.GetPIC(context.getActivity(), familyUserInfo.getMember_Portrait(), holder.imMemberFace);
        }

        holder.tvMemberName.setText(familyUserInfos.get(position).getMember_Name());
        holder.tvMemberPhoneNum.setText(date);

        if(familyUserInfo.getIsIdataRegister() == 0){
            holder.tvYuebao.setTextColor(context.getResources().getColor(R.color.bg_toolbar_title));
            holder.tvYuebao.setText(familyUserInfo.getIsIdataRegister_Name() + "");
            holder.tvYuebao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context.getActivity()).setTitle("帐号只能免费绑定一个家庭成员").setMessage("你确定要绑定该成员?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("familyUserInfo", familyUserInfos.get(position));
                            message.setData(bundle);
                            message.what = 0x12312;
                            handler.sendMessage(message);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });
        }else {
            holder.tvYuebao.setText(familyUserInfo.getIsIdataRegister_Name() + "");
            holder.tvYuebao.setClickable(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("familyUserInfo",familyUserInfos.get(position));
                UIHelper.showSimpleBackBundleForResult(context, SimplebackActivity.RESULT_ADDUSER, SimpleBackPage.ADDUSER,bundle);
//                UIHelper.showSimpleBackBundleForResult(context, SimplebackActivity.RESULT_USERINFO, SimpleBackPage.USERINFO,bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return familyUserInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public FamilyRecyclerAdapter(List<FamilyUserInfo> familyUserInfos, Handler handler, Fragment context){
        this.familyUserInfos = familyUserInfos;
        this.handler = handler;
        this.context = context;
    }

    public void setData(List<FamilyUserInfo> familyUserInfos){
        this.familyUserInfos = familyUserInfos;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imMemberFace;
        TextView tvMemberName;
        TextView tvMemberPhoneNum;
        TextView tvYuebao;

        public ViewHolder(View itemView) {
            super(itemView);
            imMemberFace = (ImageView) itemView.findViewById(R.id.iv_member_face);
            tvMemberName = (TextView) itemView.findViewById(R.id.tv_member_name);
            tvMemberPhoneNum = (TextView) itemView.findViewById(R.id.tv_member_phonenum);
            tvYuebao = (TextView) itemView.findViewById(R.id.yuebao_tv);
        }

    }

}

