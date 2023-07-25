package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnRelative = findViewById(R.id.btn_relative_layout);
        Button btnLinear = findViewById(R.id.btn_linear_layout);
        Button btnConstrain = findViewById(R.id.btn_constrain_layout);
        btnRelative.setOnClickListener(this);
        btnLinear.setOnClickListener(this);
        btnConstrain.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Intent switchActivityIntent = null;
        switch (view.getId()) {
            case R.id.btn_relative_layout: {
                switchActivityIntent = new Intent(this, RelativeLayout.class);
                break;
            }
            case R.id.btn_linear_layout: {
                switchActivityIntent = new Intent(this, LinearLayout.class);
                break;
            }
            case R.id.btn_constrain_layout: {
                switchActivityIntent = new Intent(this, ConstrainLayout.class);
                break;
            }
        }
        if (switchActivityIntent != null) {
            startActivity(switchActivityIntent);
        }
    }
}