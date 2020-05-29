package com.waterdiary.drinkreminder;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_notifisettings extends MasterBaseActivity {
    AppCompatTextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_notif_setting);
        title = findViewById(R.id.lbl_toolbar_title);
        title.setText("Notification Settings");
    }
}
