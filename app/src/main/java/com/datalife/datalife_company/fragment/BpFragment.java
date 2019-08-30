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
import android.widget.Toast;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.HealthMonitorActivity;
import com.datalife.datalife_company.bean.MeasureBpBean;
import com.datalife.datalife_company.contract.MeasureContract;
import com.datalife.datalife_company.presenter.MeasurePresenter;
import com.datalife.datalife_company.util.DeviceData;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.widget.HealthDialProgress;
import com.linktop.infs.OnBpResultListener;
import com.linktop.whealthService.task.BpTask;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 血压
 * Created by LG on 2019/7/19.
 */
public class BpFragment extends MeasureFragment implements OnBpResultListener,MeasureContract {

    @BindView(R.id.tv_hr)
    TextView mHrTv;//舒张压/收缩压
    @BindView(R.id.btn_starttest)
    Button mClickTestBtn;
    @BindView(R.id.tv_new_dbp_test)
    TextView mNewDbpTest;
    @BindView(R.id.tv_old_dbp_test)
    TextView mOldDbpTest;
    @BindView(R.id.tv_old_test)
    TextView mOldText;
    @BindView(R.id.tv_new_test)
    TextView mNewTest;
    @BindView(R.id.dial_progress_bar)
    HealthDialProgress mHealthSbpDialProgress;
    @BindView(R.id.tv_dial_sbp)
    TextView mSbpTv;
    @BindView(R.id.pb_sbp)
    ProgressBar mSbpProgressBar;
    @BindView(R.id.tv_bp_chart)
    TextView mBpChart;
    @BindView(R.id.ic_bp_chart)
    ImageView mIvBpChart;

    private MeasureBpBean model;
    private BpTask mBpTask;
    private boolean isStartMeasure = false;
    private final int BPDATA = 0x123;
    private MeasurePresenter measurePresenter = new MeasurePresenter();

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case BPDATA:

                    IDatalifeConstant.startAlarm(getActivity());

                    int sbpValue = model.getSbp();
                    int dbpValue = model.getDbp();

                    mClickTestBtn.setText(R.string.start);
                    mSbpProgressBar.setVisibility(View.GONE);

                    mHealthSbpDialProgress.setcolor(IDatalifeConstant.getColor(getActivity()));
                    mHealthSbpDialProgress.setValue(getBpResult(sbpValue,dbpValue));
                    mSbpTv.setVisibility(View.VISIBLE);

                    mNewDbpTest.setText(sbpValue+"/"+ dbpValue+ "(mmHg)");

                    if (dbpValue < 10 || sbpValue<10){
                        toast("您测量不准确,请准确测量");
                        return;
                    }

                    isStartMeasure = false;

                    try{
                        IDatalifeConstant.saveBpData(getActivity(),IDatalifeConstant.serialize(model));
                    }catch (Exception e){
                        toast("血压没保存");
                    }
                    measurePresenter.putBpValue(HealthMonitorActivity.mMemberId + "",HealthMonitorActivity.mMachine.getMachineId(),String.valueOf(IDatalifeConstant.BPINT),
                            HealthMonitorActivity.mMachine.getMachineSn().toLowerCase(), DeviceData.getUniqueId(getActivity()),
                            model.getDbp() + "",model.getSbp() + "",model.getHr()+"","","","");


