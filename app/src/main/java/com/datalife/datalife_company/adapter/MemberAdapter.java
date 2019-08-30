package com.datalife.datalife_company.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.HealthMonitorActivity;
import com.datalife.datalife_company.bean.MeasureFamilyUserInfo;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.widget.RoundImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by LG on 2019/7/19.
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder>{

    private Context context;
    private ArrayList<MeasureFamilyUserInfo> familyUserInfos = null;
    private int mNowPosition = 0;
    private OnItemClickListener mOnItemClickListener;//声明接口

    public MemberAdapter(Context context){
        this.context = context;
        familyUserInfos =  (ArrayList<MeasureFamilyUserInfo>) DBManager.getInstance(context).queryMeasureFamilyUserInfoList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_member_test,parent,false);
        int width = IDatalifeConstant.display(context).getWidth();

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_adapter_member);
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.width = width/5;
        linearLayout.setLayoutParams(layoutParams);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (familyUserInfos.get(position).getMember_Id() == Integer.valueOf(HealthMonitorActivity.mMemberId)) {
            holder.mTextView.setTextColor(context.getResources().getColor(R.color.black_text_bg));
            holder.mBgRoundImageView.setVisibility(View.GONE);
            holder.mTextView.setText(familyUserInfos.get(position).getMember_Name());
        }else {
            holder.mTextView.setText(familyUserInfos.get(position).getMember_Name());
            holder.mTextView.setTextColor(Color.GRAY);
            holder.mBgRoundImageView.setVisibility(View.VISIBLE);
        }

        if (familyUserInfos.get(position).getMember_Portrait().toString().length() > 2){
            Picasso.with(context).load(familyUserInfos.get(position).getMember_Portrait()).into(holder.mRoundImageView);
        }else {
            holder.mRoundImageView.setImageResource(R.mipmap.ic_head);
        }

    }

    @Override
    public int getItemCount() {
        return familyUserInfos.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onLongClick(View view, int posotion);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        RoundImageView mRoundImageView;
        TextView mTextView;
        RoundImageView mBgRoundImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mRoundImageView = itemView.findViewById(R.id.iv_member_face);
            mTextView = itemView.findViewById(R.id.tv_name);
            mBgRoundImageView = itemView.findViewById(R.id.iv_member_bg);
        }
    }
}
