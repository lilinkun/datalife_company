package com.datalife.datalife_company.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.util.DensityUtil;
import com.datalife.datalife_company.util.MeasureNorm;

import java.math.BigDecimal;

import aicare.net.cn.iweightlibrary.entity.BodyFatData;

/**
 * 体脂称历史记录中的自定义控件
 * Created by LG on 2019/7/25.
 */
public class FatRecordLayout extends LinearLayout {
    private Context mContext;
    private View v;
    private TextView weightValue;
    private TextView defattedBodyWeightValue;
    private TextView bonesValue;
    private TextView metabolizeValue;
    private TextView muscleValue;
    private TextView waterValue;
    private TextView uviValue;
    private TextView bmiValue;
    private TextView bfrValue;

    private TextView mWeightStandardTv;
    private TextView mBmiStandardTv;
    private TextView mBfrStandardTv;
    private TextView mMuscleStandardTv;
    private TextView mWaterStandardTv;
    private TextView mDefattedBodyWeightStandardTv;
    private TextView mBonesStandardTv;
    private TextView mMetabolizeStandardTv;
    private TextView mUviStandardTv;

    private ImageView mArrowWeightIv;
    private ImageView mArrowBmiIv;
    private ImageView mArrowBfrIv;
    private ImageView mArrowMuscleIv;
    private ImageView mArrowWaterIv;
    private ImageView mArrowDefattedBodyWeightIv;
    private ImageView mArrowBonesIv;
    private ImageView mArrowMetabolizeIv;
    private ImageView mArrowUviIv;

    private TextView mContrastWeightTv;
    private TextView mContrastBmiTv;
    private TextView mContrastMuscleTv;
    private TextView mContrastBfrTv;
    private TextView mContrastWaterTv;
    private TextView mContrastDefattedBodyWeightTv;
    private TextView mContrastBonesTv;
    private TextView mContrastMetabolizeTv;
    private TextView mContrastUviTv;

    private BodyFatData mBodyFatData;

    private int standar = 1;
    private int low = 0;
    private int high = 2;

