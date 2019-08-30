package com.datalife.datalife_company.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.adapter.FragmentAdapter;
import com.datalife.datalife_company.adapter.MeasureMemberAdapter;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseActivity;
import com.datalife.datalife_company.base.BaseRecordFragment;
import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.contract.RecordContract;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.fragment.RecordBpFragment;
import com.datalife.datalife_company.fragment.RecordBtFragment;
import com.datalife.datalife_company.fragment.RecordEcgFragment;
import com.datalife.datalife_company.fragment.RecordHrFragment;
import com.datalife.datalife_company.fragment.RecordSpo2hFragment;
import com.datalife.datalife_company.interf.OnMeasureRecordLisener;
import com.datalife.datalife_company.presenter.RecordPresenter;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.widget.CustomTitleBar;
import com.datalife.datalife_company.widget.CustomViewPager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2019/7/24.
 */
public class RecordActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener,OnMeasureRecordLisener {

    @BindView(R.id.view_page)
    CustomViewPager customViewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.custom_title)
    CustomTitleBar customTitleBar;
    @BindView(R.id.ll_content)
    LinearLayout linearLayout;
    @BindView(R.id.tv_membername)
    TextView mMemberName;
    @BindView(R.id.roundiv_member)
    ImageView mRoundMember;
    @BindView(R.id.rl_record)
    RelativeLayout mRlMember;

    private List<FamilyUserInfo> familyUserInfos = null;
    private int page = 0;
    public static String memberID = "";
    private PopupWindow popupWindow = null;

    private RecordBtFragment recordBtFragment = null;
    private RecordBpFragment recordBpFragment = null;
    private RecordSpo2hFragment recordSpo2hFragment = null;
    private RecordHrFragment recordHrFragment = null;
    private RecordEcgFragment recordEcgFragment = null;

    private final SparseArray<BaseRecordFragment> sparseArray = new SparseArray<>();

    public static int SPO2HRECORD = 0;
    public static int HRRECORD = 1;
    public static int BPRECORD = 2;
    public static int BTRECORD = 3;
    public static int ECGRECORD = 4;

    public static String ShowCount = "100";//获取历史记录的条数


    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initEventAndData() {
        familyUserInfos = DBManager.getInstance(this).queryFamilyUserInfoList();

        if (getIntent()!= null){
            Bundle bundle = getIntent().getBundleExtra(IDatalifeConstant.BUNDLEMEMBER);
            if (bundle != null) {
                memberID = bundle.getString(IDatalifeConstant.BUNDLEMEMBERID);
                if (String.valueOf(bundle.getInt(IDatalifeConstant.PAGE)) != null){
                    page = bundle.getInt(IDatalifeConstant.PAGE);
                }
                for (FamilyUserInfo familyUserInfo : familyUserInfos) {
                    if (String.valueOf(familyUserInfo.getMember_Id()).equals(memberID)) {
                        mMemberName.setText(familyUserInfo.getMember_Name());
                        if (familyUserInfo.getMember_Portrait().toString().length() > 1) {
                            Picasso.with(this).load(familyUserInfo.getMember_Portrait()).into(mRoundMember);
                        }else if (familyUserInfo.getMember_Portrait().toString().length() == 1) {
                            IDatalifeConstant.GetPIC(this, familyUserInfo.getMember_Portrait(), mRoundMember);
                        }else {
                            mRoundMember.setImageResource(R.mipmap.ic_head);
                        }
                    }
                }
            }else {
                memberID = String.valueOf(familyUserInfos.get(0).getMember_Id());
                mMemberName.setText(familyUserInfos.get(0).getMember_Name());
                if (familyUserInfos.get(0).getMember_Portrait().toString().length() > 1) {
                    Picasso.with(this).load(familyUserInfos.get(0).getMember_Portrait()).into(mRoundMember);
                }else {
                    mRoundMember.setImageResource(R.mipmap.ic_head);
                }
            }
        }

        recordBtFragment = new RecordBtFragment();
        recordEcgFragment = new RecordEcgFragment();
        recordHrFragment = new RecordHrFragment();
        recordBpFragment = new RecordBpFragment();
        recordSpo2hFragment = new RecordSpo2hFragment();

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),this);
        getMenusFragments();
        adapter.setFragments(sparseArray);
        Bundle bundle1 = new Bundle();
        bundle1.putString(IDatalifeConstant.BUNDLEMEMBERID,memberID);
        adapter.setBundle(bundle1);
        customViewPager.setOffscreenPageLimit(4);
        customViewPager.setPageMargin(10);
        customViewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(this);
        customViewPager.addOnPageChangeListener(this);
        customViewPager.setCurrentItem(0, false);
        customViewPager.setCanScroll(false);
        tabLayout.setupWithViewPager(customViewPager);


    }


    private void getMenusFragments() {
        sparseArray.put(SPO2HRECORD, recordSpo2hFragment);
        sparseArray.put(HRRECORD, recordHrFragment);
        sparseArray.put(BPRECORD, recordBpFragment);
        sparseArray.put(BTRECORD, recordBtFragment);
        sparseArray.put(ECGRECORD, recordEcgFragment);
    }

    @OnClick({R.id.iv_head_left,R.id.rl_record})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_head_left:
                finish();
                break;

            case R.id.rl_record:

                View contentView = LayoutInflater.from(this).inflate(R.layout.member_popup_listview, null);

                RelativeLayout relativeLayout = (RelativeLayout) contentView.findViewById(R.id.rl_add_member);
                GridView gridView = (GridView) contentView.findViewById(R.id.lv_add_member);
                relativeLayout.setVisibility(View.VISIBLE);
                gridView.setAdapter(new MeasureMemberAdapter(this,null));

                LinearLayout llx = (LinearLayout) contentView.findViewById(R.id.ll_x);
                llx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                popupWindow = new PopupWindow(contentView,
                        LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT, true);
                popupWindow.setContentView(contentView);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
                popupWindow.showAtLocation(findViewById(R.id.ll_total), Gravity.FILL,0,0);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
