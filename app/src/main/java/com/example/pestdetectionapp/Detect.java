package com.example.pestdetectionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.pestdetectionapp.ml.BestFp16;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Detect extends AppCompatActivity {


    ImageView detectImage;
    TextView prediction, confidences, result;
    int imageSize = 640;
    Bitmap bitmap;
    Bitmap result_bitmap;
    Blob image;
    YOLOv5TFLiteDetector yolOv5TFLiteDetector;
    Paint boxpaint = new Paint();
    Paint texpaint = new Paint();
    DatabaseHandler database;
    String currentDate;
    String currentTime;

    location_Tracker loc = location_Tracker.getInstance();

    String Id = null;
    id_Holder idHolder = id_Holder.getInstance();
    Detect_Result_Adapter adapter;
    RecyclerView recyclerView;
    Detect_Result_Clicklistener clickListener;

    holdBitmap hold = holdBitmap.getInstance();
    NetworkUtil net = new NetworkUtil();
    baseUrl url = baseUrl.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);

        database = new DatabaseHandler(this);

        detectImage = findViewById(R.id.imagetodetect);
        prediction = findViewById(R.id.detection_name);
        confidences = findViewById(R.id.confidence);
        result = findViewById(R.id.PestOrNot);
        //Initialize yolo Detector
        yolOv5TFLiteDetector = new YOLOv5TFLiteDetector();
        yolOv5TFLiteDetector.setModelFile("best-fp16.tflite");
        yolOv5TFLiteDetector.initialModel(this);

        // Get current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        currentTime = timeFormat.format(new Date());

        //for the bounding box
        boxpaint.setStrokeWidth(2);
        boxpaint.setStyle(Paint.Style.STROKE);
        boxpaint.setColor(Color.GREEN);

        //for the class label and confidence level
        texpaint.setTextSize(30);
        texpaint.setColor(Color.GREEN);
        texpaint.setStyle(Paint.Style.FILL);


