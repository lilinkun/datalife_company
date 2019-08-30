package com.datalife.datalife_company.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.util.DensityUtil;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.MeasureNorm;
import com.datalife.datalife_company.util.StandardUtils;

import aicare.net.cn.iweightlibrary.entity.BodyFatData;
import aicare.net.cn.iweightlibrary.entity.WeightData;

/**
 * 体脂称各项检测数据layout
 * Created by LG on 2019/7/24.
 */
public class CommonLayout extends LinearLayout implements View.OnClickListener {

    private Resources res;
    private LinearLayout mDetailsFirstLayout;
    private LinearLayout mDetailsSecondLayout;
    private LinearLayout mDetailsThirdLayout;
    private LinearLayout mDetailsFourLayout;
    private ImageView iv_weight;
    private ImageView ic_bmi;
    private ImageView ic_bfr;
    //    private ImageView ic_fat;
    private ImageView ic_uvi;
    private ImageView ic_water;
    private ImageView ic_muscle;
    private ImageView ic_bones;
    //    private ImageView ic_bodyage;
    private ImageView ic_defatted_body_weight;
    private ImageView ic_metabolize;
    private TextView mFatRow1;
    private TextView mFatRow2;
    private TextView mFatRow3;
    private TextView mFatRow4;
    private TextView weightValue;
    private TextView defattedBodyWeightValue;
    private TextView bonesValue;
    private TextView metabolizeValue;
    //    private TextView fatValue;
    private TextView muscleValue;
    private TextView waterValue;
    private TextView uviValue;
    //    private TextView bodyageValue;
    private TextView bmiValue;
    private TextView bfrValue;

    private RelativeLayout weightRl;
    private RelativeLayout bmiRl;
    private RelativeLayout bfrRl;
    private RelativeLayout muscleRl;
    private RelativeLayout waterRl;
    private RelativeLayout defattedBodyWeightRl;
    private RelativeLayout bonesRl;
    //    private RelativeLayout bodyageRl;
    private RelativeLayout metabolizeRl;
    //    private RelativeLayout fatRl;
    private RelativeLayout uviRl;

    private TextView mWeightStandardTv;
    private TextView mBmiStandardTv;
    private TextView mBfrStandardTv;
    private TextView mMuscleStandardTv;
    private TextView mWaterStandardTv;
    private TextView mDefattedBodyWeightStandardTv;
    private TextView mBonesStandardTv;
    //    private TextView mBodyAgeStandardTv;
    private TextView mMetabolizeStandardTv;
    //    private TextView mFatStandardTv;
    private TextView mUviStandardTv;

    private BigStateView mStateView1;
    private BigStateView mStateView2;
    private BigStateView mStateView3;
    private BigStateView mStateView4;

    private int icPosition = 0;
    private int layoutRowPosition = 0;
    private Context mContext;
    private BodyFatData mBodyFatData = null;

    private Handler myHandler;

    private int standar = 1;
    private int low = 0;
    private int high = 2;

    public CommonLayout(Context context) {
        super(context);
        init(context);
    }

