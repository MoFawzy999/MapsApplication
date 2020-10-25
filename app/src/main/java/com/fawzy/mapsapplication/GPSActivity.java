package com.fawzy.mapsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GPSActivity extends AppCompatActivity {

    private FusedLocationProviderClient providerClient ;
    private String appPerm [] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS};
    private static final int PERMISSION_REQ = 1 ;
    private TextView showGPS , subAdminArea , AddressLine ;
    private EditText Phone_Number ;
    private Button Send_SMS ;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_p_s);

         providerClient = LocationServices.getFusedLocationProviderClient(this);
         getlocation();

        showGPS = findViewById(R.id.txt);
        subAdminArea = findViewById(R.id.txt2);
        AddressLine = findViewById(R.id.txt3);
        Phone_Number = findViewById(R.id.phone_num);
        Send_SMS = findViewById(R.id.sendButton);

        Send_SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SMS = showGPS.getText().toString() +"-"+
                        subAdminArea.getText().toString() +"-" +
                        AddressLine.getText().toString() ;
                String Phone = Phone_Number.getText().toString();
                if (!TextUtils.isEmpty(SMS) && !TextUtils.isEmpty(Phone)){
                    if (CheckAndRequestPermission()){
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(Phone,null,SMS,null,null);
                    }
                }
            }
        });

        CheckAndRequestPermission();

    }

    public boolean CheckAndRequestPermission(){
        List<String> permissionsNeeded = new ArrayList<>();
        for (String perm :appPerm ){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(perm);
            }
            }

        if (!permissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[permissionsNeeded.size()])
            , PERMISSION_REQ);
            return false ;
        }
        return true ;
    }

    @SuppressLint("MissingPermission")
    public void getlocation(){
        LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        providerClient.requestLocationUpdates(request, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                showGPS.setText(location.getLatitude()+"-"+location.getLongitude());
                Log.d("My_location",location.toString());
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (addressList != null && addressList.size() > 0) {
                        Log.d("address", addressList.get(0).toString());
                        if (addressList.get(0).getCountryName() != null){
                            showGPS.setText( addressList.get(0).getCountryName().toString());
                        }
                        if (addressList.get(0).getAddressLine(0) != null){
                            AddressLine.setText(addressList.get(0).getSubAdminArea().toString());
                        }
                        if (addressList.get(0).getSubAdminArea() != null){
                            subAdminArea.setText(addressList.get(0).getAddressLine(0).toString());
                        }
                    } else {
                        Log.d("address", "Address not found");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PERMISSION_REQ == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getlocation();
            }
        }
    }



}