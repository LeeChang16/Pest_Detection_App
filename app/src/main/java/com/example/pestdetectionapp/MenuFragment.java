package com.example.pestdetectionapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuFragment extends Fragment {

    Button gallery, about_app, edit, signout;
    ImageView profile_image;
    TextView name, address, occupation;
    DatabaseHandler database;
    int id = 0;

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


        signout.setOnClickListener(v -> toLogout());
        gallery.setOnClickListener(v -> toLibrary());
        about_app.setOnClickListener(v -> toAbout());
        edit.setOnClickListener(v -> toEdit());


        return rootView;
    }

    public void toLogout() {
        database = new DatabaseHandler(requireContext());

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
//        Intent intent = new Intent(requireContext(), about.class);
//        startActivity(intent);
    }

}