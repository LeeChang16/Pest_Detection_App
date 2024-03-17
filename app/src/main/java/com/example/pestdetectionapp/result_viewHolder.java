package com.example.pestdetectionapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class result_viewHolder extends RecyclerView.ViewHolder {

    TextView name, scientific_name;
    View view;

    result_viewHolder(View itemView) {
        super(itemView);

        name = (new TextView(itemView.getContext()).findViewById(R.id.pest_name));
        scientific_name = (new TextView(itemView.getContext()).findViewById(R.id.pest_scientific_name));

        view = itemView;

    }
}
