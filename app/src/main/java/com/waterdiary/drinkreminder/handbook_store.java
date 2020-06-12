package com.waterdiary.drinkreminder;

        import android.os.Bundle;
        import android.util.Log;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;

        import androidx.annotation.NonNull;

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
    ArrayList coup_list = new ArrayList<>();
    store_adapt adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_store);
        nListView=findViewById(R.id.couponlis);
        nDatabase = FirebaseDatabase.getInstance().getReference();
        nDatabase.keepSynced(true);
        adapter=  new store_adapt(this,R.layout.store_view,coup_list);
        nListView.setAdapter(adapter);
        Log.d("meo", "monkeys");
        final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        coupon_class coupon=childDataSnapshot.getValue(coupon_class.class);
                        assert coupon != null;
                        coup_list.add(coupon);
                        adapter.notifyDataSetChanged();
                        Log.d("meowkers", coupon.path+"  "+coupon.isUsed+"   "+coupon.cost);
                    }
                }

                @Override
                public void onCancelled (DatabaseError databaseError){

                }
        };
        nDatabase.child("/coupons").addValueEventListener(postListener);

        /*ImageView storebal = findViewById(R.id.co1);
        storebal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), handbook_balcheck.class);
                startActivity(intent);
            }
        });*/

    }
}
