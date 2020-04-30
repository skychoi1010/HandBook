package com.waterdiary.drinkreminder.receiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.basic.appbasiclibs.utils.Constant;
import com.basic.appbasiclibs.utils.Date_Helper;
import com.basic.appbasiclibs.utils.Preferences_Helper;
import com.waterdiary.drinkreminder.R;
import com.waterdiary.drinkreminder.Screen_Dashboard;
import com.waterdiary.drinkreminder.Screen_Select_Bottle;
import com.waterdiary.drinkreminder.Screen_Select_Snooze;
import com.waterdiary.drinkreminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.HashMap;

class NotificationHelper {

    private Context mContext;
    private static final String NOTIFICATION_CHANNEL_ID = "10001";
    private static final String NOTIFICATION_SILENT_CHANNEL_ID = "10002";
    private static final String NOTIFICATION_VIBRATE_CHANNEL_ID = "10003";
    private static final String NOTIFICATION_SILENT_VIBRATE_CHANNEL_ID = "10004";

    Date_Helper dth=new Date_Helper();
    Preferences_Helper ph;

    NotificationHelper(Context context) {
        mContext = context;
        ph=new Preferences_Helper(mContext);

        if(URLFactory.notification_ringtone==null)
            URLFactory.notification_ringtone=RingtoneManager.getRingtone(mContext, getSound());
    }

