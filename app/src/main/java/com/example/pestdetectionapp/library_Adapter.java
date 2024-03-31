package com.example.pestdetectionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class library_Adapter extends RecyclerView.Adapter<result_viewHolder> {


    List<library_Data> list = Collections.emptyList();
    Context context;
    ClickListener listener;

    public library_Adapter(List<library_Data> list, Context context, ClickListener listener){
        this.list = list;
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public result_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout
        View photoView = inflater.inflate(R.layout.result_recycler_layout, parent, false);
        result_viewHolder viewHolder = new result_viewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final result_viewHolder viewHolder,final int position) {
        final int index = viewHolder.getAdapterPosition();
        viewHolder.name.setText(list.get(position).name);
        viewHolder.scientificName.setText(list.get(position).scientific_name);
//        viewHolder.Image.setImageBitmap(list.get(position).Image);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                listener.click(index);
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
