package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.IdRes;
import androidx.appcompat.widget.AppCompatTextView;
import com.waterdiary.drinkreminder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_notifisettings extends MasterBaseActivity {
    AppCompatTextView title;
    ImageView back;
    RadioGroup radioGroup1;
    RadioGroup radioGroup2;
    String noti_type;
    int interval;
    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_notif_setting);
        confirm = findViewById(R.id.button2);
        title = findViewById(R.id.lbl_toolbar_title);
        title.setText("Notification Settings");
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act, Screen_Dashboard.class);
                startActivity(intent);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // No session user
            Toast.makeText(handbook_notifisettings.this, "Please log in to save data", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = user.getUid();
        //Example you need save a Store in
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userdata = database.getReference("UserData").child(userId);

        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radioGroup2.setOnCheckedChangeListener(radioGroupButtonChangeListener2);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdata.child("noti_type").setValue(noti_type);
                userdata.child("interval").setValue(interval);
                Intent intent = new Intent(handbook_notifisettings.this, Screen_Dashboard.class);
                startActivity(intent);
            }
        });


    }
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.rdo_15){
                interval = 15;
            }
            else if(i == R.id.rdo_20){
                interval = 20;
            }
            else if(i == R.id.rdo_30){
                interval = 30;
            }
            else if(i == R.id.rdo_35){
                interval = 35;
            }
        }
    };
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.rdo_push){
                noti_type = "push";
            }
            else if(i == R.id.rdo_vibrate){
                noti_type = "vibration";
            }
            else if(i == R.id.rdo_LED){
                noti_type = "LED";
            }
        }
    };
}
