package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_stretching extends MasterBaseActivity {
    AppCompatTextView title, game, self;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_stretch_drawer);
        title = findViewById(R.id.lbl_toolbar_title);
        title.setText("Do More Stretching");
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act, Screen_Dashboard.class);
                startActivity(intent);
            }
        });

        game = findViewById(R.id.game);
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act, Screen_Dashboard.class);
                startActivity(intent);
            }
        });

        self = findViewById(R.id.self);
        self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act, handbook_stretching_self.class);
                startActivity(intent);
            }
        });

    }
}