//        if (getIntent().hasExtra("imageUri")) {
//            //receiving the URI path as String
//            String dat = getIntent().getStringExtra("imageUri");
//            //Converting the String back to Uri
//            Uri imageUri = Uri.parse(dat);
//                try {
//                    // Locating the image path using URI and store the image in bitmap
//                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            //Resize image according to the expected image size of the model
//            bitmap = Bitmap.createScaledBitmap(bitmap,imageSize,imageSize,false);
//
//        } else if (getIntent().hasExtra("byteArray")) {
//            // receive captured image as bytearray and then converting bytearray to bitmap
//            bitmap = BitmapFactory.decodeByteArray(
//                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
//
//            //Resize image according to the expected image size of the model
//            bitmap = Bitmap.createScaledBitmap(bitmap,imageSize,imageSize,false);
//        }
//
//        // Calling the predict function to process image and get results.
//        if (bitmap.equals(null)) {
//
//            Toast.makeText(this, "Null Bitmap",Toast.LENGTH_SHORT).show();
//        }
//        else{
//            predict();
//        }
        bitmap = hold.getImage();
        bitmap = Bitmap.createScaledBitmap(bitmap,imageSize,imageSize,false);

        predict();


        //Recyclers View
        List<Detect_Result_Data> list = new ArrayList<>();
        list = getData();

        recyclerView = findViewById(R.id.recycler_View);
        clickListener = new Detect_Result_Clicklistener() {
            @Override
            public void click(int index){
                Toast.makeText(Detect.this,"clicked item index is "+index, Toast.LENGTH_SHORT).show();
            }
        };
        adapter = new Detect_Result_Adapter(list, this.getApplication(),clickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        
    }

    @SuppressLint("DefaultLocale")
    public void predict(){

        ArrayList<Recognition>recognitions = yolOv5TFLiteDetector.detect(bitmap);
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(mutableBitmap);

        String pestname = "Not a pest";
        String confidence = null;
        String confidence2 = null;
        float highest_confidence= 0;
        for(Recognition recognition: recognitions){
            if(recognition.getConfidence() > 0.40){
                RectF location = recognition.getLocation();
                canvas.drawRect(location,boxpaint);
                confidence2 = String.format("%.2f%%", recognition.getConfidence() * 100.0f);
                confidence = String.format("(%.0f%%) ", recognition.getConfidence() * 100.0f);
                canvas.drawText(recognition.getLabelName()+ ": " + confidence,location.left,location.top,texpaint);

                // getting the ID of the class
                pestname = String.valueOf(recognition.getLabelName());
                Id = String.valueOf(recognition.getLabelId());
                // getting the highest confidence
                float current_confidence = recognition.getConfidence() * 100.0f;

                if(current_confidence>highest_confidence){
                    highest_confidence = current_confidence;
                }

                result.setText("Pest Detected!");
                result.setTextColor(Color.parseColor("#90EE90"));
            }else{
            }
        }

        String confidenceStr = String.format("%.2f",highest_confidence);
        result_bitmap = mutableBitmap;
        detectImage.setImageBitmap(mutableBitmap);
        prediction.setText(pestname);
        confidences.setText(confidenceStr+" %");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        result_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        //insert to Database

        if(highest_confidence>40){
            String id = String.valueOf(idHolder.retrieve_id());
        //Save to server if connected
        Onlinedatabase(pestname,confidenceStr,image,currentTime,currentDate,id,loc.Addressline);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity1.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private List<Detect_Result_Data> getData()
    {
        int current_id = 0;
        if(Id != null){
            current_id = Integer.parseInt(Id) + 1;
        }
        System.out.println("CURRENT ID: "+Id);
        List<Detect_Result_Data> list = new ArrayList<>();
        Cursor cursor = database.getReadableDatabase().rawQuery("SELECT * FROM Pest_Information WHERE Pest_Id = "+current_id+" ", null);

        if(cursor.moveToFirst()){
            do {
                //Specify Which Column to get
                String name = cursor.getString(1);
                String scientific = cursor.getString(2);
                String order = cursor.getString(3);
                String family = cursor.getString(4);
                String description = cursor.getString(5);
                String intervention = cursor.getString(6);
                byte[] imageBytes = cursor.getBlob(7);
                // Decode the byte array stored in sqlite
                Bitmap bitmap = null;
                //Checking if the byte array is not null
                if(imageBytes!=null){
                        bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);}
                // adding all the data to the list array
                list.add(new Detect_Result_Data(bitmap, name,scientific,order, family, description, intervention));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
    public void Onlinedatabase(String name, String confidence, byte [] image, String time, String date,String userid, String location) {
        Handler handler = new Handler();
        String pictureString = Base64.encodeToString(image, Base64.DEFAULT);
        if (isNetworkConnected()) {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    String[] field = new String[9];

                    field[0] = "name";
                    field[1] = "confidence";
                    field[2] = "image";
                    field[3] = "time";
                    field[4] = "date";
                    field[5] = "userid";
                    field[6] = "uploaded";
                    field[7] = "location";
                    field[8] = "method";


                    String[] data = new String[9];

                    data[0] = name;
                    data[1] = confidence;
                    data[2] = pictureString;
                    data[3] = time;
                    data[4] = date;
                    data[5] = userid;
                    data[6] = "1";
                    data[7] = location ;
                    data[8] = hold.getMethod() ;


                    PutData putData = new PutData(url.getUrl() + "detection.php", "POST", field, data);

                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult(); // Assuming the response is a JSON string
                            System.out.println(result);
                            database.insertPest(name, confidence, image, time, date, userid, 1,location, hold.getMethod() );

                        } else {
                            Log.e("Login Error", "Failed to execute request.");
                            // Handle request execution failures
                            Toast.makeText(getApplicationContext(), "Can't connect to server", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Log.e("Login Error", "Failed to start request.");
                        // Handle request initialization failures
                    }

                }
            });

        }else{
            Log.d("ERROR", "Onlinedatabase: Can't connect to server ");
            Toast.makeText(getApplicationContext(), "Results saved offline", Toast.LENGTH_SHORT).show();
            database.insertPest(name, confidence, image, time, date, userid, 0, location, hold.getMethod());
        }




    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



//    public void Onlinedatabase(String name, String confidence, byte[] image, String time, String date, String userid) {
//        if (net.canConnectToUrl(url.getUrl())) {
//            // Create and execute the AsyncTask
//            new InsertPestTask(name, confidence, image, time, date, userid).execute();
//            database.insertPest(name, confidence, image, time, date, userid, 1);
//        } else {
//            Log.d("ERROR", "Onlinedatabase: Can't connect to server ");
//            database.insertPest(name, confidence, image, time, date, userid, 0); // Insert locally if no internet
//        }
//    }
//
//    private class InsertPestTask extends AsyncTask<Void, Void, String> {
//
//        private final String name;
//        private final String confidence;
//        private final byte[] image;
//        private final String time;
//        private final String date;
//        private final String userid;
//
//        public InsertPestTask(String name, String confidence, byte[] image, String time, String date, String userid) {
//            this.name = name;
//            this.confidence = confidence;
//            this.image = image;
//            this.time = time;
//            this.date = date;
//            this.userid = userid;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            String pictureString = Base64.encodeToString(image, Base64.DEFAULT);
//
//            String[] field = new String[7];
//            field[0] = "name";
//            field[1] = "confidence";
//            field[2] = "image";
//            field[3] = "time";
//            field[4] = "date";
//            field[5] = "userid";
//            field[6] = "uploaded";
//
//            String[] data = new String[7];
//            data[0] = name;
//            data[1] = confidence;
//            data[2] = pictureString;
//            data[3] = time;
//            data[4] = date;
//            data[5] = userid;
//            data[6] = "1"; // Set uploaded flag to 1 (uploaded)
//
//            PutData putData = new PutData(url.getUrl() + "detection.php", "POST", field, data);
//
//            if (putData.startPut()) {
//                if (putData.onComplete()) {
//                    String result = putData.getResult(); // Assuming the response is a JSON string
//                    return result; // Return the response from the server (optional)
//                } else {
//                    Log.e("Login Error", "Failed to execute request.");
//                    return null; // Indicate failure (optional)
//                }
//            } else {
//                Log.e("Login Error", "Failed to start request.");
//                return null; // Indicate failure (optional)
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            if (result != null) {
//                // Handle successful response (optional)
//                System.out.println(result); // Or process the JSON response
//            } else {
//                // Handle failed insertion (optional)
//                Toast.makeText(getApplicationContext(), "Failed to insert data online!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }



}