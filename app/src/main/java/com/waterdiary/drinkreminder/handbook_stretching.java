package com.waterdiary.drinkreminder;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_stretching extends MasterBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_stretch_drawer);
        AppCompatTextView title = findViewById(R.id.lbl_toolbar_title);
        title.setText("Do More Stretching");
    }
}
