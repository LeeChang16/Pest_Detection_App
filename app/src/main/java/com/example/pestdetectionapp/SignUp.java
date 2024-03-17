package com.example.pestdetectionapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignUp extends AppCompatActivity {

    Bitmap bitmap;
    Bitmap reducedSize;
    byte [] image;
    ImageView profile_pic;
    EditText fullname,province,town,occupation,username,password;
    DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        profile_pic = findViewById(R.id.profile_pic);
        fullname = findViewById(R.id.fullname);
        province = findViewById(R.id.province);
        town = findViewById(R.id.town);
        occupation = findViewById(R.id.occupation);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

    }

    public void selectImage(View view) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 2);


    }

    public void signUp(View view) {

        database = new DatabaseHandler(getApplicationContext());

        String name = fullname.getText().toString();
        String provinces = province.getText().toString();
        String towns = town.getText().toString();
        String occupations = occupation.getText().toString();
        String user = username.getText().toString();
        String pass = password.getText().toString();

//        ByteArrayOutputStream bs = new ByteArrayOutputStream();
//        capturedImage.compress(Bitmap.CompressFormat.PNG, 50, bs);

        // Convert the bitmap image to a byte array


        if(checkCredentials(user,pass)){
            Toast.makeText(getApplicationContext(),"Credential Already Exist!",Toast.LENGTH_SHORT).show();
        }else{

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            reducedSize.compress(Bitmap.CompressFormat.PNG, 50, stream);
            image = stream.toByteArray();

            database.insertClient(name,provinces,towns,occupations,image);
            database.insertAccount(user, pass, 0);
            Toast.makeText(getApplicationContext(),"Account Created!",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkCredentials(String user, String pass){

        database = new DatabaseHandler(getApplicationContext());
        Cursor cursor = database.getReadableDatabase().rawQuery("SELECT * FROM Account WHERE Username = ? AND Password = ?", new String[]{user, pass});

        boolean validCredentials = cursor.getCount() > 0;

        return validCredentials;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (resultCode == RESULT_OK) {

            if (requestCode == 2) {

                // Get the selected image and store it in a bitmap
                Uri dat = data.getData();
                try {
                    // Locating the image path using URI and store the image in bitmap
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            reducedSize = reduceImageSize(bitmap,240,240);
            profile_pic.setImageBitmap(reducedSize);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public Bitmap reduceImageSize(Bitmap originalBitmap, int maxWidth, int maxHeight) {
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();

        // Calculate the correct scale size
        float scale = Math.min(((float)maxWidth / originalWidth), ((float)maxHeight / originalHeight));

        // Create a matrix for manipulation
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap with the same color model as the original bitmap, but scaled down.
        Bitmap scaledBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalWidth, originalHeight, matrix, true);

        return scaledBitmap;
    }



}