package com.waterdiary.drinkreminder.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class MyAlarmManager
{
    public static void scheduleAutoRecurringAlarm(Context context,Calendar updateTime2,int _id) // for auto
    {
        Calendar updateTime=Calendar.getInstance();
        updateTime.setTimeInMillis(updateTime2.getTimeInMillis());

        if(updateTime.getTimeInMillis() < System.currentTimeMillis()) {
            updateTime.add(Calendar.DATE, 1);
        }

        Intent intentAlarm = new Intent(context, AlarmReceiver.class);

        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent recurringDownload = PendingIntent.getBroadcast(
                context,
                _id,
                intentAlarm,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Log.d("MAINALARMID A :",_id+" @@@ "+updateTime.getTimeInMillis());

        //alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,updateTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY, recurringDownload);
        alarms.setRepeating(AlarmManager.RTC_WAKEUP,updateTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY, recurringDownload);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarms.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,updateTime.getTimeInMillis(), recurringDownload);
        }
        else
            alarms.setExact(AlarmManager.RTC_WAKEUP,updateTime.getTimeInMillis(), recurringDownload);*/

        //alarms.setAndAllowWhileIdle();
    }

    public static void scheduleManualRecurringAlarm(Context context, int dayOfWeek, int hour, int minute, int _id)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        // Check we aren't setting it in the past which would trigger it to fire instantly
        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        /*// Set this to whatever you were planning to do at the given time
        PendingIntent yourIntent;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
        calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, yourIntent);*/


        Intent intentAlarm = new Intent(context, AlarmReceiver.class);

        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent recurringDownload = PendingIntent.getBroadcast(
                context,
                _id,
                intentAlarm,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Log.d("MAINALARMID M :",_id+" @@@ "+calendar.getTimeInMillis());

        //alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY * 7, recurringDownload);
        alarms.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY * 7, recurringDownload);
    }

    public static void cancelRecurringAlarm(Context context,int _id)
    {
        Log.d("MAINALARMID C :",""+_id);

        Intent intentAlarm = new Intent(context, AlarmReceiver.class);

        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                _id,
                intentAlarm,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        alarms.cancel(pendingIntent);
    }

    //https://stackoverflow.com/a/21234044
    //https://stackoverflow.com/a/28545763


    public static void scheduleAutoBackupAlarm(Context context, Calendar updateTime2, int _id, int autoBackupType) // for auto backup
    {
        Calendar updateTime=Calendar.getInstance();
        updateTime.setTimeInMillis(updateTime2.getTimeInMillis());
        if(updateTime.getTimeInMillis() < System.currentTimeMillis())
        {
            updateTime.add(Calendar.DATE, 1);
        }


        Intent intentAlarm = new Intent(context, BackupReceiver.class);

        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent recurringDownload = PendingIntent.getBroadcast(
                context,
                _id,
                intentAlarm,
                PendingIntent.FLAG_UPDATE_CURRENT
        );



        long interval=AlarmManager.INTERVAL_DAY;

        if(autoBackupType==1)
            interval=AlarmManager.INTERVAL_DAY * 7;
        else if(autoBackupType==2)
            interval=AlarmManager.INTERVAL_DAY * 30;

        Log.d("MAINBACKUP A :",_id+" @@@ "+updateTime.getTimeInMillis()+" @@@ "+interval);

        //alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,updateTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY, recurringDownload);
        alarms.setRepeating(AlarmManager.RTC_WAKEUP,updateTime.getTimeInMillis()
                ,interval, recurringDownload);
    }

}