//                            mElectrocarDiogramLayout.setBackgroundResource(R.color.transparent);
                        linearLayout.setBackgroundResource(R.color.transparent);
                    }
                });
                linearLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FamilyUserInfo familyUserInfo = familyUserInfos.get(position);
                        if (familyUserInfo.getMember_Portrait().toString().length() > 1){
                            Picasso.with(RecordActivity.this).load(familyUserInfo.getMember_Portrait()).into(mRoundMember);
                        }else if (familyUserInfo.getMember_Portrait().toString().length() == 1) {
                            IDatalifeConstant.GetPIC(RecordActivity.this, familyUserInfo.getMember_Portrait(), mRoundMember);
                        }else {
                            mRoundMember.setImageResource(R.mipmap.ic_head);
                        }
                        mMemberName.setText(familyUserInfo.getMember_Name());
                        memberID = String.valueOf(familyUserInfo.getMember_Id());

                        if (recordBpFragment != null){
                            recordBpFragment.getRecordData();
                        }
                        if (recordBtFragment != null){
                            recordBtFragment.getRecordData();
                        }
                        if (recordEcgFragment != null){
                            recordEcgFragment.getRecordData();
                        }
                        if (recordSpo2hFragment != null){
                            recordSpo2hFragment.getRecordData();
                        }
                        if (recordHrFragment != null){
                            recordHrFragment.getRecordData();
                        }

//                        onMemberClick(customViewPager.getCurrentItem());
                        popupWindow.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBtIntent(int Date, int pageIndex, String startDate, String finishDate) {

    }

    @Override
    public void onSpo2hIntent(int Date, int pageIndex, String startDate, String finishDate) {

    }

    @Override
    public void onHrIntent(int Date, int pageIndex, String startDate, String finishDate) {

    }

    @Override
    public void onEcgIntent(int date, int pageIndex) {

    }

    @Override
    public void onBpIntent(int Date, int pageIndex, String startDate, String finishDate) {

    }
}
