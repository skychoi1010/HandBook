package com.waterdiary.drinkreminder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.waterdiary.drinkreminder.worker.handbook_hospitaldata;

import java.util.ArrayList;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_healthtips extends MasterBaseActivity {
    ArrayList<handbook_hospitaldata> hosp_list = new ArrayList<>();
    ArrayList<handbook_hospitaldata> hosp_near = new ArrayList<>();
    //ArrayList<Double> userlocation= new ArrayList<Double>();
    double latitude;
    double longitude;
    DatabaseReference nDatabase;
    hospadapter adap;
    ListView nListView;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.handbook_healthtips);
            TextView Tips = findViewById(R.id.healthTips);
            TextView News = findViewById(R.id.news);
            nDatabase = FirebaseDatabase.getInstance().getReference();
            nDatabase.keepSynced(true);
            nListView = (ListView) findViewById(R.id.hosp_list);

            adap = new hospadapter(this, R.layout.handbook_hosp, hosp_list);
            //System.out.println(hosp_list);
            nListView.setAdapter(adap);
            getFirebase();
            Log.d("we back yo", "onCreate: we back");
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            getLastLocation();
  /*          //ArrayList<Double> user = (ArrayList<Double>) getLastLocation();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // No session user
            Toast.makeText(getApplicationContext(), "Please log in to save data", Toast.LENGTH_SHORT).show();
        }
        assert user != null;
        String userId = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userdata = database.getReference("UserData").child(userId);
        getLastLocation();

        userdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double latitude = dataSnapshot.getValue(Double.class);
                double longitude = dataSnapshot.getValue(Double.class);
                for (int i = 0; i < hosp_list.size(); i++) {
                    handbook_hospitaldata nam = handbook_hospitaldata.class.cast(hosp_list.get(i));
                    assert nam != null;
                    float hosp_lat = Float.parseFloat(nam.lat);
                    float hosp_lon = Float.parseFloat(nam.lon);
                    float distance = calculateDistanceInKilometer(latitude, longitude, hosp_lat, hosp_lon);
                    Log.d("distance", String.valueOf(distance));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
            Tips.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getApplicationContext(), handbook_tips.class);
                                            startActivity(intent);
                                        }
                                    }
            );
            News.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            Intent intent2 = new Intent(getApplicationContext(), handbook_news.class);
                                            startActivity(intent2);
                                        }
                                    }
            );
        }
    public void getFirebase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hosp_list.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    handbook_hospitaldata hosp = childDataSnapshot.getValue(handbook_hospitaldata.class);
                    hosp_list.add(hosp);
                    adap.notifyDataSetChanged();
                    assert hosp != null;
                    Log.d("hosp", hosp.name);
                    Log.d("doctor",hosp.doctor);
                }
            }

            @Override
            public void onCancelled (DatabaseError databaseError){
            }
        };
        nDatabase.child("/hospitals").addValueEventListener(postListener);
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
        Log.d("tagjgdhagdjhfdjafd", "calculateDistanceInKilometer: whooh");
        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {

                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latitude=location.getLatitude();
                                    longitude=location.getLongitude();
                                    Log.d("log8", location.getLatitude()+"");
                                    Log.d("log8",location.getLongitude()+"");
                                    /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user == null) {
                                        // No session user
                                        Toast.makeText(getApplicationContext(), "Please log in to save data", Toast.LENGTH_SHORT).show();
                                    }
                                    assert user != null;
                                    String userId = user.getUid();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference userdata = database.getReference("UserData").child(userId);
                                    userdata.child("longitude").setValue(longitude);
                                    userdata.child("latitude").setValue(latitude);*/
                                        getFirebase();
                                        Log.d("hosplis", String.valueOf(hosp_list.size()));
                                        for (int i = 0; i < hosp_list.size(); i++) {
                                        handbook_hospitaldata nam = handbook_hospitaldata.class.cast(hosp_list.get(i));
                                        assert nam != null;
                                        double hosp_lat = Float.parseFloat(nam.lat);
                                        double hosp_lon = Float.parseFloat(nam.lon);
                                        double distance = calculateDistanceInKilometer(latitude, longitude, hosp_lat, hosp_lon);
                                        Log.d("distance"+i, String.valueOf(distance));
                                    }

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
        System.out.print(latitude+"returnloc"+longitude+"\n");
    return ;
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
            Log.d("log8", mLastLocation.getLatitude()+"");
            Log.d("log8",mLastLocation.getLongitude()+"");
            getFirebase();
            for (int i = 0; i < hosp_list.size(); i++) {
                handbook_hospitaldata nam = handbook_hospitaldata.class.cast(hosp_list.get(i));
                assert nam != null;
                double hosp_lat = Float.parseFloat(nam.lat);
                double hosp_lon = Float.parseFloat(nam.lon);
                double distance = calculateDistanceInKilometer(latitude, longitude, hosp_lat, hosp_lon);
                Log.d("distance"+i, String.valueOf(distance));
            }
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

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }
}
