package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.waterdiary.drinkreminder.base.MasterBaseActivity;
import com.waterdiary.drinkreminder.worker.coup_u;
import com.waterdiary.drinkreminder.worker.coup_user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class barcoup extends MasterBaseActivity {
    DatabaseReference mDatabase,cou;
    coup_adapter adapter;
    ArrayList<coup_u> his_list = new ArrayList<coup_u>();
    ListView mListView;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coup_store);
        Intent intent = getIntent();
        String bar = intent.getStringExtra("bar");
        ImageView v=(ImageView)findViewById(R.id.bimg);
        final String img=intent.getStringExtra("img");
        getImage(bar,v,img);
        Log.d("flower", "getView");
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


    public void getImage(String Path, final ImageView image, String img){
        final FirebaseStorage storage  = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(img).child(Path+".jpg");
        // Load the image using Glide

        pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(mContext.getApplicationContext())
                            .load(task.getResult())
                            .into(image);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(mContext.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
