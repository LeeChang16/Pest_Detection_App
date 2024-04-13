package com.example.pestdetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class Intro_UI extends AppCompatActivity {
    Button login,signup;
    DatabaseHandler db;
    id_Holder idHolder = id_Holder.getInstance();

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.farm_pest, R.drawable.cricket_mobile, R.drawable.worm, R.drawable.eggs, R.drawable.worms};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_ui);

        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);


        if(idHolder.has_value()){
            Intent intent1 = new Intent(this, MainActivity1.class);
            startActivity(intent1);
        }

        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);





        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intro_UI.this, Login.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intro_UI.this,SignUp.class);
                startActivity(intent);
            }
        });

    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
//            Toast.makeText(Intro_UI.this, "Clicked: "+position,Toast.LENGTH_SHORT).show();
        }
    };
}