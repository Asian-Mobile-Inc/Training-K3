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
        dataPoints.add(new Data(50, 30));
        dataPoints.add(new Data(30, 100));
        dataPoints.add(new Data(70, 10));
// Thêm dữ liệu của các cột cần vẽ biểu đồ vào dataPoints
        myChartView.setData(dataPoints);
    }
}