package com.example.pestdetectionapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class library_viewHolder extends RecyclerView.ViewHolder {
     ImageView Image;
     TextView name;
     TextView scientificName;

     View view;

     library_viewHolder(View itemView) {
        super(itemView);
         Image = itemView.findViewById(R.id.pest_image);
         name = itemView.findViewById(R.id.pest_name);
         scientificName = itemView.findViewById(R.id.pest_scientific_name);


        view = itemView;

    }
}
