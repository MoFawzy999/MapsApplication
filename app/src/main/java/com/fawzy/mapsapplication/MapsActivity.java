package com.fawzy.mapsapplication;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fawzy.mapsapplication.DB.DB;
import com.fawzy.mapsapplication.DB.places;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener , GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private LatLng point1 = new LatLng(30.058918,31.260967);
    private Marker mpoint1 ;
    private String MyAdress = "" ;
   // private LocationManager locationManager ;
   // private LocationListener locationListener ;
    private DB db ;
    private FusedLocationProviderClient providerClient ;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        providerClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT < 23){
           getLocation();
        }else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                }, 0);
            } else {
             //  getLocation();
            }
        }
        }

   @SuppressLint("MissingPermission")
   public void getLocation(){

       LocationRequest request = new LocationRequest();
       request.setInterval(1000);
       request.setFastestInterval(1000);
       request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

       providerClient.requestLocationUpdates(request, new LocationCallback() {
           @Override
           public void onLocationResult(LocationResult locationResult) {
               super.onLocationResult(locationResult);

               Location location = locationResult.getLastLocation();
/*
               Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
               try {
                   List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                   if (addressList != null && addressList.size() > 0) {
                       Log.d("address", addressList.get(0).toString());
                        if (addressList.get(0).getCountryName() != null){
                           MyAdress += addressList.get(0).getCountryName().toString()+"/" ;
                       }
                       if (addressList.get(0).getAddressLine(0) != null){
                           MyAdress += addressList.get(0).getAddressLine(0).toString()+"/" ;
                       }
                       if (addressList.get(0).getSubAdminArea() != null){
                           MyAdress += addressList.get(0).getSubAdminArea().toString()+"/";
                       }
                   } else {
                       Log.d("address", "Address not found");
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }*/
               LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
               MarkerOptions options = new MarkerOptions();
               options.title( MyAdress );
               options.position(myLocation);
               mMap.clear();
               mMap.addMarker(options);
               mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17f));
           }
       }, null);
   }



    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // saglt an override method ali tht tab3a lal object mMap
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);

        db = new DB(this);

        List<Marker> markerList = new ArrayList<>();
        List<places> placesList = db.getAllPlaces();
        for (places place : placesList){
            String MyInfo = "ID:" + place.getId() + "Latitude:" + place.getLatitude() + "Longitude:" + place.getLongtitude()
                    + "Tittle:" + place.getTittle();
            Log.d("MyInfo",MyInfo);
            markerList.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(place.getLatitude())
                            ,Double.parseDouble(place.getLongtitude()))).title(place.getTittle())
                    .zIndex( place.getId() )   ));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(place.getLatitude())
                    ,Double.parseDouble(place.getLongtitude())),10));
        }


    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              //  getLocation();
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(MapsActivity.this,ShowActivity.class);
        intent.putExtra("latitude" , marker.getPosition().latitude);
        intent.putExtra("longitude" , marker.getPosition().longitude);
        intent.putExtra("title" , marker.getTitle());
        intent.putExtra("id",marker.getZIndex());
        startActivity(intent);
        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
   //     mMap.addMarker(new MarkerOptions().position(latLng).title(latLng.toString()).icon(BitmapDescriptorFactory.defaultMarker
            //    (BitmapDescriptorFactory.HUE_GREEN)));
        Intent intent = new Intent(MapsActivity.this,AddActivity.class);
        intent.putExtra("latitude",latLng.latitude);
        intent.putExtra("longitude",latLng.longitude);
        startActivity(intent);
    }




}