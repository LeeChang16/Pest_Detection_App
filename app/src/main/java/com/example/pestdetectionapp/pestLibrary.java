package com.example.pestdetectionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class pestLibrary extends AppCompatActivity {

    library_Adapter adapter;
    RecyclerView recyclerView;
    ClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_library);

        List<library_Data> list = new ArrayList<>();
        list = getData();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        clickListener = new ClickListener() {
            @Override
            public void click(int index){
                Toast.makeText(pestLibrary.this,"clicked item index is "+index, Toast.LENGTH_SHORT).show();
            }
        };
        adapter
                = new library_Adapter(
                list, getApplication(),clickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(pestLibrary.this));
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    // Sample data for RecyclerView
    private List<library_Data> getData()
    {
        List<library_Data> list = new ArrayList<>();
        list.add(new library_Data("StemBorer", "Innonata"));
        list.add(new library_Data("Second Exam", "June 09, 2015"));
        list.add(new library_Data("My Test Exam", "April 27, 2017"));
        list.add(new library_Data("StemBorer", "Innonata"));
        list.add(new library_Data("Second Exam", "June 09, 2015"));
        list.add(new library_Data("My Test Exam", "April 27, 2017"));
        list.add(new library_Data("StemBorer", "Innonata"));
        list.add(new library_Data("Second Exam", "June 09, 2015"));
        list.add(new library_Data("My Test Exam", "April 27, 2017"));
        list.add(new library_Data("StemBorer", "Innonata"));
        list.add(new library_Data("Second Exam", "June 09, 2015"));
        list.add(new library_Data("My Test Exam", "April 27, 2017"));

        return list;
    }

}
