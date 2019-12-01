package com.andrezacampbell.mapsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.Arrays;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng newYork = new LatLng(40.7611409, -73.9388664);
        mMap.addMarker(new MarkerOptions().position(newYork).title("New York"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newYork,20f));

        setMapClick();
//        cleanMap();
        setmMapTitle();
        setPoiClick(mMap);
        enableMyLocation();
        mapDetail();

    }

    private void setMapClick(){
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng));

            }
        });
    }
//    remove um ponto marcado
    private void cleanMap(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
                    public boolean onMarkerClick(Marker marker){
                marker.remove();
                return true;
            }
        });
    }
//    apos o click na informacao do titulo via para uma tela de detalhes
//    - nao esquecer de criar uma ativity para esse click senao da erro
    private void mapDetail(){
    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            Intent intent = new Intent(MapsActivity.this, DetalheActivity.class);
            startActivity(intent);
        }
    });

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps_options, menu);
        return true;
    }

    private void setmMapTitle(){
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String title = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);

                mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(getString(R.string.titulo))
                .snippet(title));
            }
        });
    }

    private void setPoiClick(final GoogleMap map){
        mMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener(){
            @Override
            public void onPoiClick(PointOfInterest pointOfInterest) {
                Marker poi = mMap.addMarker(new MarkerOptions()
                .position(pointOfInterest.latLng)
                .title(pointOfInterest.name));

                poi.showInfoWindow();
            }

        });
    }

    private boolean isPermissionGranted(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void enableMyLocation(){
        String[] permission = Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION).toArray(new String[0]);

        if (isPermissionGranted()){
            mMap.setMyLocationEnabled(true);
        }else {
            ActivityCompat.requestPermissions(this, permission, REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.normal_map) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            return true;

        } else if (item.getItemId() == R.id.hybrid_map) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            return true;
        } else if (item.getItemId() == R.id.satellite_map) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        } else if (item.getItemId() == R.id.terrain_map) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
