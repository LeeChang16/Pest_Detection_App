package com.example.pestdetectionapp;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class MenuFragment extends Fragment {

    Button gallery, about_app, signout, analysis, insert;
    EditText pestname, scientificname, order,family, description, intervention;
    ImageView profile_image,editdetails, edit_image, pestimage;
    TextView name, address, occupation, location;
    DatabaseHandler database;
    id_Holder idHolder = id_Holder.getInstance();
    int id = 0;
    location_Tracker track = location_Tracker.getInstance();


    String fullnames = "";
    String Provinces = "";
    String Towns = "";
    String Occupations = "";
    String users ="";
    byte[] byteArray = null;
    Bitmap bitmap;
    Uri dat;
    Bitmap new_image;
    boolean has_selected = false;

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
        analysis = rootView.findViewById(R.id.Analysis);
        insert = rootView.findViewById(R.id.insert);

        profile_image = rootView.findViewById(R.id.profile_images);
        name = rootView.findViewById(R.id.name);
        address = rootView.findViewById(R.id.address);
        occupation = rootView.findViewById(R.id.occupation);
        location = rootView.findViewById(R.id.location);
        editdetails = rootView.findViewById(R.id.editdetails);


        Cursor cursors = database.getReadableDatabase().rawQuery("SELECT * FROM Account WHERE  Account_Id = ?", new String[]{String.valueOf(id)});
        if (cursors.moveToFirst()) {
            users = cursors.getString(1);
        }
        cursors.close();

        Cursor cursor = database.getReadableDatabase().rawQuery("SELECT * FROM Personal_Information WHERE Client_Id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            fullnames = cursor.getString(1);
            Provinces = cursor.getString(2);
            Towns = cursor.getString(3);
            Occupations = cursor.getString(4);
            byteArray = cursor.getBlob(5);
        }
        cursor.close();


        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        new_image = bitmap;

        name.setText(fullnames);
        address.setText(Towns + ", " + Provinces);
        occupation.setText(Occupations);
        profile_image.setImageBitmap(bitmap);
        location.setText(track.getAddressline());

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //        Dummy PopUp to darken the background when the actual popup is displayed
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
                View popupView = inflater.inflate(R.layout.pop_up_dialog, null);

                //Instantiating the views
                Button yes = popupView.findViewById(R.id.yes);
                Button no = popupView.findViewById(R.id.no);






                //Creating the pop up window
//                int width = ScrollView.LayoutParams.WRAP_CONTENT;
//                int height = ScrollView.LayoutParams.WRAP_CONTENT;
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


                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        database = new DatabaseHandler(requireContext());

                        database.delete_user_data(id);
                        idHolder.hold_id(0);
                        popupWindow.dismiss();
                        Intent intent = new Intent(requireContext(), Intro_UI.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();


                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();

                    }
                });

            }
        });




        gallery.setOnClickListener(v -> toLibrary());
        about_app.setOnClickListener(v -> toAbout());
        analysis.setOnClickListener(v -> toAnalysis());
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
                edit_image = popupView.findViewById(R.id.pop_image);
                ImageView change_image = popupView.findViewById(R.id.change_image);
                EditText fullname = popupView.findViewById(R.id.editname);
                EditText province = popupView.findViewById(R.id.editprovince);
                EditText town = popupView.findViewById(R.id.edittown);
                EditText occupation = popupView.findViewById(R.id.editoccupation );
                EditText user = popupView.findViewById(R.id.edituser);
                EditText pass = popupView.findViewById(R.id.editpass);
                EditText matchpass = popupView.findViewById(R.id.confirmpass);
                Button confirm = popupView.findViewById(R.id.edit_confirm);

                edit_image.setImageBitmap(bitmap);
                fullname.setText(fullnames);
                province.setText(Provinces);
                town.setText(Towns);
                occupation.setText(Occupations);
                user.setText(users);



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


                change_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectGallery();
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ByteArrayOutputStream bs = new ByteArrayOutputStream();
                        new_image.compress(Bitmap.CompressFormat.PNG, 100, bs);

                        String newName = fullname.getText().toString();
                        String newProvince = province.getText().toString();
                        String newTown = town.getText().toString();
                        String newOccupation = occupation.getText().toString();
                        String newUser = user.getText().toString();
                        String newPass = pass.getText().toString();
                        String confirmpass = matchpass.getText().toString();
                        String hashpass = null;
                        try {
                            hashpass = hash.hashString(newPass);
                        } catch (NoSuchAlgorithmException e) {
                            throw new RuntimeException(e);
                        }
                        if(confirmpass.equals(newPass)){
                            boolean updatedclient = database.updateData(String.valueOf(id),newName,newProvince,newTown,newOccupation,bs.toByteArray());
                            boolean updatedaccount = database.updateAccountData(String.valueOf(id),newUser,hashpass);

                            if(updatedclient && updatedaccount){
                                Toast.makeText(requireContext(),"Updated Successfully", Toast.LENGTH_SHORT).show();

                                name.setText(newName);
                                address.setText(newTown + ", " + newProvince);
                                occupation.setText(newOccupation);
                                profile_image.setImageBitmap(new_image);
                            }

                        }


                    }
                });

            }
        });


        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LayoutInflater inflater0 = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView0 = inflater0.inflate(R.layout.dummy_popup, null);

                int windth0 = WindowManager.LayoutParams.MATCH_PARENT;
                int height0 = WindowManager.LayoutParams.MATCH_PARENT;
                PopupWindow dummyPopup = new PopupWindow(popupView0, windth0, height0, true);
                dummyPopup.showAtLocation(view, Gravity.CENTER, 0, 0);


                // ACTUAL POPUP WINDOW
                //Inflating the customlayout
                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.pop_insert, null);

                //Instantiating the views
                final RelativeLayout rootLayout = popupView.findViewById(R.id.root_layout);
                edit_image = popupView.findViewById(R.id.pop_image);
                 pestimage = popupView.findViewById(R.id.change_image);
                 pestname = popupView.findViewById(R.id.insertpestname);
                 scientificname = popupView.findViewById(R.id.insertscientificname);
                 order = popupView.findViewById(R.id.insertorder);
                 family = popupView.findViewById(R.id.insertfamily);
                 description = popupView.findViewById(R.id.insertdescription);
                 intervention = popupView.findViewById(R.id.insertintervention);
                Button confirm = popupView.findViewById(R.id.edit_confirm);



                //Creating the pop up window
                int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                //Show the popUpwindow
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (dummyPopup.isShowing()) {
                            dummyPopup.dismiss();
                        }
                    }
                });

                pestimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectGallery();
                    }
                });



                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String id = String.valueOf(database.getCount());
                        if(validate()){

                            ByteArrayOutputStream bs = new ByteArrayOutputStream();
                            new_image.compress(Bitmap.CompressFormat.PNG, 100, bs);

                            String Pestname = pestname.getText().toString();
                            String Scientificname = scientificname.getText().toString();
                            String Order = order.getText().toString();
                            String Family = family.getText().toString();
                            String Description = description.getText().toString();
                            String Intervention = intervention.getText().toString();

                            database.insertPestDetails(id,Pestname,Scientificname,Order,Family,Description,Intervention, bs.toByteArray());
                            popupWindow.dismiss();
                            Toast.makeText(requireContext(),"New Pest Added",Toast.LENGTH_SHORT ).show();

                        }

                        }



                });




            }
        });
        return rootView;
    }

    public void toLogout() {

    }

    public void toAnalysis() {
        Intent intent = new Intent(requireContext(), Analysis.class);
        startActivity(intent);

    }
    private void selectGallery(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 1);

    }

    public void toLibrary(){
        Intent intent = new Intent(requireContext(), pestLibrary.class);
        startActivity(intent);
    }

    public void toAbout(){
        Intent intent = new Intent(requireContext(), about.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){

            //Use 3 for RequestCode in capturing image.
            if(requestCode == 1) {

                // Get the selected image and store it in a bitmap
                assert data != null;
                 dat = data.getData();
                try {
                    new_image = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), dat);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                edit_image.setImageBitmap(new_image);

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    public boolean validate(){
        boolean valid = true;

        String Pestname = pestname.getText().toString();
        String Scientificname = scientificname.getText().toString();
        String Order = order.getText().toString();
        String Family = family.getText().toString();
        String Description = description.getText().toString();
        String Intervention = intervention.getText().toString();


        if(TextUtils.isEmpty(Pestname)){
            pestname.setError("This field is required!");
            valid = false;
        }else{pestname.setError(null);}

        if(TextUtils.isEmpty(Scientificname)){
            scientificname.setError("This field is required!");
            valid = false;
        }else{scientificname.setError(null);}

        if(TextUtils.isEmpty(Order)){
            order.setError("This field is required!");
            valid = false;
        }else{order.setError(null);}

        if(TextUtils.isEmpty(Family)){
            family.setError("This field is required!");
            valid = false;
        }else{family.setError(null);}

        if(TextUtils.isEmpty(Description)){
            description.setError("This field is required!");
            valid = false;
        }else{description.setError(null);}

        if(TextUtils.isEmpty(Intervention)){
            intervention.setError("This field is required!");
            valid = false;
        } else{intervention.setError(null);}

        if(pestimage == null){
            Toast.makeText(requireContext(),"Please Set Pest Image", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

}