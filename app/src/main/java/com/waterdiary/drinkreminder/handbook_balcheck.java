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
import androidx.appcompat.widget.AppCompatTextView;

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

import java.util.Date;

public class handbook_balcheck extends MasterBaseActivity {
    DatabaseReference userd,coupon;
    AppCompatTextView title;
    ImageView back;
    Button no_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_balcheck);
        title = findViewById(R.id.lbl_toolbar_title);
        title.setText("Coupon Purchase");
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act, handbook_store.class);
                startActivity(intent);
            }
        });
        no_buy = findViewById(R.id.no_buy);
        no_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act, handbook_store.class);
                startActivity(intent);
            }
        });
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
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        userd = database.getReference().child("UserData").child(userId);
        userd.keepSynced(true);
        assert img != null;
        coupon = database.getReference().child("coupons").child(img).child("coupons");
        Log.d("coin_bala", String.valueOf(userd));
        //final Date date = Calendar.getInstance().getTime();
        //final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        final String date = java.text.DateFormat.getDateTimeInstance().format(new Date());
        System.out.println("Current time => " + date);

        userd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Long name1 = dataSnapshot.child("coins").getValue(Long.class);
                assert name1 != null;
                TextView tp = (TextView)findViewById(R.id.totpoint);
                tp.setText(String.valueOf(name1));
                TextView cp = (TextView)findViewById(R.id.coupcost);
                cp.setText(coupon_cost);
                final Long val= (name1)-Long.parseLong(coupon_cost);
                TextView np = (TextView)findViewById(R.id.new_bal);
                if (val>0){
                    np.setText(String.valueOf(name1-Long.parseLong(coupon_cost)));
                }
                else {
                np.setText("0");
                }
                ImageView v=(ImageView)findViewById(R.id.imageView14);
                getImage(img,v);

                assert img != null;
                Log.d("tagmyimagecoup", img);
                buyco.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val > 0) {
                            coupon.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    long i=0;
                                    long count= dataSnapshot.getChildrenCount();
                                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){

                                            String ckey= childDataSnapshot.getKey();
                                            final String coup = childDataSnapshot.child("path").getValue(String.class);
                                            String use = childDataSnapshot.child("isUsed").getValue(String.class);
                                            if(use.equals("")){
                                                coupon.child(ckey).child("isUsed").setValue(userId);
                                                coupon.child(ckey).child("date").setValue(date);
                                                //Log.d("missfor", "flagged");
                                                userd.child("coins").setValue(val);
                                                i=1;
                                                Intent SCoupon= new Intent(handbook_balcheck.this,barcoup.class);
                                                SCoupon.putExtra("bar",coup);
                                                SCoupon.putExtra("img",img);
                                                startActivity(SCoupon);
                                                break;
                                            }
                                            else if(i==count-1)
                                            {Toast.makeText(getApplicationContext(), "Sorry this coupon is not available.\n Please try again later.", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                            i+=1;}
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            }
                            );
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Not enough coins", Toast.LENGTH_SHORT).show();
                            finish();
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
        StorageReference pathReference = storageRef.child(Path+".jpg");
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
