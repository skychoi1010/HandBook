package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_stretching_self extends MasterBaseActivity {
    AppCompatTextView title;
    ImageView back;
    CountDownTimer cdTimer = null;
    int value = 10;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_stretch_self);
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

        CountDownTimer timer = new CountDownTimer(6000, 1000) {

            @Override
            public void onFinish() {
                //TODO : 카운트다운타이머 종료시 처리
                finish();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                //TODO : 카운트다운타이머 onTick구현
                value--;
                title.setText(Integer.toString(value));
            }
        };

    }
}
