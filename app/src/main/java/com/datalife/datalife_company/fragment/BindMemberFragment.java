package com.datalife.datalife_company.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.adapter.HorizontalMemberAdapter;
import com.datalife.datalife_company.adapter.MemberPupwindowAdapter;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseFragment;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.MemberListBean;
import com.datalife.datalife_company.contract.BindMemberContract;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.dao.MachineBean;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.presenter.BindMemberPresenter;
import com.datalife.datalife_company.util.DrawUtil;
import com.datalife.datalife_company.widget.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定成员
 * Created by LG on 2019/7/18.
 */
public class BindMemberFragment extends BaseFragment implements BindMemberContract, AdapterView.OnItemClickListener {

    @BindView(R.id.horizon_listview)
    HorizontalListView horizon_listview;
    @BindView(R.id.ll_bindmember)
    RelativeLayout mBindMemberLayout;
    @BindView(R.id.tv_machinename)
    TextView mMachineName;
    @BindView(R.id.ic_background)
    ImageView imageView;
    @BindView(R.id.btn_bindmember)
    Button bindmember;

    List<FamilyUserInfo> familyUserInfos = null;
    List<FamilyUserInfo> memberList = null;
    private View contentView;
    private PopupWindow popupWindow;
    private ListView mListView;
    private Button button;
    private MemberPupwindowAdapter pupwindowAdapter;
    private List<MachineBindMemberBean> machineBindMemberBeans;
    private MachineBean machineBean;
    private int mMemberId = 0;
    private int position = -1;
    private ArrayList<MemberListBean> memberListBeans = null;
    private HorizontalMemberAdapter horizontalMemberAdapter = null;

    BindMemberPresenter bindMemberPresenter = new BindMemberPresenter();

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x1234:

                    mMemberId = msg.getData().getInt("position");
                    break;
            }
        }
    };

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_bindmember;
    }

    @Override
    protected void initEventAndData() {
        familyUserInfos = DBManager.getInstance(getActivity()).queryFamilyUserInfoList();
        bindMemberPresenter.onCreate(getActivity(),this);

        if (getArguments() != null){
            machineBean = (MachineBean) getArguments().getSerializable("machine");
            if (machineBean != null){
                mMachineName.setText(machineBean.getMachineName());
                bindMemberPresenter.getBindMemberList("1","60",machineBean.getMachineBindId(), ProApplication.SESSIONID);
                if (Integer.valueOf(machineBean.getMachineId()) == 3){
                    bindmember.setVisibility(View.GONE);
                }
            }else{
//                machineBean = DBManager.getInstance(getActivity()).queryMachineBeanList().get(0);
            }
        }

    }

    @OnClick({R.id.ll_bindmember,R.id.btn_bindmember})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.ll_bindmember:

                showPopwindow();
                openPopWindow(view);

                break;

            case R.id.btn_bindmember:

                showPopwindow();
                openPopWindow(view);


                break;

        }

    }

    private void showPopwindow(){
        contentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout,null);

        mListView = (ListView) contentView.findViewById(R.id.lv_bind_member);
        button = (Button) contentView.findViewById(R.id.btn_member);
        memberList = familyUserInfos;
        //绑定过的成员不显示在添加绑定成员列表内
        if (machineBindMemberBeans != null  && machineBindMemberBeans.size() > 0){
            for (int i = 0;i <machineBindMemberBeans.size();i++){
                String index = machineBindMemberBeans.get(i).getMember_Id();
                for (int j = 0;j<memberList.size();j++){
                    if (String.valueOf(memberList.get(j).getMember_Id()).equals(index)){
                        memberList.remove(j);
                    }
                }
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != -1) {
                    if (memberList != null && memberList.size() != 0) {
                        mMemberId = memberList.get(position).getMember_Id();
                        bindMemberPresenter.putBindMember(machineBean.getMachineBindId(), machineBean.getMachineId(), mMemberId + "", ProApplication.SESSIONID);
                    }
                }
                popupWindow.dismiss();
            }
        });
        memberListBeans = new ArrayList<>();
        if (memberList != null && memberList.size() > 0){
            for (int i = 0;i<memberList.size();i++){
                MemberListBean memberListBean = new MemberListBean();
                memberListBean.setFamilyUserInfo(memberList.get(i));
                memberListBean.setSelector(false);
                memberListBeans.add(memberListBean);
            }
        }

        pupwindowAdapter = new MemberPupwindowAdapter(getActivity(),memberListBeans,myHandler);

        mListView.setAdapter(pupwindowAdapter);

        mListView.setOnItemClickListener(this);

        int ints = DrawUtil.getScreenWidthSize(getActivity())/2;

        //设置弹出框的宽度和高度
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ints);
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置可以点击
        popupWindow.setTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                imageView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 按钮的监听
     * @param v
     */
    public void openPopWindow(View v) {
        imageView.setVisibility(View.VISIBLE);
        //从底部显示
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        if (memberListBeans != null){
            memberListBeans.get(position).setSelector(!memberListBeans.get(position).isSelector());
            for (int i = 0; i < memberListBeans.size(); i++){
                if (i != position){
                    memberListBeans.get(i).setSelector(false);
                }

            }
            pupwindowAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getBindMemberSuccess(List<MachineBindMemberBean> machineBindMemberBean) {

        machineBindMemberBeans = (ArrayList<MachineBindMemberBean>)DBManager.getInstance(getActivity()).queryMachineBindMemberBeanList(machineBean.getMachineBindId());
        boolean isHave = false;
        for (MachineBindMemberBean bindMemberBean : machineBindMemberBean){
            isHave = false;
            for (int i = 0;i<machineBindMemberBeans.size();i++){
                if (machineBindMemberBeans.get(i).getMember_Id().equals(bindMemberBean.getMember_Id())){
                    isHave = true;
                }
            }
            if (!isHave){
                DBManager.getInstance(getActivity()).insertMachineBindMember(bindMemberBean);
            }
        }
        machineBindMemberBeans = machineBindMemberBean;

        if (horizontalMemberAdapter == null) {
            horizontalMemberAdapter = new HorizontalMemberAdapter(getActivity(), machineBindMemberBeans);
            horizon_listview.setAdapter(horizontalMemberAdapter);
        }else{
            horizontalMemberAdapter.setList(machineBindMemberBeans);
            horizontalMemberAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getBindMemberFail(String msg) {
        toast(msg+"");
    }

    @Override
    public void putBindSuccess() {
        bindMemberPresenter.getBindMemberList("1","16",machineBean.getMachineBindId(), ProApplication.SESSIONID);
    }

    @Override
    public void putBindFail(String msg) {

    }
}
