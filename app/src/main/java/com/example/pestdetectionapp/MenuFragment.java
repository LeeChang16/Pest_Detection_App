package com.example.pestdetectionapp;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MenuFragment extends Fragment {

    Button gallery, about_app, edit, signout;
    ImageView profile_image,editdetails;
    TextView name, address, occupation, location;
    DatabaseHandler database;
    id_Holder idHolder = id_Holder.getInstance();
    int id = 0;
    location_Tracker track = location_Tracker.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        database = new DatabaseHandler(requireContext());
        if (getArguments() != null) {
            id = getArguments().getInt("Id");
        }

        gallery = rootView.findViewById(R.id.Gallery);
        about_app = rootView.findViewById(R.id.About_app);
        signout = rootView.findViewById(R.id.logout);
        edit = rootView.findViewById(R.id.edit);

        profile_image = rootView.findViewById(R.id.profile_images);
        name = rootView.findViewById(R.id.name);
        address = rootView.findViewById(R.id.address);
        occupation = rootView.findViewById(R.id.occupation);
        location = rootView.findViewById(R.id.location);
        editdetails = rootView.findViewById(R.id.editdetails);


        String fullname = "";
        String Province = "";
        String Town = "";
        String Occupation = "";
        byte[] byteArray = null;

        Cursor cursor = database.getReadableDatabase().rawQuery("SELECT * FROM Personal_Information WHERE Client_Id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            fullname = cursor.getString(1);
            Province = cursor.getString(2);
            Town = cursor.getString(3);
            Occupation = cursor.getString(4);
            byteArray = cursor.getBlob(5);
        }
        cursor.close();

        Bitmap bitmap;
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        name.setText(fullname);
        address.setText(Town + ", " + Province);
        occupation.setText(Occupation);
        profile_image.setImageBitmap(bitmap);
        location.setText(track.getAddressline());

        signout.setOnClickListener(v -> toLogout());
        gallery.setOnClickListener(v -> toLibrary());
        about_app.setOnClickListener(v -> toAbout());
        edit.setOnClickListener(v -> toEdit());
        editdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater0 = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView0 = inflater0.inflate(R.layout.dummy_popup, null);

                int windth0 = WindowManager.LayoutParams.MATCH_PARENT;
                int height0 = WindowManager.LayoutParams.MATCH_PARENT;
                PopupWindow dummyPopup = new PopupWindow(popupView0,windth0,height0,true);
                dummyPopup.showAtLocation(view, Gravity.CENTER, 0, 0);



                // ACTUAL POPUP WINDOW
                //Inflating the customlayout
                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.pop_edit_profile, null);

                //Instantiating the views
                final RelativeLayout rootLayout = popupView.findViewById(R.id.root_layout);
                ImageView image = popupView.findViewById(R.id.pop_image);
                TextView pestname = popupView.findViewById(R.id.pop_name);
                TextView confidence = popupView.findViewById(R.id.pop_confidence);
                TextView date_time = popupView.findViewById(R.id.pop_time_detected);

//                image.setImageBitmap(selectedItem.Image);
//                pestname.setText(selectedItem.name);
//                confidence.setText(selectedItem.confidence);
//                date_time.setText("Date & Time detected: "+selectedItem.date+" / "+selectedItem.time);

                //Creating the pop up window
                int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
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


        return rootView;
    }

    public void toLogout() {
        database = new DatabaseHandler(requireContext());
            idHolder.hold_id(0);
            database.turn_off_active_status(id);

            Intent intent = new Intent(requireContext(), Intro_UI.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();

    }

    public void toLibrary(){
        Intent intent = new Intent(requireContext(), pestLibrary.class);
        startActivity(intent);
    }

    public void toAbout(){
        Intent intent = new Intent(requireContext(), about.class);
        startActivity(intent);
    }

    public void toEdit(){
        Intent intent = new Intent(requireContext(), InsertPestInfo.class);
        startActivity(intent);
    }


}