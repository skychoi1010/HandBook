package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_tips extends MasterBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_tips);
        TextView Tips = findViewById(R.id.textView13);
        Tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Screen_Dashboard.class);
                startActivity(intent);
            }
        }
        );
    }
}
