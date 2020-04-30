package com.waterdiary.drinkreminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.waterdiary.drinkreminder.adapter.AlarmAdapter;
import com.waterdiary.drinkreminder.adapter.IntervalAdapter;
import com.waterdiary.drinkreminder.adapter.SoundAdapter;
import com.waterdiary.drinkreminder.base.MasterBaseActivity;
import com.waterdiary.drinkreminder.base.MasterBaseAppCompatActivity;
import com.waterdiary.drinkreminder.model.AlarmModel;
import com.waterdiary.drinkreminder.model.IntervalModel;
import com.waterdiary.drinkreminder.model.SoundModel;
import com.waterdiary.drinkreminder.receiver.MyAlarmManager;
import com.waterdiary.drinkreminder.utils.URLFactory;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.waterdiary.drinkreminder.R.*;

public class Screen_Reminder extends MasterBaseActivity
{
    LinearLayout right_icon_block,left_icon_block;

    ArrayList<AlarmModel> alarmModelList=new ArrayList<>();

    RecyclerView alarmRecyclerView;
    AlarmAdapter alarmAdapter;

    BottomSheetDialog bottomSheetDialogSound;

    int from_hour=0,from_minute=0,to_hour=0,to_minute=0;
    int interval=30;

    List<String> lst_interval=new ArrayList<>();

    AppCompatTextView lbl_no_record_found;

    RadioButton rdo_auto,rdo_off,rdo_silent;

    LinearLayout sound_block;
    List<SoundModel> lst_sounds=new ArrayList<>();
    SoundAdapter soundAdapter;

    SwitchCompat switch_vibrate;


    //==================

    AppCompatTextView lbl_wakeup_time,lbl_bed_time,lbl_interval;
    IntervalAdapter intervalAdapter;
    List<IntervalModel> lst_intervals=new ArrayList<>();


    RelativeLayout manual_reminder_block,auto_reminder_block;
    RadioButton rdo_auto_alarm,rdo_manual_alarm;
    RelativeLayout add_reminder;
    RelativeLayout save_reminder;


