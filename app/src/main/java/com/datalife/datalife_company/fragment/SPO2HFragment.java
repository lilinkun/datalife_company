package com.datalife.datalife_company.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.HealthMonitorActivity;
import com.datalife.datalife_company.bean.MeasureSpo2hBean;
import com.datalife.datalife_company.contract.MeasureContract;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.presenter.MeasurePresenter;
import com.datalife.datalife_company.util.DeviceData;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UIHelper;
import com.datalife.datalife_company.widget.HealthDialProgress;
import com.datalife.datalife_company.widget.OxWaveView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnSPO2HResultListener;
import com.linktop.whealthService.task.OxTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2019/7/19.
 */

public class SPO2HFragment extends MeasureFragment implements MeasureContract, OnSPO2HResultListener {

    @BindView(R.id.tv_spo2h)
    TextView mSpo2hTv;
    @BindView(R.id.oxWave)
    OxWaveView mOxWaveTv;
    @BindView(R.id.iv_spo2h)
    ImageView ivspo2h;
    @BindView(R.id.btn_starttest)
    Button mStartTestBtn;
    @BindView(R.id.ic_heartrate)
    ImageView mIvHeartrate;
    @BindView(R.id.tv_old_test)
    TextView mOldTest;
    @BindView(R.id.tv_new_test)
    TextView mNewTest;
    @BindView(R.id.dial_progress_bar)
    HealthDialProgress healthDialProgress;
    @BindView(R.id.iv_color_dial_spo2h)
    ImageView mIvSpo2hDial;
    @BindView(R.id.tv_dial_spo2h)
    TextView mTvSpo2hDial;
    @BindView(R.id.pb_spo2h)
    ProgressBar mSpo2hProgressBar;

    private final int SPO2HDATA = 0x222;
    private MeasureSpo2hBean measureSpo2hBean;
    private OxTask mOxTask;
    private MeasurePresenter measurePresenter = new MeasurePresenter();


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case SPO2HDATA:

                    IDatalifeConstant.startAlarm(getActivity());
//                    mSpo2hHrTv.setText(model.getHr() + "");
                    mSpo2hTv.setText(measureSpo2hBean.getValue() + "%");
                    mNewTest.setText(measureSpo2hBean.getValue() + "");
                    mSpo2hProgressBar.setVisibility(View.GONE);
                    if (measureSpo2hBean.getValue() < 50){
                        toast("您测量不准确");
                        return;
                    }
                    healthDialProgress.setcolor(IDatalifeConstant.getColor(getActivity()));
                    healthDialProgress.setValue((float)(measureSpo2hBean.getValue()-60));

                    IDatalifeConstant.saveSpo2hData(getActivity(),measureSpo2hBean.getValue()+"");

                    if (measureSpo2hBean.getValue() != 0) {
                        measurePresenter.putBpValue(HealthMonitorActivity.mMemberId + "",HealthMonitorActivity.mMachine.getMachineBindId(), String.valueOf(IDatalifeConstant.SPO2HINT), HealthMonitorActivity.mMachine.getMachineSn().toLowerCase(), DeviceData.getUniqueId(getActivity()), measureSpo2hBean.getValue() + "", measureSpo2hBean.getHr() + "","","","","");
                    }
                    break;
            }

        }
    };


    @Override
    protected int getlayoutId() {
        return R.layout.fragment_spo2h;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mHcService != null) {
            mOxTask = mHcService.getBleDevManager().getOxTask();
            mOxTask.setOnSPO2HResultListener(this);
        }
    }

    @Override
    protected void initEventAndData() {
        measureSpo2hBean = new MeasureSpo2hBean();
    }

    @Override
    public boolean startMeasure() {
        if (mOxTask != null) {
            mOxTask.start();
            mSpo2hTv.setText("");

            if (mNewTest !=null && !mNewTest.getText().toString().equals("-")){
                mOldTest.setText(mNewTest.getText().toString());
                mNewTest.setText("-");
                healthDialProgress.setValue(0);
            }
        }
        return true;
    }

    @Override
    public void stopMeasure() {
        if (mOxTask != null) {
            mOxTask.stop();
            mSpo2hProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void getchange(String str) {
        mStartTestBtn.setText(str);
        if (str != null  && str.equals(getString(R.string.stopmeasure))){
            if (mIvHeartrate != null && mIvHeartrate.isShown()) {
                mIvHeartrate.setVisibility(View.GONE);
                mSpo2hProgressBar.setVisibility(View.VISIBLE);
            }
        }else {
            if (mIvHeartrate != null && mIvHeartrate.isShown()) {
                mIvHeartrate.setVisibility(View.VISIBLE);
                mSpo2hProgressBar.setVisibility(View.GONE);
                mOxWaveTv.clear();
            }
        }
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

    @OnClick({R.id.btn_starttest,R.id.ll_more_historyrecord})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_starttest:
                clickMeasure(view);
                break;
            case R.id.ll_more_historyrecord:

                break;
        }
    }

    @Override
    public void onSPO2HResult(int spo2h, int heartRate) {
        measureSpo2hBean.setTs(System.currentTimeMillis() / 1000L);
        measureSpo2hBean.setValue(spo2h);
        measureSpo2hBean.setHr(heartRate);

        handler.sendEmptyMessage(SPO2HDATA);
    }

    @Override
    public void onSPO2HWave(int value) {
        mOxWaveTv.preparePoints(value);
    }
}
