package com.datalife.datalife_company.activity;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.adapter.FatMemberAdapter;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseActivity;
import com.datalife.datalife_company.bean.MaxMinValueBean;
import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.contract.FatRecordContract;
import com.datalife.datalife_company.custom.MyFatMarkerView;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.interf.CoupleChartGestureListener;
import com.datalife.datalife_company.presenter.FatRecordPresenter;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.widget.FatRecordLayout;
import com.datalife.datalife_company.widget.RoundImageView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aicare.net.cn.iweightlibrary.entity.BodyFatData;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2019/7/25.
 */

public class FatRecordActivity extends BaseActivity implements CoupleChartGestureListener.OnEdgeListener, OnChartValueSelectedListener ,FatRecordContract{

    @BindView(R.id.my_chart)
    LineChart mChart;
    @BindView(R.id.ll_head_left)
    LinearLayout ll_head_left;
    @BindView(R.id.iv_round_right)
    RoundImageView iv_round_right;
    @BindView(R.id.fat_record_layout)
    LinearLayout linearLayout;
    @BindView(R.id.fat_record_common)
    FatRecordLayout fat_record_common;

    private List<FamilyUserInfo> familyUserInfos = null;
    private FamilyUserInfo familyUserInfo;
    private XAxis xAxis;
    private Typeface mTfLight;
    private List<String> timeList1 = new ArrayList<>(); //存储x轴的时间
    private List<String> timeList = new ArrayList<>(); //存储x轴的时间
    private ArrayList<Entry> yVals1 = new ArrayList<Entry>();
    private YAxis rightAxis = null;
    private ArrayList<MeasureRecordBean> measureRecordBeans = null;
    private int zoomInt = 5;
    private boolean isUpdate = false;
    private Entry entryClick;
    private SimpleDateFormat df1 = new SimpleDateFormat("MM月dd日");//设置日期格式  
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式  
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private FatRecordPresenter fatRecordPresenter = new FatRecordPresenter();
    private int pageIndex = 1;
    private int pageCount = 40;
    private boolean isPage = false;
    private String memberID = "";
    private PopupWindow popupWindow = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case IDatalifeConstant.MESSAGEWHAT_CHART:
                    String pos = (msg.getData().getString("value"));
                    int x = (int)(msg.getData().getFloat("x"));
                    setHandlerData(x);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fat_record;
    }

    @Override
    protected void initEventAndData() {
        fatRecordPresenter.onCreate(this,this);
        familyUserInfos = DBManager.getInstance(this).queryFamilyUserInfoList();

        if (getIntent().getBundleExtra(IDatalifeConstant.BUNDLEMEMBER).getSerializable(IDatalifeConstant.FAMILYUSERINFO) != null){
            this.familyUserInfo = (FamilyUserInfo) getIntent().getBundleExtra(IDatalifeConstant.BUNDLEMEMBER).getSerializable(IDatalifeConstant.FAMILYUSERINFO);
            memberID = familyUserInfo.getMember_Id()+"";
        }

        fatRecordPresenter.onGetListValue(pageIndex+"",pageCount+"",familyUserInfo.getMember_Id()+"",IDatalifeConstant.MACHINE_FAT,
                "","","", ProApplication.SESSIONID);


        mChart.setOnChartValueSelectedListener(this);// no description text
        mChart.getDescription().setEnabled(false);

        mTfLight = Typeface.createFromAsset(this.getAssets(), "OpenSans-Light.ttf");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDrawBorders(false);
        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
//        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        mChart.setPinchZoom(false);


        // add data
//        setData(12, 50);
        MyFatMarkerView mv = new MyFatMarkerView(this, R.layout.custom_marker_view,handler);
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv);

        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setFormLineWidth(0);
        l.setFormSize(0);
        l.setTypeface(mTfLight);
        l.setTextColor(getResources().getColor(R.color.transparent));
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(false);
        xAxis.setAxisLineWidth(0f);
