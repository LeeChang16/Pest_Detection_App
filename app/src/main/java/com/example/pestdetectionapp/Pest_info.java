package com.example.pestdetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Pest_info extends AppCompatActivity {
    TextView pestname, scientificname, order, family, description, intervention;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_info);

        pestname = findViewById(R.id.pest_Name);
        scientificname = findViewById(R.id.scientific_Name);
        order = findViewById(R.id.pest_Order);
        family = findViewById(R.id.pest_Family);
        description = findViewById(R.id.pest_Description);
        intervention = findViewById(R.id.pest_Intervention);
        image = findViewById(R.id.pest_image);





    }
}