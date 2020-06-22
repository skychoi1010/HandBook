package com.waterdiary.drinkreminder;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.waterdiary.drinkreminder.base.MasterBaseActivity;
import com.waterdiary.drinkreminder.worker.News;
import com.waterdiary.drinkreminder.worker.handbook_hospitaldata;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class handbook_healthtips extends MasterBaseActivity {
    ArrayList<handbook_hospitaldata> hosp_list = new ArrayList<>();
    ArrayList<handbook_hospitaldata> hosp_near = new ArrayList<>();
    double latitude;
    double longitude;
    DatabaseReference nDatabase;
    hospadapter adap;
    ListView nListView,pListView;
    ArrayList<News> news_list = new ArrayList<>();
    DatabaseReference mDatabase;
    NewsListAdapter adapter;
    coup_adapter ada;
    ListView mListView;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    AppCompatTextView title;
    ImageView back;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.handbook_healthtips);
        title = findViewById(R.id.lbl_toolbar_title);
        title.setText("Health Tips");
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(act, Screen_Dashboard.class);
                startActivity(intent);
            }
        });
            TextView Tips = findViewById(R.id.healthTips);
            mListView = findViewById(R.id.newslist);
            nDatabase = FirebaseDatabase.getInstance().getReference();
            nDatabase.keepSynced(true);
            nListView = (ListView) findViewById(R.id.hosp_list);
            //pListView =(ListView) findViewById(R.id.tipslis);
            adap = new hospadapter(this, R.layout.handbook_hosp, hosp_near);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);
            adapter = new NewsListAdapter(this, R.layout.handbook_newslist, news_list);

            mListView.setAdapter(adapter);
            getFirebaseNews();
            nListView.setAdapter(adap);
            //getFirebase();
            Log.d("we back yo", "onCreate: we back");
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            getLastLocation();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.link);
                final String text = textView.getText().toString();
               // TextView txt= (TextView) findViewById(R.id.home); //txt is object of TextView
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                textView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(text));
                        startActivity(browserIntent);
                    }
                });
                finish();
            }});
            Tips.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getApplicationContext(), handbook_tips.class);
                                            startActivity(intent);
                                        }
                                    }
            );
    }

    public void getFirebaseNews() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                news_list.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    News news = childDataSnapshot.getValue(News.class);
                    news_list.add(news);
                    Log.d("news", news.content);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled (DatabaseError databaseError){
            }
        };
        mDatabase.child("/news").addValueEventListener(postListener);
    }

    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    public int calculateDistanceInKilometer(double userLat, double userLng,
                                            double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        Log.d("why you repeat", "onComplete: ");
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {

                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                final Location location = task.getResult();

                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latitude=location.getLatitude();
                                    longitude=location.getLongitude();

                                    final ValueEventListener postListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            hosp_list.clear();
                                            TreeMap<Integer,String> map = new TreeMap<Integer,String>();
                                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                                handbook_hospitaldata hosp = childDataSnapshot.getValue(handbook_hospitaldata.class);
                                                hosp_list.add(hosp);
                                                adap.notifyDataSetChanged();
                                                assert hosp != null;
                                                int distance=calculateDistanceInKilometer(location.getLatitude(),location.getLongitude(),Double.parseDouble(hosp.lat),Double.parseDouble(hosp.lon));
                                                map.put(distance,hosp.name);
                                            }
                                            SortedMap<Integer, String> cutmap = map.subMap(0,25);
                                            for (final Map.Entry<Integer, String> pair : cutmap.entrySet()) {
                                                final ValueEventListener postListener = new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                                            handbook_hospitaldata hosp = childDataSnapshot.getValue(handbook_hospitaldata.class);
                                                            assert hosp != null;
                                                            if (pair.getValue().equals(hosp.name)) {
                                                                hosp_near.add(hosp);
                                                                adap.notifyDataSetChanged();
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled (DatabaseError databaseError){
                                                    }
                                                };
                                                nDatabase.child("/hospitals").addValueEventListener(postListener);
                                            }
                                        }

                                        @Override
                                        public void onCancelled (DatabaseError databaseError){
                                        }
                                    };
                                    nDatabase.child("/hospitals").addValueEventListener(postListener);
                                }
                            }

                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }
        else {
            requestPermissions();
        }

    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

   private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
    /*@Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }*/

}
