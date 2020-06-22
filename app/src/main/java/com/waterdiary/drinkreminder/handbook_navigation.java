package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_navigation extends MasterBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_drawer);
        Button buyco = findViewById(R.id.store);

        buyco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), handbook_store.class);
                startActivity(intent);
            }
        }
        );
    }
}
