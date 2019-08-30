package com.datalife.datalife_company.util;

import java.util.Calendar;

/**
 * 填写成员信息的时候弹出的年月日界面
 */
public class MyCalendar {

    private Calendar calendar;

    public static MyCalendar getInstance()
    {
        return new MyCalendar();
    }

    public MyCalendar()
    {
        this.calendar = Calendar.getInstance();
    }

    public int get(int field)
    {
        return calendar.get(field);
    }

    public long getTimeInMillis()
    {
        return calendar.getTimeInMillis()/1000;
    }

    public void set(int field,int value)
    {
        calendar.set(field, value);
    }

    public void setTimeInMillis(long milliseconds)
    {
        String str = milliseconds+"";
        long timeMills = milliseconds;
        if(str.length() == 10)
        {
            timeMills = milliseconds*1000;
        }
        calendar.setTimeInMillis(timeMills);
    }

    public void set(int year,int month,int day)
    {
        calendar.set(year, month, day);
    }

    public void set(int year,int month,int day,int hourOfDay,int minute)
    {
        calendar.set(year, month, day, hourOfDay, minute);
    }

    public void set(int year,int month,int day,int hourOfDay,int minute,int second)
    {
        calendar.set(year, month, day, hourOfDay, minute, second);
    }

    public void clear()
    {
        calendar.clear();
    }

    public void setCalendar(Calendar c)
    {
        this.calendar = c;
    }

    public Calendar getCalendar()
    {
        return this.calendar;
    }

}

