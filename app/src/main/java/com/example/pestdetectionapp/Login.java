package com.example.pestdetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Button login;
    EditText username, password;
    DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    public void logIn(View view){
        database = new DatabaseHandler(Login.this);

        String pass = password.getText().toString();
        String user = username.getText().toString();

        if(checkCredentials(user,pass)){
            int id = database.getId(user,pass);
                    database.turn_on_active_status(id);
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("IdValue",id);
                    startActivity(intent);
//                    Toast.makeText(getApplicationContext(),"Logging In", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(),"Credentials doesn't Exist!",Toast.LENGTH_SHORT).show();
        }
    }

    //Function to check if the user and password entered exists
    //Will return boolean value
    public boolean checkCredentials(String user, String pass){

        database = new DatabaseHandler(Login.this);
        Cursor cursor = database.getReadableDatabase().rawQuery("SELECT * FROM Account WHERE Username = ? AND Password = ?", new String[]{user, pass});

        boolean validCredentials = cursor.getCount() > 0;

        return validCredentials;
    }
}