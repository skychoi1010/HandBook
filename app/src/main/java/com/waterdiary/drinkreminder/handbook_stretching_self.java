package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;

import java.util.Random;

public class handbook_stretching_self extends MasterBaseActivity {
    AppCompatTextView title;
    ImageView back, stretch_img;
    CountDownTimer cdTimer = null;
    int value = 5;
    AppCompatTextView time, start, next, skip;
    String current;

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

        time = findViewById(R.id.timer);
        stretch_img = findViewById(R.id.stretch_img);
        current = RandomStretch();

        start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdTimer.start();
            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setText("Start stretching");
                String temp = RandomStretch();
                time.setText("5");
                start.setText("Start Stretching");
                if(current.equals(temp)){
                    RandomStretch();
                }
            }
        });

        skip = findViewById(R.id.skipThis);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = RandomStretch();
                time.setText("5");
                if(current.equals(temp)){
                    RandomStretch();
                }
            }
        });

        cdTimer = new CountDownTimer(5000, 1000) {

            @Override
            public void onFinish() {
                //TODO : 카운트다운타이머 종료시 처리
                time.setText("5");
                start.setText("Try again");
                value = 5;
            }

            @Override
            public void onTick(long millisUntilFinished) {
                //TODO : 카운트다운타이머 onTick구현
                value--;
                time.setText(Integer.toString(value));
            }

        };

    }

    public String RandomStretch(){
        Random random = new Random();
        int pos = random.nextInt(3);
        String img;
        switch (pos){
            case 0 :
                stretch_img.setImageResource(R.drawable.stretching_can);
                img = "stretching_can";
                return img;
            case 1:
                stretch_img.setImageResource(R.drawable.stretching_finger);
                img = "stretching_finger";
                return img;
            case 2:
                stretch_img.setImageResource(R.drawable.stretching_fist);
                img = "stretching_fist";
                return img;
            case 3:
                stretch_img.setImageResource(R.drawable.stretching_wrist);
                img = "stretching_wrist";
                return img;
            default:
                return null;
        }
    }
}
