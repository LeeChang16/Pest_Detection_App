package com.example.pestdetectionapp;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;
import static android.content.Intent.parseIntent;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    ImageButton GalleryButton, CameraButton;
    int REQUEST_CODE = 100;
    public Bitmap capturedImage;

    TextView temp_reading;
    bitmapHolder holder;

    recentAdapter adapter;
    RecyclerView recyclerView;
    recentClicklistener clickListener;
    int id =0;
    DatabaseHandler db;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViews = inflater.inflate(R.layout.fragment_home, container, false);

        GalleryButton = rootViews.findViewById(R.id.togallery);
        CameraButton = rootViews.findViewById(R.id.tocamera);
        temp_reading = rootViews.findViewById(R.id.temp_reading);


        GalleryButton.setOnClickListener(v ->selectGallery());
        CameraButton.setOnClickListener(v ->openCamera());

        db = new DatabaseHandler(requireContext());



        if (getArguments() != null) {
            id = getArguments().getInt("Id");
        }

        temp_reading.setText(""+id);


        //Recyclers View
        List<recentdetectionData> list = new ArrayList<>();
        list = getData();

        recyclerView = (RecyclerView)rootViews.findViewById(R.id.recent);
        clickListener = new recentClicklistener() {
            @Override
            public void click(int index){
                Toast.makeText(requireContext(),"clicked item index is "+index, Toast.LENGTH_SHORT).show();
            }
        };
        adapter = new recentAdapter(list, getActivity().getApplication(),clickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext()));




        return rootViews;

    }

    private void openCamera(){

        if (checkCameraPermission()) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 3);
        } else {
            requestCameraPermission();
        }
    }

    private void selectGallery(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){

            //Use 3 for RequestCode in capturing image.
            if(requestCode == 1) {

                // Get the selected image and store it in a bitmap
                assert data != null;
                Uri dat = data.getData();

                // Pass the image Uri to Detect.java
                Intent intent = new Intent(requireContext(), Detect.class);
                assert dat != null;
                intent.putExtra("imageUri", dat.toString());
                startActivity(intent);
            }

            else if (requestCode == 3) {
                // Get the Captured image and store it in a Bitmap Variable
                assert data != null;
                capturedImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                assert capturedImage != null;
                int dimension = Math.min(capturedImage.getWidth(), capturedImage.getHeight());
                capturedImage = ThumbnailUtils.extractThumbnail(capturedImage, dimension, dimension);

                Intent i = new Intent(requireContext(), Detect.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                capturedImage.compress(Bitmap.CompressFormat.PNG, 100, bs);
                i.putExtra("byteArray", bs.toByteArray());
                startActivity(i);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Check if the Camera Permission Is Granted
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    // Request Permission for the Camera
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
    }


    private List<recentdetectionData> getData()
    {
        List<recentdetectionData> list = new ArrayList<>();
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM Detected_Pest WHERE User_Id = "+id+" ORDER BY Pest_Id DESC", null);

        if(cursor.moveToFirst()){
            do {
                //Specify Which Column to get
                String name = cursor.getString(1);
                String confidence = cursor.getString(2);
                byte[] imageBytes = cursor.getBlob(3);
                String time = cursor.getString(4);
                String date = cursor.getString(5);
                // Decode the byte array stored in sqlite
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                // adding all the data to the list array
                list.add(new recentdetectionData(name,confidence+" %",bitmap,time,date));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


}