package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_notifisettings extends MasterBaseActivity {
    AppCompatTextView title;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_notif_setting);
        title = findViewById(R.id.lbl_toolbar_title);
        title.setText("Notification Settings");
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act,Screen_Dashboard.class);
                startActivity(intent);
            }
        });
    }
}
