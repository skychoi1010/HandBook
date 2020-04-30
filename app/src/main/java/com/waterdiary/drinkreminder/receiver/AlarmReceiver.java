package com.waterdiary.drinkreminder.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String DEBUG_TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(DEBUG_TAG, "Recurring alarm; requesting download service.");

        WakeLocker.acquire(context);
        WakeLocker.release();

        //Toast.makeText(context,"Call",1).show();

        String action = ""+intent.getAction();

        if(action.equalsIgnoreCase("SNOOZE_ACTION"))
        {
            //Log.d(DEBUG_TAG, "IF");

            Intent snoozeIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);
            AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarms.setExact(AlarmManager.RTC_WAKEUP,Calendar.getInstance().getTimeInMillis() + 2 * 60000, snoozePendingIntent);

            NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(0);

        }
        else if(action.equals("android.intent.action.BOOT_COMPLETED"))
        {
            //Toast.makeText(context,"BOOT_COMPLETED",1).show();

            AlarmHelper alarmHelper = new AlarmHelper(context);
            alarmHelper.createAlarm();
        }
        else
        {
            //Log.d(DEBUG_TAG, "ELSE");

            NotificationHelper notificationHelper = new NotificationHelper(context);
            notificationHelper.createNotification();
        }

        /*if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.SetAlarm(context);
        }*/
    }

}