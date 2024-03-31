package com.example.pestdetectionapp;

import android.graphics.Bitmap;

public class Detect_Result_Data {

    Bitmap Image;
    String name;
    String scientific;
    String order;
    String family;
    String description;
    String intervention;
    Detect_Result_Data(Bitmap Image, String name, String scientific, String order, String family, String description, String intervention ){
        this.Image = Image;
        this.name = name;
        this.scientific = scientific;
        this.order = order;
        this.family = family;
        this.description = description;
        this.intervention = intervention;
    }
}
