package com.example.pestdetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Intro_UI extends AppCompatActivity {
    Button login,signup;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_ui);

        db = new DatabaseHandler(Intro_UI.this);

        if(db.active_user()){
            int id = db.get_active_user_Id();
            Intent intent = new Intent(Intro_UI.this, MainActivity.class);
            intent.putExtra("IdValue",id);
            startActivity(intent);
        }else {
            Toast.makeText(Intro_UI.this,"Failed to Auto Login. Please Login Manually", Toast.LENGTH_SHORT).show();
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
}