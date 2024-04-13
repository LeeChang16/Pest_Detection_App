package com.example.pestdetectionapp;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {


    Handler handler;
    DatabaseHandler db;
    byte[] imagebyte = null;
    Bitmap Image;

    id_Holder idHolder = id_Holder.getInstance();
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    location_Tracker track = location_Tracker.getInstance();
    baseUrl url = baseUrl.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }



        db = new DatabaseHandler(SplashActivity.this);
        if (isNetworkConnected()) {

            processdata();


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
                            track.set_location(address.getAddressLine(0), address.getLocality(), address.getAdminArea(), address.getCountryName());


                        }
                    }
                });
            }

        } else {
            Toast.makeText(SplashActivity.this,"Entering Offline Mode",Toast.LENGTH_SHORT).show();
        }




        handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {


                if (db.active_user()) {
                    int id = db.get_active_user_Id();
                    idHolder.hold_id(id);

//                Toast.makeText(SplashActivity.this, "User Id: "+id, Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(SplashActivity.this, MainActivity1.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Intent intent2 = new Intent(SplashActivity.this, Intro_UI.class);
                    startActivity(intent2);
                    finish();

                }
            }
        }, 4000);

    }


//    public interface MyApi {
//
//        @GET("http://192.168.100.10/MyAdmin/RestApi.php") // Replace with URL if using an API
//        Call<List<mydata>> getData(); // Replace MyData with your data model class
//
//    }

    public void processdata() {

        db.getWritableDatabase();
        Call<List<mydata>> call = apicontroller
                .getInstance()
                .getapi().getData();

        call.enqueue(new Callback<List<mydata>>() {
            @Override
            public void onResponse(Call<List<mydata>> call, Response<List<mydata>> response) {
                List<mydata> data = response.body();
                for (mydata item : data) {

                    String Id = item.getId();
                    String Name = item.getName();
                    String ScientificName = item.getScientificName();
                    String PestOrder = item.getPestOrder();
                    String PestFamily = item.getPestFamily();
                    String Description = item.getDescription();
                    String Intervention = item.getIntervention();
                    String ImagePath = item.getImagePath();
                    String ImageUrl = url.getUrl()+ImagePath;

                    Glide.with(getApplication()).asBitmap().load(ImageUrl).into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                    // Handle the downloaded bitmap here (e.g., store it in a variable)
                                    // 'bitmap' contains the image data
                                    // For example:
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                                    imagebyte = stream.toByteArray();

                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Called when the image load is cleared (e.g., view recycled)
                                }
                            });

                    if(db.checkpest_id(Integer.parseInt(Id))) {
                        db.insertPestDetails(Id, Name, ScientificName, PestOrder, PestFamily, Description, Intervention, imagebyte);
                    }else{
                        Log.d("PEST", "onResourceReady: PEST ALREADY EXISTED");
                    }

                }

            }

            @Override
            public void onFailure(Call<List<mydata>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                System.out.println("NAY ERROR: " + t.toString());
            }
        });
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}