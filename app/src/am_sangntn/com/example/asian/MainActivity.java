package com.example.asian;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyChartView myChartView = findViewById(R.id.myChartView);

        myChartView.addBar(new Data("Jan", 50000, 130000));
        myChartView.addBar(new Data("Feb", 100000, 20000));
        myChartView.addBar(new Data("Mar", 7000, 100000));
        myChartView.addBar(new Data("Apr", 70000, 10000));
        myChartView.addBar(new Data("May", 40000, 90000));
        myChartView.addBar(new Data("Jun", 25000, 60000));
    }
}