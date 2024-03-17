package com.example.pestdetectionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pestdetectionapp.ClickListener;
import com.example.pestdetectionapp.R;
import com.example.pestdetectionapp.pest_data_recycler;
import com.example.pestdetectionapp.result_viewHolder;

import java.util.Collections;
import java.util.List;

public class recentAdapter extends RecyclerView.Adapter<recentViewholder> {


    List<recentdetectionData> list = Collections.emptyList();
    Context context;
    recentClicklistener listener;

    public recentAdapter(List<recentdetectionData> list, Context context, recentClicklistener listener){
        this.list = list;
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public recentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout
        View photoView = inflater.inflate(R.layout.recent_detection_layoute, parent, false);
        recentViewholder recent = new recentViewholder(photoView);
        return recent;
    }

    @Override
    public void onBindViewHolder(@NonNull final recentViewholder viewHolder,final int position) {
//        final index = viewHolder.getAdapterPosition();
        viewHolder.name.setText(list.get(position).name);
        viewHolder.scientificName.setText(list.get(position).scientific_name);
//        viewHolder.Image.setImageBitmap(list.get(position).Image);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                listener.click(index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
