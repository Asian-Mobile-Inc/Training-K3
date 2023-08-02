package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mButtonRelative;
    private Button mButtonLinear;
    private Button mButtonConstraint;
    private Intent mSwitchActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        listener();
    }

    private void initView() {
        mButtonRelative = findViewById(R.id.btnRelativeLayout);
        mButtonLinear = findViewById(R.id.btnLinearLayout);
        mButtonConstraint = findViewById(R.id.btnConstrainLayout);
    }

    private void listener() {
        mButtonRelative.setOnClickListener(view -> {
            mSwitchActivity = new Intent(this, RelativeLayout.class);
            startActivity(mSwitchActivity);
        });
        mButtonLinear.setOnClickListener(view -> {
            mSwitchActivity = new Intent(this, LinearLayout.class);
            startActivity(mSwitchActivity);
        });
        mButtonConstraint.setOnClickListener(view -> {
            mSwitchActivity = new Intent(this, ConstrainLayout.class);
            startActivity(mSwitchActivity);
        });
    }
}
