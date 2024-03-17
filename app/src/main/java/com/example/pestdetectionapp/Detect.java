package com.example.pestdetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pestdetectionapp.ml.BestFp16;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Blob;
import java.util.ArrayList;

public class Detect extends AppCompatActivity {


    ImageView detectImage;
    TextView prediction;
    int imageSize = 640;
    Bitmap bitmap;
    Blob image;

    YOLOv5TFLiteDetector yolOv5TFLiteDetector;
    Paint boxpaint = new Paint();
    Paint texpaint = new Paint();
    DatabaseHandler database;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);

        database = new DatabaseHandler(this);

        detectImage = findViewById(R.id.imagetodetect);
        prediction = findViewById(R.id.detection_name);

        yolOv5TFLiteDetector = new YOLOv5TFLiteDetector();
        yolOv5TFLiteDetector.setModelFile("best-fp16.tflite");
        yolOv5TFLiteDetector.initialModel(this);


        //for the bounding box
        boxpaint.setStrokeWidth(2);
        boxpaint.setStyle(Paint.Style.STROKE);
        boxpaint.setColor(Color.GREEN);

        //for the class label and confidence level
        texpaint.setTextSize(30);
        texpaint.setColor(Color.GREEN);
        texpaint.setStyle(Paint.Style.FILL);


        if (getIntent().hasExtra("imageUri")) {
            //receiving the URI path as String
            String dat = getIntent().getStringExtra("imageUri");
            //Converting the String back to Uri
            Uri imageUri = Uri.parse(dat);
                try {
                    // Locating the image path using URI and store the image in bitmap
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            //Resize image according to the expected image size of the model
            bitmap = Bitmap.createScaledBitmap(bitmap,imageSize,imageSize,false);

        } else if (getIntent().hasExtra("byteArray")) {
            // receive captured image as bytearray and then converting bytearray to bitmap
            bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);

            //Resize image according to the expected image size of the model
            bitmap = Bitmap.createScaledBitmap(bitmap,imageSize,imageSize,false);
        }

        // Calling the predict function to process image and get results.
        predict();
        
    }

    public void predict(){

        ArrayList<Recognition>recognitions = yolOv5TFLiteDetector.detect(bitmap);
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(mutableBitmap);
        String Id = null;
        String pestname = null;
        String confidence = null;

        for(Recognition recognition: recognitions){
            if(recognition.getConfidence() > 0.45
            ){
                RectF location = recognition.getLocation();
                canvas.drawRect(location,boxpaint);

                confidence = String.format("(%.0f%%) ", recognition.getConfidence() * 100.0f);
                canvas.drawText(recognition.getLabelName()+ ": " + confidence,location.left,location.top,texpaint);

                // getting the ID of the class
                confidence = String.valueOf(recognition.getConfidence());
                Id = String.valueOf(recognition.getLabelId());

            }
        }

        detectImage.setImageBitmap(mutableBitmap);
        prediction.setText(Id);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);

        //insert to Database
        database.insertPest(Id,pestname, confidence, bitmap );
    }
}