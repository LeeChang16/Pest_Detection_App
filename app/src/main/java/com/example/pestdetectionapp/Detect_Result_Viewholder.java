package com.example.pestdetectionapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Detect_Result_Viewholder extends RecyclerView.ViewHolder {

    TextView name;
    TextView scientific;
    ImageView Image;
    View view;

    Detect_Result_Viewholder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.pest_name);
        scientific = itemView.findViewById(R.id.pest_scientific_name);
        Image = itemView.findViewById(R.id.pest_image);
        view = itemView;

    }
}
