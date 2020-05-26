package com.waterdiary.drinkreminder;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.AppCompatTextView;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.waterdiary.drinkreminder.adapter.OnBoardingPagerAdapter;
import com.waterdiary.drinkreminder.base.MasterBaseAppCompatActivity;
import com.waterdiary.drinkreminder.custom.NonSwipeableViewPager;
import com.waterdiary.drinkreminder.receiver.MyAlarmManager;
import com.waterdiary.drinkreminder.utils.URLFactory;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Screen_OnBoarding extends MasterBaseAppCompatActivity
{
    NonSwipeableViewPager viewPager;
    OnBoardingPagerAdapter onBoardingPagerAdapter;
    DotsIndicator dots_indicator;

    LinearLayout btn_back,btn_next;
    View space;
    AppCompatTextView lbl_next;

    int current_page_idx=0;
    int max_page=5;

    private static final int ALL_PERMISSION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_onboarding);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(mContext.getResources().getColor(R.color.water_color));
        }

        FindViewById();
        Body();
    }

    public void FindViewById()
    {
        viewPager=findViewById(R.id.viewPager);
        dots_indicator=findViewById(R.id.dots_indicator);

        btn_back=findViewById(R.id.btn_back);
        btn_next=findViewById(R.id.btn_next);
        lbl_next=findViewById(R.id.lbl_next);
        space=findViewById(R.id.space);

        dots_indicator.setDotsClickable(false);
    }

    public void Body()
    {
        onBoardingPagerAdapter=new OnBoardingPagerAdapter(getSupportFragmentManager(),mContext);
        viewPager.setAdapter(onBoardingPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                current_page_idx=position;

                if(position==0)
                {
                    btn_back.setVisibility(View.GONE);
                    space.setVisibility(View.GONE);
                }
                else
                {
                    btn_back.setVisibility(View.VISIBLE);
                    space.setVisibility(View.VISIBLE);
                }

                if(position==max_page-1)
                {
                    lbl_next.setText(sh.get_string(R.string.str_get_started));
                }
                else
                {
                    lbl_next.setText(sh.get_string(R.string.str_next));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        viewPager.setOffscreenPageLimit(4);

        dots_indicator.setViewPager(viewPager);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(current_page_idx>0);
                    current_page_idx-=1;

                viewPager.setCurrentItem(current_page_idx);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //ah.customAlert(""+ph.getString(URLFactory.USER_NAME));
/*
                if(current_page_idx==0)
                {
                    if(sh.check_blank_data(ph.getString(URLFactory.USER_NAME))) {
                        ah.customAlert(sh.get_string(R.string.str_your_name_validation));
                        return;
                    }

                    if(ph.getString(URLFactory.USER_NAME).length()<3) {
                        ah.customAlert(sh.get_string(R.string.str_valid_name_validation));
                        return;
                    }
                }*/
                /*if(current_page_idx==1)
                {
                    try
                    {
                        if(sh.check_blank_data(ph.getString(URLFactory.PERSON_HEIGHT)))
                        {
                            ah.customAlert(sh.get_string(R.string.str_height_validation));
                            return;
                        }

                        if(sh.check_blank_data(ph.getString(URLFactory.PERSON_WEIGHT)))
                        {
                            ah.customAlert(sh.get_string(R.string.str_weight_validation));
                            return;
                        }

                        float val=Float.parseFloat(""+ph.getString(URLFactory.PERSON_HEIGHT));
                        if(val<2)
                        {
                            ah.customAlert(sh.get_string(R.string.str_height_validation));
                            return;
                        }

                        float val2=Float.parseFloat(""+ph.getString(URLFactory.PERSON_WEIGHT));
                        if(val2<30)
                        {
                            ah.customAlert(sh.get_string(R.string.str_weight_validation));
                            return;
                        }
                    }
                    catch (Exception e){}


                    /*if(sh.check_blank_data(ph.getString(URLFactory.USER_NAME))) {
                        ah.customAlert(sh.get_string(R.string.str_your_name_validation));
                        return;
                    }*/
                //}

                /*if(current_page_idx==5)
                {
                    if(sh.check_blank_data(ph.getString(URLFactory.WAKE_UP_TIME)) || sh.check_blank_data(ph.getString(URLFactory.BED_TIME)))
                    {
                        ah.customAlert(sh.get_string(R.string.str_from_to_invalid_validation));
                        return;
                    }
                    else if(ph.getBoolean(URLFactory.IGNORE_NEXT_STEP))
                    {
                        ah.customAlert(sh.get_string(R.string.str_from_to_invalid_validation));
                        return;
                    }

                    //setAlarm();
                }*/

                if(current_page_idx<max_page-1) {
                    current_page_idx += 1;
                    viewPager.setCurrentItem(current_page_idx);
                }
                else
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkStoragePermissions();
                    } else {
                        gotoHomeScreen();
                    }
                }
            }
        });
    }

    /*public boolean isNextDayEnd()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat.parse(ph.getString(URLFactory.WAKE_UP_TIME));
            date2 = simpleDateFormat.parse(ph.getString(URLFactory.BED_TIME));

            if(date1.getTime()>date2.getTime())
                return true;
            else
                return false;
        }
        catch (Exception e){}

        return false;
    }

    public void setAlarm()
    {
        int minute_interval=ph.getInt(URLFactory.INTERVAL);

        if(sh.check_blank_data(ph.getString(URLFactory.WAKE_UP_TIME)) || sh.check_blank_data(ph.getString(URLFactory.BED_TIME)))
        {
            //ah.customAlert(sh.get_string(R.string.str_from_to_invalid_validation));
            return;
        }
        else
        {
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, ph.getInt(URLFactory.WAKE_UP_TIME_HOUR));
            startTime.set(Calendar.MINUTE, ph.getInt(URLFactory.WAKE_UP_TIME_MINUTE));
            startTime.set(Calendar.SECOND,0);

            Calendar endTime = Calendar.getInstance();
            endTime.set(Calendar.HOUR_OF_DAY, ph.getInt(URLFactory.BED_TIME_HOUR));
            endTime.set(Calendar.MINUTE, ph.getInt(URLFactory.BED_TIME_MINUTE));
            endTime.set(Calendar.SECOND,0);

            if(isNextDayEnd())
                endTime.add(Calendar.DATE,1);

            if(startTime.getTimeInMillis()<endTime.getTimeInMillis())
            {
                deleteAutoAlarm(true);

                int _id = (int) System.currentTimeMillis();



                ContentValues initialValues = new ContentValues();
                initialValues.put("AlarmTime", ""+ ph.getString(URLFactory.WAKE_UP_TIME)+" - "+ph.getString(URLFactory.BED_TIME));
                initialValues.put("AlarmId", ""+ _id);
                initialValues.put("AlarmType", "R");
                initialValues.put("AlarmInterval", ""+minute_interval);
                dh.INSERT("tbl_alarm_details", initialValues);

                String getLastId=dh.GET_LAST_ID("tbl_alarm_details");

                while(startTime.getTimeInMillis()<=endTime.getTimeInMillis())
                {
                    Log.d("ALARMTIME  : ",""+startTime.get(Calendar.HOUR_OF_DAY)+":"+startTime.get(Calendar.MINUTE)+":"+startTime.get(Calendar.SECOND));

                    try
                    {
                        _id = (int) System.currentTimeMillis();

                        String formatedDate = "";
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
                        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a", Locale.US);
                        Date dt;
                        String time = "";

                        time = startTime.get(Calendar.HOUR_OF_DAY)+":"+startTime.get(Calendar.MINUTE)+":"+startTime.get(Calendar.SECOND);
                        dt = sdf.parse(time);
                        formatedDate = sdfs.format(dt);

                        if(!dh.IS_EXISTS("tbl_alarm_details","AlarmTime='"+formatedDate+"'") && !dh.IS_EXISTS("tbl_alarm_sub_details","AlarmTime='"+formatedDate+"'"))
                        {
                            MyAlarmManager.scheduleAutoRecurringAlarm(mContext,startTime,_id);

                            ContentValues initialValues2 = new ContentValues();
                            initialValues2.put("AlarmTime", "" + formatedDate);
                            initialValues2.put("AlarmId", "" + _id);
                            initialValues2.put("SuperId", "" + getLastId);
                            dh.INSERT("tbl_alarm_sub_details", initialValues2);


                            int _id_sunday = (int) System.currentTimeMillis();
                            int _id_monday = (int) System.currentTimeMillis();
                            int _id_tuesday = (int) System.currentTimeMillis();
                            int _id_wednesday = (int) System.currentTimeMillis();
                            int _id_thursday = (int) System.currentTimeMillis();
                            int _id_friday = (int) System.currentTimeMillis();
                            int _id_saturday = (int) System.currentTimeMillis();
                            ContentValues initialValues3 = new ContentValues();
                            initialValues3.put("AlarmTime", "" + formatedDate);
                            initialValues3.put("AlarmId", ""+ _id_sunday);
                            initialValues3.put("SundayAlarmId", ""+ _id_sunday);
                            initialValues3.put("MondayAlarmId", ""+ _id_monday);
                            initialValues3.put("TuesdayAlarmId", ""+ _id_tuesday);
                            initialValues3.put("WednesdayAlarmId", ""+ _id_wednesday);
                            initialValues3.put("ThursdayAlarmId", ""+ _id_thursday);
                            initialValues3.put("FridayAlarmId", ""+ _id_friday);
                            initialValues3.put("SaturdayAlarmId", ""+ _id_saturday);
                            initialValues3.put("AlarmType", "M");
                            initialValues3.put("AlarmInterval", "0");
                            dh.INSERT("tbl_alarm_details", initialValues3);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    startTime.add(Calendar.MINUTE, minute_interval);
                }
            }
            else
            {
                return;
            }
        }
    }

    public void deleteAutoAlarm(boolean alsoData)
    {
        ArrayList<HashMap<String,String>> arr_data = dh.getdata("tbl_alarm_details");

        for(int k=0;k<arr_data.size();k++)
        {
            MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("AlarmId")));

            ArrayList<HashMap<String,String>> arr_data2 = dh.getdata("tbl_alarm_sub_details","SuperId="+arr_data.get(k).get("id"));
            for(int j=0;j<arr_data2.size();j++)
                MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data2.get(j).get("AlarmId")));
        }

        if(alsoData) {
            dh.REMOVE("tbl_alarm_details");
            dh.REMOVE("tbl_alarm_sub_details");
        }
    }
*/
    public void checkStoragePermissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, ALL_PERMISSION);
            }
            else
            {
                gotoHomeScreen();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ALL_PERMISSION:
                /*if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                }
                else
                {
                }*/

                gotoHomeScreen();

                break;
        }
    }

    public void gotoHomeScreen()
    {
        ph.savePreferences(URLFactory.HIDE_WELCOME_SCREEN,true);

        //setAlarm();

        intent=new Intent(act,handbook_start.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed()
    {
        if(current_page_idx>0)
        {
            current_page_idx -= 1;
            viewPager.setCurrentItem(current_page_idx);
        }
        else
            super.onBackPressed();
    }
}