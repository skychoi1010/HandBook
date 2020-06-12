package com.waterdiary.drinkreminder;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.TextView;

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
    ArrayList<coupon_class> coup_list = new ArrayList<>();
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
                    //coup_list.clear();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        coupon_class coupon=childDataSnapshot.getValue(coupon_class.class);
                        assert coupon != null;
                        if (coupon.isUsed.equals("False")){
                            coup_list.add(coupon);
                            adapter.notifyDataSetChanged();
                        }
                        Log.d("meowkers", coupon.path+"  "+coupon.isUsed+"   "+coupon.cost);
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
                Log.d("spacesk", text);
                Intent intent = new Intent(handbook_store.this,handbook_balcheck.class);
                intent.putExtra("coin",text);
                intent.putExtra("img",text2);
                Log.d("spacesk", text+"hhfhjhj");
                startActivity(intent);

            }});
    }

}
