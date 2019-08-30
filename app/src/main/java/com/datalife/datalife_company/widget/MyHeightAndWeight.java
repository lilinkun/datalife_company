package com.datalife.datalife_company.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;

import java.util.ArrayList;

/**
 * 身高体重
 * Created by LG on 2019/7/17.
 */
public class MyHeightAndWeight extends LinearLayout implements View.OnTouchListener {

    private Context context;
    private MyDateLinear.MyDateLinearListener listener;

    private RelativeLayout relative;
    private LinearLayout linear, bottom_linear;
    private TextView title, year_text, month_text, cancle, sure,unit;
    private ListView year_listview, month_listview;
    private View view;

    private int relative_length;
    private int item_length;

    private final int YEAR = 0;
    private final int MONTH = 1;

    private ArrayList<String> arrayListYear, arrayListMonth;
    private DateAdapter adapterYear, adapterMonth;

    private int select_year, select_month;
    private String height_one, height_two;
    private String title_one, title_two;

    private int left_id, right_id;

    public MyHeightAndWeight(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public MyHeightAndWeight(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.my_height_weight, null);
        this.addView(view);
        year_listview = (ListView) view.findViewById(R.id.my_height_weight_year_listview);
        month_listview = (ListView) view.findViewById(R.id.my_height_weight_month_listview);

        year_text = (TextView) view.findViewById(R.id.my_height_weight_year_text);
        month_text = (TextView) view.findViewById(R.id.my_height_weight_month_text);
        cancle = (TextView) view.findViewById(R.id.my_height_weight_cancle);
        sure = (TextView) view.findViewById(R.id.my_height_weight_sure);
        relative = (RelativeLayout) view.findViewById(R.id.my_height_weight_relative);
        linear = (LinearLayout) view.findViewById(R.id.my_height_weight_linear);
        bottom_linear = (LinearLayout) view.findViewById(R.id.my_height_weight_bottom_linear);
        title = (TextView) view.findViewById(R.id.my_height_weight_title);
        unit = (TextView) view.findViewById(R.id.my_height_weight_unit);
        cancle.setOnTouchListener(this);
        sure.setOnTouchListener(this);
    }

    public void setList() {
        month_listview.setVisibility(View.GONE);
    }

    public void init() {
        count(listener.getscreenHeight());
        setListViewWidth(year_listview, listener.getscreenWidth() * 1);
//        setListViewWidth(month_listview, listener.getscreenWidth() * 1 / 2);

        LinearLayout.LayoutParams params_relative = (LinearLayout.LayoutParams) relative.getLayoutParams();
        params_relative.height = relative_length;
        relative.setLayoutParams(params_relative);

        RelativeLayout.LayoutParams params_linear = (RelativeLayout.LayoutParams) linear.getLayoutParams();
        params_linear.height = item_length;
        linear.setLayoutParams(params_linear);

//        LayoutParams params_bottom_linear = (LayoutParams) bottom_linear.getLayoutParams();
//        params_bottom_linear.height = item_length + 30;
//        bottom_linear.setLayoutParams(params_bottom_linear);

        RelativeLayout.LayoutParams params_tv = (RelativeLayout.LayoutParams) title.getLayoutParams();
        params_tv.height = item_length + 20;
        title.setLayoutParams(params_tv);

        adapterYear = new DateAdapter(context, arrayListYear, YEAR);
        adapterMonth = new DateAdapter(context, arrayListMonth, MONTH);

        initListView(year_listview, YEAR);
        initListView(month_listview, MONTH);

        year_listview.setAdapter(adapterYear);
        month_listview.setAdapter(adapterMonth);

//		year_listview.setSelection(arrayListYear.size()/2-3);
//		month_listview.setSelection(arrayListMonth.size()/2-3);
        int year_id;
        int month_id;
        if (left_id > 3) {
            year_listview.setSelection(left_id - 3);
            year_id = left_id - 3 + 2;
        } else {
            year_listview.setSelection(arrayListYear.size() + left_id - 3);
            year_id = (arrayListYear.size() + left_id - 3 + 2) % arrayListYear.size();
        }
        if (right_id > 3) {
            month_listview.setSelection(right_id - 3);
            month_id = right_id - 3 + 2;
        } else {
            month_listview.setSelection(arrayListMonth.size() + right_id - 3);
            month_id = (arrayListMonth.size() + right_id - 3 + 2) % arrayListMonth.size();
        }
        height_one = arrayListYear.get(year_id);
        height_two = arrayListMonth.get(month_id).substring(1);
//        title.setText(title_one + ":" + height_one + "." + height_two + title_two);
//        title.setText(title_one + ":" + height_one + title_two);

        unit.setText(title_two +"");

        if (title_one.equals(getResources().getString(R.string.user_height))){
            title.setText(getResources().getString(R.string.choose_height));
        }else if (title_one.equals(getResources().getString(R.string.my_weight))){
            title.setText(getResources().getString(R.string.choose_weight));
        }


    }

