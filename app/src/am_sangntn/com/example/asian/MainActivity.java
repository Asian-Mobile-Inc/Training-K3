package com.example.asian;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyChartView myChartView = findViewById(R.id.myChartView);
        List<Data> dataPoints = new ArrayList<>();
        dataPoints.add(new Data("Jan", 50000, 130000));
        dataPoints.add(new Data("Feb", 100000, 20000));
        dataPoints.add(new Data("Mar", 10000, 100000));
        dataPoints.add(new Data("Apr", 70000, 10000));
        dataPoints.add(new Data("May", 40000, 90000));
        dataPoints.add(new Data("Jun", 25000, 60000));

        myChartView.setData(dataPoints);
    }
}