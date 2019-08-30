package com.datalife.datalife_company.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.widget.TextView;

import com.datalife.datalife_company.base.BaseHealthFragment;

/**
 * 健康监测仪监测项目adapter
 * Created by LG on 2019/7/19.
 */
public class FragmentsHealthAdapter extends FragmentPagerAdapter {

    private SparseArray<BaseHealthFragment> fragmentSparseArr;
    private TextView textView;

    public FragmentsHealthAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(SparseArray<BaseHealthFragment> fragmentSparseArray) {
        this.fragmentSparseArr = fragmentSparseArray;
        notifyDataSetChanged();
    }

    public void onTextViewTitle(TextView textView){
        this.textView = textView;
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

}
