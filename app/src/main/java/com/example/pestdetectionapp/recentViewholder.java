package com.example.pestdetectionapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Blob;

public class recentViewholder extends RecyclerView.ViewHolder {

    TextView name;
    TextView scientificName;
    ImageView Image;
    View view;

    recentViewholder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.pest_name);
        scientificName = (TextView) itemView.findViewById(R.id.pest_scientific_name);
        Image = (ImageView) itemView.findViewById(R.id.pest_image);
        view = itemView;

    }
}
