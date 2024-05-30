package com.example.pestdetectionapp;

import android.graphics.Bitmap;

public class holdBitmap {
    private static holdBitmap instance;
    Bitmap image;
    String method;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public static synchronized holdBitmap getInstance() {
        if (instance == null) {
            instance = new holdBitmap();
        }
        return instance;
    }
}
