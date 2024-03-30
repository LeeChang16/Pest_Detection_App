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
    id_Holder idHolder = id_Holder.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_ui);


//        if(db.active_user()){
//            int id = db.get_active_user_Id();
//            Intent intent1 = new Intent(this, MainActivity1.class);
//            intent1.putExtra("IdValue",id);
//            startActivity(intent1);
//            finish();
//        }else {
//
//        }

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
}