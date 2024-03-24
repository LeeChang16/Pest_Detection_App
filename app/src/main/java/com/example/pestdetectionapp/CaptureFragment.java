package com.example.pestdetectionapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class CaptureFragment extends Fragment {
    public static ImageButton viewDetectionBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_capture, container, false);
        viewDetectionBtn = rootView.findViewById(R.id.toggleFlash);
        viewDetectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),DetectorActivity.class);
                startActivity(intent);

            }
        });
        return rootView;
    }

}