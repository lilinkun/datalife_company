package com.datalife.datalife_company.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
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
import com.datalife.datalife_company.util.IDatalifeConstant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LG on 2019/7/17.
 */

public class MyDateLinear  extends LinearLayout implements View.OnTouchListener {

    private Context context;
    private MyDateLinearListener listener;

    private RelativeLayout relative;
    private LinearLayout linear;
    public TextView title, year_text, month_text, day_text, cancle, sure;
    public ListView year_listview, month_listview, day_listview;
    private View view;

    private int relative_length;
    private int item_length;
    private long time = 24192000;
    ;
    private final int YEAR = 0;
    private final int MONTH = 1;
    private final int DAY = 2;

    private ArrayList<String> arrayListYear, arrayListMonth, arrayListDay;
    private DateAdapter adapterYear, adapterMonth, adapterDay;

    private int select_year, select_month, select_day;
    private int select_my_year, select_my_month, select_my_day;

    private int maxYear = 2100;// 默认
    private int minYear = 1920;// 默认

    private int maxDay;
    //0代表天，1代表周，2代表月，3代表年
    private int isChoose = 0;

    public MyDateLinear(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public MyDateLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public void init(Context context) {
        Calendar c = Calendar.getInstance();
        maxYear = (c.get(Calendar.YEAR));
        view = LayoutInflater.from(context).inflate(R.layout.mydate, null);
        this.addView(view);
        year_listview = (ListView) view.findViewById(R.id.mydate_year_listview);
        month_listview = (ListView) view
                .findViewById(R.id.mydate_month_listview);
        day_listview = (ListView) view.findViewById(R.id.mydate_day_listview);

        year_text = (TextView) view.findViewById(R.id.mydate_year_text);
        month_text = (TextView) view.findViewById(R.id.mydate_month_text);
        day_text = (TextView) view.findViewById(R.id.mydate_day_text);
        cancle = (TextView) view.findViewById(R.id.mydate_cancle);
        sure = (TextView) view.findViewById(R.id.mydate_sure);
        relative = (RelativeLayout) view.findViewById(R.id.mydate_relative);
        linear = (LinearLayout) view.findViewById(R.id.mydate_linear);
//        bottom_linear = (LinearLayout) view
//                .findViewById(R.id.mydate_bottom_linear);
        title = (TextView) view.findViewById(R.id.mydate_title);
        cancle.setOnTouchListener(this);
        sure.setOnTouchListener(this);
    }

    public void init() {
        count(listener.getscreenHeight());
        setListViewWidth(year_listview, listener.getscreenWidth() * 1 / 3);
        setListViewWidth(month_listview, listener.getscreenWidth() * 1 / 3);
        setListViewWidth(day_listview, listener.getscreenWidth() * 1 / 3);

        LinearLayout.LayoutParams params_relative = (LayoutParams) relative
                .getLayoutParams();
        params_relative.height = relative_length;
        relative.setLayoutParams(params_relative);

        RelativeLayout.LayoutParams params_linear = (RelativeLayout.LayoutParams) linear
                .getLayoutParams();
        params_linear.height = item_length;
        linear.setLayoutParams(params_linear);

//        LayoutParams params_bottom_linear = (LayoutParams) bottom_linear
//                .getLayoutParams();
//        params_bottom_linear.height = item_length + 30;
//        bottom_linear.setLayoutParams(params_bottom_linear);

        RelativeLayout.LayoutParams params_tv = (RelativeLayout.LayoutParams) title
                .getLayoutParams();
        params_tv.height = item_length + 20;
        title.setLayoutParams(params_tv);

        arrayListYear = new ArrayList<String>();
        arrayListMonth = new ArrayList<String>();
        arrayListDay = new ArrayList<String>();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        initYearArrayList(arrayListYear);
        initMonthArrayList(arrayListMonth);
        initDayArrayList(arrayListDay, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1);

        adapterYear = new DateAdapter(context, arrayListYear, YEAR);
        adapterMonth = new DateAdapter(context, arrayListMonth, MONTH);
        adapterDay = new DateAdapter(context, arrayListDay, DAY);

        initListView(year_listview, YEAR);
        initListView(month_listview, MONTH);
        initListView(day_listview, DAY);

        year_listview.setAdapter(adapterYear);
        month_listview.setAdapter(adapterMonth);
        day_listview.setAdapter(adapterDay);

    }

    public void initYearArrayList(ArrayList<String> arrayList) {
        for (int i = minYear; i <= maxYear; i++) {
            String str = "" + i ;
            arrayList.add(str);
        }
    }

    public void initMonthArrayList(ArrayList<String> arrayList) {
        for (int i = 1; i <= 24; i++) {
            int id = i % 12;
            String str = null;
            if (id == 0) {
                str = "12";
            } else if (id < 10) {
                str = "0" + id ;
            } else {
                str = "" + id ;
            }
            arrayList.add(str);
        }
    }

    public void initDayArrayList(ArrayList<String> arrayList, int year,
                                 int month) {
        int maxDay = getDaysByYearMonth(year, month);
        if (this.maxDay == maxDay) {
            return;
        }
        this.maxDay = maxDay;
        arrayList.clear();
        for (int i = 1; i <= maxDay * 2; i++) {
            int id = i % maxDay;
            String str = null;
            if (id == 0) {
                str = "" + maxDay ;
            } else if (id < 10) {
                str = "0" + id ;
            } else {
                str = "" + id ;
            }
            arrayList.add(str);
        }
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    int id;

    public void init(int year, int month, int day) {

        select_my_year = year;
        select_my_month = month;
        select_my_day = day;

//        title.setText(select_my_year + "-" + select_my_month + "-" + select_my_day);

        id = year - minYear;
        year_listview.setSelection(id - 2);
        if (month < 3) {
            month_listview.setSelection(12 + month - 3);
        } else {
            month_listview.setSelection(month - 3);
        }
        if (day < 3) {
            day_listview.setSelection(day + arrayListDay.size() / 2 - 3);
        } else {
            day_listview.setSelection(day - 3);
        }
    }

    public int yearTranDay(int year) {
        int day = 0;

        return day;
    }

    public int day(String selecDate) {
        int numbler = 0;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = df.format(curDate);
        long to;
        try {

            to = df.parse(str).getTime();
            long from = df.parse(selecDate).getTime();

            numbler = (int) ((to - from) / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return numbler;

    }

    public Date getDate(String dateString, int beforeDays)
            throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Date inputDate = dateFormat.parse(dateString);

        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);

        int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, inputDayOfYear - beforeDays);

        return cal.getTime();
    }

    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    public void initListView(ListView listview, final int state) {
        Calendar mCalendar = Calendar.getInstance();
        final int year = mCalendar.get(Calendar.YEAR);
        final int month = mCalendar.get(Calendar.MONTH) + 1;
        final int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        final long currentTimes = mCalendar.getTimeInMillis();

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int stateScroll) {
                if (stateScroll == SCROLL_STATE_IDLE) {
                    long selectTime = getTimes(select_my_year, select_my_month, select_my_day);
//					switch (state) {
//					case YEAR:
                    initDayArrayList(arrayListDay, select_my_year, select_my_month);
                    adapterDay.setArrayList(arrayListDay);
                    adapterDay.notifyDataSetChanged();
                    year_listview.setSelection(select_year);
                    day_listview.setSelection(select_day);
                    month_listview.setSelection(select_month);

//						Log.e("select_my_year", "select_my_year  = "+select_my_year);
//						Log.e("select_my_month", "select_my_month  = "+select_my_month);
//						Log.e("select_my_day", "select_my_day  = "+select_my_day);

                    if (selectTime > currentTimes) {
                        select_my_year = year;
                        select_my_month = month;
                        select_my_day = day;
                        setPosition();
                    }
                    listener.realTime("");
//						if(UtilDate.INPUT_DATA == 0)
//						{
//							long l = (long)280*24*60*60*1000;
//							long min = currentTimes - l;
////							Log.e("lllllll~~~~~", "llllllllll~~~~  = "+l);
////							Log.e("min~~~~~", "min~~~~  = "+min);
//							if(selectTime < min)
//							{
//								Calendar cc = Calendar.getInstance();
//								cc.setTimeInMillis(min);
//								select_my_year = cc.get(Calendar.YEAR);
//								select_my_month = cc.get(Calendar.MONTH)+1;
//								select_my_day = cc.get(Calendar.DAY_OF_MONTH);
////								Log.e("select_my_year~~~~~", "select_my_year~~~~  = "+select_my_year);
////								Log.e("select_my_month~~~~~~~", "select_my_month~~~~~~  = "+select_my_month);
////								Log.e("select_my_day~~~~~~~~", "select_my_day~~~~~~~~  = "+select_my_day);
//								setPosition();
//							}
//						}else
//						{
////							if(selectTime>)
//						}
//						initDayArrayList(arrayListDay, select_my_year,select_my_month);
//						adapterDay.setArrayList(arrayListDay);
//						adapterDay.notifyDataSetChanged();

//						Calendar myCalendar = Calendar.getInstance();
//						myCalendar.set(select_my_year, select_my_month,select_my_day);
//						System.out.println(UtilDate.STATE+ "===============>start");
//						if (UtilDate.INPUT_DATA == 0) {
//							if (day(select_my_year + "-" + select_my_month+ "-" + select_my_day) > 280) {
//								Date mDate = getDateBefore(new Date(), 280);
//								System.out.println(new Date() + "-------->3"+ mDate.getDay() + "============>#");
//								year_listview.setSelection(mDate.getYear() - 2);
//								month_listview.setSelection(mDate.getMonth() - 2);
//								day_listview.setSelection(mDate.getDay() + 21);
//							} else if (select_my_year > year
//									|| select_my_month > month
//									|| select_my_day > day) {
//								// Toast.makeText(context, "不能大于当前年",
//								// 3000).show();
//								year_listview.setSelection(id - 2);
//								month_listview.setSelection(month - 2);
//								day_listview.setSelection(day - 3);
//							}
//						} else if (select_my_year > year) {
//							year_listview.setSelection(id - 2);
//						}
//						break;
//					case MONTH:

//						initDayArrayList(arrayListDay, select_my_year,select_my_month);
//						adapterDay.setArrayList(arrayListDay);
//						adapterDay.notifyDataSetChanged();

//						if (UtilDate.INPUT_DATA == 0) {
//							if (day(select_my_year + "-" + select_my_month + "-" + select_my_day) > 280) {
//								Date mDate = getDateBefore(new Date(), 280);
//								year_listview.setSelection(mDate.getYear() - 2);
//								month_listview.setSelection(mDate.getMonth() - 2);
//								day_listview.setSelection(mDate.getDay() + 21);
//							} else if (select_my_year == year) {
//								if (select_my_month > month + 1
//										|| select_my_month > month
//										|| select_my_day > day) {
//									day_listview.setSelection(day - 3);
//									month_listview.setSelection(month - 2);
//								}
//							}
//						} else if (select_my_month > month + 1) {
//							// Toast.makeText(context, "不能大于当前年",
//							// 3000).show();
//							month_listview.setSelection(month - 2);
//						}
//						break;
//					case DAY:

//						if (UtilDate.INPUT_DATA == 0) {
//							if (select_my_month == month + 1) {
//								if (select_my_day > day) {
//									// Toast.makeText(context, "不能大于当前天",
//									// 3000).show();
//									day_listview.setSelection(day - 3);
//								}
//							} else {
//								if (day(select_my_year + "-" + select_my_month + "-" + select_my_day) > 280) {
//									Date mDate = getDateBefore(new Date(), 280);
//									year_listview.setSelection(mDate.getYear() - 2);
//									month_listview.setSelection(mDate.getMonth() - 2);
//									day_listview.setSelection(mDate.getDay() + 21);
//								}
//							}
//						} else if (select_my_day > day) {
//
//							day_listview.setSelection(day - 3);
//						}
//						break;
//					}
                }
            }

            @Override
            public void onScroll(AbsListView arg0, int first, int visible, int totle) {
                switch (state) {
                    case YEAR:
                        int id_year = (first + 2) % arrayListYear.size();
                        year_text.setText(arrayListYear.get(id_year));
                        if (first == 0) {
                            year_listview.setSelection(arrayListYear.size());
                        }
                        select_year = first;
                        if (!TextUtils.isEmpty(arrayListYear.get(id_year))) {
                            String str_year = arrayListYear.get(id_year).substring(0, 4);
                            select_my_year = Integer.parseInt(str_year);
                        }

                        /*if (isChoose == IDatalifeConstant.WEEK){
                            title.setText(getDate(select_my_year + "-" + select_my_month + "-" + select_my_day));
                        }else if (isChoose == IDatalifeConstant.DAY){
                            title.setText(select_my_year + "-" + select_my_month + "-" + select_my_day);
                        }else if (isChoose == IDatalifeConstant.MONTH){
                            title.setText(select_my_year + "-" + select_my_month);
                        }else if (isChoose == IDatalifeConstant.YEAR){
                            title.setText(select_my_year+"");
                        }*/
                        break;
                    case MONTH:
                        int id_month = (first + 2) % arrayListMonth.size();
                        String monthStr = arrayListMonth.get(id_month);
                        month_text.setText(monthStr + " ");
                        if (first == 0) {
                            month_listview.setSelection(arrayListMonth.size());
                        }
                        select_month = first;
                        String str_month = arrayListMonth.get(id_month).substring(0, 2);
                        select_my_month = Integer.parseInt(str_month);

                        /*if (isChoose == IDatalifeConstant.WEEK){
                            title.setText(getDate(select_my_year + "-" + select_my_month + "-" + select_my_day));
                        }else if (isChoose == IDatalifeConstant.DAY){
                            title.setText(select_my_year + "-" + select_my_month + "-" + select_my_day);
                        }else if (isChoose == IDatalifeConstant.MONTH){
                            title.setText(select_my_year + "-" + select_my_month);
                        }else if (isChoose == IDatalifeConstant.YEAR){
                            title.setText(select_my_year+"");
                        }*/
                        break;
                    case DAY:
                        int id_day = (first + 2) % arrayListDay.size();
                        String dayStr = arrayListDay.get(id_day);
                        day_text.setText(dayStr);
                        if (first == 0) {
                            day_listview.setSelection(arrayListDay.size());
                        }
                        select_day = first;
                        String str_day = arrayListDay.get(id_day).substring(0, 2);
                        select_my_day = Integer.parseInt(str_day);

                        /*if (isChoose == IDatalifeConstant.WEEK){
                            title.setText(getDate(select_my_year + "-" + select_my_month + "-" + select_my_day));
                        }else if (isChoose == IDatalifeConstant.DAY){
                            title.setText(select_my_year + "-" + select_my_month + "-" + select_my_day);
                        }else if (isChoose == IDatalifeConstant.MONTH){
                            title.setText(select_my_year + "-" + select_my_month);
                        }else if (isChoose == IDatalifeConstant.YEAR){
                            title.setText(select_my_year+"");
                        }*/
                        break;
                }
            }
        });
    }

    public void count(int screenHeight) {
        relative_length = screenHeight * 2 / 7;
        item_length = relative_length / 5;
    }

    public void setMaxYear(int year) {
        this.maxYear = year;
    }

    public void setMinYear(int year) {
        this.minYear = year;
    }

    @SuppressLint("NewApi")
    public void setListViewWidth(ListView listview, int width) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) listview
                .getLayoutParams();
        params.width = width;
        listview.setLayoutParams(params);
    }

    public void setListYear(){
        year_listview.setSelection(78);
    }

    class DateAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> arrayList;
        private int state;

        public DateAdapter(Context context, ArrayList<String> arrayList,
                           int state) {
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
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.year_item, null);
                    holder.text = (TextView) convertView
                            .findViewById(R.id.year_item_tv);
                } else if (state == MONTH || state == DAY) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.month_day_item, null);
                    holder.text = (TextView) convertView
                            .findViewById(R.id.month_day_item_tv);
                }
                LayoutParams params = (LayoutParams) holder.text
                        .getLayoutParams();
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

    public void setMyDateLinearListener(
            MyDateLinearListener myDateLinearListener) {
        this.listener = myDateLinearListener;
    }

    public int getSelcet_my_year() {
        return select_my_year;
    }

    public int getSelcet_my_month() {
        return select_my_month;
    }

    public int getSelcet_my_day() {
        return select_my_day;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.mydate_cancle:
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    cancle.setTextColor(Color.parseColor("#0000CA"));
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    cancle.setTextColor(Color.parseColor("#31BEF3"));
                listener.cancle();
//                }
                break;
            case R.id.mydate_sure:
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

    public interface MyDateLinearListener {
        /**
         * 设置屏幕的宽
         *
         * @return
         */
        public int getscreenWidth();

        /**
         * 设置屏幕的高
         *
         * @return
         */
        public int getscreenHeight();

        public void cancle();

        public void sure(String date);

        public void realTime(String data);
    }

    public long getTimes(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        return c.getTimeInMillis();
    }

    public void setPosition() {
        initDayArrayList(arrayListDay, select_my_year, select_my_month);
        adapterDay.setArrayList(arrayListDay);
        adapterDay.notifyDataSetChanged();
        if (isChoose == IDatalifeConstant.WEEK){
            title.setText(getDate(select_my_year + "-" + select_my_month + "-" + select_my_day));
        }else if (isChoose == IDatalifeConstant.DAY_History){
            title.setText(select_my_year + "-" + select_my_month + "-" + select_my_day);
        }else if (isChoose == IDatalifeConstant.MONTH){
            title.setText(select_my_year + "-" + select_my_month);
        }else if (isChoose == IDatalifeConstant.YEAR){
            title.setText(select_my_year+"");
        }
        id = select_my_year - minYear;
        year_listview.setSelection(id - 2);
        if (select_my_month < 3) {
            month_listview.setSelection(12 + select_my_month - 3);
        } else {
            month_listview.setSelection(select_my_month - 3);
        }
        if (select_my_day < 3) {
            day_listview.setSelection(select_my_day + arrayListDay.size() / 2 - 3);
        } else {
            day_listview.setSelection(select_my_day - 3);
        }
    }

    public void setChooseDate(int isChooseItem){
        isChoose = isChooseItem;
        if (isChoose == IDatalifeConstant.WEEK){
            title.setText(getDate(select_my_year + "-" + select_my_month + "-" + select_my_day));
        }else if (isChoose == IDatalifeConstant.DAY_History){
            title.setText(select_my_year + "-" + select_my_month + "-" + select_my_day);
        }else if (isChoose == IDatalifeConstant.MONTH){
            title.setText(select_my_year + "-" + select_my_month);
        }else if (isChoose == IDatalifeConstant.YEAR){
            title.setText(select_my_year+"");
        }
    }

    private String getDate(String str){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(str);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            Date date1 = calendar.getTime();
            String dateStr =simpleDateFormat.format(date1);

            return dateStr + " ～ " + str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
