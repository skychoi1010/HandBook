package com.waterdiary.drinkreminder.receiver;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.basic.appbasiclibs.utils.Constant;
import com.basic.appbasiclibs.utils.Date_Helper;
import com.basic.appbasiclibs.utils.Preferences_Helper;
import com.waterdiary.drinkreminder.model.backuprestore.AlarmDetails;
import com.waterdiary.drinkreminder.model.backuprestore.AlarmSubDetails;
import com.waterdiary.drinkreminder.model.backuprestore.BackupRestore;
import com.waterdiary.drinkreminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

class AlarmHelper
{
    private Context mContext;

    Date_Helper dth=new Date_Helper();
    Preferences_Helper ph;
    List<AlarmDetails> alarmDetailsList=new ArrayList<>();


    AlarmHelper(Context context) {
        mContext = context;
        ph=new Preferences_Helper(mContext);
        Constant.SDB=mContext.openOrCreateDatabase(Constant.DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY,null);
    }

    public void createAlarm()
    {
        loadData();
        setAlarm();
    }

    public void setAlarm()
    {
        BackupRestore backupRestore=new BackupRestore();
        backupRestore.setAlarmDetails(alarmDetailsList);
        backupRestore.isManualReminderActive(ph.getBoolean(URLFactory.IS_MANUAL_REMINDER));

        for(int k=0;k<alarmDetailsList.size();k++)
        {
            if (backupRestore.getAlarmDetails().get(k).getAlarmType().equalsIgnoreCase("M")
                    && backupRestore.isManualReminderActive())
            {
                int hourOfDay = Integer.parseInt("" +
                        dth.FormateDateFromString("hh:mm a", "HH",
                                backupRestore.getAlarmDetails().get(k).getAlarmTime().trim()));
                int minute = Integer.parseInt("" +
                        dth.FormateDateFromString("hh:mm a", "mm",
                                backupRestore.getAlarmDetails().get(k).getAlarmTime().trim()));

                if (backupRestore.getAlarmDetails().get(k).getSunday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.SUNDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmSundayId()));

                if (backupRestore.getAlarmDetails().get(k).getMonday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.MONDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmMondayId()));

                if (backupRestore.getAlarmDetails().get(k).getTuesday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.TUESDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmTuesdayId()));

