package com.example.pestdetectionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    Button login;
    EditText username, password;
    DatabaseHandler database;
    id_Holder idHolder = id_Holder.getInstance();
    baseUrl url = baseUrl.getInstance();
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        database = new DatabaseHandler(Login.this);
    }

    public void logIn(View view) throws NoSuchAlgorithmException {
        if(validate()) {
            String pass = password.getText().toString();
            String user = username.getText().toString();

            String hashed_pass = hash.hashString(pass);


            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String[] field = new String[2];
                    field[0] = "username";
                    field[1] = "password";

                    String[] data = new String[2];
                    data[0] = user;
                    data[1] = hashed_pass;

                    PutData putData = new PutData(url.getUrl()+"Login.php", "POST", field, data);

                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult(); // Assuming the response is a JSON string

                            try {
                                // Parse the JSON string
                                JSONObject userJson = new JSONObject(result);

                                // Check for success (optional)
                                if (userJson.getBoolean("success")) {
                                    // Extract individual user information
                                    String userId = userJson.getString("Id");
                                    String userFullname = userJson.getString("Fullname");
                                    String userTown = userJson.getString("Town");
                                    String userProvince = userJson.getString("Province");
                                    String userOccupation = userJson.getString("Occupation");
                                    String userName = userJson.getString("Username");
                                    String userPassword = userJson.getString("Password");
                                    String userImagepath = userJson.getString("picture");
                                    String ImageUrl = url.getUrl()+userImagepath;

                                    id = Integer.parseInt(userId);


                                    System.out.println(userId+" "+userFullname+" "+userTown+" "+userProvince+" "+userOccupation+" "+userName+" "+userPassword);
                                    System.out.println(ImageUrl);
                                    Glide.with(getApplication()).asBitmap().load(ImageUrl).into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                            // Handle the downloaded bitmap here (e.g., store it in a variable)
                                            // 'bitmap' contains the image data
                                            // For example:
                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                                            byte[] imagebyte = stream.toByteArray();
                                            database.insertClient(id,userFullname,userProvince,userTown,userOccupation,imagebyte);
                                            database.insertAccount(id,userName,userPassword,1);

                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                            // Called when the image load is cleared (e.g., view recycled)
                                        }
                                    });

//                                    database.turn_on_active_status(id);
                                    idHolder.hold_id(id);
                                    Intent intent = new Intent(Login.this, MainActivity1.class);
                                    startActivity(intent);

                                } else {
                                    String error = userJson.getString("error");
                                    Log.w("Login Error", "Invalid credentials: " + error);
                                    // Handle login failure (e.g., display an error message to the user)
                                }
                            } catch (JSONException e) {
                                Log.e("Login Error", "Failed to parse JSON response: "  + e.getMessage());
                                // Handle JSON parsing errors
                            }


                        } else {
                            Log.e("Login Error", "Failed to execute request.");
                            // Handle request execution failures
                            Toast.makeText(getApplicationContext(), "Credentials doesn't Exist!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("Login Error", "Failed to start request.");
                        // Handle request initialization failures
                    }

                }
            });


        }
    }


    public boolean validate(){
        boolean valid = true;
        String pass = password.getText().toString();
        String user = username.getText().toString();

        if(TextUtils.isEmpty(pass)){
            password.setError("Enter Password!");
            valid = false;
        }else{password.setError(null);}

        if(TextUtils.isEmpty(user)){
            username.setError("Enter Username!");
            valid = false;
        }else{username.setError(null);}

        return valid;
    }
}