package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_healthtips extends MasterBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_healthtips);
        TextView Tips = findViewById(R.id.healthTips);
        TextView News = findViewById(R.id.news);
        Tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), handbook_tips.class);
                startActivity(intent);
            }
        }
        );
        News.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), handbook_news.class);
                startActivity(intent2);
                                    }
        }
        );
    }
}
