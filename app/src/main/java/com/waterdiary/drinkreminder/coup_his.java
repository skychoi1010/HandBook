package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.waterdiary.drinkreminder.base.MasterBaseActivity;
import com.waterdiary.drinkreminder.worker.coup_u;
import com.waterdiary.drinkreminder.worker.coup_user;

import java.util.ArrayList;

public class coup_his extends MasterBaseActivity {
    DatabaseReference mDatabase,cou;
    coup_adapter adapter;
    ArrayList<coup_u> his_list = new ArrayList<coup_u>();
    ListView mListView;
    AppCompatTextView title;
    ImageView back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coup_store);
        title = findViewById(R.id.lbl_toolbar_title);
        title.setText("Coupons Bought");
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act, Screen_Dashboard.class);
                startActivity(intent);
            }
        });
        Log.d("news", "bichua");
        mListView = findViewById(R.id.his_lis);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // No session user
            Toast.makeText(getApplicationContext(), "Please log in to save data", Toast.LENGTH_SHORT).show();
        }
        assert user != null;
        final String userId = user.getUid();
        adapter = new coup_adapter(this, R.layout.coup_adap, his_list);
        mListView.setAdapter(adapter);
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    his_list.clear();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        final String key = childDataSnapshot.getKey();
                        cou=mDatabase.child("coupons").child(key).child("coupons");
                        cou.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                    String keys = childDataSnapshot.getKey();

                                    coup_user co = childDataSnapshot.getValue(coup_user.class);
                                    coup_u ca= new coup_u(co.date,co.isUsed,co.path,key);
                                    //Log.d("coup_d_etat", keys);
                                    //assert co != null;
                                    //Log.d("coup_d_etat", co.isUsed);
                                    if(co.isUsed.equals(userId)){
                                        his_list.add(ca);
                                        //Log.d("5:10", coupon.path+key);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled (DatabaseError databaseError){
                }
            };
            mDatabase.child("/coupons").addValueEventListener(postListener);


    }
}

