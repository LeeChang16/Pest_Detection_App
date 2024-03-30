package com.example.pestdetectionapp;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {


    Handler handler;
    DatabaseHandler db;
    id_Holder idHolder = id_Holder.getInstance();
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        db = new DatabaseHandler(SplashActivity.this);
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="http://localhost/simple/insert.php";
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//
//                            JSONArray jsonArray = new JSONArray(response);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                // Get data from jsonObject
//                                String pestname = jsonObject.getString("firstName");
//                                String lastName = jsonObject.getString("lastName");
//                                // Store data in SQLite database
//                                // You need to implement the method to store data in SQLite
////                                db.insertAccount();
////                                storeDataInSQLite(firstName, lastName);
//                                System.out.println("Volley: "+i);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle error
//                    }
//                });
//         queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
//

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
    }, 3000);

    }

}