                if (backupRestore.getAlarmDetails().get(k).getWednesday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.WEDNESDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmWednesdayId()));

                if (backupRestore.getAlarmDetails().get(k).getThursday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.THURSDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmThursdayId()));

                if (backupRestore.getAlarmDetails().get(k).getFriday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.FRIDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmFridayId()));

                if (backupRestore.getAlarmDetails().get(k).getSaturday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.SATURDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmSaturdayId()));
            }

            List<AlarmSubDetails> alarmSubDetailsList=backupRestore.getAlarmDetails().get(k).getAlarmSubDetails();

            for(int j=0;j<alarmSubDetailsList.size();j++)
            {
                if(!backupRestore.isManualReminderActive()) {

                    int hourOfDay = Integer.parseInt("" +
                            dth.FormateDateFromString("hh:mm a", "HH",
                                    alarmSubDetailsList.get(j).getAlarmTime().trim()));
                    int minute = Integer.parseInt("" +
                            dth.FormateDateFromString("hh:mm a", "mm",
                                    alarmSubDetailsList.get(j).getAlarmTime().trim()));

                    Calendar updateTime = Calendar.getInstance();
                    updateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    updateTime.set(Calendar.MINUTE, minute);
                    updateTime.set(Calendar.SECOND,0);

                    MyAlarmManager.scheduleAutoRecurringAlarm(mContext, updateTime, Integer.parseInt("" + alarmSubDetailsList.get(j).getAlarmId()));
                }
            }
        }
    }

    public void loadData()
    {
        ArrayList<HashMap<String,String>> arr_data = getdata("tbl_alarm_details");

        alarmDetailsList.clear();

        for(int k=0;k<arr_data.size();k++)
        {
            AlarmDetails alarmDetails=new AlarmDetails();
            alarmDetails.setAlarmId(arr_data.get(k).get("AlarmId"));
            alarmDetails.setAlarmInterval(arr_data.get(k).get("AlarmInterval"));
            alarmDetails.setAlarmTime(arr_data.get(k).get("AlarmTime"));
            alarmDetails.setAlarmType(arr_data.get(k).get("AlarmType"));
            alarmDetails.setId(arr_data.get(k).get("id"));

            alarmDetails.setAlarmSundayId(arr_data.get(k).get("SundayAlarmId"));
            alarmDetails.setAlarmMondayId(arr_data.get(k).get("MondayAlarmId"));
            alarmDetails.setAlarmTuesdayId(arr_data.get(k).get("TuesdayAlarmId"));
            alarmDetails.setAlarmWednesdayId(arr_data.get(k).get("WednesdayAlarmId"));
            alarmDetails.setAlarmThursdayId(arr_data.get(k).get("ThursdayAlarmId"));
            alarmDetails.setAlarmFridayId(arr_data.get(k).get("FridayAlarmId"));
            alarmDetails.setAlarmSaturdayId(arr_data.get(k).get("SaturdayAlarmId"));

            alarmDetails.setIsOff(Integer.parseInt(arr_data.get(k).get("IsOff")));
            alarmDetails.setSunday(Integer.parseInt(arr_data.get(k).get("Sunday")));
            alarmDetails.setMonday(Integer.parseInt(arr_data.get(k).get("Monday")));
            alarmDetails.setTuesday(Integer.parseInt(arr_data.get(k).get("Tuesday")));
            alarmDetails.setWednesday(Integer.parseInt(arr_data.get(k).get("Wednesday")));
            alarmDetails.setThursday(Integer.parseInt(arr_data.get(k).get("Thursday")));
            alarmDetails.setFriday(Integer.parseInt(arr_data.get(k).get("Friday")));
            alarmDetails.setSaturday(Integer.parseInt(arr_data.get(k).get("Saturday")));


            List<AlarmSubDetails> alarmSubDetailsList=new ArrayList<>();

            ArrayList<HashMap<String,String>> arr_data2 = getdata("tbl_alarm_sub_details","SuperId="+arr_data.get(k).get("id"));

            Log.d("arr_data2 : ",""+arr_data2.size());

            for(int j=0;j<arr_data2.size();j++)
            {
                AlarmSubDetails alarmSubDetails=new AlarmSubDetails();
                alarmSubDetails.setAlarmId(arr_data2.get(j).get("AlarmId"));
                alarmSubDetails.setAlarmTime(arr_data2.get(j).get("AlarmTime"));
                alarmSubDetails.setId(arr_data2.get(j).get("id"));
                alarmSubDetails.setSuperId(arr_data2.get(j).get("SuperId"));
                alarmSubDetailsList.add(alarmSubDetails);
            }

            alarmDetails.setAlarmSubDetails(alarmSubDetailsList);

            alarmDetailsList.add(alarmDetails);
        }


    }


    public ArrayList<HashMap<String, String>> getdata(String table_name)
    {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String,String>>();

        String query = "SELECT * FROM "+table_name;

        Cursor c = Constant.SDB.rawQuery(query, null);

        if(c.moveToFirst())
        {
            do
            {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<c.getColumnCount();i++)
                {
                    map.put(c.getColumnName(i), c.getString(i));
                }

                maplist.add(map);
            }
            while (c.moveToNext());
        }

        return maplist;
    }

    public ArrayList<HashMap<String, String>> getdata(String table_name, String where_con)
    {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String,String>>();

        String query = "SELECT * FROM "+table_name;

        query+=" where "+where_con;

        Cursor c = Constant.SDB.rawQuery(query, null);

        System.out.println("SELECT QUERY : "+ query);

        if(c.moveToFirst())
        {
            do
            {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<c.getColumnCount();i++)
                {
                    map.put(c.getColumnName(i), c.getString(i));
                }

                maplist.add(map);
            }
            while (c.moveToNext());
        }

        return maplist;
    }
}