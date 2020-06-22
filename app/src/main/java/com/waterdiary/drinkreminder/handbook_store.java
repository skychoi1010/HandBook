package com.waterdiary.drinkreminder;

        import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
        import android.widget.ImageView;
        import android.widget.ListView;
import android.widget.TextView;

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
import com.waterdiary.drinkreminder.worker.coupon_class;

import java.util.ArrayList;

public class handbook_store extends MasterBaseActivity {
    ListView nListView;
    DatabaseReference nDatabase;
    ArrayList<coupon_class> coup_list = new ArrayList<>();
    store_adapt adapter;
    TextView history;
    AppCompatTextView title;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_store);

        title = findViewById(R.id.lbl_toolbar_title);
        title.setText("Coupon Store");
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act, Screen_Dashboard.class);
                startActivity(intent);
            }
        });

        nListView=findViewById(R.id.couponlis);
        nDatabase = FirebaseDatabase.getInstance().getReference();
        nDatabase.keepSynced(true);
        adapter=  new store_adapt(this,R.layout.store_view,coup_list);
        nListView.setAdapter(adapter);
        history=findViewById(R.id.textView4);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //coup_list.clear();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            String key= childDataSnapshot.getKey();
                            coupon_class coupon= childDataSnapshot.child("cd").getValue(coupon_class.class);
                            coup_list.add(coupon);
                            Log.d("5:10", coupon.path+key);
                            adapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled (DatabaseError databaseError){
                }
        };
        nDatabase.child("/coupons").addValueEventListener(postListener);
        nListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.coin);
                String text = textView.getText().toString();
                TextView tex = (TextView) view.findViewById(R.id.img);
                String text2 = tex.getText().toString();
                Intent intent = new Intent(handbook_store.this,handbook_balcheck.class);
                intent.putExtra("coin",text);
                intent.putExtra("img",text2);
                startActivity(intent);
                finish();
            }});
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),coup_his.class);
                startActivity(in);
            }
    });

}
}
