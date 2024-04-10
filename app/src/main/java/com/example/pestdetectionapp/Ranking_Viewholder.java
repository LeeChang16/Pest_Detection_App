package com.example.pestdetectionapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Ranking_Viewholder extends RecyclerView.ViewHolder {

    TextView name;
    TextView frequency;
    View view;

    Ranking_Viewholder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.pestname);
        frequency = itemView.findViewById(R.id.pestfrequency);
        view = itemView;

    }
}
