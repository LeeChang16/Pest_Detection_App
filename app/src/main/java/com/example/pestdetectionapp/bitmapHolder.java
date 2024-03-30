package com.example.pestdetectionapp;

import android.graphics.Bitmap;

public class bitmapHolder {

    Bitmap bitmap;


    public Bitmap get_bitmap(){
        return bitmap;
    }

    public void hold_bitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }


}
