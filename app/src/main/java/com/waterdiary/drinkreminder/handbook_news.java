package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.waterdiary.drinkreminder.worker.News;

import java.util.ArrayList;
public class handbook_news extends AppCompatActivity {
    TextView a,b;
    ArrayList<News> news_list = new ArrayList<>();
    DatabaseReference mDatabase;
    NewsListAdapter adapter;
    ListView mListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_health);
        TextView buyco = findViewById(R.id.textView13);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        buyco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), handbook_store.class);
                startActivity(intent);
            }
        });
        mListView = (ListView) findViewById(R.id.newslist);
        adapter = new NewsListAdapter(this, R.layout.handbook_newslist, news_list);
        System.out.println(news_list);
        mListView.setAdapter(adapter);
        getFirebase();
    }
    public void getFirebase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                news_list.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    News news = childDataSnapshot.getValue(News.class);
                    news_list.add(news);
                    Log.d("news", news.content);
                }
            }

            @Override
            public void onCancelled (DatabaseError databaseError){
            }
        };
        mDatabase.child("/news").addValueEventListener(postListener);
    }
}
