package com.waterdiary.drinkreminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.View;
import android.widget.RelativeLayout;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;
import com.waterdiary.drinkreminder.receiver.AlarmReceiver;

import java.util.Calendar;

public class Screen_Select_Snooze extends MasterBaseActivity
{
    AppCompatTextView one,two,three;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_select_snooze);
        getWindow().setLayout(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);

        NotificationManager notificationManager =(NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);

        FindViewById();
        Body();
    }

    private void FindViewById()
    {
        one=findViewById(R.id.one);
        two=findViewById(R.id.two);
        three=findViewById(R.id.three);
    }

    private void Body()
    {
        one.setText("5 "+sh.get_string(R.string.str_minutes));
        two.setText("10 "+sh.get_string(R.string.str_minutes));
        three.setText("15 "+sh.get_string(R.string.str_minutes));

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSnooze(5);
                finish();
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSnooze(10);
                finish();
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSnooze(15);
                finish();
            }
        });
    }

    public void setSnooze(int minutes)
    {
        Intent snoozeIntent = new Intent(act, AlarmReceiver.class);
        PendingIntent snoozePendingIntent =PendingIntent.getBroadcast(act, 0, snoozeIntent, 0);
        AlarmManager alarms = (AlarmManager) act.getSystemService(Context.ALARM_SERVICE);
        alarms.setExact(AlarmManager.RTC_WAKEUP,Calendar.getInstance().getTimeInMillis() + minutes * 60000, snoozePendingIntent);


    }
}