//        xAxis.setLabelCount(zoomInt);
        xAxis.setAxisLineColor(Color.GRAY);
        xAxis.setEnabled(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        mChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                String time = timeList1.get((int) value % timeList1.size()) + " " + timeList.get((int) value % timeList.size());
                return time;

//                return timeList1.get((int) value % timeList1.size());
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setEnabled(false);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(100);
        leftAxis.setAxisMinimum(-5);
        leftAxis.setLabelCount(4);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setDrawZeroLine(true); // draw a zero line
        leftAxis.setZeroLineColor(Color.GRAY);
        leftAxis.setZeroLineWidth(1f);
        leftAxis.setAxisLineWidth(1f);
        leftAxis.setAxisLineColor(Color.GRAY);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setEnabled(false);
        rightAxis.setLabelCount(4,true);
        rightAxis.setDrawGridLines(false);
        rightAxis.setStartAtZero(false);
        rightAxis.setAxisMinValue(20f);

        mChart.setScaleEnabled(false);
        mChart.setOnChartGestureListener(new CoupleChartGestureListener(this,mChart));
        mChart.setAutoScaleMinMaxEnabled(true);


    }
    @OnClick({R.id.ll_head_left,R.id.iv_round_right})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_head_left:

                finish();

                break;

            case R.id.iv_round_right:
                View contentView = LayoutInflater.from(this).inflate(R.layout.member_popup_listview, null);

                RelativeLayout relativeLayout = (RelativeLayout) contentView.findViewById(R.id.rl_add_member);
                GridView gridView = (GridView) contentView.findViewById(R.id.lv_add_member);
                relativeLayout.setVisibility(View.VISIBLE);
                gridView.setAdapter(new FatMemberAdapter(this,null,true));

                LinearLayout llx = (LinearLayout) contentView.findViewById(R.id.ll_x);
                LinearLayout lv_layout = (LinearLayout) contentView.findViewById(R.id.lv_layout);
                lv_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
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
                popupWindow.showAtLocation(findViewById(R.id.fat_record_layout), Gravity.FILL,0,0);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
//                            mElectrocarDiogramLayout.setBackgroundResource(R.color.transparent);
                        linearLayout.setBackgroundResource(R.color.transparent);
                    }
                });
                linearLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
//                    mElectrocarDiogramLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            mMemberId = String.valueOf(machineBindMemberBean.get(position).getMember_Id());
                        familyUserInfo = familyUserInfos.get(position);

                        if (familyUserInfo.getMember_Portrait().toString().length() > 1){
                            Picasso.with(FatRecordActivity.this).load(familyUserInfo.getMember_Portrait()).into(iv_round_right);
                        }else if (familyUserInfo.getMember_Portrait().toString().length() == 1) {
                            IDatalifeConstant.GetPIC(FatRecordActivity.this, familyUserInfo.getMember_Portrait(), iv_round_right);
                        }else {
                            iv_round_right.setImageResource(R.mipmap.ic_head);
                        }

                        isUpdate = true;
                        isPage = false;
                        if (!memberID.equals(String.valueOf(familyUserInfo.getMember_Id()))){
                            isUpdate = true;
                            memberID = String.valueOf(familyUserInfo.getMember_Id());
                            fatRecordPresenter.onMaxMinValue(memberID,ProApplication.SESSIONID);
                            fatRecordPresenter.onGetListValue("1",pageCount+"",memberID,IDatalifeConstant.MACHINE_FAT,"","","", ProApplication.SESSIONID);
                        }
