package com.waterdiary.drinkreminder;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
public class handbook_maps extends FragmentActivity implements OnMapReadyCallback {
   GoogleMap map;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.handbook_map);
       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
               .findFragmentById(R.id.map);
       assert mapFragment != null;
       mapFragment.getMapAsync(this);
   }
   @Override
    public void onMapReady(GoogleMap googleMap) {
       map = googleMap;
       Float lat = getIntent().getFloatExtra("lat",0);
       Float lon = getIntent().getFloatExtra("lon",0);
       String name = getIntent().getStringExtra("name");
       Log.d("TAGpewpewpepwpew", lat+"      "+lon);
       LatLng hospital = new LatLng(lat,lon);
       map.addMarker(new MarkerOptions().position(hospital).title(name));
       map.moveCamera(CameraUpdateFactory.newLatLng(hospital));
    }
}
