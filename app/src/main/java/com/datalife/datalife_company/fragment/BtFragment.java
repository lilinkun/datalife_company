package com.datalife.datalife_company.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.HealthMonitorActivity;
import com.datalife.datalife_company.bean.MeasureBtBean;
import com.datalife.datalife_company.contract.MeasureContract;
import com.datalife.datalife_company.presenter.MeasurePresenter;
import com.datalife.datalife_company.util.DeviceData;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UIHelper;
import com.datalife.datalife_company.widget.CircleProgressBar;
import com.datalife.datalife_company.widget.HealthDialProgress;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnBtResultListener;
import com.linktop.whealthService.MeasureType;
import com.linktop.whealthService.task.BtTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2019/7/19.
 */
public class BtFragment extends MeasureFragment implements OnBtResultListener,MeasureContract {

    @BindView(R.id.tv_temp_start)
    TextView mTempStartTv;
    @BindView(R.id.tv_temp)
    TextView mTempTv;
    @BindView(R.id.btn_starttest)
    Button mStartTestBtn;
    @BindView(R.id.bt_progress)
    CircleProgressBar mProgressBar;
    @BindView(R.id.tv_new_test)
    TextView mNewTestView;
    @BindView(R.id.tv_old_test)
    TextView mOldTestView;
    @BindView(R.id.dial_progress_bar)
    HealthDialProgress dialProgress;
    @BindView(R.id.iv_color_dial_bt)
    ImageView mIvBtDial;
    @BindView(R.id.tv_dial_bt)
    TextView mbtTextView;
    @BindView(R.id.pb_bt)
    ProgressBar mProgress;

    private BtTask mBtTask;
    private MeasureBtBean measureBtBean;
    private final int BTDATA = 0x111;

    MeasurePresenter measurePresenter = new MeasurePresenter();

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case BTDATA:
                    IDatalifeConstant.startAlarm(getActivity());
                    mProgress.setVisibility(View.GONE);
                    if(measureBtBean.getTemp() > 43 || measureBtBean.getTemp() < 34){
                        toast("请准确测量");
                        return;
                    }
                    dialProgress.setcolor(IDatalifeConstant.getColor(getActivity()));
                    dialProgress.setValue((float)(measureBtBean.getTemp()-31.5));
                    setValue(measureBtBean.getTemp());
                    IDatalifeConstant.saveBtData(getActivity(),String.valueOf(measureBtBean.getTemp()));
                    mNewTestView.setText(measureBtBean.getTemp()+"");
                    mStartTestBtn.setText(getString(R.string.start));


//                    double temp = model.getTemp()- 35;
//                    mProgressBar.setProgress(temp);
                    measurePresenter.putBpValue(HealthMonitorActivity.mMemberId + "",HealthMonitorActivity.mMachine.getMachineBindId(),String.valueOf(IDatalifeConstant.BTINT),
                            HealthMonitorActivity.mMachine.getMachineSn().toLowerCase(), DeviceData.getUniqueId(getActivity()), measureBtBean.getTemp()+"",
                            "","","","","");


                    break;
            }
        }
    };

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_bt;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mHcService != null) {
            mBtTask = mHcService.getBleDevManager().getBtTask();
            mBtTask.setOnBtResultListener(this);
        }
    }

    @Override
    protected void initEventAndData() {
        measurePresenter.onCreate(getActivity(),this);

        measureBtBean = new MeasureBtBean();

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
    public boolean startMeasure() {
        if(mBtTask!= null) {
            mBtTask.start();
            mTempTv.setText("");
            mProgress.setVisibility(View.VISIBLE);
        }

        if (!mNewTestView.getText().toString().equals("-")) {
            setOldTestView(mNewTestView.getText().toString());
            mNewTestView.setText("-");
            dialProgress.setValue(0);
        }

        return true;
    }

    @Override
    public void stopMeasure() {
        if (mBtTask != null) {
            mBtTask.stop();
            mStartTestBtn.setText(getString(R.string.start));
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    protected void getchange(String str) {

    }

    @Override
    public void clickUploadData(View v) {

    }

    @Override
    public void onBtResult(double tempValue) {
        measureBtBean.setTemp(tempValue);
        measureBtBean.setTs(System.currentTimeMillis() / 1000L);
        myHandler.sendEmptyMessage(BTDATA);
    }

    @Override
    public void onUploadSuccess() {
        Log.v("BtFragment","success");
    }

    @Override
    public void onUploadFail(String str) {
        toast(str);
    }

    public void setOldTestView(String bttemp){
        if (bttemp != null && bttemp.trim().length() != 0){
            mOldTestView.setText(bttemp + "");
        }
    }

    //圆环下显示的字
    public void setValue(double bt){
        if (bt >= 37.5){
            mIvBtDial.setBackground(getResources().getDrawable(R.color.dial_red));
            mbtTextView.setText(getResources().getString(R.string.high_fever));
        }else if (bt <=  37.4 && bt >= 36){
            mIvBtDial.setBackground(getResources().getDrawable(R.color.dial_green));
            mbtTextView.setText(getResources().getString(R.string.normal));
        }else if (bt <= 35.9){
            mIvBtDial.setBackground(getResources().getDrawable(R.color.dial_blue));
            mbtTextView.setText(getResources().getString(R.string.low_fever));
        }

    }
}