    AppCompatTextView lblwt,lblbt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_reminder);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(mContext.getResources().getColor(R.color.str_green_card));
        }

        FindViewById();
        Body();

        lblwt=findViewById(R.id.lblwt);
        lblbt=findViewById(R.id.lblbt);

        String str=sh.get_string(R.string.str_bed_time);
        str=str.substring(0,1).toUpperCase()+""+str.substring(1).toLowerCase();
        lblbt.setText(str);
        //=======
        str=sh.get_string(string.str_wakeup_time);
        str=str.substring(0,1).toUpperCase()+""+str.substring(1).toLowerCase();
        lblwt.setText(str);

        //ah.Show_Alert_Dialog(""+dh.TOTAL_ROW(""));

        ArrayList<HashMap<String,String>> arr_data = dh.getdata("tbl_alarm_details");
        Log.d("setAutoAlarmAnd",""+new Gson().toJson(arr_data));
        ArrayList<HashMap<String,String>> arr_data2 = dh.getdata("tbl_alarm_sub_details");
        Log.d("setAutoAlarmAnd",""+new Gson().toJson(arr_data2));

    }

    private void FindViewById()
    {
        right_icon_block=findViewById(id.right_icon_block);
        left_icon_block=findViewById(id.left_icon_block);

        alarmRecyclerView=findViewById(R.id.alarmRecyclerView);
        lbl_no_record_found=findViewById(R.id.lbl_no_record_found);

        rdo_auto=findViewById(R.id.rdo_auto);
        rdo_off=findViewById(R.id.rdo_off);
        rdo_silent=findViewById(R.id.rdo_silent);

        sound_block=findViewById(R.id.sound_block);

        switch_vibrate=findViewById(R.id.switch_vibrate);


        lbl_wakeup_time=findViewById(R.id.lbl_wakeup_time);
        lbl_bed_time=findViewById(R.id.lbl_bed_time);
        lbl_interval=findViewById(R.id.lbl_interval);

        manual_reminder_block=findViewById(R.id.manual_reminder_block);
        auto_reminder_block=findViewById(R.id.auto_reminder_block);

        rdo_auto_alarm=findViewById(R.id.rdo_auto_alarm);
        rdo_manual_alarm=findViewById(R.id.rdo_manual_alarm);

        add_reminder=findViewById(R.id.add_reminder);
        save_reminder=findViewById(R.id.save_reminder);
    }


    public void setAutoAlarmAndRemoveAllManualAlarm()
    {
        for(int k=0;k<alarmModelList.size();k++)
        {
            AlarmModel time=alarmModelList.get(k);

            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmSundayId()));
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmMondayId()));
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmTuesdayId()));
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmWednesdayId()));
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmThursdayId()));
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmFridayId()));
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmSaturdayId()));
        }

        setAutoAlarm(false);
    }

    public void setAllManualAlarmAndRemoveAutoAlarm()
    {
        ArrayList<HashMap<String,String>> arr_data = dh.getdata("tbl_alarm_details","AlarmType='R'");

        for(int k=0;k<arr_data.size();k++)
        {
            MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("AlarmId")));

            ArrayList<HashMap<String,String>> arr_data2 = dh.getdata("tbl_alarm_sub_details","SuperId="+arr_data.get(k).get("id"));
            for(int j=0;j<arr_data2.size();j++)
                MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data2.get(j).get("AlarmId")));
        }

        //ah.Show_Alert_Dialog(""+alarmModelList.size());

        for(int k=0;k<alarmModelList.size();k++)
        {
            AlarmModel time=alarmModelList.get(k);

            int hourOfDay=Integer.parseInt(""+
                    dth.FormateDateFromString("hh:mm a","HH",time.getDrinkTime().trim()));
            int minute=Integer.parseInt(""+
                    dth.FormateDateFromString("hh:mm a","mm",time.getDrinkTime().trim()));

            //setRecurringAlarm(mContext,hourOfDay,minute,Integer.parseInt(""+time.getAlarmId()));

            Log.d("setAllManualAlarm : ",""+time.getSunday());

            if(time.getSunday()==1)
                MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.SUNDAY,hourOfDay
                        ,minute,Integer.parseInt(""+time.getAlarmSundayId()));
            if(time.getMonday()==1)
                MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.MONDAY,hourOfDay
                        ,minute,Integer.parseInt(""+time.getAlarmMondayId()));
            if(time.getTuesday()==1)
                MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.TUESDAY,hourOfDay
                        ,minute,Integer.parseInt(""+time.getAlarmTuesdayId()));
            if(time.getWednesday()==1)
                MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.WEDNESDAY,hourOfDay
                        ,minute,Integer.parseInt(""+time.getAlarmWednesdayId()));
            if(time.getThursday()==1)
                MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.THURSDAY,hourOfDay
                        ,minute,Integer.parseInt(""+time.getAlarmThursdayId()));
            if(time.getFriday()==1)
                MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.FRIDAY,hourOfDay
                        ,minute,Integer.parseInt(""+time.getAlarmFridayId()));
            if(time.getSaturday()==1)
                MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.SATURDAY,hourOfDay
                        ,minute,Integer.parseInt(""+time.getAlarmSaturdayId()));
        }
    }

    public void load_AutoDataFromDB()
    {
        ArrayList<HashMap<String, String>>  arr_data=dh.getdata("tbl_alarm_details","AlarmType='R'");

        //ah.Show_Alert_Dialog(""+arr_data.size());

        if(arr_data.size()>0)
        {
            String str_date[]=arr_data.get(0).get("AlarmTime").split("-");

            if(str_date.length>1) {
                lbl_wakeup_time.setText(str_date[0].trim());
                lbl_bed_time.setText(str_date[1].trim());
            }

            from_hour=Integer.parseInt(""+dth.FormateDateFromString("hh:mm a","HH",str_date[0].trim()));
            from_minute=Integer.parseInt(""+dth.FormateDateFromString("hh:mm a","mm",str_date[0].trim()));

            to_hour=Integer.parseInt(""+dth.FormateDateFromString("hh:mm a","HH",str_date[1].trim()));
            to_minute=Integer.parseInt(""+dth.FormateDateFromString("hh:mm a","mm",str_date[1].trim()));

            //ah.Show_Alert_Dialog(""+dth.FormateDateFromString("hh:mm a","HH",str_date[1].trim()));

            interval=Integer.parseInt(""+arr_data.get(0).get("AlarmInterval"));

            if(arr_data.get(0).get("AlarmInterval").equalsIgnoreCase("60"))
                lbl_interval.setText("1 "+sh.get_string(R.string.str_hour));
            else
                lbl_interval.setText(arr_data.get(0).get("AlarmInterval")+" "+sh.get_string(R.string.str_min));
        }
    }

    private void Body()
    {
        /*ArrayList<HashMap<String, String>>  arr_data=dh.getdata("tbl_alarm_details","AlarmType='R'");

        if(arr_data.size()>0)
        {
            String str_date[]=arr_data.get(0).get("AlarmTime").split("-");

            if(str_date.length>1) {
                lbl_wakeup_time.setText(str_date[0].trim());
                lbl_bed_time.setText(str_date[1].trim());
            }

            from_hour=Integer.parseInt(""+dth.FormateDateFromString("hh:mm a","HH",str_date[0].trim()));
            from_minute=Integer.parseInt(""+dth.FormateDateFromString("hh:mm a","mm",str_date[0].trim()));

            to_hour=Integer.parseInt(""+dth.FormateDateFromString("hh:mm a","HH",str_date[1].trim()));
            to_minute=Integer.parseInt(""+dth.FormateDateFromString("hh:mm a","mm",str_date[1].trim()));

            //ah.Show_Alert_Dialog(""+dth.FormateDateFromString("hh:mm a","HH",str_date[1].trim()));

            interval=Integer.parseInt(""+arr_data.get(0).get("AlarmInterval"));

            if(arr_data.get(0).get("AlarmInterval").equalsIgnoreCase("60"))
                lbl_interval.setText("1 "+sh.get_string(R.string.str_hour));
            else
                lbl_interval.setText(arr_data.get(0).get("AlarmInterval")+" "+sh.get_string(R.string.str_min));
        }*/

        load_AutoDataFromDB();

        lbl_wakeup_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutoTimePicker2(lbl_wakeup_time,true);
            }
        });

        lbl_bed_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutoTimePicker2(lbl_bed_time,false);
            }
        });

        lbl_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIntervalPicker();
            }
        });


        if(ph.getBoolean(URLFactory.IS_MANUAL_REMINDER)) {
            rdo_manual_alarm.setChecked(true);
            manual_reminder_block.setVisibility(View.VISIBLE);
            auto_reminder_block.setVisibility(View.GONE);
        }
        else {
            rdo_auto_alarm.setChecked(true);
            manual_reminder_block.setVisibility(View.GONE);
            auto_reminder_block.setVisibility(View.VISIBLE);
        }


        rdo_auto_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manual_reminder_block.setVisibility(View.GONE);
                auto_reminder_block.setVisibility(View.VISIBLE);
                ph.savePreferences(URLFactory.IS_MANUAL_REMINDER,false);
            }
        });

        rdo_manual_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manual_reminder_block.setVisibility(View.VISIBLE);
                auto_reminder_block.setVisibility(View.GONE);
                ph.savePreferences(URLFactory.IS_MANUAL_REMINDER,true);
                if(alarmModelList.size()>0)
                    lbl_no_record_found.setVisibility(View.GONE);
                else
                    lbl_no_record_found.setVisibility(View.VISIBLE);
            }
        });

        rdo_auto_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    setAutoAlarmAndRemoveAllManualAlarm();
                }
            }
        });

        rdo_manual_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    setAllManualAlarmAndRemoveAutoAlarm();
                }
            }
        });


        save_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidDate()) {
                    setAutoAlarm(true);
                }
                else
                {
                    ah.customAlert(sh.get_string(R.string.str_from_to_invalid_validation));
                }
            }
        });

        add_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker();
            }
        });


        //==============================


        lst_interval.clear();
        //lst_interval.add("5 "+sh.get_string(R.string.str_minutes));
        lst_interval.add("30 "+sh.get_string(R.string.str_minutes));
        lst_interval.add("45 "+sh.get_string(R.string.str_minutes));
        lst_interval.add("60 "+sh.get_string(R.string.str_minutes));
        lst_interval.add("90 "+sh.get_string(R.string.str_minutes));
        lst_interval.add("120 "+sh.get_string(R.string.str_minutes));

        left_icon_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        right_icon_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openMenuPicker();
                showMenu(view);
            }
        });

        load_alarm();

        switch_vibrate.setChecked(!ph.getBoolean(URLFactory.REMINDER_VIBRATE));

        switch_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ph.savePreferences(URLFactory.REMINDER_VIBRATE,!isChecked);
            }
        });

        alarmAdapter = new AlarmAdapter(act, alarmModelList, new AlarmAdapter.CallBack() {
            @Override
            public void onClickSelect(AlarmModel time, int position) {

            }

            @Override
            public void onClickRemove(final AlarmModel time, final int position)
            {
                AlertDialog.Builder dialog=  new  AlertDialog.Builder(act)
                        .setMessage(sh.get_string(R.string.str_reminder_remove_confirm_message))
                        .setPositiveButton(sh.get_string(R.string.str_yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmId()));

                                        MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmSundayId()));
                                        MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmMondayId()));
                                        MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmTuesdayId()));
                                        MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmWednesdayId()));
                                        MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmThursdayId()));
                                        MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmFridayId()));
                                        MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmSaturdayId()));

                                        alarmModelList.remove(position);
                                        dh.REMOVE("tbl_alarm_details","id="+time.getId());

                                        //ah.customAlert(sh.get_string(R.string.str_successfully_cancel_alarm));

                                        alarmAdapter.notifyDataSetChanged();

                                        if(alarmModelList.size()>0)
                                            lbl_no_record_found.setVisibility(View.GONE);
                                        else
                                            lbl_no_record_found.setVisibility(View.VISIBLE);

                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setNegativeButton(sh.get_string(R.string.str_no),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        );

                dialog.show();
            }

            @Override
            public void onClickEdit(AlarmModel time, int position) {

                //ah.customAlert("edit "+time.getIsOff());

                if(time.getIsOff()!=1)
                    openEditTimePicker(time);
            }

            @Override
            public void onClickSwitch(AlarmModel time, int position,boolean isOn) {
                ContentValues initialValues = new ContentValues();
                initialValues.put("IsOff", isOn?0:1);
                dh.UPDATE("tbl_alarm_details", initialValues,"id="+time.getId());

                alarmModelList.get(position).setIsOff(isOn?0:1);

                if(isOn)
                {
                    int hourOfDay=Integer.parseInt(""+
                            dth.FormateDateFromString("hh:mm a","HH",time.getDrinkTime().trim()));
                    int minute=Integer.parseInt(""+
                            dth.FormateDateFromString("hh:mm a","mm",time.getDrinkTime().trim()));

                    //setRecurringAlarm(mContext,hourOfDay,minute,Integer.parseInt(""+time.getAlarmId()));

                    if(time.getSunday()==1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.SUNDAY,hourOfDay
                            ,minute,Integer.parseInt(""+time.getAlarmSundayId()));
                    if(time.getMonday()==1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.MONDAY,hourOfDay
                            ,minute,Integer.parseInt(""+time.getAlarmMondayId()));
                    if(time.getTuesday()==1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.TUESDAY,hourOfDay
                            ,minute,Integer.parseInt(""+time.getAlarmTuesdayId()));
                    if(time.getWednesday()==1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.WEDNESDAY,hourOfDay
                            ,minute,Integer.parseInt(""+time.getAlarmWednesdayId()));
                    if(time.getThursday()==1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.THURSDAY,hourOfDay
                            ,minute,Integer.parseInt(""+time.getAlarmThursdayId()));
                    if(time.getFriday()==1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.FRIDAY,hourOfDay
                            ,minute,Integer.parseInt(""+time.getAlarmFridayId()));
                    if(time.getSaturday()==1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,Calendar.SATURDAY,hourOfDay
                            ,minute,Integer.parseInt(""+time.getAlarmSaturdayId()));

                    if(alarmModelList.get(position).getSunday()==0 &&
                            alarmModelList.get(position).getMonday()==0 &&
                            alarmModelList.get(position).getTuesday()==0 &&
                            alarmModelList.get(position).getThursday()==0 &&
                            alarmModelList.get(position).getWednesday()==0 &&
                            alarmModelList.get(position).getFriday()==0 &&
                            alarmModelList.get(position).getSunday()==0)
                    {

                        int tmp_from_hour=Integer.parseInt(""+
                                dth.FormateDateFromString("hh:mm a","HH",time.getDrinkTime().trim()));
                        int tmp_from_minute=Integer.parseInt(""+
                                dth.FormateDateFromString("hh:mm a","mm",time.getDrinkTime().trim()));


                        Calendar date=Calendar.getInstance();
                        int week_pos = date.get(Calendar.DAY_OF_WEEK);

                        ContentValues initialValues4 = new ContentValues();

                        if(week_pos==1) {
                            initialValues4.put("Sunday", 1);
                            alarmModelList.get(position).setSunday(1);

                            int _id = Integer.parseInt(""+time.getAlarmSundayId());

                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.SUNDAY,tmp_from_hour,tmp_from_minute,_id);

                        }
                        else if(week_pos==2) {
                            initialValues4.put("Monday", 1);
                            alarmModelList.get(position).setMonday(1);

                            int _id = Integer.parseInt(""+time.getAlarmMondayId());

                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.MONDAY,tmp_from_hour,tmp_from_minute,_id);
                        }
                        else if(week_pos==3) {
                            initialValues4.put("Tuesday", 1);
                            alarmModelList.get(position).setTuesday(1);

                            int _id = Integer.parseInt(""+time.getAlarmTuesdayId());

                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.TUESDAY,tmp_from_hour,tmp_from_minute,_id);
                        }
                        else if(week_pos==4) {
                            initialValues4.put("Wednesday", 1);
                            alarmModelList.get(position).setWednesday(1);

                            int _id = Integer.parseInt(""+time.getAlarmWednesdayId());

                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.WEDNESDAY,tmp_from_hour,tmp_from_minute,_id);
                        }
                        else if(week_pos==5) {
                            initialValues4.put("Thursday", 1);
                            alarmModelList.get(position).setThursday(1);

                            int _id = Integer.parseInt(""+time.getAlarmThursdayId());

                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.THURSDAY,tmp_from_hour,tmp_from_minute,_id);
                        }
                        else if(week_pos==6) {
                            initialValues4.put("Friday", 1);
                            alarmModelList.get(position).setFriday(1);

                            int _id = Integer.parseInt(""+time.getAlarmFridayId());

                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.FRIDAY,tmp_from_hour,tmp_from_minute,_id);
                        }
                        else if(week_pos==7) {
                            initialValues4.put("Saturday", 1);
                            alarmModelList.get(position).setSaturday(1);

                            int _id = Integer.parseInt(""+time.getAlarmSaturdayId());

                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.SATURDAY,tmp_from_hour,tmp_from_minute,_id);
                        }

                        alarmRecyclerView.post(new Runnable()
                        {
                            @Override
                            public void run() {
                                alarmAdapter.notifyDataSetChanged();
                            }
                        });

                        //alarmAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmSundayId()));
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmMondayId()));
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmTuesdayId()));
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmWednesdayId()));
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmThursdayId()));
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmFridayId()));
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmSaturdayId()));
                }

                /*if(isOn)
                {
                    String tmp=dth.FormateDateFromString("hh:mm a","HH:mm",
                            time.getDrinkTime());
                    String[] time2=tmp.split(":");
                    int hourOfDay=Integer.parseInt(time2[0]);
                    int minute=Integer.parseInt(time2[1]);

                    //ah.Show_Alert_Dialog(""+hourOfDay+" @@@ "+minute);

                    setRecurringAlarm(mContext,hourOfDay,minute,Integer.parseInt(""+time.getAlarmId()));
                }
                else
                {
                    cancelRecurringAlarm(mContext,Integer.parseInt(time.getAlarmId()));
                }*/
            }

            @Override
            public void onClickWeek(AlarmModel time, int position, int week_pos, boolean isOn)
            {
                int tmp_from_hour=Integer.parseInt(""+
                        dth.FormateDateFromString("hh:mm a","HH",time.getDrinkTime().trim()));
                int tmp_from_minute=Integer.parseInt(""+
                        dth.FormateDateFromString("hh:mm a","mm",time.getDrinkTime().trim()));

                //ah.Show_Alert_Dialog(""+isOn);

                ContentValues initialValues = new ContentValues();

                if(isOn) {
                    initialValues.put("IsOff", "0");
                    alarmModelList.get(position).setIsOff(isOn?0:1);
                }

                if(week_pos==0) {
                    initialValues.put("Sunday", isOn ? 1 : 0);
                    alarmModelList.get(position).setSunday(isOn?1:0);

                    int _id = Integer.parseInt(""+time.getAlarmSundayId());

                    if(isOn)
                    {
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.SUNDAY,tmp_from_hour,tmp_from_minute,_id);
                    }
                    else
                    {
                        MyAlarmManager.cancelRecurringAlarm(mContext,_id);
                    }
                }
                else if(week_pos==1) {
                    initialValues.put("Monday", isOn ? 1 : 0);
                    alarmModelList.get(position).setMonday(isOn?1:0);

                    int _id = Integer.parseInt(""+time.getAlarmMondayId());

                    if(isOn)
                    {
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.MONDAY,tmp_from_hour,tmp_from_minute,_id);
                    }
                    else
                    {
                        MyAlarmManager.cancelRecurringAlarm(mContext,_id);
                    }
                }
                else if(week_pos==2) {
                    initialValues.put("Tuesday", isOn ? 1 : 0);
                    alarmModelList.get(position).setTuesday(isOn?1:0);

                    int _id = Integer.parseInt(""+time.getAlarmTuesdayId());

                    if(isOn)
                    {
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.TUESDAY,tmp_from_hour,tmp_from_minute,_id);
                    }
                    else
                    {
                        MyAlarmManager.cancelRecurringAlarm(mContext,_id);
                    }
                }
                else if(week_pos==3) {
                    initialValues.put("Wednesday", isOn ? 1 : 0);
                    alarmModelList.get(position).setWednesday(isOn?1:0);

                    int _id = Integer.parseInt(""+time.getAlarmWednesdayId());

                    if(isOn)
                    {
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.WEDNESDAY,tmp_from_hour,tmp_from_minute,_id);
                    }
                    else
                    {
                        MyAlarmManager.cancelRecurringAlarm(mContext,_id);
                    }
                }
                else if(week_pos==4) {
                    initialValues.put("Thursday", isOn ? 1 : 0);
                    alarmModelList.get(position).setThursday(isOn?1:0);

                    int _id = Integer.parseInt(""+time.getAlarmThursdayId());

                    if(isOn)
                    {
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.THURSDAY,tmp_from_hour,tmp_from_minute,_id);
                    }
                    else
                    {
                        MyAlarmManager.cancelRecurringAlarm(mContext,_id);
                    }
                }
                else if(week_pos==5) {
                    initialValues.put("Friday", isOn ? 1 : 0);
                    alarmModelList.get(position).setFriday(isOn?1:0);

                    int _id = Integer.parseInt(""+time.getAlarmFridayId());

                    if(isOn)
                    {
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.FRIDAY,tmp_from_hour,tmp_from_minute,_id);
                    }
                    else
                    {
                        MyAlarmManager.cancelRecurringAlarm(mContext,_id);
                    }
                }
                else if(week_pos==6) {
                    initialValues.put("Saturday", isOn ? 1 : 0);
                    alarmModelList.get(position).setSaturday(isOn?1:0);

                    int _id = Integer.parseInt(""+time.getAlarmSaturdayId());

                    if(isOn)
                    {
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.SATURDAY,tmp_from_hour,tmp_from_minute,_id);
                    }
                    else
                    {
                        MyAlarmManager.cancelRecurringAlarm(mContext,_id);
                    }
                }
                dh.UPDATE("tbl_alarm_details", initialValues,"id="+time.getId());

                if(alarmModelList.get(position).getSunday()==0 &&
                        alarmModelList.get(position).getMonday()==0 &&
                        alarmModelList.get(position).getTuesday()==0 &&
                        alarmModelList.get(position).getThursday()==0 &&
                        alarmModelList.get(position).getWednesday()==0 &&
                        alarmModelList.get(position).getFriday()==0 &&
                        alarmModelList.get(position).getSunday()==0)
                {
                    ContentValues initialValues2 = new ContentValues();
                    initialValues2.put("IsOff", "1");
                    dh.UPDATE("tbl_alarm_details", initialValues2,"id="+time.getId());
                    alarmModelList.get(position).setIsOff(1);
                }

                alarmAdapter.notifyDataSetChanged();
            }
        });

        alarmRecyclerView.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));

        alarmRecyclerView.setAdapter(alarmAdapter);

        if(ph.getInt(URLFactory.REMINDER_OPTION)==1)
        {
            rdo_off.setChecked(true);
        }
        else if(ph.getInt(URLFactory.REMINDER_OPTION)==2)
        {
            rdo_silent.setChecked(true);
        }
        else
        {
            rdo_auto.setChecked(true);
        }


        rdo_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ph.savePreferences(URLFactory.REMINDER_OPTION,0);
            }
        });

        rdo_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ph.savePreferences(URLFactory.REMINDER_OPTION,1);
            }
        });

        rdo_silent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ph.savePreferences(URLFactory.REMINDER_OPTION,2);
            }
        });

        loadSounds();

        sound_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSoundMenuPicker();
            }
        });
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                switch (menuItem.getItemId()) {
                    case R.id.delete_item:
                        // do your code

                        AlertDialog.Builder dialog=  new  AlertDialog.Builder(act)
                                .setMessage(sh.get_string(R.string.str_reminder_remove_all_confirm_message))
                                .setPositiveButton(sh.get_string(R.string.str_yes),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                deleteAllManualAlarm(true);
                                                rdo_auto_alarm.setChecked(true);
                                                dialog.dismiss();
                                            }
                                        }
                                )
                                .setNegativeButton(sh.get_string(R.string.str_no),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                            }
                                        }
                                );

                        dialog.show();



                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(menu.delete_all_menu);
        popup.show();
    }


    public void deleteAllManualAlarm(boolean alsoData) // @@@@
    {
        for(int k=0;k<alarmModelList.size();k++)
        {
            AlarmModel time=alarmModelList.get(k);

            //cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmId()));

            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmSundayId()));
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmMondayId()));
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmTuesdayId()));
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmWednesdayId()));
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmThursdayId()));
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmFridayId()));
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(time.getAlarmSaturdayId()));

            if(alsoData)
                dh.REMOVE("tbl_alarm_details","id="+time.getId());
        }

        if(alsoData) {
            alarmModelList.clear();
            alarmAdapter.notifyDataSetChanged();
        }

        ph.savePreferences(URLFactory.IS_MANUAL_REMINDER,false);

        manual_reminder_block.setVisibility(View.GONE);
        auto_reminder_block.setVisibility(View.VISIBLE);

        setAutoAlarm(false);

    }




    public void loadInterval()
    {
        lst_intervals.clear();

        lst_intervals.add(getIntervalModel(15,"15 "+sh.get_string(R.string.str_min)));
        lst_intervals.add(getIntervalModel(30,"30 "+sh.get_string(R.string.str_min)));
        lst_intervals.add(getIntervalModel(45,"45 "+sh.get_string(R.string.str_min)));
        lst_intervals.add(getIntervalModel(60,"1 "+sh.get_string(R.string.str_hour)));

    }

    public IntervalModel getIntervalModel(int index,String name)
    {
        IntervalModel intervalModel=new IntervalModel();
        intervalModel.setId(index);
        intervalModel.setName(name);
        intervalModel.isSelected(interval==index);

        return intervalModel;
    }


    public void openIntervalPicker()
    {
        loadInterval();

        final Dialog dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        final View view = LayoutInflater.from(act).inflate(layout.dialog_pick_interval, null, false);


        RelativeLayout btn_cancel=view.findViewById(R.id.btn_cancel);
        RelativeLayout btn_save=view.findViewById(R.id.btn_save);


        RecyclerView intervalRecyclerView=view.findViewById(R.id.intervalRecyclerView);

        intervalAdapter = new IntervalAdapter(act, lst_intervals, new IntervalAdapter.CallBack() {
            @Override
            public void onClickSelect(IntervalModel time, int position)
            {
                for(int k=0;k<lst_intervals.size();k++)
                {
                    lst_intervals.get(k).isSelected(false);
                }

                lst_intervals.get(position).isSelected(true);
                intervalAdapter.notifyDataSetChanged();
            }
        });

        intervalRecyclerView.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));

        intervalRecyclerView.setAdapter(intervalAdapter);



        /*final RadioButton rdo_15,rdo_30,rdo_45,rdo_60;

        rdo_15=view.findViewById(R.id.rdo_15);
        rdo_30=view.findViewById(R.id.rdo_30);
        rdo_45=view.findViewById(R.id.rdo_45);
        rdo_60=view.findViewById(R.id.rdo_60);

        rdo_15.setText("15 "+sh.get_string(R.string.str_min));
        rdo_30.setText("30 "+sh.get_string(R.string.str_min));
        rdo_45.setText("45 "+sh.get_string(R.string.str_min));
        rdo_60.setText("1 "+sh.get_string(R.string.str_hour));

        if(interval==60)
            rdo_60.setChecked(true);
        else if(interval==45)
            rdo_45.setChecked(true);
        else if(interval==15)
            rdo_15.setChecked(true);
        else
            rdo_30.setChecked(true);*/


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for(int k=0;k<lst_intervals.size();k++)
                {
                    if(lst_intervals.get(k).isSelected()) {
                        interval = lst_intervals.get(k).getId();
                        lbl_interval.setText(lst_intervals.get(k).getName());
                        break;
                    }
                }

                //interval=rdo_15.isChecked()?15:rdo_30.isChecked()?30:rdo_45.isChecked()?45:60;

                /*String str=rdo_15.isChecked()?rdo_15.getText().toString().trim()
                        :rdo_30.isChecked()?rdo_30.getText().toString().trim()
                        :rdo_45.isChecked()?rdo_45.getText().toString().trim():rdo_60.getText().toString().trim();
                lbl_interval.setText(str);*/

                dialog.dismiss();
            }
        });

        dialog.setContentView(view);

        dialog.show();
    }

    public void openAutoTimePicker2(final AppCompatTextView appCompatTextView, final boolean isFrom)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second)
            {
                String formatedDate = "";
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a", Locale.US);
                Date dt;
                String time = "";

                try
                {
                    if(isFrom)
                    {
                        from_hour=hourOfDay;
                        from_minute=minute;
                    }
                    else
                    {
                        to_hour=hourOfDay;
                        to_minute=minute;
                    }

                    /*if(isValidDate())
                    {*/
                        Log.d("openAutoTimePicker : ", "" + from_hour + "  @@@@  " + from_minute);

                        time = "" + hourOfDay + ":" + minute + ":" + "00";
                        dt = sdf.parse(time);
                        formatedDate = sdfs.format(dt);
                        appCompatTextView.setText("" + formatedDate);
                    /*}
                    else
                    {
                        if(isFrom)
                        {
                            from_hour=8;
                            from_minute=0;
                            appCompatTextView.setText("08:00 AM");
                        }
                        else
                        {
                            to_hour=20;
                            to_minute=0;
                            appCompatTextView.setText("10:00 PM");
                        }


                        ah.customAlert(sh.get_string(R.string.str_from_to_invalid_validation));
                    }*/

                    //setCount();
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        };

        Calendar now = Calendar.getInstance();
        if(isFrom) {
            now.set(Calendar.HOUR_OF_DAY, from_hour);
            now.set(Calendar.MINUTE, from_minute);
        }
        else {
            now.set(Calendar.HOUR_OF_DAY, to_hour);
            now.set(Calendar.MINUTE, to_minute);
        }
        TimePickerDialog tpd;
        if (!DateFormat.is24HourFormat(act)) {
            //12 hrs format
            tpd = TimePickerDialog.newInstance(
                    onTimeSetListener,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE), false);

            tpd.setSelectableTimes(generateTimepoints(23.50, 30));

            tpd.setMaxTime(23, 30, 00);
        } else {
            //24 hrs format
            tpd = TimePickerDialog.newInstance(
                    onTimeSetListener,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE), true);

            tpd.setSelectableTimes(generateTimepoints(23.50, 30));

            tpd.setMaxTime(23, 30, 00);
        }



        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
        tpd.show(act.getFragmentManager(), "Datepickerdialog");
        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
    }

    public boolean isValidDate()
    {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, from_hour);
        startTime.set(Calendar.MINUTE, from_minute);
        startTime.set(Calendar.SECOND,0);

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, to_hour);
        endTime.set(Calendar.MINUTE, to_minute);
        endTime.set(Calendar.SECOND,0);

        // @@@@@
        if(isNextDayEnd())
            endTime.add(Calendar.DATE,1);

        Log.d("isValidDate",""+startTime.getTimeInMillis()+" @@@ "+endTime.getTimeInMillis());

        long mills = endTime.getTimeInMillis() - startTime.getTimeInMillis();

        /*int hours = (int) (mills/(1000 * 60 * 60));
        int mins = hours * 60;*/

        int hours = (int) (mills/(1000 * 60 * 60));
        int mins = (int) ((mills/(1000*60)) % 60);
        float total_minutes=(hours*60)+mins;

        /*ah.Show_Alert_Dialog(""+interval+"  @@@  "+hours+":"+mins+"\n\n"+from_hour+":"+from_minute+"\n\n"
                +"\n\n"+to_hour+":"+to_minute+" \n"+isNextDayEnd()+" @@@ "+mills);*/

        //ah.Show_Alert_Dialog(""+(interval<mins));

        if(interval<=total_minutes)
            return true;


        return false;

        /*Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, from_hour);
        startTime.set(Calendar.MINUTE, from_minute);
        startTime.set(Calendar.SECOND,0);

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, to_hour);
        endTime.set(Calendar.MINUTE, to_minute);
        endTime.set(Calendar.SECOND,0);

        if(startTime.getTimeInMillis()<endTime.getTimeInMillis())
        {
            return true;
        }

        return false;*/
    }

    public void deleteAutoAlarm(boolean alsoData)
    {
        ArrayList<HashMap<String,String>> arr_data = dh.getdata("tbl_alarm_details","AlarmType='R'");

        for(int k=0;k<arr_data.size();k++)
        {
            MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("AlarmId")));

            ArrayList<HashMap<String,String>> arr_data2 = dh.getdata("tbl_alarm_sub_details","SuperId="+arr_data.get(k).get("id"));
            for(int j=0;j<arr_data2.size();j++)
                MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data2.get(j).get("AlarmId")));

            if(alsoData) {
                //dh.REMOVE("tbl_alarm_details","AlarmType='R'");
                dh.REMOVE("tbl_alarm_details","id="+arr_data.get(k).get("id"));
                dh.REMOVE("tbl_alarm_sub_details","SuperId="+arr_data.get(k).get("id"));
            }

        }
    }

    public boolean isNextDayEnd()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a",Locale.US);

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat.parse(lbl_wakeup_time.getText().toString().trim());
            date2 = simpleDateFormat.parse(lbl_bed_time.getText().toString().trim());

            if(date1.getTime()>date2.getTime())
                return true;
            else
                return false;
        }
        catch (Exception e){}

        return false;
    }

    public void setAutoAlarm(boolean moveScreen)
    {
        int minute_interval=interval;

        if(sh.check_blank_data(lbl_wakeup_time.getText().toString()) ||
                sh.check_blank_data(lbl_bed_time.getText().toString()))
        {
            ah.customAlert(sh.get_string(R.string.str_from_to_invalid_validation));
            return;
        }
        else
        {
            if(!moveScreen)
                load_AutoDataFromDB();

            Log.d("setAutoAlarm :",""+from_hour+":"+from_minute+"  @@@  "+to_hour+":"+to_minute);

            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, from_hour);
            startTime.set(Calendar.MINUTE, from_minute);
            startTime.set(Calendar.SECOND,0);

            Calendar endTime = Calendar.getInstance();
            endTime.set(Calendar.HOUR_OF_DAY, to_hour);
            endTime.set(Calendar.MINUTE, to_minute);
            endTime.set(Calendar.SECOND,0);

            if(isNextDayEnd())
                endTime.add(Calendar.DATE,1);

            if(startTime.getTimeInMillis()<endTime.getTimeInMillis())
            {
                deleteAutoAlarm(true);

                int _id = (int) System.currentTimeMillis();

                ContentValues initialValues = new ContentValues();
                initialValues.put("AlarmTime", ""+ lbl_wakeup_time.getText().toString() +" - "+lbl_bed_time.getText().toString());
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
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.US);
                        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a",Locale.US);
                        Date dt;
                        String time = "";

                        time = startTime.get(Calendar.HOUR_OF_DAY)+":"+startTime.get(Calendar.MINUTE)+":"+startTime.get(Calendar.SECOND);
                        dt = sdf.parse(time);
                        formatedDate = sdfs.format(dt);

                        //if(!dh.IS_EXISTS("tbl_alarm_details","AlarmTime='"+formatedDate+"'") && !dh.IS_EXISTS("tbl_alarm_sub_details","AlarmTime='"+formatedDate+"'"))
                        //{
                            //setRecurringAlarm(mContext,startTime,_id);
                            MyAlarmManager.scheduleAutoRecurringAlarm(mContext,startTime,_id);

                            ContentValues initialValues2 = new ContentValues();
                            initialValues2.put("AlarmTime", "" + formatedDate);
                            initialValues2.put("AlarmId", "" + _id);
                            initialValues2.put("SuperId", "" + getLastId);
                            dh.INSERT("tbl_alarm_sub_details", initialValues2);

                        //}
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    startTime.add(Calendar.MINUTE, minute_interval);
                }

                if(moveScreen) {
                    //ah.customAlert(sh.get_string(R.string.str_successfully_set_alarm));
                    finish();
                }
            }
            else
            {
                ah.customAlert(sh.get_string(R.string.str_from_to_invalid_validation));
                return;
            }
        }
    }

    public void openEditTimePicker(final AlarmModel alarmtime)
    {
        int tmp_from_hour=Integer.parseInt(""+
                dth.FormateDateFromString("hh:mm a","HH",alarmtime.getDrinkTime().trim()));
        int tmp_from_minute=Integer.parseInt(""+
                dth.FormateDateFromString("hh:mm a","mm",alarmtime.getDrinkTime().trim()));


        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second)
            {

                String formatedDate = "";
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.US);
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a",Locale.US);
                Date dt;
                String time = "";

                try
                {
                    time = "" + hourOfDay + ":" + minute + ":" + "00";
                    dt = sdf.parse(time);
                    formatedDate = sdfs.format(dt);

                    //if(!dh.IS_EXISTS("tbl_alarm_details","AlarmTime='"+formatedDate+"'") && !dh.IS_EXISTS("tbl_alarm_sub_details","AlarmTime='"+formatedDate+"'"))
                    if(!dh.IS_EXISTS("tbl_alarm_details","AlarmTime='"+formatedDate+"'"))
                    {
                        //int _id = Integer.parseInt(""+alarmtime.getAlarmId());

                        ContentValues initialValues = new ContentValues();
                        initialValues.put("AlarmTime", ""+ formatedDate);
                        dh.UPDATE("tbl_alarm_details", initialValues,"id="+alarmtime.getId());

                        int _id_sunday = Integer.parseInt(""+alarmtime.getAlarmSundayId());
                        int _id_monday = Integer.parseInt(""+alarmtime.getAlarmMondayId());
                        int _id_tuesday = Integer.parseInt(""+alarmtime.getAlarmTuesdayId());
                        int _id_wednesday = Integer.parseInt(""+alarmtime.getAlarmWednesdayId());
                        int _id_thursday = Integer.parseInt(""+alarmtime.getAlarmThursdayId());
                        int _id_friday = Integer.parseInt(""+alarmtime.getAlarmFridayId());
                        int _id_saturday = Integer.parseInt(""+alarmtime.getAlarmSaturdayId());


                        if(alarmtime.getSunday()==1)
                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.SUNDAY,hourOfDay,minute,_id_sunday);

                        if(alarmtime.getMonday()==1)
                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.MONDAY,hourOfDay,minute,_id_monday);

                        if(alarmtime.getTuesday()==1)
                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.TUESDAY,hourOfDay,minute,_id_tuesday);

                        if(alarmtime.getWednesday()==1)
                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.WEDNESDAY,hourOfDay,minute,_id_wednesday);

                        if(alarmtime.getThursday()==1)
                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.THURSDAY,hourOfDay,minute,_id_thursday);

                        if(alarmtime.getFriday()==1)
                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.FRIDAY,hourOfDay,minute,_id_friday);

                        if(alarmtime.getSaturday()==1)
                            MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                    Calendar.SATURDAY,hourOfDay,minute,_id_saturday);

                        //setRecurringAlarm(mContext,hourOfDay,minute,_id);

                        load_alarm();

                        alarmAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        ah.customAlert(sh.get_string(R.string.str_set_alarm_validation));
                    }

                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        };

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, tmp_from_hour);
        now.set(Calendar.MINUTE, tmp_from_minute);
        TimePickerDialog tpd;
        if (!DateFormat.is24HourFormat(act)) {
            //12 hrs format
            tpd = TimePickerDialog.newInstance(
                    onTimeSetListener,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE), false);

            tpd.setSelectableTimes(generateTimepoints(23.50, 15));

            tpd.setMaxTime(23, 30, 00);
        } else {
            //24 hrs format
            tpd = TimePickerDialog.newInstance(
                    onTimeSetListener,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE), true);

            tpd.setSelectableTimes(generateTimepoints(23.50, 15));

            tpd.setMaxTime(23, 30, 00);
        }

        //tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
        tpd.show(act.getFragmentManager(), "Datepickerdialog");
        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
    }








    public void loadSounds()
    {
        lst_sounds.clear();

        lst_sounds.add(getSoundModel(0,"Default"));
        lst_sounds.add(getSoundModel(1,"Bell"));
        lst_sounds.add(getSoundModel(2,"Blop"));
        lst_sounds.add(getSoundModel(3,"Bong"));
        lst_sounds.add(getSoundModel(4,"Click"));
        lst_sounds.add(getSoundModel(5,"Echo droplet"));
        lst_sounds.add(getSoundModel(6,"Mario droplet"));
        lst_sounds.add(getSoundModel(7,"Ship bell"));
        lst_sounds.add(getSoundModel(8,"Simple droplet"));
        lst_sounds.add(getSoundModel(9,"Tiny droplet"));

    }

    public SoundModel getSoundModel(int index,String name)
    {
        SoundModel soundModel=new SoundModel();
        soundModel.setId(index);
        soundModel.setName(name);
        soundModel.isSelected(ph.getInt(URLFactory.REMINDER_SOUND)==index);

        return soundModel;
    }

    public void openSoundMenuPicker()
    {
        bottomSheetDialogSound=new BottomSheetDialog(act);

        LayoutInflater layoutInflater=LayoutInflater.from(act);
        View view = layoutInflater.inflate(R.layout.dialog_sound_pick, null, false);

        AppCompatTextView btnCancel=view.findViewById(R.id.btnCancel);
        RecyclerView soundRecyclerView=view.findViewById(R.id.soundRecyclerView);




        soundAdapter = new SoundAdapter(act, lst_sounds, new SoundAdapter.CallBack() {
            @Override
            public void onClickSelect(SoundModel time, int position) {
                for(int k=0;k<lst_sounds.size();k++)
                {
                    lst_sounds.get(k).isSelected(false);
                }

                lst_sounds.get(position).isSelected(true);
                soundAdapter.notifyDataSetChanged();

                ph.savePreferences(URLFactory.REMINDER_SOUND,position);

                if(position>0)
                    playSound(position);
            }
        });


        soundRecyclerView.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));

        soundRecyclerView.setAdapter(soundAdapter);



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogSound.dismiss();
            }
        });

        bottomSheetDialogSound.setContentView(view);

        bottomSheetDialogSound.show();
    }

    public void playSound(int idx)
    {
        MediaPlayer mp = null;

        if(idx==1)
            mp = MediaPlayer.create(this, R.raw.bell);
        else if(idx==2)
            mp = MediaPlayer.create(this, R.raw.blop);
        else if(idx==3)
            mp = MediaPlayer.create(this, raw.bong);
        else if(idx==4)
            mp = MediaPlayer.create(this, raw.click);
        else if(idx==5)
            mp = MediaPlayer.create(this, raw.echo_droplet);
        else if(idx==6)
            mp = MediaPlayer.create(this, raw.mario_droplet);
        else if(idx==7)
            mp = MediaPlayer.create(this, raw.ship_bell);
        else if(idx==8)
            mp = MediaPlayer.create(this, raw.simple_droplet);
        else if(idx==9)
            mp = MediaPlayer.create(this, raw.tiny_droplet);

        mp.start();
    }

    public void load_alarm()
    {
        alarmModelList.clear();

        ArrayList<HashMap<String, String>>  arr_data=dh.getdata("tbl_alarm_details","AlarmType='M'");

        for(int k=0;k<arr_data.size();k++)
        {
            AlarmModel alarmModel=new AlarmModel();
            alarmModel.setDrinkTime(arr_data.get(k).get("AlarmTime"));
            alarmModel.setId(arr_data.get(k).get("id"));
            alarmModel.setAlarmId(arr_data.get(k).get("AlarmId"));
            alarmModel.setAlarmType(arr_data.get(k).get("AlarmType"));
            alarmModel.setAlarmInterval(arr_data.get(k).get("AlarmInterval"));

            alarmModel.setIsOff(Integer.parseInt(arr_data.get(k).get("IsOff")));
            alarmModel.setSunday(Integer.parseInt(arr_data.get(k).get("Sunday")));
            alarmModel.setMonday(Integer.parseInt(arr_data.get(k).get("Monday")));
            alarmModel.setTuesday(Integer.parseInt(arr_data.get(k).get("Tuesday")));
            alarmModel.setWednesday(Integer.parseInt(arr_data.get(k).get("Wednesday")));
            alarmModel.setThursday(Integer.parseInt(arr_data.get(k).get("Thursday")));
            alarmModel.setFriday(Integer.parseInt(arr_data.get(k).get("Friday")));
            alarmModel.setSaturday(Integer.parseInt(arr_data.get(k).get("Saturday")));

            alarmModel.setAlarmSundayId(arr_data.get(k).get("SundayAlarmId"));
            alarmModel.setAlarmMondayId(arr_data.get(k).get("MondayAlarmId"));
            alarmModel.setAlarmTuesdayId(arr_data.get(k).get("TuesdayAlarmId"));
            alarmModel.setAlarmWednesdayId(arr_data.get(k).get("WednesdayAlarmId"));
            alarmModel.setAlarmThursdayId(arr_data.get(k).get("ThursdayAlarmId"));
            alarmModel.setAlarmFridayId(arr_data.get(k).get("FridayAlarmId"));
            alarmModel.setAlarmSaturdayId(arr_data.get(k).get("SaturdayAlarmId"));

            alarmModelList.add(alarmModel);
        }

        if(alarmModelList.size()>0)
            lbl_no_record_found.setVisibility(View.GONE);
        else
            lbl_no_record_found.setVisibility(View.VISIBLE);

    }

    public void openTimePicker()
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second)
            {

                 String formatedDate = "";
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.US);
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a",Locale.US);
                Date dt;
                String time = "";

               try
                {
                    time = "" + hourOfDay + ":" + minute + ":" + "00";
                    dt = sdf.parse(time);
                    formatedDate = sdfs.format(dt);

                    //if(!dh.IS_EXISTS("tbl_alarm_details","AlarmTime='"+formatedDate+"'") && !dh.IS_EXISTS("tbl_alarm_sub_details","AlarmTime='"+formatedDate+"'"))
                    if(!dh.IS_EXISTS("tbl_alarm_details","AlarmTime='"+formatedDate+"'"))
                    {
                        int _id = (int) System.currentTimeMillis();

                        int _id_sunday = (int) System.currentTimeMillis();

                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.SUNDAY,hourOfDay,minute,_id_sunday);

                        int _id_monday = (int) System.currentTimeMillis();

                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.MONDAY,hourOfDay,minute,_id_monday);

                        int _id_tuesday = (int) System.currentTimeMillis();

                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.TUESDAY,hourOfDay,minute,_id_tuesday);

                        int _id_wednesday = (int) System.currentTimeMillis();

                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.WEDNESDAY,hourOfDay,minute,_id_wednesday);

                        int _id_thursday = (int) System.currentTimeMillis();

                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.THURSDAY,hourOfDay,minute,_id_thursday);

                        int _id_friday = (int) System.currentTimeMillis();

                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.FRIDAY,hourOfDay,minute,_id_friday);

                        int _id_saturday = (int) System.currentTimeMillis();

                        MyAlarmManager.scheduleManualRecurringAlarm(mContext,
                                Calendar.SATURDAY,hourOfDay,minute,_id_saturday);


                        ContentValues initialValues = new ContentValues();
                        initialValues.put("AlarmTime", ""+ formatedDate);
                        initialValues.put("AlarmId", ""+ _id);

                        initialValues.put("SundayAlarmId", ""+ _id_sunday);
                        initialValues.put("MondayAlarmId", ""+ _id_monday);
                        initialValues.put("TuesdayAlarmId", ""+ _id_tuesday);
                        initialValues.put("WednesdayAlarmId", ""+ _id_wednesday);
                        initialValues.put("ThursdayAlarmId", ""+ _id_thursday);
                        initialValues.put("FridayAlarmId", ""+ _id_friday);
                        initialValues.put("SaturdayAlarmId", ""+ _id_saturday);

                        initialValues.put("AlarmType", "M");
                        initialValues.put("AlarmInterval", "0");
                        dh.INSERT("tbl_alarm_details", initialValues);

                        //setRecurringAlarm(mContext,hourOfDay,minute,_id);

                        load_alarm();

                        alarmAdapter.notifyDataSetChanged();

                        ah.customAlert(sh.get_string(string.str_successfully_set_alarm));
                    }
                    else
                    {
                        ah.customAlert(sh.get_string(R.string.str_set_alarm_validation));
                    }

                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }

            }
        };

        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd;
        if (!DateFormat.is24HourFormat(act)) {
            //12 hrs format
            tpd = TimePickerDialog.newInstance(
                    onTimeSetListener,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE), false);

            tpd.setSelectableTimes(generateTimepoints(23.50, 15));

            tpd.setMaxTime(23, 30, 00);
        } else {
            //24 hrs format
            tpd = TimePickerDialog.newInstance(
                    onTimeSetListener,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE), true);

            tpd.setSelectableTimes(generateTimepoints(23.50, 15));

            tpd.setMaxTime(23, 30, 00);
        }

        //tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
        tpd.show(act.getFragmentManager(), "Datepickerdialog");
        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
    }

    public static Timepoint[] generateTimepoints(double maxHour, int minutesInterval) {

        int lastValue = (int) (maxHour * 60);

        List<Timepoint> timepoints = new ArrayList<>();

        for (int minute = 0; minute <= lastValue; minute += minutesInterval) {
            int currentHour = minute / 60;
            int currentMinute = minute - (currentHour > 0 ? (currentHour * 60) : 0);
            if (currentHour == 24)
                continue;
            timepoints.add(new Timepoint(currentHour, currentMinute));
        }
        return timepoints.toArray(new Timepoint[timepoints.size()]);
    }
}