package com.example.asian;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyChartView myChartView = findViewById(R.id.myChartView);

        List<SellExpense> data = mockData();
        myChartView.setData(data);
        myChartView.setSalesColor(ContextCompat.getColor(this, R.color.colorLightBlue));
        myChartView.setExpensesColor(ContextCompat.getColor(this, R.color.colorDartRed));
    }

    private List<SellExpense> mockData() {
        List<SellExpense> newListData = new ArrayList<>();
        newListData.add(new SellExpense(1, 70000, 10000));
        newListData.add(new SellExpense(2, 80000, 17000));
        newListData.add(new SellExpense(3, 76000, 20000));
        newListData.add(new SellExpense(4, 90000, 30000));
        newListData.add(new SellExpense(5, 105000, 42000));
        newListData.add(new SellExpense(6, 130000, 80000));
        newListData.add(new SellExpense(7, 170000, 120000));
        newListData.add(new SellExpense(8, 200000, 110000));
        return newListData;
    }
}
