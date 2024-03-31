package com.example.pestdetectionapp;

import android.graphics.Bitmap;

public class library_Data {
     Bitmap Image;
     String name;
     String scientific_name;
    String order;
    String family;
    String description;
    String intervention;


    library_Data(Bitmap image,String name, String scientific_name, String order, String family, String description, String intervention){
        this.Image = image;
        this.name = name;
        this.scientific_name = scientific_name;
        this.order = order;
        this.family = family;
        this.description = description;
        this.intervention = intervention;


    }
}
