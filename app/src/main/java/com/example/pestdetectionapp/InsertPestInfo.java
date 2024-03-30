package com.example.pestdetectionapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InsertPestInfo extends AppCompatActivity {
    EditText id,name,scientific,order,fammily,description,intervention;
    ImageView image;
    Bitmap reducedSize;
    Bitmap bitmap;
    byte [] images;
    DatabaseHandler database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pest_info);

        database = new DatabaseHandler(this);
        id = findViewById(R.id.pesId);
        name = findViewById(R.id.pestName);
        scientific = findViewById(R.id.pestScientific);
        order = findViewById(R.id.pestOrder);
        fammily = findViewById(R.id.pestFamily);
        description = findViewById(R.id.pestDescription);
        intervention = findViewById(R.id.pestIntervention);
        image = findViewById(R.id.pestImage);

    }

    public void Insert(View view){
        String pestid = id.getText().toString();
        String pestname = name.getText().toString();
        String pestscientific = scientific.getText().toString();
        String pestorder = order.getText().toString();
        String pestfamily = fammily.getText().toString();
        String pestdesc = description.getText().toString();
        String pestinter = intervention.getText().toString();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        reducedSize.compress(Bitmap.CompressFormat.PNG, 50, stream);
        images = stream.toByteArray();

        database.insertPestDetails(pestid,pestname,pestscientific,pestorder, pestfamily,pestdesc,pestinter,images);

        id.getText().clear();
        name.getText().clear();
        scientific.getText().clear();
        order.getText().clear();
        fammily.getText().clear();
        description.getText().clear();
        intervention.getText().clear();
        image.setImageBitmap(null);

    }


    public void selectPest(View view) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 2);

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
            image.setImageBitmap(reducedSize);
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