    public void setSelectLeftId(int left_id)//设置　初始选中第几个  从第一个算起　
    {
        this.left_id = left_id;
    }

    public void setSelectRightId(int right_id)//设置　初始选中第几个  从第一个算起　
    {
        this.right_id = right_id;
    }

    public void setArrayListYear(ArrayList<String> arrayList) {
        this.arrayListYear = arrayList;
    }

    public void setArrayListMonth(ArrayList<String> arrayList) {
        this.arrayListMonth = arrayList;
    }

    public void initListView(ListView listview, final int state) {
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int stateScroll) {
                if (stateScroll == SCROLL_STATE_IDLE) {
                    switch (state) {
                        case YEAR:
                            year_listview.setSelection(select_year);
                            break;
                        case MONTH:
                            month_listview.setSelection(select_month);
                            break;
                    }
//                    title.setText(title_one + ":" + height_one + "." + height_two + title_two);
//                    title.setText(title_one + ":" + height_one + title_two);
                    listener.realTime("");
                }
            }

            @Override
            public void onScroll(AbsListView arg0, int first, int visible, int totle) {
                switch (state) {
                    case YEAR:
                        int id_year = (first + 2) % arrayListYear.size();
                        String years = arrayListYear.get(id_year);
                        year_text.setText(years);
                        height_one = arrayListYear.get(id_year);
                        if (first == 0) {
                            year_listview.setSelection(arrayListYear.size());
                        }
                        select_year = first;
                        break;
                    case MONTH:
                        int id_month = (first + 2) % arrayListMonth.size();
                        month_text.setText(arrayListMonth.get(id_month));
                        height_two = arrayListMonth.get(id_month).substring(1) + "";
                        if (first == 0) {
                            month_listview.setSelection(arrayListMonth.size());
                        }
                        select_month = first;
                        break;
                }
            }
        });
    }

    public void count(int screenHeight) {
        relative_length = screenHeight * 2 / 7;
        item_length = relative_length / 5;
    }

    @SuppressLint("NewApi")
    public void setListViewWidth(ListView listview, int width) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) listview.getLayoutParams();
        params.width = width;
        listview.setLayoutParams(params);
    }

    class DateAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> arrayList;
        private int state;

        public DateAdapter(Context context, ArrayList<String> arrayList, int state) {
            this.context = context;
            this.arrayList = arrayList;
            this.state = state;
        }

        public void setArrayList(ArrayList<String> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int arg0) {
            return arrayList.get(arg0 % arrayList.size());
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            DateHolder holder = null;
            int id = position % arrayList.size();
            if (convertView == null) {
                holder = new DateHolder();
                if (state == YEAR) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.my_height_weight_item_1, null);
                    holder.text = (TextView) convertView.findViewById(R.id.my_height_weight_item_1_text);
                } else if (state == MONTH) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.my_height_weight_item_2, null);
                    holder.text = (TextView) convertView.findViewById(R.id.my_height_weight_item_2_text);
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.text.getLayoutParams();
                params.height = item_length;
                holder.text.setLayoutParams(params);
                convertView.setTag(holder);
            } else {
                holder = (DateHolder) convertView.getTag();
            }
            holder.text.setText(arrayList.get(id));
            return convertView;
        }

    }

    class DateHolder {
        public TextView text;
    }

    public void setMyDateLinearListener(MyDateLinear.MyDateLinearListener myDateLinearListener) {
        this.listener = myDateLinearListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.my_height_weight_cancle:
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    cancle.setTextColor(Color.parseColor("#0000CA"));
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    cancle.setTextColor(Color.parseColor("#31BEF3"));
                listener.cancle();
//                }
                break;
            case R.id.my_height_weight_sure:
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    sure.setTextColor(Color.parseColor("#0000CA"));
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    sure.setTextColor(Color.parseColor("#31BEF3"));
                listener.sure(title.getText().toString());
//                }
                break;
        }
        return true;
    }

    public String getData() {
        String str = height_one + "." + height_two;
        return str;
    }

    public void setTitle_one(String title_one) {
        this.title_one = title_one;
    }

    public void setTitle_two(String title_two) {
        this.title_two = title_two;
    }
}


