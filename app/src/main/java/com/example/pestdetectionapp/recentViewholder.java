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
    TextView confidence;
    ImageView Image;
    TextView date;
    TextView time;
    View view;

    recentViewholder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.pest_name);
        confidence = itemView.findViewById(R.id.pest_confidence);
        Image = itemView.findViewById(R.id.pest_image);
        date = itemView.findViewById(R.id.date);
        time = itemView.findViewById(R.id.time);
        view = itemView;

    }
}
