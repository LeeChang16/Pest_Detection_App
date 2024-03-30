package com.example.pestdetectionapp;

import android.graphics.Bitmap;

import java.sql.Blob;

public class recentdetectionData {

    String name;
    String confidence;
    Bitmap Image;
    String time;
    String date;

    recentdetectionData(String name, String confidence, Bitmap Image, String time, String date){

        this.name = name;
        this.confidence = confidence;
        this.Image = Image;
        this.time = time;
        this.date = date;


    }
}
