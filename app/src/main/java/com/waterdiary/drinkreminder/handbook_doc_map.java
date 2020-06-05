package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.waterdiary.drinkreminder.worker.handbook_hospitaldata;

import java.util.ArrayList;

public class handbook_doc_map extends AppCompatActivity {
    TextView a,b;
    ArrayList<handbook_hospitaldata> hosp_list = new ArrayList<>();
    DatabaseReference nDatabase;
    hospadapter adapter;
    ListView nListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_healthtips);
        //TextView buyco = findViewById(R.id.textView13);

        nDatabase = FirebaseDatabase.getInstance().getReference();
        nDatabase.keepSynced(true);

        //buyco.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View view) {
              //  Intent intent = new Intent(getApplicationContext(), Screen_Dashboard.class);
                //startActivity(intent);
            //}
        //});
        nListView = (ListView) findViewById(R.id.hosp_list);
        adapter = new hospadapter(this, R.layout.handbook_hosp, hosp_list);
        nListView.setAdapter(adapter);
        getFirebase();
    }
    public void getFirebase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hosp_list.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    handbook_hospitaldata hosp = childDataSnapshot.getValue(handbook_hospitaldata.class);
                    hosp_list.add(hosp);
                    assert hosp != null;
                    Log.d("hosp", hosp.name);
                }
            }

            @Override
            public void onCancelled (DatabaseError databaseError){
            }
        };
        nDatabase.child("/hospitals").addValueEventListener(postListener);
    }
}

