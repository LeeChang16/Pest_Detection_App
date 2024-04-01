package com.example.pestdetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    Button login;
    EditText username, password;
    DatabaseHandler database;
    id_Holder idHolder = id_Holder.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        database = new DatabaseHandler(Login.this);
    }

    public void logIn(View view) throws NoSuchAlgorithmException {
        if(validate()) {
            String pass = password.getText().toString();
            String user = username.getText().toString();

            String hashed_pass = hash.hashString(pass);

            if (hashed_pass.equals(database.get_hash(hashed_pass))) {
                int id = database.getId(hashed_pass);
                database.turn_on_active_status(id);
                idHolder.hold_id(id);
                Intent intent = new Intent(Login.this, MainActivity1.class);
                startActivity(intent);

            } else {

                Toast.makeText(getApplicationContext(), "Credentials doesn't Exist!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public boolean validate(){
        boolean valid = true;
        String pass = password.getText().toString();
        String user = username.getText().toString();

        if(TextUtils.isEmpty(pass)){
            password.setError("Enter Password!");
            valid = false;
        }else{password.setError(null);}

        if(TextUtils.isEmpty(user)){
            username.setError("Enter Username!");
            valid = false;
        }else{username.setError(null);}

        return valid;
    }
}