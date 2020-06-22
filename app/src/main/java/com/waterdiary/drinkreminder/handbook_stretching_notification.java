package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_stretching_notification extends MasterBaseActivity {
    AppCompatTextView title;
    ImageView back;
    AppCompatTextView gameStretch, selfStretch, skipNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_stretching_choice);
        title = findViewById(R.id.lbl_toolbar_title);
        title.setText("");
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act, Screen_Dashboard.class);
                startActivity(intent);
            }
        });
        gameStretch = findViewById(R.id.gameStretch);
        gameStretch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act, Screen_Dashboard.class);
                startActivity(intent);
            }
        });
        selfStretch = findViewById(R.id.selfStretch);
        selfStretch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act, handbook_stretching_self.class);
                startActivity(intent);
            }
        });
        skipNow = findViewById(R.id.skipNow);
        skipNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}