    void createNotification()
    {

        //Toast.makeText(mContext,""+ph.getInt(URLFactory.REMINDER_OPTION),1).show();

        Log.d("createNotification",""+ph.getInt(URLFactory.REMINDER_OPTION));
        Log.d("createNotification V",""+ph.getBoolean(URLFactory.REMINDER_VIBRATE));



        if(ph.getInt(URLFactory.REMINDER_OPTION)==1)
            return;

        if(reachedDailyGoal() && ph.getBoolean(URLFactory.DISABLE_NOTIFICATION))
            return;


        Intent intent = new Intent(mContext , Screen_Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,0 , intent,PendingIntent.FLAG_UPDATE_CURRENT);


        /*Intent snoozeIntent = new Intent(mContext, AlarmReceiver.class);
        snoozeIntent.setAction("SNOOZE_ACTION");
        PendingIntent snoozePendingIntent =PendingIntent.getBroadcast(mContext, 0, snoozeIntent, 0);*/

        Intent snoozeIntent = new Intent(mContext, Screen_Select_Snooze.class);
        snoozeIntent.setAction("SNOOZE_ACTION");
        snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent snoozePendingIntent =PendingIntent.getActivity(mContext, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent addWaterIntent = new Intent(mContext, Screen_Select_Bottle.class);
        addWaterIntent.setAction("ADD_WATER_ACTION");
        addWaterIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent addWaterPendingIntent =PendingIntent.getActivity(mContext, 0, addWaterIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        /*String KEY_REPLY = "key_reply";
        String replyLabel = "Enter your reply here";

        //Initialise RemoteInput
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REPLY).setLabel(replyLabel).build();

        //Notification Action with RemoteInput instance added.
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(android.R.drawable.sym_action_chat, "Snooze", resultPendingIntent)
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();*/


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.drawable.ic_small_app_icon);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
        mBuilder.setContentTitle(mContext.getResources().getString(R.string.str_drink_water))
                .setContentText(""+get_today_report())
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

        //.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)

        /*if(ph.getInt(URLFactory.REMINDER_OPTION)==0)
            mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        else {
            mBuilder.setSound(null);
            //mBuilder.setDefaults(0);
        }*/

        if(ph.getInt(URLFactory.REMINDER_OPTION)==0 && !ph.getBoolean(URLFactory.REMINDER_VIBRATE))
        {
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O)
            {
                mBuilder.setSound(getSound());
            }
        }
        else if(ph.getInt(URLFactory.REMINDER_OPTION)==0 && ph.getBoolean(URLFactory.REMINDER_VIBRATE))
        {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O)
            {
                mBuilder.setSound(getSound());
            }
        }
        else if(ph.getInt(URLFactory.REMINDER_OPTION)==2 && !ph.getBoolean(URLFactory.REMINDER_VIBRATE))
        {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O)
            {
                mBuilder.setSound(null);
            }
        }
        else
        {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O)
            {
                mBuilder.setSound(null);
            }
        }

        mBuilder.addAction(R.drawable.ic_plus,  mContext.getResources().getString(R.string.str_add_water),addWaterPendingIntent);
        mBuilder.addAction(R.drawable.ic_notification, mContext.getResources().getString(R.string.str_snooze),snoozePendingIntent);




        //mBuilder.addAction(replyAction);

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            mNotificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.deleteNotificationChannel(NOTIFICATION_VIBRATE_CHANNEL_ID);

            if(ph.getInt(URLFactory.REMINDER_OPTION)==0)
            {
                if(!ph.getBoolean(URLFactory.REMINDER_VIBRATE))
                {

                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Reminder", importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.setSound(null, null);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});


                    assert mNotificationManager != null;
                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);

                    /*AudioAttributes att = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build();
                    notificationChannel.setSound(getSound(),att);*/





                    mNotificationManager.createNotificationChannel(notificationChannel);

                }
                else {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_VIBRATE_CHANNEL_ID, "Vibrate Reminder", importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setSound(null, null);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(false);
                    notificationChannel.setVibrationPattern(new long[]{0});

                    /*AudioAttributes att = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build();
                    notificationChannel.setSound(getSound(),att);*/


                    assert mNotificationManager != null;
                    mBuilder.setChannelId(NOTIFICATION_VIBRATE_CHANNEL_ID);

                    mNotificationManager.createNotificationChannel(notificationChannel);
                }

                try {
                    //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    //Ringtone r = RingtoneManager.getRingtone(mContext, getSound());
                    //r.play();

                    if(!URLFactory.notification_ringtone.isPlaying())
                        URLFactory.notification_ringtone.play();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else
            {
                if(!ph.getBoolean(URLFactory.REMINDER_VIBRATE)) {
                    NotificationChannel channel_none = new NotificationChannel(NOTIFICATION_SILENT_CHANNEL_ID, "Silent Reminder", NotificationManager.IMPORTANCE_HIGH);
                    channel_none.setSound(null, null);
                    channel_none.enableVibration(true);
                    channel_none.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                    assert mNotificationManager != null;
                    mBuilder.setChannelId(NOTIFICATION_SILENT_CHANNEL_ID);
                    mNotificationManager.createNotificationChannel(channel_none);
                }
                else
                {
                    NotificationChannel channel_none = new NotificationChannel(NOTIFICATION_SILENT_VIBRATE_CHANNEL_ID, "Silent-Vibrate Reminder", NotificationManager.IMPORTANCE_HIGH);
                    channel_none.setSound(null, null);
                    channel_none.enableVibration(false);
                    channel_none.setVibrationPattern(new long[]{0});
                    assert mNotificationManager != null;
                    mBuilder.setChannelId(NOTIFICATION_SILENT_VIBRATE_CHANNEL_ID);
                    mNotificationManager.createNotificationChannel(channel_none);
                }
            }
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }

    public Uri getSound()
    {
        Uri uri=Settings.System.DEFAULT_NOTIFICATION_URI;

        Log.d("getSound",""+ph.getInt(URLFactory.REMINDER_SOUND));

        if(ph.getInt(URLFactory.REMINDER_SOUND)==1)
            uri=Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.bell);
        else if(ph.getInt(URLFactory.REMINDER_SOUND)==2)
            uri=Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.blop);
        else if(ph.getInt(URLFactory.REMINDER_SOUND)==3)
            uri=Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.bong);
        else if(ph.getInt(URLFactory.REMINDER_SOUND)==4)
            uri=Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.click);
        else if(ph.getInt(URLFactory.REMINDER_SOUND)==5)
            uri=Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.echo_droplet);
        else if(ph.getInt(URLFactory.REMINDER_SOUND)==6)
            uri=Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.mario_droplet);
        else if(ph.getInt(URLFactory.REMINDER_SOUND)==7)
            uri=Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.ship_bell);
        else if(ph.getInt(URLFactory.REMINDER_SOUND)==8)
            uri=Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.simple_droplet);
        else if(ph.getInt(URLFactory.REMINDER_SOUND)==9)
            uri=Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.tiny_droplet);

        return uri;
    }

    public boolean reachedDailyGoal()
    {
        Constant.SDB=mContext.openOrCreateDatabase(Constant.DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY,null);

        if(ph.getFloat(URLFactory.DAILY_WATER)==0)
        {
            URLFactory.DAILY_WATER_VALUE=2500;
        }
        else
        {
            URLFactory.DAILY_WATER_VALUE=ph.getFloat(URLFactory.DAILY_WATER);
        }

        ArrayList<HashMap<String, String>> arr_data=getdata("tbl_drink_details","DrinkDate ='"+dth.getCurrentDate("dd-MM-yyyy")+"'");

        float drink_water=0;
        for(int k=0;k<arr_data.size();k++)
        {
            if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
                drink_water+=Double.parseDouble(arr_data.get(k).get("ContainerValue"));
            else
                drink_water+=Double.parseDouble(arr_data.get(k).get("ContainerValueOZ"));
        }

        if(drink_water>=URLFactory.DAILY_WATER_VALUE)
            return true;
        else
            return false;
    }

    @SuppressLint("WrongConstant")
    public String get_today_report()
    {
        Constant.SDB=mContext.openOrCreateDatabase(Constant.DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY,null);

        if(ph.getFloat(URLFactory.DAILY_WATER)==0)
        {
            URLFactory.DAILY_WATER_VALUE=2500;
        }
        else
        {
            URLFactory.DAILY_WATER_VALUE=ph.getFloat(URLFactory.DAILY_WATER);
        }

        if(check_blank_data(""+ph.getString(URLFactory.WATER_UNIT)))
        {
            URLFactory.WATER_UNIT_VALUE="ML";
        }
        else
        {
            URLFactory.WATER_UNIT_VALUE=ph.getString(URLFactory.WATER_UNIT);
        }

        ArrayList<HashMap<String, String>> arr_data=getdata("tbl_drink_details","DrinkDate ='"+dth.getCurrentDate("dd-MM-yyyy")+"'");

        float drink_water=0;
        for(int k=0;k<arr_data.size();k++)
        {
            if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
                drink_water+=Double.parseDouble(arr_data.get(k).get("ContainerValue"));
            else
                drink_water+=Double.parseDouble(arr_data.get(k).get("ContainerValueOZ"));
        }

        return mContext.getResources().getString(R.string.str_have_u_had_any_water_yet);

    }

    public boolean check_blank_data(String data)
    {
        if(data.equals("") || data.isEmpty() || data.length()==0 || data.equals("null") || data==null)
            return true;

        return false;
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