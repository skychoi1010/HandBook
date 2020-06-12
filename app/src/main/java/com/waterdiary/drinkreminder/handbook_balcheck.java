package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.waterdiary.drinkreminder.base.MasterBaseActivity;
import com.waterdiary.drinkreminder.worker.coupon_class;
import com.waterdiary.drinkreminder.worker.user_coin;

public class handbook_balcheck extends MasterBaseActivity {
    DatabaseReference coins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_balcheck);
        final Button buyco = findViewById(R.id.buy_co);
        final Intent intent= getIntent();
        final String coupon_cost= intent.getStringExtra("coin");
        final String img=intent.getStringExtra("img");
        Log.d("coin_bal", coupon_cost);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // No session user
            Toast.makeText(getApplicationContext(), "Please log in to save data", Toast.LENGTH_SHORT).show();
        }
        assert user != null;
        final String userId = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        coins = database.getReference().child("UserData").child(userId);
        coins.keepSynced(true);
        //coup= database.getReference().child("coupons");
        //coup.keepSynced(true);
        //userdata.child("longitude").setValue(longitude);
        //userdata.child("latitude").setValue(latitude);
        //ChildEventListener coin = coins.addChildEventListener((ChildEventListener) coins);
        //ValueEventListener coin = coins.addValueEventListener((ValueEventListener) coins);
        //Log.d("balch", String.valueOf(coin));
        //nDatabase = FirebaseDatabase.getInstance().getReference(userId);
        //nDatabase.keepSynced(true);
        Log.d("coin_bala", String.valueOf(coins));
        coins.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String name1 = dataSnapshot.child("coins").getValue(String.class);
                assert name1 != null;
                TextView tp = (TextView)findViewById(R.id.totpoint);
                tp.setText(name1);
                TextView cp = (TextView)findViewById(R.id.coupcost);
                cp.setText(coupon_cost);
                TextView np = (TextView)findViewById(R.id.new_bal);
                np.setText(String.valueOf(Long.parseLong(name1)-Long.parseLong(coupon_cost)));
                ImageView v=(ImageView)findViewById(R.id.imageView14);
                getImage(img,v);
                buyco.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //setContentView(R.layout.handbook_balcheck);
                        Long val= Long.parseLong(name1)-Long.parseLong(coupon_cost);

                        if (val > 0) {
                            coins.child("coins").setValue(String.valueOf(val));
                            //coup.child("isUsed").setValue("True");
                            Intent intent1= new Intent(getApplicationContext(),handbook_store.class);
                            startActivity(intent1);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Not enough coins", Toast.LENGTH_SHORT).show();
                            Intent intent1= new Intent(getApplicationContext(),handbook_store.class);
                            startActivity(intent1);
                        }
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
               // Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
        Log.d("coin_balalala", String.valueOf(coupon_cost));

    }
    public void getImage(String Path, final ImageView image){
        final FirebaseStorage storage  = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(Path);
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
