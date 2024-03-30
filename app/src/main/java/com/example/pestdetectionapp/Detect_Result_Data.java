package com.example.pestdetectionapp;

import android.graphics.Bitmap;

public class Detect_Result_Data {

    String name;
    String scientific;
    Bitmap Image;

    Detect_Result_Data(String name, String scientific, Bitmap Image){

        this.name = name;
        this.scientific = scientific;
        this.Image = Image;


    }
}
