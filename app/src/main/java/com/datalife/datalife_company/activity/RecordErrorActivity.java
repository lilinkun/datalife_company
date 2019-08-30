package com.datalife.datalife_company.activity;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.adapter.MeasureItemAdapter;
import com.datalife.datalife_company.adapter.MeasureListAdapter;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseActivity;
import com.datalife.datalife_company.bean.MeasureErrorListBean;
import com.datalife.datalife_company.bean.MeasureRecordListBean;
import com.datalife.datalife_company.bean.TypeBean;
import com.datalife.datalife_company.contract.RecordErrorContract;
import com.datalife.datalife_company.mvp.IView;
import com.datalife.datalife_company.presenter.RecordErrorPresenter;
import com.datalife.datalife_company.util.MeasureEnum;
import com.datalife.datalife_company.util.MeasureStatus;
import com.datalife.datalife_company.widget.Eyes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2019/8/1.
 */

public class RecordErrorActivity extends BaseActivity implements RecordErrorContract {

    @BindView(R.id.rv_record_error)
    RecyclerView recyclerView;
    @BindView(R.id.et_record_search)
    EditText et_record_search;
    @BindView(R.id.ll_measure_tip)
    LinearLayout ll_measure_tip;
    @BindView(R.id.iv_measure_tip)
    ImageView iv_measure_tip;
    @BindView(R.id.tv_measure_tip)
    TextView tv_measure_tip;
    @BindView(R.id.ll_measure_status)
    LinearLayout ll_measure_status;
    @BindView(R.id.iv_measure_status)
    ImageView iv_measure_status;
    @BindView(R.id.tv_measure_status)
    TextView tv_measure_status;

    private MeasureListAdapter measureListAdapter = null;
    private ArrayList<MeasureRecordListBean> measureRecordListBeans = new ArrayList<>();
    PopupWindow popupWindow = null;
    private ArrayList<TypeBean> typeBeans = new ArrayList<>();
    private TypeBean typeBeanStatus;
    private int status;

    public static int MEASURETYPE = 0;
    public static int MEASURESTATUS = 1;
    RecordErrorPresenter recordErrorPresenter = new RecordErrorPresenter();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_error;
    }

    @Override
    protected void initEventAndData() {

        Eyes.setStatusBarWhiteColor(this, getResources().getColor(R.color.white));


        TypeBean typeBean = new TypeBean();
        typeBean.setName("全部类别");
        typeBean.setValue(0);

        typeBeanStatus = typeBean;
        typeBeans.add(typeBean);


        recordErrorPresenter.onCreate(this,this);
        recordErrorPresenter.getMeasureInfo(ProApplication.SESSIONID);

        recordErrorPresenter.getMeasureList(null,"20","1",null,null,"1",ProApplication.SESSIONID);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);


    }

    @OnClick({R.id.ll_measure_tip,R.id.ll_measure_status})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_measure_status:

                setClickTip(tv_measure_status.getText().toString(),MEASURESTATUS,null);

                iv_measure_status.setImageResource(R.mipmap.ic_click_tip);
                tv_measure_status.setTextColor(getResources().getColor(R.color.ecg_bg));

                break;

            case R.id.ll_measure_tip:

                setClickTip(tv_measure_tip.getText().toString(),MEASURETYPE,typeBeans);

                iv_measure_tip.setImageResource(R.mipmap.ic_click_tip);
                tv_measure_tip.setTextColor(getResources().getColor(R.color.ecg_bg));


                break;
        }
    }

    private void setClickTip(String name, final int type, final ArrayList<TypeBean> typeBeans){

        View contentView = LayoutInflater.from(this).inflate(R.layout.member_popup_listview, null);

        ListView listView1 = (ListView) contentView.findViewById(R.id.lv_member);
        listView1.setVisibility(View.VISIBLE);
        listView1.setAdapter(new MeasureItemAdapter(this,name,type,typeBeans));

        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(contentView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(ll_measure_tip);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (type == MEASURETYPE) {
                    iv_measure_tip.setImageResource(R.mipmap.ic_unclick_tip);
                    tv_measure_tip.setTextColor(getResources().getColor(R.color.black_text_bg));
                }else if (type == MEASURESTATUS){
                    iv_measure_status.setImageResource(R.mipmap.ic_unclick_tip);
                    tv_measure_status.setTextColor(getResources().getColor(R.color.black_text_bg));
                }
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (type == MEASURETYPE) {
                    tv_measure_tip.setText(typeBeans.get(position).getName());
                    typeBeanStatus = typeBeans.get(position);
                }else if (type == MEASURESTATUS){
                    tv_measure_status.setText(MeasureStatus.values()[position].getMeasureName());
                    status = MeasureStatus.values()[position].getMeasureCode();
                }

                recordErrorPresenter.getMeasureList(typeBeanStatus.getValue()+"","20","1",null,null,status+"",ProApplication.SESSIONID);

                popupWindow.dismiss();

            }
        });
    }

    @Override
    public void getDataSuccess(ArrayList<TypeBean> typeBeans) {
        this.typeBeans.addAll(typeBeans);
    }

    @Override
    public void getDataFail(String msg) {
    }

    @Override
    public void getDataListSuccess(ArrayList<MeasureErrorListBean> measureErrorListBeans) {

        if (measureListAdapter == null) {
            measureListAdapter = new MeasureListAdapter(this, measureErrorListBeans);

            recyclerView.setAdapter(measureListAdapter);
        }else {
            measureListAdapter.setData(measureErrorListBeans);
        }
    }

    @Override
    public void getDataListFail(String msg) {

    }
}

