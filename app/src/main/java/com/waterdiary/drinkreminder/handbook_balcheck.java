package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_balcheck extends MasterBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_balcheck);
        Button buyco = findViewById(R.id.buy_co);

        buyco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.handbook_balcheck);
                Intent intent = new Intent(getApplicationContext(), handbook_store.class);
                startActivity(intent);
            }
        });
    }
}