                    break;
            }
        }
    };

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_bp;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mHcService != null){
            mBpTask = mHcService.getBleDevManager().getBpTask();
            mBpTask.setOnBpResultListener(this);
        }
    }

    @Override
    protected void initEventAndData() {
        measurePresenter.onCreate(getActivity(),this);
        model = new MeasureBpBean();
        mHealthSbpDialProgress.setcolor(IDatalifeConstant.getColor(getActivity()));
    }

    public void setStart(){
        mClickTestBtn.setText("开始测量");
    }

    @Override
    public void stopMeasure() {
        if (mBpTask != null) {
            mBpTask.stop();
            isStartMeasure = false;
            mSbpProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void getchange(String str) {
        mClickTestBtn.setText(str);
    }

    @Override
    public boolean startMeasure() {

        if (mHcService.getBleDevManager().getBatteryTask().getPower() < 5){
            toast("设备电量过低，请充电");
            setStart();
            return false;
        }
        isStartMeasure = true;
        mBpTask.start();

        return true;
    }

    @Override
    public void clickUploadData(View v) {

    }

    @OnClick({R.id.btn_starttest,R.id.tv_bp_chart})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_starttest:
                if (mClickTestBtn.getText().toString().equals(getResources().getString(R.string.back))){
                    mIvBpChart.setVisibility(View.GONE);
                    if (isStartMeasure){
                        mClickTestBtn.setText(R.string.stopmeasure);
                    }else {
                        mClickTestBtn.setText(R.string.start);
                    }
                }else {
                    clickMeasure(view);
                }
                break;

            case R.id.tv_bp_chart:
                mIvBpChart.setVisibility(View.VISIBLE);

                mClickTestBtn.setText(R.string.back);

                break;
        }
    }


    /**
     * 获取血压的结果
     * @param systolicPressure
     * @param diastolicPressure
     * @param heartRate
     */
    @Override
    public void onBpResult(final int systolicPressure, final int diastolicPressure, final int heartRate) {
        model.setTs(System.currentTimeMillis() / 1000L);
        model.setSbp(systolicPressure);
        model.setDbp(diastolicPressure);
        model.setHr(heartRate);
        myHandler.sendEmptyMessage(BPDATA);
    }

    @Override
    public void onLeakError(int errorType) {
        Observable.just(errorType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer error) {
                        int textId = 0;
                        switch (error) {
                            case 0:
                                mSbpProgressBar.setVisibility(View.GONE);
                                textId = R.string.leak_and_check;
                                setStart();
                                break;
                            case 1:
                                mSbpProgressBar.setVisibility(View.GONE);
                                textId = R.string.measurement_void;
                                setStart();
                                break;
                            default:
                                break;
                        }
                        if (textId != 0)
                            Toast.makeText(getContext(), getString(textId), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private float getBpResult(int sbpValue,int dbpValue){

        if(90 > sbpValue){
            if (dbpValue < 60){
                mSbpTv.setText(R.string.bp_low);
                return 5f;
            }else if (60<= dbpValue && dbpValue<= 85) {
                mSbpTv.setText(R.string.bp_normal);
                return 15f;
            }else if (dbpValue > 85  && dbpValue<=90){
                mSbpTv.setText(R.string.bp_scarce);
                return 25f;
            }else if (dbpValue > 90  && dbpValue<=100){
                mSbpTv.setText(R.string.bp_little);
                return 35f;
            }else if (dbpValue > 100  && dbpValue<=110){
                mSbpTv.setText(R.string.bp_middle);
                return 45f;
            }else if (dbpValue > 110 ){
                mSbpTv.setText(R.string.bp_very_high);
                return 55f;
            }
        }else if (90 <= sbpValue && sbpValue <= 130){
            if (dbpValue<= 85) {
                mSbpTv.setText(R.string.bp_normal);
                return 15f;
            }else if (dbpValue > 85  && dbpValue<=90){
                mSbpTv.setText(R.string.bp_scarce);
                return 25f;
            }else if (dbpValue > 90  && dbpValue<=100){
                mSbpTv.setText(R.string.bp_little);
                return 35f;
            }else if (dbpValue > 100  && dbpValue<=110){
                mSbpTv.setText(R.string.bp_middle);
                return 45f;
            }else if (dbpValue > 110 ){
                mSbpTv.setText(R.string.bp_very_high);
                return 55f;
            }
        }else if ( 130 < sbpValue && sbpValue <= 140){
            if (dbpValue <= 90){
                mSbpTv.setText(R.string.bp_scarce);
                return 25f;
            }else if (dbpValue > 90  && dbpValue<=100){
                mSbpTv.setText(R.string.bp_little);
                return 35f;
            }else if (dbpValue > 100  && dbpValue<=110){
                mSbpTv.setText(R.string.bp_middle);
                return 45f;
            }else if (dbpValue > 110 ){
                mSbpTv.setText(R.string.bp_very_high);
                return 55f;
            }
        }else if (140 < sbpValue && sbpValue <= 160){
            if (dbpValue <= 100){
                mSbpTv.setText(R.string.bp_little);
                return 35f;
            }else if (dbpValue > 100  && dbpValue<=110){
                mSbpTv.setText(R.string.bp_middle);
                return 45f;
            }else if (dbpValue > 110 ){
                mSbpTv.setText(R.string.bp_very_high);
                return 55f;
            }
        }else if (160 < sbpValue && sbpValue <= 180){
            if(dbpValue <= 110){
                mSbpTv.setText(R.string.bp_middle);
                return 45f;
            } else if (dbpValue > 110 ){
                mSbpTv.setText(R.string.bp_very_high);
                return 55f;
            }
        }else if (180 < sbpValue){
            mSbpTv.setText(R.string.bp_very_high);
            return 55f;
        }

        return 15f;
    }

    @Override
    public void onUploadSuccess() {

    }

    @Override
    public void onUploadFail(String str) {

    }
}
