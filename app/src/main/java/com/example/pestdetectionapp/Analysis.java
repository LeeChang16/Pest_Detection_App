package com.example.pestdetectionapp;

import static java.lang.reflect.Array.getInt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Analysis extends AppCompatActivity {

    RecyclerView recyclerView;
    Ranking_Adapter adapter;
    Ranking_Clicklistener clickListener;
    GraphView graphView;
    TextView most_detected_pest, total_pest, recommendation;
    DatabaseHandler db;
    String pest;
    String intervention;
    int count;
    String str_id;
    RelativeLayout errormsg;
    id_Holder id = id_Holder.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        graphView = findViewById(R.id.linegraph);
        most_detected_pest = findViewById(R.id.most_detected);
        total_pest = findViewById(R.id.total_pest);
        recommendation = findViewById(R.id.recommendation);
        errormsg = findViewById(R.id.errormsg);

        db = new DatabaseHandler(Analysis.this);
        str_id = String.valueOf(id.retrieve_id());


        if(db.check_DataAvailable(String.valueOf(id.retrieve_id()))){

            pest = db.get_MostDetectedPest(String.valueOf(id.retrieve_id()));
            count = db.get_TotalPestCount(String.valueOf(id.retrieve_id()));
            most_detected_pest.setText(pest);
            total_pest.setText(""+count);
            intervention = db.get_Recommendation(pest);
            recommendation.setText(intervention);
        }

        DataPoint[] dataPoints = new DataPoint[]{
//                new DataPoint(0, 1),
//                new DataPoint(1, 3),
//                new DataPoint(2, 1),
//                new DataPoint(3, 3),
//                new DataPoint(4, 1),
//                new DataPoint(5, 3),
                // Add more data points...
        };

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graphView.addSeries(series);

// Customize the x-axis labels as day-month
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // Convert the value (assumed to be an integer) to a Date
                    Date date = new Date((long) value);
                    // Format the date as "dd-MMMM" (e.g., "09-April")
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM", Locale.getDefault());
                    return sdf.format(date);
                }
                return super.formatLabel(value, isValueX);
            }
        });

// Set the viewport bounds (optional)
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(12);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(4);

//        // on below line we are adding data to our graph view.
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
//                // on below line we are adding
//                // each point on our x and y axis.
//                new DataPoint(0, 1),
//                new DataPoint(1, 3),
//                new DataPoint(2, 4),
//                new DataPoint(3, 9),
//                new DataPoint(4, 6),
//                new DataPoint(5, 3),
//                new DataPoint(6, 6),
//                new DataPoint(7, 1),
//                new DataPoint(8, 9),
//                new DataPoint(9, 6),
//                new DataPoint(10, 3),
//                new DataPoint(11, 6),
//                new DataPoint(12, 1),
//        });

        graphView.setTitle("Recent Detections");
        graphView.animate();
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalableY(true);
//        // on below line we are setting
//        // text color to our graph view.
        graphView.setTitleColor(R.color.charcoal);
//
//        // on below line we are setting
//        // our title text size.
        graphView.setTitleTextSize(50);
//
//        // on below line we are adding
//        // data series to our graph view.
//        graphView.addSeries(series);


        //Recyclers View
        List<Ranking_Data> list = new ArrayList<>();
        list = getData();

        recyclerView = findViewById(R.id.ranking_recyclerview);
        clickListener = new Ranking_Clicklistener() {
            @Override
            public void click(int index){
                Toast.makeText(Analysis.this,"clicked item index is "+index, Toast.LENGTH_SHORT).show();
            }
        };
        adapter = new Ranking_Adapter(list, this.getApplication(),clickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        if(list.isEmpty()){
            errormsg.setVisibility(View.VISIBLE);
        }
    }


    private List<Ranking_Data> getData()
    {
//        int current_id = Integer.parseInt(Id);
//        current_id += 1;
//        System.out.println("CURRENT ID: "+Id);
        List<Ranking_Data> list = new ArrayList<>();
//        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM Detected_Pest WHERE User_Id = ? GROUP BY Pest_Name ORDER BY COUNT(Pest_Name) DESC LIMIT 1", new String[]{String.valueOf(id.retrieve_id())});

        Cursor cursor = db.getReadableDatabase().rawQuery(
                "SELECT Pest_Name, COUNT(Pest_Name) AS Pest_Count " +
                        "FROM Detected_Pest " +
                        "WHERE User_Id = ? " +
                        "GROUP BY Pest_Name " +
                        "ORDER BY Pest_Count DESC ",
                new String[]{str_id}
        );

        if(cursor.moveToFirst()){
            do {
//                String name = getString(cursor.getColumnIndex("Pest_Name"));
//                int frequency = cursor.getInt(cursor.getColumnIndexOrThrow("Pest_Count"));
                String name = cursor.getString(0);
                int frequency = cursor.getInt(1);

                // adding all the data to the list array
                list.add(new Ranking_Data(name,frequency));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return list;
    }



}