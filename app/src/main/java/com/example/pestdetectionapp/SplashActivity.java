package com.example.pestdetectionapp;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {


    Handler handler;
    DatabaseHandler db;
    id_Holder idHolder = id_Holder.getInstance();
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    location_Tracker track = location_Tracker.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        db = new DatabaseHandler(SplashActivity.this);

        //for location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the missing permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions are granted, you can call getLastLocation()
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations, this can be null.
                    if (location != null) {
                        // Logic to handle location object
//                        locationStr = "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude();
//                        System.out.println("LOCATION: "+locationStr);

                        Geocoder geocoder = new Geocoder(SplashActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        assert addresses != null;
                        Address address = addresses.get(0);
                        track.set_location(address.getAddressLine(0),address.getLocality(),address.getAdminArea(),address.getCountryName());



                    }
                }
            });
        }


        handler = new Handler();

    handler.postDelayed(new Runnable() {

        @Override
        public void run() {


            if(db.active_user()){
                int id = db.get_active_user_Id();
                idHolder.hold_id(id);

//                Toast.makeText(SplashActivity.this, "User Id: "+id, Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(SplashActivity.this, MainActivity1.class);
                startActivity(intent1);
                finish();
            }else {
                Intent intent2 = new Intent(SplashActivity.this, Intro_UI.class);
                startActivity(intent2);
                finish();

            }
        }
    }, 4000);

    }

}