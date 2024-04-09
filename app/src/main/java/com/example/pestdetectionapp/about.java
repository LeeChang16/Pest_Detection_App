package com.example.pestdetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }


    public void gotoLink1(View v){

        String url = "https://profile.thezionlab.com/chandleerey.belarmino/";
        // Create an Intent to open a browser
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void gotoLink2(View v){

        String url = "https://profile.thezionlab.com/jhunde.donasco/";
        // Create an Intent to open a browser
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }



}