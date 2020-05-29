package com.waterdiary.drinkreminder;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageView;

        import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_store extends MasterBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_store);
        ImageView storebal = findViewById(R.id.co1);
        storebal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), handbook_balcheck.class);
                startActivity(intent);
            }
        });
    }
}
