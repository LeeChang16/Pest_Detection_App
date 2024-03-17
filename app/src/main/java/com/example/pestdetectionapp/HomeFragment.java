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
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class HomeFragment extends Fragment {

    ImageButton GalleryButton, CameraButton;
    int REQUEST_CODE = 100;
    public Bitmap capturedImage;

    TextView temp_reading;


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

        int id =0;
        if (getArguments() != null) {
            id = getArguments().getInt("Id");
        }

        temp_reading.setText(""+id);

        return rootViews;

    }

//    public int Id(){
//        int id = 0;
//        Intent intent = getActivity().getIntent();
//        if (intent != null){
//            id = intent.getIntExtra("IdValue",0);
//        }
//
//        return id;
//
//    }


    private void openCamera(){

        if (checkCameraPermission()) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 3);
        } else {
            requestCameraPermission();
        }
    }

    private void selectGallery(){
        // function to open gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){

            //Use 3 for RequestCode in capturing image.
            if(requestCode == 1) {

                // Get the selected image and store it in a bitmap
                Uri dat = data.getData();

                // Pass the image Uri to Detect.java
                Intent intent = new Intent(requireContext(), Detect.class);
                intent.putExtra("imageUri", dat.toString());
                startActivity(intent);
            }

            else if (requestCode == 3) {
                // Get the Captured image and store it in a Bitmap Variable
                capturedImage = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(capturedImage.getWidth(), capturedImage.getHeight());
                capturedImage = ThumbnailUtils.extractThumbnail(capturedImage, dimension, dimension);

                Intent i = new Intent(requireContext(), Detect.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                capturedImage.compress(Bitmap.CompressFormat.PNG, 50, bs);
                i.putExtra("byteArray", bs.toByteArray());
                startActivity(i);




                // Pass the bitmap image to Detect.java
//                Intent intent = new Intent(requireContext(), Detect.class);
//                intent.putExtra("BitmapImage", capturedImage);
//                startActivity(intent);
                Intent intent = new Intent(requireContext(),Detect.class);
                startActivity(intent);

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


}