package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnRelative;
    private Button btnLinear;
    private Button btnConstrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRelative = findViewById(R.id.btn_relative_layout);
        btnLinear = findViewById(R.id.btn_linear_layout);
        btnConstrain = findViewById(R.id.btn_constrain_layout);
        btnRelative.setOnClickListener(this);
        btnLinear.setOnClickListener(this);
        btnConstrain.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_relative_layout:
            {
                switchActivities(RelativeLayout.class);
                break;
            }
            case R.id.btn_linear_layout:
            {
                switchActivities(LinearLayout.class);
                break;
            }
            case R.id.btn_constrain_layout:
            {
                switchActivities(ConstrainLayout.class);
                break;
            }
        }
    }
    private void switchActivities(Class tClass) {
        Intent switchActivityIntent = new Intent(this, tClass);
        startActivity(switchActivityIntent);
    }
}