package com.example.pestdetectionapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class realtime_result_viewHolder extends RecyclerView.ViewHolder {

     ImageView Image;
     TextView name;
     TextView scientific;
     View view;

     realtime_result_viewHolder(View itemView) {
        super(itemView);

         Image = itemView.findViewById(R.id.pest_IMAGE);
         name = itemView.findViewById(R.id.pest_NAME);
         scientific = itemView.findViewById(R.id.pest_SCIENTIFIC);
         view = itemView;
    }
}
