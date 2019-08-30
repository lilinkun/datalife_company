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
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.util.IDatalifeConstant;

import java.util.List;

/**
 * 横向绑定成员ａｄａｐｔｅｒ
 * Created by LG on 2019/7/18.
 */
public class HorizontalMemberAdapter extends BaseAdapter {

    private Context mContext;
    private List<MachineBindMemberBean> mMemberItems;
    private List<FamilyUserInfo> familyUserInfos;

    public HorizontalMemberAdapter(Context context,List<MachineBindMemberBean> memberItems){
        this.mContext = context;
        this.mMemberItems = memberItems;
    }

    public void setList(List<MachineBindMemberBean> memberItems){
        this.mMemberItems = memberItems;
    }

    @Override
    public int getCount() {
        return mMemberItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMemberItems.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_horizontal_list_item,null);
            viewHolder.mImgHead = (ImageView) convertView.findViewById(R.id.img_list_item);
            viewHolder.mMemberName = (TextView) convertView.findViewById(R.id.text_list_item);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        familyUserInfos = DBManager.getInstance(mContext).queryFamilyUserInfoList();
        viewHolder.mMemberName.setSelected(true);
        for (int i = 0 ; i < familyUserInfos.size(); i++){
            if(String.valueOf(familyUserInfos.get(i).getMember_Id()).equals(mMemberItems.get(position).getMember_Id())){

                IDatalifeConstant.GetPIC(mContext,familyUserInfos.get(i).getMember_Portrait(),viewHolder.mImgHead);
                viewHolder.mMemberName.setText(familyUserInfos.get(i).getMember_Name());
            }
        }


        return convertView;
    }

    class ViewHolder{
        ImageView mImgHead;
        TextView mMemberName;
    }
}
