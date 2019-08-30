package com.datalife.datalife_company.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.ECGLargeChartActivity;
import com.datalife.datalife_company.activity.HealthMonitorActivity;
import com.datalife.datalife_company.adapter.HealthTestAdapter;
import com.datalife.datalife_company.bean.MeasureEcgBean;
import com.datalife.datalife_company.contract.MeasureContract;
import com.datalife.datalife_company.presenter.MeasurePresenter;
import com.datalife.datalife_company.util.DeviceData;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UIHelper;
import com.datalife.datalife_company.widget.EcgWaveView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnEcgResultListener;
import com.linktop.whealthService.task.EcgTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2019/7/19.
 */
public class ECGFragment extends MeasureFragment implements MeasureContract, OnEcgResultListener {

    @BindView(R.id.fl_openECGLarge)
    FrameLayout mOpenECGLargeFl;
    @BindView(R.id.ecg_draw_chart)
    EcgWaveView mEcgWaveView;
    @BindView(R.id.tv_ecg_hr)
    TextView mEcgHrTv;
    @BindView(R.id.tv_ecg_hrv)
    TextView mEcgHrvTv;
    @BindView(R.id.tv_ecg_mood)
    TextView mEcgMoodTv;
    @BindView(R.id.tv_ecg_br)
    TextView mEcgBrTv;
    @BindView(R.id.tv_rrMin)
    TextView mEcgRrMinTv;
    @BindView(R.id.tv_rrMax)
    TextView mEcgRrMaxTv;
    @BindView(R.id.btn_starttest)
    Button mStartTestBtn;
    @BindView(R.id.ll_table)
    RelativeLayout mTableLayout;
    @BindView(R.id.rv_test)
    RecyclerView rvTest;
    @BindView(R.id.rv_old_test)
    RecyclerView rvOldTest;

    private MeasureEcgBean measureEcgBean;
    private EcgTask mEcgTask;
    private HealthTestAdapter oldHealthTestAdapter = null;
    private HealthTestAdapter newHealthTestAdapter = null;
    private ArrayList<String> oldList = new ArrayList<>();
    private ArrayList<String> newList = new ArrayList<>();
    private final int ECGHANDLERHR = 0X2120;
    private final int ECGHANDLERBR = 0X2121;
    private final int ECGHANDLERRRMAX = 0X2122;
    private final int ECGHANDLERRRMIN = 0X2123;
    private final int ECGHANDLERMOOD = 0X2124;
    private final int ECGHANDLERHRV = 0X2125;
    private MeasurePresenter measurePresenter = new MeasurePresenter();
    public final int ECGHANDDATA = 0x3313;
    private boolean isOver = false;
    private final StringBuilder ecgWaveBuilder = new StringBuilder();

    private Handler myEcgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ECGHANDDATA:

                    if (measureEcgBean.getBr() == 0 || measureEcgBean.getHr() == 0 || measureEcgBean.getHrv() == 0){
                        toast("请准确测量");
                        return;
                    }


                    IDatalifeConstant.startAlarm(getActivity());
                    mEcgBrTv.setText(measureEcgBean.getBr() + "");
                    mEcgRrMaxTv.setText(measureEcgBean.getRrMax() + "");
                    mEcgRrMinTv.setText(measureEcgBean.getRrMin() + "");
                    mEcgHrTv.setText(measureEcgBean.getHr() + "");
                    mEcgMoodTv.setText(measureEcgBean.getMood() + "");
                    mEcgHrvTv.setText(measureEcgBean.getHrv() + "");

                    newList.clear();
                    newList.add(measureEcgBean.getRrMax() + "");
                    newList.add(measureEcgBean.getRrMin() + "");
                    newList.add(measureEcgBean.getHrv() + "");
                    newList.add(measureEcgBean.getHr() + "");
                    newList.add(measureEcgBean.getMood() + "");
                    newList.add(measureEcgBean.getBr() + "");
                    newHealthTestAdapter.setArrayList(newList);
                    newHealthTestAdapter.notifyDataSetChanged();
                    measurePresenter.putBpValue(HealthMonitorActivity.mMemberId+"",HealthMonitorActivity.mMachine.getMachineBindId(),String.valueOf(IDatalifeConstant.ECGINT),
                            HealthMonitorActivity.mMachine.getMachineSn(), DeviceData.getUniqueId(getActivity()),measureEcgBean.getRrMax()+"",measureEcgBean.getRrMin() + "",measureEcgBean.getHr() + "",
                            measureEcgBean.getHrv() + "",measureEcgBean.getMood() + "",measureEcgBean.getBr() + "");