//                        onMemberClick(customViewPager.getCurrentItem());
                        popupWindow.dismiss();
                    }
                });
                break;
        }
    }


    private void setHandlerData(int x){
        mChart.getXAxis().setTextColor(R.color.blue);

        if (measureRecordBeans != null) {
            MeasureRecordBean measureRecordBean = measureRecordBeans.get(x);
            BodyFatData bodyFatData = new BodyFatData();
            bodyFatData.setWeight(Double.parseDouble(measureRecordBean.getCheckValue3()));
            bodyFatData.setBmi(Double.parseDouble(measureRecordBean.getCheckValue4()));
            bodyFatData.setBfr(Double.parseDouble(measureRecordBean.getCheckValue5()));
            bodyFatData.setRom(Double.parseDouble(measureRecordBean.getCheckValue6()));
            bodyFatData.setVwc(Double.parseDouble(measureRecordBean.getCheckValue7()));
            bodyFatData.setDbw(Double.parseDouble(measureRecordBean.getCheckValue8()));
            bodyFatData.setBm(Double.parseDouble(measureRecordBean.getCheckValue9()));
            if (measureRecordBean.getCheckValue10().equals("0.0")) {
                bodyFatData.setUvi(0);
            } else {
                double v = Double.parseDouble(measureRecordBean.getCheckValue10());
                bodyFatData.setUvi((int) v);
            }
            bodyFatData.setBmr(Double.parseDouble(measureRecordBean.getCheckValue11()));
            bodyFatData.setSex(Integer.valueOf(familyUserInfo.getMember_Sex()));
            bodyFatData.setHeight((float)(familyUserInfo.getMember_Stature()));
            bodyFatData.setAge(IDatalifeConstant.getAgeByBirthDay(familyUserInfo.getMember_DateOfBirth()));

            if (x >= 1) {
                MeasureRecordBean measureRecordBean1 = measureRecordBeans.get(x - 1);
                BodyFatData bodyFatData1 = new BodyFatData();
                bodyFatData1.setWeight(Double.parseDouble(measureRecordBean1.getCheckValue3()));
                bodyFatData1.setBmi(Double.parseDouble(measureRecordBean1.getCheckValue4()));
                bodyFatData1.setBfr(Double.parseDouble(measureRecordBean1.getCheckValue5()));
                bodyFatData1.setRom(Double.parseDouble(measureRecordBean1.getCheckValue6()));
                bodyFatData1.setVwc(Double.parseDouble(measureRecordBean1.getCheckValue7()));
                bodyFatData1.setDbw(Double.parseDouble(measureRecordBean1.getCheckValue8()));
                bodyFatData1.setBm(Double.parseDouble(measureRecordBean1.getCheckValue9()));
                if (measureRecordBean1.getCheckValue10().equals("0.0")) {
                    bodyFatData1.setUvi(0);
                } else {
                    double v = Double.parseDouble(measureRecordBean1.getCheckValue10());
                    bodyFatData1.setUvi((int) v);
                }
                bodyFatData1.setBmr(Double.parseDouble(measureRecordBean1.getCheckValue11()));
                bodyFatData1.setSex(Integer.valueOf(familyUserInfo.getMember_Sex()));
                bodyFatData1.setHeight((float) (familyUserInfo.getMember_Stature()));
                bodyFatData1.setAge(IDatalifeConstant.getAgeByBirthDay(familyUserInfo.getMember_DateOfBirth()));
                fat_record_common.setData(bodyFatData,bodyFatData1);
            }else {
                fat_record_common.setData(bodyFatData,null);
            }
        }
    }

    @Override
    public void edgeLoad(float x, boolean left) {
        isUpdate = false;
//        toast(i++ + "");
        if(!isPage) {
            fatRecordPresenter.onMaxMinValue(familyUserInfo.getMember_Id()+"",ProApplication.SESSIONID);
            pageIndex++;
            fatRecordPresenter.onGetListValue(pageIndex + "", pageCount + "", familyUserInfo.getMember_Id()+"", IDatalifeConstant.MACHINE_FAT, "", "", "", ProApplication.SESSIONID);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void setData(){
        if (measureRecordBeans.size() <= zoomInt && measureRecordBeans.size() > 0) {
            mChart.getXAxis().setLabelCount(measureRecordBeans.size()-1,false);
        }else {
            mChart.getXAxis().setLabelCount(zoomInt-1,false);
        }

        if (isUpdate){
            timeList.clear();
            timeList1.clear();
            yVals1.clear();
            mChart.clear();
//            mChart.resetZoom();
//            mChart.setPinchZoom(true);
            mChart.setScaleMinima(1f,1f);
            mChart.getViewPortHandler().refresh(new Matrix(), mChart, true);
//            mChart.clearValues();
        }
        for (int i = 0; i < measureRecordBeans.size() ; i++) {
            try {
                Date date = simpleDateFormat.parse(measureRecordBeans.get(i).getCreateDate());
                timeList.add(df.format(date));
                timeList1.add(df1.format(date));
                Entry entry = new Entry(i, Float.valueOf(measureRecordBeans.get(i).getCheckValue3()), getResources().getDrawable(R.mipmap.ic_line_spot));
                if (i == measureRecordBeans.size() - 1){
                    entryClick = entry;
                }
                yVals1.add(entry);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        LineDataSet set1;

        set1 = new LineDataSet(yVals1,"");

        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set1.setColor(getResources().getColor(R.color.bg_line_chart));
        set1.setDrawCircleHole(true);
        set1.setLineWidth(2f);
        set1.setCircleRadius(5f);
//            set1.setCircleSize(7f);
        set1.setFillAlpha(65);
        set1.setHighlightEnabled(false);
//        set1.setHighLightColor(Color.rgb(244, 117, 117));
//            set1.setDrawCircleHole(false);
        set1.setDrawValues(false);
        set1.setFillDrawable(getResources().getDrawable(R.drawable.ic_blue_fill));
        set1.setDrawFilled(true);
        set1.setDrawIcons(true);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);
        data.setHighlightEnabled(true);
//        mChart.postInvalidate();
        // set data
        mChart.setHighlightPerDragEnabled(false);
        mChart.setData(data);

//        mChart.refreshDrawableState();

        mChart.notifyDataSetChanged();
        mChart.invalidate();
        if (measureRecordBeans.size() > 5 && measureRecordBeans.size() < 8) {
            mChart.zoom(2, 1f, 0, 0);
        }else if (measureRecordBeans.size() < 5 && measureRecordBeans.size() > 0) {
            mChart.zoom(1,1f,0,0);
        }else {
            mChart.zoom((float)measureRecordBeans.size() / (float)zoomInt, 1f, 0, 0);
        }

        mChart.moveViewToX(measureRecordBeans.size() -1);
//        mChart.getEntryByTouchPoint(entryClick.getX(),entryClick.getY()).setIcon(getResources().getDrawable(R.mipmap.ic_line_full_spot));
        setHandlerData(measureRecordBeans.size()-1);
    }


    @Override
    public void getDataSuccess(ArrayList<MeasureRecordBean> measureRecordBeans) {
        if(pageIndex * pageCount > measureRecordBeans.size()){
            isPage = true;
        }
        if (isUpdate){
            this.measureRecordBeans = measureRecordBeans;
        }else {
            isUpdate = true;
            if (this.measureRecordBeans != null) {
                measureRecordBeans.addAll(this.measureRecordBeans);
            }
            this.measureRecordBeans = measureRecordBeans;
        }
        setData();
    }

    @Override
    public void getDataFail(String msg) {

    }

    @Override
    public void onSuccessMaxMinValue(MaxMinValueBean maxMinValueBean) {
        mChart.getAxisLeft().resetAxisMinimum();
        mChart.getAxisLeft().resetAxisMaximum();
        mChart.getAxisLeft().setAxisMinimum(maxMinValueBean.getMin_weight() - 10);
        mChart.getAxisLeft().setAxisMaximum(maxMinValueBean.getMax_weight() + 10);
    }

    @Override
    public void onFailMaxMinValue(String value) {
        toast(value);
    }
}
