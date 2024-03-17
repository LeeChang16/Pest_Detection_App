package com.example.pestdetectionapp;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class CaptureFragment extends Fragment {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private PreviewView previewView;
    private ImageCapture imageCapture;
    private int currentFlashMode = ImageCapture.FLASH_MODE_ON;

    ImageButton toggleflash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_capture, container, false);


        toggleflash = rootView.findViewById(R.id.toggleFlash);

        previewView = rootView.findViewById(R.id.cameraPreview);


        // Check camera permission
        if (checkCameraPermission()) {
            // Initialize CameraX and other camera-related logic
            initializeCamera();
        } else {
            // Request camera permission
            requestCameraPermission();
        }

        return rootView;
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void initializeCamera() {
        // Initialize CameraX
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindCameraUseCases(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }, requireContext().getMainExecutor());
        }
    }

    private void bindCameraUseCases(ProcessCameraProvider cameraProvider) {
        // Set up preview use case
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Set up image capture use case
        imageCapture = new ImageCapture.Builder()
                .setFlashMode(currentFlashMode) // Set initial flash mode based on your preference (OFF by default)
                .build();

        // Select back camera by default
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();



        // Attach use cases to the camera
        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);



        toggleflash.setOnClickListener(v -> {
            switch (currentFlashMode) {
                case ImageCapture.FLASH_MODE_OFF:
                    currentFlashMode = ImageCapture.FLASH_MODE_ON;
                    // Update UI to reflect flash on
                    toggleflash.setImageResource(R.drawable.baseline_flash_on_24); // Replace with your on icon resource
                    break;
                case ImageCapture.FLASH_MODE_ON:
                    currentFlashMode = ImageCapture.FLASH_MODE_AUTO;
                    // Update UI to reflect flash auto
                    toggleflash.setImageResource(R.drawable.baseline_flash_auto_24); // Replace with your auto icon resource
                    break;
                case ImageCapture.FLASH_MODE_AUTO:
                    currentFlashMode = ImageCapture.FLASH_MODE_OFF;
                    // Update UI to reflect flash off
                    toggleflash.setImageResource(R.drawable.baseline_flash_off_24); // Replace with your off icon resource
                    break;
            }

            // Update ImageCapture flash mode
            imageCapture.setFlashMode(currentFlashMode);
        });


    }

    private void selectGallery(){
        // function to open gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);



    }

    private void captureImage() {
        // Implement image capture logic here
        // Save the captured image to storage or process it further
        // Example: imageCapture.takePicture(executor, imageSavedCallback)

        // Show a "captured" toast message
        Toast.makeText(requireContext(), "Captured", Toast.LENGTH_SHORT).show();
    }

    // Override onRequestPermissionsResult to handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted; proceed with camera setup
                initializeCamera();
            } else {
                // Permission denied; handle accordingly (e.g., show a message)
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}