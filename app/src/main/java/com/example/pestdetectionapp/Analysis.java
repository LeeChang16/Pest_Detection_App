package com.example.pestdetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Analysis extends AppCompatActivity {


    GraphView graphView;
    TextView most_detected_pest, total_pest, recommendation;
    DatabaseHandler db;
    String pest;
    String intervention;
    int count;

    id_Holder id = id_Holder.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        graphView = findViewById(R.id.linegraph);
        most_detected_pest = findViewById(R.id.most_detected);
        total_pest = findViewById(R.id.total_pest);
        recommendation = findViewById(R.id.recommendation);

        db = new DatabaseHandler(Analysis.this);



        if(db.check_DataAvailable(String.valueOf(id.retrieve_id()))){

            pest = db.get_MostDetectedPest(String.valueOf(id.retrieve_id()));
            count = db.get_TotalPestCount(String.valueOf(id.retrieve_id()));
            most_detected_pest.setText(pest);
            total_pest.setText(""+count);
            intervention = db.get_Recommendation(pest);
            recommendation.setText(intervention);
        }


        // on below line we are adding data to our graph view.
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                // on below line we are adding
                // each point on our x and y axis.
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, 9),
                new DataPoint(4, 6),
                new DataPoint(5, 3),
                new DataPoint(6, 6),
                new DataPoint(7, 1),
        });

        graphView.setTitle("This Week");

        // on below line we are setting
        // text color to our graph view.
        graphView.setTitleColor(R.color.purple_200);

        // on below line we are setting
        // our title text size.
        graphView.setTitleTextSize(18);

        // on below line we are adding
        // data series to our graph view.
        graphView.addSeries(series);
    }
}