package com.example.pestdetectionapp;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class Detect_Result_Adapter extends RecyclerView.Adapter<Detect_Result_Viewholder> {


    List<Detect_Result_Data> list = Collections.emptyList();
    Context context;
    Detect_Result_Clicklistener listener;

    public Detect_Result_Adapter(List<Detect_Result_Data> list, Context context, Detect_Result_Clicklistener listener){
        this.list = list;
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public Detect_Result_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout
        View photoView = inflater.inflate(R.layout.result_recycler_layout, parent, false);
        Detect_Result_Viewholder recent = new Detect_Result_Viewholder(photoView);
        return recent;
    }

    @Override
    public void onBindViewHolder(@NonNull final Detect_Result_Viewholder viewHolder, @SuppressLint("RecyclerView") final int position2) {
        final int index = viewHolder.getAdapterPosition();

        viewHolder.Image.setImageBitmap(list.get(position2).Image);
        viewHolder.name.setText(list.get(position2).name);
        viewHolder.scientific.setText(list.get(position2).scientific);

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Detect_Result_Data selectedItem = list.get(position2);

                //Dummy PopUp to darken the background when the actual popup is displayed
                //INflating the customlayout
                LayoutInflater inflater0 = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView0 = inflater0.inflate(R.layout.dummy_popup, null);

                int windth0 = WindowManager.LayoutParams.MATCH_PARENT;
                int height0 = WindowManager.LayoutParams.MATCH_PARENT;
                PopupWindow dummyPopup = new PopupWindow(popupView0,windth0,height0,true);
                dummyPopup.showAtLocation(view, Gravity.CENTER, 0, 0);


                // ACTUAL POPUP WINDOW
               //Inflating the customlayout
                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.pop_pest_info, null);

              //Instantiating the views
                ImageView image = popupView.findViewById(R.id.pestImage);
                TextView name = popupView.findViewById(R.id.pestName);
                TextView scientific = popupView.findViewById(R.id.pestScientific);
                TextView order = popupView.findViewById(R.id.pestOrder);
                TextView family = popupView.findViewById(R.id.pestFamily);
                TextView description = popupView.findViewById(R.id.pestDescription);
                TextView intervention = popupView.findViewById(R.id.pestIntervention);


                image.setImageBitmap(selectedItem.Image);
                name.setText(selectedItem.name);
                scientific.setText(selectedItem.scientific);
                order.setText(selectedItem.order);
                family.setText(selectedItem.family);
                description.setText(selectedItem.description);
                intervention.setText(selectedItem.intervention);



               //Creating the pop up window
                int width = ScrollView.LayoutParams.WRAP_CONTENT;
                int height = ScrollView.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(popupView, 1022, 1636, true);
                //Show the popUpwindow
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if(dummyPopup.isShowing()){
                            dummyPopup.dismiss();
                        }
                    }
                });

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
