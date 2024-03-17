package com.example.pestdetectionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class pestLibrary extends AppCompatActivity {

    recyclerAdapter adapter;
    RecyclerView recyclerView;
    ClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_library);

        List<pest_data_recycler> list = new ArrayList<>();
        list = getData();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        clickListener = new ClickListener() {
            @Override
            public void click(int index){
                Toast.makeText(pestLibrary.this,"clicked item index is "+index, Toast.LENGTH_SHORT).show();
            }
        };
        adapter
                = new recyclerAdapter(
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
    private List<pest_data_recycler> getData()
    {
        List<pest_data_recycler> list = new ArrayList<>();
        list.add(new pest_data_recycler("StemBorer", "Innonata"));
        list.add(new pest_data_recycler("Second Exam", "June 09, 2015"));
        list.add(new pest_data_recycler("My Test Exam", "April 27, 2017"));
        list.add(new pest_data_recycler("StemBorer", "Innonata"));
        list.add(new pest_data_recycler("Second Exam", "June 09, 2015"));
        list.add(new pest_data_recycler("My Test Exam", "April 27, 2017"));
        list.add(new pest_data_recycler("StemBorer", "Innonata"));
        list.add(new pest_data_recycler("Second Exam", "June 09, 2015"));
        list.add(new pest_data_recycler("My Test Exam", "April 27, 2017"));
        list.add(new pest_data_recycler("StemBorer", "Innonata"));
        list.add(new pest_data_recycler("Second Exam", "June 09, 2015"));
        list.add(new pest_data_recycler("My Test Exam", "April 27, 2017"));

        return list;
    }

}
