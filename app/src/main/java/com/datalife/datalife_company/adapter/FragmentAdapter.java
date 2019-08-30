package com.datalife.datalife_company.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.base.BaseRecordFragment;

/**
 * Created by LG on 2019/7/24.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private SparseArray<BaseRecordFragment> fragmentSparseArr;
    private Context context;

    public FragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public void setFragments(SparseArray<BaseRecordFragment> fragmentSparseArray) {
        this.fragmentSparseArr = fragmentSparseArray;
        notifyDataSetChanged();
    }

    public void setBundle(Bundle bundle){
        for(int i = 0; i <fragmentSparseArr.size() ;i++){
            fragmentSparseArr.get(i).setArguments(bundle);
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (null != fragmentSparseArr) return fragmentSparseArr.get(position);
        return null;
    }

    @Override
    public int getCount() {
        if (null != fragmentSparseArr) return fragmentSparseArr.size();
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (fragmentSparseArr == null)
            return "";
        if (fragmentSparseArr.get(position) == null)
            return "";

        String str[] = {context.getResources().getString(R.string.spo2h_value),context.getResources().getString(R.string.hr_value),
                context.getResources().getString(R.string.bp),context.getResources().getString(R.string.bt),context.getResources().getString(R.string.ecg)};

        return str[position];
//        return fragmentSparseArr.get(position).getTitle();
    }

}