    public CommonLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context){
        mContext = context;
        View v = LayoutInflater.from(context).inflate(R.layout.layout_common,null);
        res = this.getResources();
        mDetailsFirstLayout = (LinearLayout) v.findViewById(R.id.layout_first_details);
        mDetailsSecondLayout = (LinearLayout) v.findViewById(R.id.layout_second_details);
        mDetailsThirdLayout = (LinearLayout) v.findViewById(R.id.layout_third_details);
        mDetailsFourLayout = (LinearLayout) v.findViewById(R.id.layout_four_details);
        iv_weight = (ImageView) v.findViewById(R.id.ic_weight);
        ic_bmi = (ImageView) v.findViewById(R.id.ic_bmi);
        ic_bfr = (ImageView) v.findViewById(R.id.ic_bfr);
//        ic_fat = (ImageView) v.findViewById(R.id.ic_fat);
        ic_uvi = (ImageView) v.findViewById(R.id.ic_uvi);
        ic_water = (ImageView) v.findViewById(R.id.ic_water);
        ic_muscle = (ImageView) v.findViewById(R.id.ic_muscle);
        ic_bones = (ImageView) v.findViewById(R.id.ic_bones);
//        ic_bodyage = (ImageView) v.findViewById(R.id.ic_bodyage);
        ic_defatted_body_weight = (ImageView) v.findViewById(R.id.ic_defatted_body_weight);
        ic_metabolize = (ImageView) v.findViewById(R.id.ic_metabolize);
        mFatRow1 = (TextView) v.findViewById(R.id.tv_fat_row1);
        mFatRow2 = (TextView) v.findViewById(R.id.tv_fat_row2);
        mFatRow3 = (TextView) v.findViewById(R.id.tv_fat_row3);
        mFatRow4 = (TextView) v.findViewById(R.id.tv_fat_row4);
        weightValue = (TextView) v.findViewById(R.id.tv_weight_value);
        defattedBodyWeightValue = (TextView) v.findViewById(R.id.tv_defatted_body_weight_value);
        bonesValue = (TextView) v.findViewById(R.id.tv_bones_value);
        metabolizeValue = (TextView) v.findViewById(R.id.tv_metabolize_value);
//        fatValue = (TextView) v.findViewById(R.id.tv_fat_value);
        muscleValue = (TextView) v.findViewById(R.id.tv_muscle_value);
        waterValue = (TextView) v.findViewById(R.id.tv_water_value);
        uviValue = (TextView) v.findViewById(R.id.tv_uvi_value);
//        bodyageValue = (TextView) v.findViewById(R.id.tv_bodyage_value);
        bmiValue = (TextView) v.findViewById(R.id.tv_bmi_value);
        bfrValue = (TextView) v.findViewById(R.id.tv_bfr_value);

        mStateView1 = (BigStateView) v.findViewById(R.id.stateview1);
        mStateView2 = (BigStateView) v.findViewById(R.id.stateview2);
        mStateView3 = (BigStateView) v.findViewById(R.id.stateview3);
        mStateView4 = (BigStateView) v.findViewById(R.id.stateview4);
//      R.id.iv_head_left

        mWeightStandardTv = (TextView) v.findViewById(R.id.tv_weight_standard);
        mBmiStandardTv = (TextView) v.findViewById(R.id.tv_bmi_standard);
        mBfrStandardTv = (TextView) v.findViewById(R.id.tv_bfr_standard);
        mMuscleStandardTv = (TextView) v.findViewById(R.id.tv_muscle_standard);
        mWaterStandardTv = (TextView) v.findViewById(R.id.tv_water_standard);
        mDefattedBodyWeightStandardTv = (TextView) v.findViewById(R.id.tv_defatted_body_weight_standard);
        mBonesStandardTv = (TextView) v.findViewById(R.id.tv_bones_standard);
//        mBodyAgeStandardTv = (TextView) v.findViewById(R.id.tv_bodyage_standard);
        mMetabolizeStandardTv = (TextView) v.findViewById(R.id.tv_metabolize_standard);
//        mFatStandardTv = (TextView) v.findViewById(R.id.tv_fat_standard);
        mUviStandardTv = (TextView) v.findViewById(R.id.tv_uvi_standard);

        weightRl = (RelativeLayout) v.findViewById(R.id.layout_weight);
        bmiRl = (RelativeLayout) v.findViewById(R.id.layout_bmi);
        bfrRl = (RelativeLayout) v.findViewById(R.id.layout_bfr);
        muscleRl = (RelativeLayout) v.findViewById(R.id.layout_muscle);
        waterRl = (RelativeLayout) v.findViewById(R.id.layout_water);
        defattedBodyWeightRl = (RelativeLayout) v.findViewById(R.id.layout_defatted_body_weight);
        bonesRl = (RelativeLayout) v.findViewById(R.id.layout_bones);
//        bodyageRl = (RelativeLayout) v.findViewById(R.id.layout_bodyage);
        metabolizeRl = (RelativeLayout) v.findViewById(R.id.layout_metabolize);
//        fatRl = (RelativeLayout) v.findViewById(R.id.layout_fat);
        uviRl = (RelativeLayout) v.findViewById(R.id.layout_uvi);
        weightRl.setOnClickListener(this);
        bmiRl.setOnClickListener(this);
        bfrRl.setOnClickListener(this);
        muscleRl.setOnClickListener(this);
        waterRl.setOnClickListener(this);
        defattedBodyWeightRl.setOnClickListener(this);
        bonesRl.setOnClickListener(this);
//        bodyageRl.setOnClickListener(this);
        metabolizeRl.setOnClickListener(this);
//        fatRl.setOnClickListener(this);
        uviRl.setOnClickListener(this);

        this.addView(v, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onClick(View v) {
        StandardUtils.Standard stan = new StandardUtils.Standard();
        if (myHandler != null){
            myHandler.sendEmptyMessage(0x112233);
        }
        switch (v.getId()) {
            case R.id.layout_weight:
                if (iv_weight != null && iv_weight.isShown()) {
                    iv_weight.setVisibility(View.GONE);
                    mDetailsFirstLayout.setVisibility(View.GONE);
                    icPosition = 0;
                } else {
                    goneIc();
                    iv_weight.setVisibility(View.VISIBLE);
                    mDetailsFirstLayout.setVisibility(View.VISIBLE);
                    icPosition = 1;
                    layoutRowPosition = 1;
                    mFatRow1.setText(getResources().getString(R.string.fatList_1));
                }
                break;

            case R.id.layout_bmi:
                if (ic_bmi != null && ic_bmi.isShown()) {
                    ic_bmi.setVisibility(View.GONE);
                    mDetailsFirstLayout.setVisibility(View.GONE);
                    icPosition = 0;
                } else {
                    goneIc();
                    ic_bmi.setVisibility(View.VISIBLE);
                    mDetailsFirstLayout.setVisibility(View.VISIBLE);
                    icPosition = 2;
                    layoutRowPosition = 1;
                    if (mBodyFatData != null){
                        stan = MeasureNorm.getBMI();
                        mStateView1.setLanguage(getResources().getString(R.string.text_Low_text), getResources().getString(R.string.text_Standard_text),getResources().getString(R.string.text_High_text));
                        mStateView1.setValue(IDatalifeConstant.getWM(mContext), mBodyFatData.getBmi(), stan, "");
                    }
                    mFatRow1.setText(getResources().getString(R.string.fatList_2));
                }
                break;

            case R.id.layout_bfr:
                if (ic_bfr != null && ic_bfr.isShown()) {
                    ic_bfr.setVisibility(View.GONE);
                    mDetailsFirstLayout.setVisibility(View.GONE);
                    icPosition = 0;
                } else {
                    goneIc();
                    ic_bfr.setVisibility(View.VISIBLE);
                    mDetailsFirstLayout.setVisibility(View.VISIBLE);
                    icPosition = 3;
                    layoutRowPosition = 1;
                    if (mBodyFatData != null){
                        stan = MeasureNorm.getBfr(mBodyFatData.getAge(),mBodyFatData.getSex());
                        mStateView1.setLanguage(getResources().getString(R.string.text_Low_text), getResources().getString(R.string.text_Standard_text),getResources().getString(R.string.text_High_text));
                        mStateView1.setValue(IDatalifeConstant.getWM(mContext), mBodyFatData.getBfr(), stan, "");
                    }
                    mFatRow1.setText(getResources().getString(R.string.fatList_3));
                }

                break;

            case R.id.layout_muscle:
                if (ic_muscle != null && ic_muscle.isShown()) {
                    ic_muscle.setVisibility(View.GONE);
                    mDetailsSecondLayout.setVisibility(View.GONE);
                    icPosition = 0;
                } else {
                    goneIc();
                    ic_muscle.setVisibility(View.VISIBLE);
                    mDetailsSecondLayout.setVisibility(View.VISIBLE);
                    icPosition = 4;
                    layoutRowPosition = 2;
                    if (mBodyFatData != null){
                        stan = MeasureNorm.getMuscl(mBodyFatData.getSex());
                        mStateView2.setLanguage(getResources().getString(R.string.text_Low_text), getResources().getString(R.string.text_Standard_text),getResources().getString(R.string.good));
                        mStateView2.setValue(IDatalifeConstant.getWM(mContext), mBodyFatData.getRom()/mBodyFatData.getWeight(), stan, "");
                    }
                    mFatRow2.setText(getResources().getString(R.string.fatList_4));
                }
                break;

            case R.id.layout_water:
                if (ic_water != null && ic_water.isShown()) {
                    ic_water.setVisibility(View.GONE);
                    mDetailsSecondLayout.setVisibility(View.GONE);
                    icPosition = 0;
                } else {
                    goneIc();
                    ic_water.setVisibility(View.VISIBLE);
                    mDetailsSecondLayout.setVisibility(View.VISIBLE);
                    icPosition = 5;
                    layoutRowPosition = 2;
                    mFatRow2.setText(getResources().getString(R.string.fatList_5));
                    if (mBodyFatData != null){
                        stan = MeasureNorm.getWet((int)mBodyFatData.getWeight(), mBodyFatData.getSex());
                        mStateView2.setLanguage(getResources().getString(R.string.text_Low_text), getResources().getString(R.string.text_Standard_text),getResources().getString(R.string.text_High_text));
                        mStateView2.setValue(IDatalifeConstant.getWM(mContext), mBodyFatData.getVwc(), stan, "");
                    }
                }
                break;

            case R.id.layout_defatted_body_weight:
                if (ic_defatted_body_weight != null && ic_defatted_body_weight.isShown()) {
                    ic_defatted_body_weight.setVisibility(View.GONE);
                    mDetailsSecondLayout.setVisibility(View.GONE);
                    icPosition = 0;
                } else {
                    goneIc();
                    ic_defatted_body_weight.setVisibility(View.VISIBLE);
                    mDetailsSecondLayout.setVisibility(View.VISIBLE);
                    icPosition = 6;
                    layoutRowPosition = 2;
                    mFatRow2.setText(getResources().getString(R.string.fatList_6));
                }
                break;

            case R.id.layout_bones:
                if (ic_bones != null && ic_bones.isShown()) {
                    ic_bones.setVisibility(View.GONE);
                    mDetailsThirdLayout.setVisibility(View.GONE);
                    icPosition = 0;
                } else {
                    goneIc();
                    ic_bones.setVisibility(View.VISIBLE);
                    mDetailsThirdLayout.setVisibility(View.VISIBLE);
                    icPosition = 7;
                    layoutRowPosition = 3;
                    if (mBodyFatData != null) {
                        stan = MeasureNorm.getBone((int)mBodyFatData.getWeight(),mBodyFatData.getSex());
                        mStateView3.setLanguage(getResources().getString(R.string.text_Low_text), getResources().getString(R.string.text_Standard_text), getResources().getString(R.string.good));
                        mStateView3.setValue(IDatalifeConstant.getWM(mContext), mBodyFatData.getBm(), stan, "");
                    }
                    mFatRow3.setText(getResources().getString(R.string.fatList_7));
                }
                break;

            /*case R.id.layout_bodyage:
                if (ic_bodyage != null && ic_bodyage.isShown()) {
                    ic_bodyage.setVisibility(View.GONE);
                    mDetailsThirdLayout.setVisibility(View.GONE);
                    icPosition = 0;
                } else {
                    goneIc();
                    ic_bodyage.setVisibility(View.VISIBLE);
                    mDetailsThirdLayout.setVisibility(View.VISIBLE);
                    icPosition = 8;
                    layoutRowPosition = 3;
                    mFatRow3.setText(getResources().getString(R.string.fatList_8));
                }
                break;*/

            case R.id.layout_metabolize:
                if (ic_metabolize != null && ic_metabolize.isShown()) {
                    ic_metabolize.setVisibility(View.GONE);
                    mDetailsThirdLayout.setVisibility(View.GONE);
                    icPosition = 0;
                } else {
                    goneIc();
                    ic_metabolize.setVisibility(View.VISIBLE);
                    mDetailsThirdLayout.setVisibility(View.VISIBLE);
                    icPosition = 9;
                    layoutRowPosition = 3;
                    if (mBodyFatData != null) {
                        stan = MeasureNorm.getMetabolize(mBodyFatData.getAge(),mBodyFatData.getSex(),(int)mBodyFatData.getWeight());
                        mStateView3.setLanguage(getResources().getString(R.string.text_Low_text), getResources().getString(R.string.text_Standard_text), getResources().getString(R.string.text_High_text));
                        mStateView3.setValue(IDatalifeConstant.getWM(mContext), mBodyFatData.getBmr(), stan, "");
                    }
                    mFatRow3.setText(getResources().getString(R.string.fatList_9));
                }
                break;

            /*case R.id.layout_fat:
                if (ic_fat != null && ic_fat.isShown()) {
                    ic_fat.setVisibility(View.GONE);
                    mDetailsFourLayout.setVisibility(View.GONE);
                    icPosition = 0;
                } else {
                    goneIc();
                    ic_fat.setVisibility(View.VISIBLE);
                    mDetailsFourLayout.setVisibility(View.VISIBLE);
                    icPosition = 10;
                    layoutRowPosition = 4;
                    mFatRow4.setText(getResources().getString(R.string.fatList_10));
                }
                break;*/

            case R.id.layout_uvi:
                if (ic_uvi != null && ic_uvi.isShown()) {
                    ic_uvi.setVisibility(View.GONE);
                    mDetailsThirdLayout.setVisibility(View.GONE);
                    icPosition = 0;
                } else {
                    goneIc();
                    ic_uvi.setVisibility(View.VISIBLE);
                    mDetailsThirdLayout.setVisibility(View.VISIBLE);
                    icPosition = 8;
                    layoutRowPosition = 3;
                    if (mBodyFatData != null) {
                        stan = MeasureNorm.getViscera();
                        mStateView3.setLanguage(getResources().getString(R.string.text_Standard_text), getResources().getString(R.string.text_High_text), getResources().getString(R.string.text_Very_High_text));
                        mStateView3.setValue(IDatalifeConstant.getWM(mContext), mBodyFatData.getUvi(), stan, "");
                    }
                    mFatRow3.setText(getResources().getString(R.string.fatList_11));
                }
                break;

        }
    }

    /**
     * 隐藏弹出提示
     */
    public void goneIc(){
        if (icPosition == 1){
            iv_weight.setVisibility(View.GONE);
        }else if (icPosition == 2) {
            ic_bmi.setVisibility(View.GONE);
        }else if (icPosition == 3){
            ic_bfr.setVisibility(View.GONE);
        }else if (icPosition == 4){
            ic_muscle.setVisibility(View.GONE);
        }else if (icPosition == 5){
            ic_water.setVisibility(View.GONE);
        }else if (icPosition == 6){
            ic_defatted_body_weight.setVisibility(View.GONE);
        }else if (icPosition == 7){
            ic_bones.setVisibility(View.GONE);
        }else if (icPosition == 8){
            ic_uvi.setVisibility(View.GONE);
        }else if (icPosition == 9){
            ic_metabolize.setVisibility(View.GONE);
        }

        if (layoutRowPosition == 1){
            mDetailsFirstLayout.setVisibility(View.GONE);
        }else if (layoutRowPosition == 2){
            mDetailsSecondLayout.setVisibility(View.GONE);
        }else if (layoutRowPosition == 3){
            mDetailsThirdLayout.setVisibility(View.GONE);
        }else if (layoutRowPosition == 4){
            mDetailsFourLayout.setVisibility(View.GONE);
        }
    }

    public void onGetFatData(BodyFatData bodyFatData){
        if(bodyFatData!= null){
            this.mBodyFatData = bodyFatData;
            weightValue.setText(String.valueOf(bodyFatData.getWeight()));
            waterValue.setText(String.valueOf(bodyFatData.getVwc()));

            if (bodyFatData.getDbw() == 0){
                defattedBodyWeightValue.setText(DensityUtil.doubleToString(bodyFatData.getWeight() *(1-bodyFatData.getBfr()/100)));
            }else {
                defattedBodyWeightValue.setText(String.valueOf((int)bodyFatData.getDbw()));
            }

            metabolizeValue.setText(String.valueOf(bodyFatData.getBmr()));
            bonesValue.setText(String.valueOf(bodyFatData.getBm()));
            muscleValue.setText(DensityUtil.formattedDecimalToPercentage(bodyFatData.getRom()/bodyFatData.getWeight()));
//            fatValue.setText(String.valueOf(bodyFatData.getSfr()));
            uviValue.setText(String.valueOf(bodyFatData.getUvi()));
//            bodyageValue.setText(String.valueOf(bodyFatData.getBodyAge()));
            bmiValue.setText(String.valueOf(bodyFatData.getBmi()));
            bfrValue.setText(String.valueOf(bodyFatData.getBfr()));
//            bfrValue.setText(DensityUtil.formattedDecimalToPercentage(bodyFatData.getBfr()/bodyFatData.getWeight()));

            getStandar();

        }else {
            weightValue.setText("--");
            waterValue.setText("--");
            defattedBodyWeightValue.setText("--");
            metabolizeValue.setText("--");
            bonesValue.setText("--");
            muscleValue.setText("--");
            uviValue.setText("--");
            bmiValue.setText("--");
            bfrValue.setText("--");
        }
    }

    public void onWeight(WeightData weightData){
        weightValue.setText(String.valueOf(weightData.getWeight()));
    }

    private void getStandar(){

        setbg(mWaterStandardTv,MeasureNorm.NormWet(mBodyFatData.getAge(),mBodyFatData.getSex(),mBodyFatData.getVwc()));

        setbg(mBonesStandardTv,MeasureNorm.NormBONE((int)mBodyFatData.getWeight(),mBodyFatData.getSex(),mBodyFatData.getBm()));

        setbg(mBmiStandardTv,MeasureNorm.NormBMI(mBodyFatData.getBmi()));

        setbg(mUviStandardTv,MeasureNorm.NormVISCERA(mBodyFatData.getUvi()));

//        setbg(mBodyAgeStandardTv,MeasureNorm.NormBODYAGE(mBodyFatData.getBodyAge()));

        setbg(mBfrStandardTv,MeasureNorm.NormBfr(mBodyFatData.getSex(),mBodyFatData.getBfr()/100));

        setbg(mWeightStandardTv,MeasureNorm.NormBfr(mBodyFatData.getSex(),mBodyFatData.getBfr()/100));

        setbg(mDefattedBodyWeightStandardTv,MeasureNorm.NormBfr(mBodyFatData.getSex(),mBodyFatData.getBfr()/100));

        setbg(mMuscleStandardTv,MeasureNorm.NormFat(mBodyFatData.getAge(),mBodyFatData.getSex(),mBodyFatData.getRom()));

        setbg(mMetabolizeStandardTv,MeasureNorm.Normmetabolize(mBodyFatData.getAge(),mBodyFatData.getSex(),mBodyFatData.getBmr(),mBodyFatData.getWeight()));
//        setbg(mMetabolizeStandardTv,MeasureNorm.NormKCAL(mBodyFatData.getAge(),mBodyFatData.getSex(),mBodyFatData.getBmr()));
    }

    private void setbg(TextView tv,int result){
        if (result == standar) {
            if (tv == mUviStandardTv){
                tv.setBackgroundResource(R.drawable.ic_low_circle);
                tv.setText(getResources().getString(R.string.text_High_text));
                tv.setTextColor(getResources().getColor(R.color.red_btn));
            }else {
                tv.setBackgroundResource(R.drawable.ic_standar_circle);
                tv.setText(getResources().getString(R.string.text_Standard_text));
                tv.setTextColor(getResources().getColor(R.color.green_btn));
            }
        }else if (result == low){
            if (tv == mUviStandardTv){
                tv.setBackgroundResource(R.drawable.ic_standar_circle);
                tv.setText(getResources().getString(R.string.text_Standard_text));
                tv.setTextColor(getResources().getColor(R.color.green_btn));
            }else if (tv == mBmiStandardTv || tv == mWeightStandardTv || tv==mDefattedBodyWeightStandardTv) {
                tv.setBackgroundResource(R.drawable.ic_low_circle);
                tv.setText(getResources().getString(R.string.text_thin_text));
                tv.setTextColor(getResources().getColor(R.color.red_btn));
            }else{
                tv.setBackgroundResource(R.drawable.ic_low_circle);
                tv.setText(getResources().getString(R.string.text_Low_text));
                tv.setTextColor(getResources().getColor(R.color.red_btn));
            }
        }else if(result == high){
            if (tv == mBonesStandardTv || tv == mMuscleStandardTv){
                tv.setBackgroundResource(R.drawable.ic_standar_circle);
                tv.setText(getResources().getString(R.string.good));
                tv.setTextColor(getResources().getColor(R.color.green_btn));
            }else if (tv == mUviStandardTv){
                tv.setBackgroundResource(R.drawable.ic_low_circle);
                tv.setText(getResources().getString(R.string.text_Very_High_text));
                tv.setTextColor(getResources().getColor(R.color.red_btn));
            }else if (tv == mBmiStandardTv|| tv == mWeightStandardTv || tv==mDefattedBodyWeightStandardTv) {
                tv.setBackgroundResource(R.drawable.ic_low_circle);
                tv.setText(getResources().getString(R.string.text_fat_text));
                tv.setTextColor(getResources().getColor(R.color.red_btn));
            }else {
                tv.setBackgroundResource(R.drawable.ic_low_circle);
                tv.setText(getResources().getString(R.string.text_High_text));
                tv.setTextColor(getResources().getColor(R.color.red_btn));
            }
        }

        tv.setPadding((int)(getResources().getDimension(R.dimen.padding_10)),(int)(getResources().getDimension(R.dimen.padding_3)),(int)(getResources().getDimension(R.dimen.padding_10)),(int)(getResources().getDimension(R.dimen.padding_3)));
    }

    public void setHandler(Handler handler){
        this.myHandler = handler;
    }

}
