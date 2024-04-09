package com.example.pestdetectionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class pestLibrary extends AppCompatActivity {

    library_Adapter adapter;
    RecyclerView recyclerView;
    library_ClickListener clickListener;
    DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_library);

        database = new DatabaseHandler(this);

        List<library_Data> list = new ArrayList<>();
        list = getDatas();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        clickListener = new library_ClickListener() {
            @Override
            public void click(int index) {
                Toast.makeText(pestLibrary.this, "clicked item index is " + index, Toast.LENGTH_SHORT).show();
            }
        };
        adapter
                = new library_Adapter(
                list, getApplication(), clickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(pestLibrary.this));

    }


    private List<library_Data> getDatas()
    {
//        int current_id = Integer.parseInt(Id);
//        current_id += 1;
//        System.out.println("CURRENT ID: "+Id);
        List<library_Data> list = new ArrayList<>();
        Cursor cursor = database.getReadableDatabase().rawQuery("SELECT * FROM Pest_Information", null);

        if(cursor.moveToFirst()){
            do {
                //Specify Which Column to get
                String name = cursor.getString(1);
                String scientific = cursor.getString(2);
                String order = cursor.getString(3);
                String family = cursor.getString(4);
                String description = cursor.getString(5);
                String intervention = cursor.getString(6);
                byte[] imageBytes = cursor.getBlob(7);
                // Decode the byte array stored in sqlite
                Bitmap bitmap = null;
                //Checking if the byte array is not null
                if(imageBytes!=null){
                    bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);}
                // adding all the data to the list array
                list.add(new library_Data(bitmap, name,scientific,order, family, description, intervention));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return list;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Sample data for RecyclerView
//    private List<library_Data> getData()
//    {
//        List<library_Data> list = new ArrayList<>();
//        list.add(new library_Data("StemBorer", "Innonata"));
//        list.add(new library_Data("Second Exam", "June 09, 2015"));
//        list.add(new library_Data("My Test Exam", "April 27, 2017"));
//        list.add(new library_Data("StemBorer", "Innonata"));
//        list.add(new library_Data("Second Exam", "June 09, 2015"));
//        list.add(new library_Data("My Test Exam", "April 27, 2017"));
//        list.add(new library_Data("StemBorer", "Innonata"));
//        list.add(new library_Data("Second Exam", "June 09, 2015"));
//        list.add(new library_Data("My Test Exam", "April 27, 2017"));
//        list.add(new library_Data("StemBorer", "Innonata"));
//        list.add(new library_Data("Second Exam", "June 09, 2015"));
//        list.add(new library_Data("My Test Exam", "April 27, 2017"));
//
//        return list;
//}


}
