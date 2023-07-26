package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mButtonRelative;
    private Button mButtonLinear;
    private Button mButtonConstraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        listener();
    }

    private void initView(){
        mButtonRelative = findViewById(R.id.btn_relative_layout);
        mButtonLinear = findViewById(R.id.btn_linear_layout);
        mButtonConstraint = findViewById(R.id.btn_constrain_layout);
    }

    private void listener(){
        mButtonRelative.setOnClickListener(view -> new Intent(this, RelativeLayout.class));
        mButtonLinear.setOnClickListener(view -> new Intent(this, LinearLayout.class));
        mButtonConstraint.setOnClickListener(view -> new Intent(this, ConstrainLayout.class));
    }

}