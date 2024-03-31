package com.example.pestdetectionapp;

import android.graphics.Bitmap;

public class realtime_resultData {
     Bitmap Image;
     String name;
     String scientific;
     String order;
     String family;
     String description;
     String intervention;



    realtime_resultData(Bitmap image, String name, String scientific, String order, String family, String description, String intervention){

        this.Image = image;
        this.name = name;
        this.scientific = scientific;
        this.order = order;
        this.family = family;
        this.description = description;
        this.intervention = intervention;


    }
}