    public FatRecordLayout(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public FatRecordLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public FatRecordLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init(){
        v = LayoutInflater.from(mContext).inflate(R.layout.fat_record_layout, null);

        weightValue = (TextView) v.findViewById(R.id.tv_weight_value);
        defattedBodyWeightValue = (TextView) v.findViewById(R.id.tv_defatted_body_weight_value);
        bonesValue = (TextView) v.findViewById(R.id.tv_bones_value);
        metabolizeValue = (TextView) v.findViewById(R.id.tv_metabolize_value);
        muscleValue = (TextView) v.findViewById(R.id.tv_muscle_value);
        waterValue = (TextView) v.findViewById(R.id.tv_water_value);
        uviValue = (TextView) v.findViewById(R.id.tv_uvi_value);
        bmiValue = (TextView) v.findViewById(R.id.tv_bmi_value);
        bfrValue = (TextView) v.findViewById(R.id.tv_bfr_value);

        mWeightStandardTv = (TextView) v.findViewById(R.id.tv_weight_standard);
        mBmiStandardTv = (TextView) v.findViewById(R.id.tv_bmi_standard);
        mBfrStandardTv = (TextView) v.findViewById(R.id.tv_bfr_standard);
        mMuscleStandardTv = (TextView) v.findViewById(R.id.tv_muscle_standard);
        mWaterStandardTv = (TextView) v.findViewById(R.id.tv_water_standard);
        mDefattedBodyWeightStandardTv = (TextView) v.findViewById(R.id.tv_defatted_body_weight_standard);
        mBonesStandardTv = (TextView) v.findViewById(R.id.tv_bones_standard);
        mMetabolizeStandardTv = (TextView) v.findViewById(R.id.tv_metabolize_standard);
        mUviStandardTv = (TextView) v.findViewById(R.id.tv_uvi_standard);

        mContrastWeightTv = (TextView) v.findViewById(R.id.tv_contrast_weight);
        mContrastBmiTv = (TextView) v.findViewById(R.id.tv_contrast_bmi);
        mContrastBfrTv = (TextView) v.findViewById(R.id.tv_contrast_bfr);
        mContrastWaterTv = (TextView) v.findViewById(R.id.tv_contrast_water);
        mContrastMuscleTv = (TextView) v.findViewById(R.id.tv_contrast_muscle);
        mContrastBonesTv = (TextView) v.findViewById(R.id.tv_contrast_bones);
        mContrastUviTv = (TextView) v.findViewById(R.id.tv_contrast_uvi);
        mContrastDefattedBodyWeightTv = (TextView) v.findViewById(R.id.tv_contrast_defatted_body_weight);
        mContrastMetabolizeTv = (TextView) v.findViewById(R.id.tv_contrast_metabolize);

        mArrowWeightIv = (ImageView) v.findViewById(R.id.iv_arrow_weight);
        mArrowBmiIv = (ImageView) v.findViewById(R.id.iv_arrow_bmi);
        mArrowWaterIv = (ImageView) v.findViewById(R.id.iv_arrow_water);
        mArrowBfrIv = (ImageView) v.findViewById(R.id.iv_arrow_bfr);
        mArrowBonesIv = (ImageView) v.findViewById(R.id.iv_arrow_bones);
        mArrowMetabolizeIv = (ImageView) v.findViewById(R.id.iv_arrow_metabolize);
        mArrowMuscleIv = (ImageView) v.findViewById(R.id.iv_arrow_muscle);
        mArrowUviIv = (ImageView) v.findViewById(R.id.iv_arrow_uvi);
        mArrowDefattedBodyWeightIv = (ImageView) v.findViewById(R.id.iv_arrow_defatted_body_weight);

        this.addView(v, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }

    public void setData(BodyFatData bodyFatData,BodyFatData bodyFatData1){
        this.mBodyFatData = bodyFatData;
        weightValue.setText(bodyFatData.getWeight()+"");
        waterValue.setText(String.valueOf(bodyFatData.getVwc()) + "");
        if (bodyFatData.getDbw() == 0){
            defattedBodyWeightValue.setText(DensityUtil.doubleToString(bodyFatData.getWeight() *(1-bodyFatData.getBfr()/100))+"");
        }else {
            defattedBodyWeightValue.setText(String.valueOf((int)bodyFatData.getDbw())+"");
        }

        metabolizeValue.setText(String.valueOf(bodyFatData.getBmr()) + "");
        bonesValue.setText(String.valueOf(bodyFatData.getBm()) + "");
        muscleValue.setText(DensityUtil.formattedDecimalToPercentage(bodyFatData.getRom()/bodyFatData.getWeight()));
        uviValue.setText(String.valueOf(bodyFatData.getUvi()));
        bmiValue.setText(String.valueOf(bodyFatData.getBmi()));
        bfrValue.setText(String.valueOf(bodyFatData.getBfr())+"");

        getCompare(bodyFatData,bodyFatData1);

        getStandar();
    }

    private void getCompare(BodyFatData mBodyFatData,BodyFatData bodyFatData1){
        if (bodyFatData1 != null) {
            if (bodyFatData1.getWeight() > mBodyFatData.getWeight()) {
                mArrowWeightIv.setImageResource(R.mipmap.ic_fat_down);
            }else if (bodyFatData1.getWeight() < mBodyFatData.getWeight()){
                mArrowWeightIv.setImageResource(R.mipmap.ic_fat_up);
            }
            mContrastWeightTv.setText(getDoubleValue(mBodyFatData.getWeight(),bodyFatData1.getWeight()) + "");

            if (bodyFatData1.getBmi() > mBodyFatData.getBmi()) {
                mArrowBmiIv.setImageResource(R.mipmap.ic_fat_down);
            }else if (bodyFatData1.getBmi() < mBodyFatData.getBmi()){
                mArrowBmiIv.setImageResource(R.mipmap.ic_fat_up);
            }
            mContrastBmiTv.setText(getDoubleValue(mBodyFatData.getBmi(),bodyFatData1.getBmi()) + "");

            if (bodyFatData1.getBfr() > mBodyFatData.getBfr()) {
                mArrowBfrIv.setImageResource(R.mipmap.ic_fat_down);
            }else if (bodyFatData1.getBfr() < mBodyFatData.getBfr()){
                mArrowBfrIv.setImageResource(R.mipmap.ic_fat_up);
            }
            mContrastBfrTv.setText(getDoubleValue(mBodyFatData.getBfr(),bodyFatData1.getBfr()) + "");


            if (bodyFatData1.getVwc() > mBodyFatData.getVwc()) {
                mArrowWaterIv.setImageResource(R.mipmap.ic_fat_down);
            }else if (bodyFatData1.getVwc() < mBodyFatData.getVwc()){
                mArrowWaterIv.setImageResource(R.mipmap.ic_fat_up);
            }
            mContrastWaterTv.setText(getDoubleValue(mBodyFatData.getVwc(),bodyFatData1.getVwc()) + "");

            if (bodyFatData1.getRom() > mBodyFatData.getRom()) {
                mArrowMuscleIv.setImageResource(R.mipmap.ic_fat_down);
            }else if (bodyFatData1.getRom() < mBodyFatData.getRom()){
                mArrowMuscleIv.setImageResource(R.mipmap.ic_fat_up);
            }
            mContrastMuscleTv.setText(getDoubleValue(mBodyFatData.getRom(),bodyFatData1.getRom()) + "");


            if (bodyFatData1.getBmr() > mBodyFatData.getBmr()) {
                mArrowMetabolizeIv.setImageResource(R.mipmap.ic_fat_down);
            }else if (bodyFatData1.getBmr() < mBodyFatData.getBmr()){
                mArrowMetabolizeIv.setImageResource(R.mipmap.ic_fat_up);
            }
            mContrastMetabolizeTv.setText(getDoubleValue(mBodyFatData.getBmr(),bodyFatData1.getBmr()) + "");


            if (bodyFatData1.getDbw() > mBodyFatData.getDbw()) {
                mArrowDefattedBodyWeightIv.setImageResource(R.mipmap.ic_fat_down);
            }else if (bodyFatData1.getDbw() < mBodyFatData.getDbw()){
                mArrowDefattedBodyWeightIv.setImageResource(R.mipmap.ic_fat_up);
            }
            mContrastDefattedBodyWeightTv.setText(getDoubleValue(mBodyFatData.getDbw(),bodyFatData1.getDbw()) + "");


            if (bodyFatData1.getUvi() > mBodyFatData.getUvi()) {
                mArrowUviIv.setImageResource(R.mipmap.ic_fat_down);
            }else if (bodyFatData1.getUvi() < mBodyFatData.getUvi()){
                mArrowUviIv.setImageResource(R.mipmap.ic_fat_up);
            }
            mContrastUviTv.setText(getDoubleValue(mBodyFatData.getUvi(),bodyFatData1.getUvi()) + "");


            if (bodyFatData1.getBm() > mBodyFatData.getBm()) {
                mArrowBonesIv.setImageResource(R.mipmap.ic_fat_down);
            }else if (bodyFatData1.getBm() < mBodyFatData.getBm()){
                mArrowBonesIv.setImageResource(R.mipmap.ic_fat_up);
            }
            mContrastBonesTv.setText(getDoubleValue(mBodyFatData.getBm(),bodyFatData1.getBm()) + "");
        }else {
            mArrowWeightIv.setVisibility(INVISIBLE);
            mArrowBmiIv.setVisibility(INVISIBLE);
            mArrowBfrIv.setVisibility(INVISIBLE);
            mArrowWaterIv.setVisibility(INVISIBLE);
            mArrowMuscleIv.setVisibility(INVISIBLE);
            mArrowMetabolizeIv.setVisibility(INVISIBLE);
            mArrowDefattedBodyWeightIv.setVisibility(INVISIBLE);
            mArrowUviIv.setVisibility(INVISIBLE);
            mArrowBonesIv.setVisibility(INVISIBLE);

            mContrastBonesTv.setText("--");
            mContrastBmiTv.setText("--");
            mContrastBfrTv.setText("--");
            mContrastWaterTv.setText("--");
            mContrastMuscleTv.setText("--");
            mContrastMetabolizeTv.setText("--");
            mContrastDefattedBodyWeightTv.setText("--");
            mContrastUviTv.setText("--");
            mContrastBonesTv.setText("--");
        }
    }

    private double getDoubleValue(double v1,double v2){
        BigDecimal b = new BigDecimal(v1 - v2);
        double d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }

    private void getStandar(){

        setbg(mWaterStandardTv, MeasureNorm.NormWet(mBodyFatData.getAge(),mBodyFatData.getSex(),mBodyFatData.getVwc()),waterValue);

        setbg(mBonesStandardTv,MeasureNorm.NormBONE((int)mBodyFatData.getWeight(),mBodyFatData.getSex(),mBodyFatData.getBm()),bonesValue);

        setbg(mBmiStandardTv,MeasureNorm.NormBMI(mBodyFatData.getBmi()),bmiValue);

        setbg(mUviStandardTv,MeasureNorm.NormVISCERA(mBodyFatData.getUvi()),uviValue);

        setbg(mBfrStandardTv,MeasureNorm.NormBfr(mBodyFatData.getSex(),mBodyFatData.getBfr()/100),bfrValue);

        setbg(mWeightStandardTv, MeasureNorm.NormBfr(mBodyFatData.getSex(),mBodyFatData.getBfr()/100),weightValue);

        setbg(mDefattedBodyWeightStandardTv,MeasureNorm.NormBfr(mBodyFatData.getSex(),mBodyFatData.getBfr()/100),defattedBodyWeightValue);

        setbg(mMuscleStandardTv,MeasureNorm.NormFat(mBodyFatData.getAge(),mBodyFatData.getSex(),mBodyFatData.getRom()),muscleValue);

        setbg(mMetabolizeStandardTv,MeasureNorm.Normmetabolize(mBodyFatData.getAge(),mBodyFatData.getSex(),mBodyFatData.getBmr(),mBodyFatData.getWeight()),metabolizeValue);
    }

    private void setbg(TextView tv, int result, TextView valueTv){
        if (result == standar) {
            if (tv == mUviStandardTv){
                tv.setBackgroundResource(R.drawable.bg_fat_record_yellow);
                tv.setText(getResources().getString(R.string.text_High_text));
            }else {
                tv.setBackgroundResource(R.drawable.bg_fat_record_green);
                tv.setText(getResources().getString(R.string.text_Standard_text));
            }
            valueTv.setTextColor(getResources().getColor(R.color.fat_standard));
        }else if (result == low){
            if (tv == mUviStandardTv){
                tv.setBackgroundResource(R.drawable.bg_fat_record_green);
                tv.setText(getResources().getString(R.string.text_Standard_text));
                valueTv.setTextColor(getResources().getColor(R.color.fat_standard));
            }else if (tv == mBmiStandardTv || tv == mWeightStandardTv || tv == mDefattedBodyWeightStandardTv) {
                tv.setBackgroundResource(R.drawable.bg_fat_record_orange);
                tv.setText(getResources().getString(R.string.text_thin_text));
                valueTv.setTextColor(getResources().getColor(R.color.fat_small));
            }else{
                tv.setBackgroundResource(R.drawable.bg_fat_record_orange);
                tv.setText(getResources().getString(R.string.text_Low_text));
                valueTv.setTextColor(getResources().getColor(R.color.fat_small));
            }
        }else if(result == high){
            if (tv == mBonesStandardTv || tv == mMuscleStandardTv){
                tv.setBackgroundResource(R.drawable.bg_fat_record_green);
                tv.setText(getResources().getString(R.string.good));
                valueTv.setTextColor(getResources().getColor(R.color.fat_standard));
            }else if (tv == mUviStandardTv){
                tv.setBackgroundResource(R.drawable.bg_fat_record_yellow);
                tv.setText(getResources().getString(R.string.text_Very_High_text));
                valueTv.setTextColor(getResources().getColor(R.color.fat_big));
            }else if (tv == mBmiStandardTv|| tv == mWeightStandardTv || tv == mDefattedBodyWeightStandardTv) {
                tv.setBackgroundResource(R.drawable.bg_fat_record_yellow);
                tv.setText(getResources().getString(R.string.text_fat_text));
                valueTv.setTextColor(getResources().getColor(R.color.fat_big));
            }else {
                tv.setBackgroundResource(R.drawable.bg_fat_record_yellow);
                tv.setText(getResources().getString(R.string.text_High_text));
                valueTv.setTextColor(getResources().getColor(R.color.fat_big));
            }
        }

        tv.setPadding((int)(getResources().getDimension(R.dimen.padding_10)),(int)(getResources().getDimension(R.dimen.padding_3)),(int)(getResources().getDimension(R.dimen.padding_10)),(int)(getResources().getDimension(R.dimen.padding_3)));
    }

}
