package com.example.pestdetectionapp;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class library_Adapter extends RecyclerView.Adapter<library_viewHolder> {


    List<library_Data> list = Collections.emptyList();
    Context context;
    library_ClickListener listener;

    public library_Adapter(List<library_Data> list, Context context, library_ClickListener listener){
        this.list = list;
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public library_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout
        View photoView = inflater.inflate(R.layout.library_recycler_layout, parent, false);
        library_viewHolder viewHolder = new library_viewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final library_viewHolder viewHolder, @SuppressLint("RecyclerView") final int position3) {
        final int index = viewHolder.getAdapterPosition();
        viewHolder.name.setText(list.get(position3).name);
        viewHolder.scientificName.setText(list.get(position3).scientific_name);
        viewHolder.Image.setImageBitmap(list.get(position3).Image);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                library_Data selectedItem = list.get(position3);

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
                scientific.setText(selectedItem.scientific_name);
                order.setText(selectedItem.order);
                family.setText(selectedItem.family);
                description.setText(selectedItem.description);
                intervention.setText(selectedItem.intervention);



                //Creating the pop up window
                int width = ScrollView.LayoutParams.WRAP_CONTENT;
                int height = ScrollView.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(popupView, 1022, 1866, true);
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