                    try{
                        IDatalifeConstant.saveEcgData(getActivity(),IDatalifeConstant.serialize(measureEcgBean));
                    }catch (Exception e){
                        toast("心电图数据未保存");
                    }
                    break;
                case ECGHANDLERHR:
                    mEcgHrTv.setText(measureEcgBean.getHr() + "");
                    break;
                case ECGHANDLERRRMAX:
                    mEcgRrMaxTv.setText(measureEcgBean.getRrMax() + "");
                    break;
                case ECGHANDLERRRMIN:
                    mEcgRrMinTv.setText(measureEcgBean.getRrMin() + "");
                    break;
                case ECGHANDLERHRV:
                    mEcgHrvTv.setText(measureEcgBean.getHrv() + "");
                    break;
                case ECGHANDLERMOOD:
                    mEcgMoodTv.setText(measureEcgBean.getMood() + "");
                    break;
                case ECGHANDLERBR:
                    mEcgBrTv.setText(measureEcgBean.getBr() + "");
                    break;
            }
        }
    };

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_ecg;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mHcService != null) {
            mEcgTask = mHcService.getBleDevManager().getEcgTask();
            mEcgTask.setOnEcgResultListener(this);
            mEcgTask.setUserInfo("xxx", 27, 166, 65, false);
        }
    }

    @Override
    protected void initEventAndData() {
        measureEcgBean = new MeasureEcgBean();

        measurePresenter.onCreate(getActivity(),this);

        mEcgWaveView.setCalibration(1f);
        mEcgWaveView.setPagerSpeed(1);
        if (mTableLayout != null && mTableLayout.isShown()) {
            mTableLayout.setVisibility(View.GONE);
        }

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTest.setLayoutManager(linearLayoutManager1);

        newHealthTestAdapter = new HealthTestAdapter(getActivity(),R.color.bg_toolbar_title,null);
        rvTest.setAdapter(newHealthTestAdapter);
        newHealthTestAdapter = new HealthTestAdapter(getActivity(),R.color.bg_toolbar_title,null);
        rvTest.setAdapter(newHealthTestAdapter);
        rvTest.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    rvOldTest.scrollBy(dx,dy);
                }
            }
        });

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvOldTest.setLayoutManager(linearLayoutManager2);
        oldHealthTestAdapter = new HealthTestAdapter(getActivity(),R.color.yellow_test,oldList);
        rvOldTest.setAdapter(oldHealthTestAdapter);
        rvOldTest.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    rvTest.scrollBy(dx,dy);
                }
            }
        });
    }

    @Override
    public boolean startMeasure() {
        if (mEcgTask != null) {

            if (mHcService.getBleDevManager().getBatteryTask().isCharging()) {
                toast(R.string.health_charging);
                return true;
            }

            if (newList!=null && oldHealthTestAdapter != null && newList.size()>0){
                oldHealthTestAdapter.setArrayList(newList);
                oldHealthTestAdapter.notifyDataSetChanged();
                newHealthTestAdapter.setArrayList(null);
                newHealthTestAdapter.notifyDataSetChanged();
            }

            isOver = false;

            if (mEcgTask.isModuleExist()) {
                mEcgRrMaxTv.setText("");
                mEcgRrMinTv.setText("");
                mEcgHrTv.setText("");
                mEcgBrTv.setText("");
                mEcgHrvTv.setText("");
                mEcgMoodTv.setText("");

                mEcgTask.initEcgTg();
                mEcgTask.start();
                if (mTableLayout != null && mTableLayout.isShown()) {
                    mTableLayout.setVisibility(View.GONE);
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void stopMeasure() {
        if (mEcgTask != null) {
            mEcgTask.stop();
        }
    }

    @OnClick({R.id.fl_openECGLarge, R.id.btn_starttest,R.id.tv_reference_value,R.id.tv_back,R.id.ll_more_historyrecord})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fl_openECGLarge:
                if (isOver){
                    openECGLarge();
                }
                break;
            case R.id.btn_starttest:
                clickMeasure(view);
                break;
            case R.id.tv_reference_value:
                mTableLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_back:
                mTableLayout.setVisibility(View.GONE);
                break;
            case R.id.ll_more_historyrecord:

                break;
        }

    }

    public void openECGLarge() {
        Intent intent = new Intent(mActivity, ECGLargeChartActivity.class);
        intent.putExtra("pagerSpeed", 1);
        intent.putExtra("calibration", 1f);
        intent.putExtra("model", measureEcgBean);
        startActivity(intent);
    }


    @Override
    protected void getchange(String str) {
        mStartTestBtn.setText(str);
    }

    @Override
    public void clickUploadData(View v) {

    }

    @Override
    public void onUploadSuccess() {

    }

    @Override
    public void onUploadFail(String str) {

    }

    /*
     * 心电图数据点
     */
    @Override
    public void onDrawWave(int wave) {
        //将数据点在心电图控件里描绘出来
        mEcgWaveView.preparePoint(wave);
        //将数据点存入容器，查看大图使用
        ecgWaveBuilder.append(wave).append(",");
    }

    @Override
    public void onSignalQuality(int i) {

    }

    @Override
    public void onAvgHr(int avgHr) {
        measureEcgBean.setHr(avgHr);
        myEcgHandler.sendEmptyMessage(ECGHANDLERHR);
    }

    @Override
    public void onRRMax(int rr) {
        measureEcgBean.setRrMax(rr);
        myEcgHandler.sendEmptyMessage(ECGHANDLERRRMAX);
    }

    @Override
    public void onRRMin(int rr) {
        measureEcgBean.setRrMin(rr);
        myEcgHandler.sendEmptyMessage(ECGHANDLERRRMIN);
    }

    @Override
    public void onHrv(int hrv) {
        measureEcgBean.setHrv(hrv);
        myEcgHandler.sendEmptyMessage(ECGHANDLERHRV);
    }

    @Override
    public void onMood(int mood) {
        measureEcgBean.setMood(mood);
        myEcgHandler.sendEmptyMessage(ECGHANDLERMOOD);
    }

    @Override
    public void onBr(int br) {
        measureEcgBean.setBr(br);
        myEcgHandler.sendEmptyMessage(ECGHANDLERBR);
    }

    @Override
    public void onEcgDuration(long duration) {
        measureEcgBean.setDuration(duration);
        measureEcgBean.setTs(System.currentTimeMillis() / 1000L);
        String ecgWave = ecgWaveBuilder.toString();
        ecgWave = ecgWave.substring(0, ecgWave.length() - 1);
        measureEcgBean.setWave(ecgWave);

        myEcgHandler.sendEmptyMessage(ECGHANDDATA);
        isOver = true;
    }
}
