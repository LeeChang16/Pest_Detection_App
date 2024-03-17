package com.example.pestdetectionapp;

import android.graphics.Bitmap;

import java.sql.Blob;

public class recentdetectionData {

    String name;
    String scientific_name;
//     Bitmap Image;

    recentdetectionData(String name, String scientific_name){

        this.name = name;
        this.scientific_name = scientific_name;
//        this.Image = Image;

    }
}
