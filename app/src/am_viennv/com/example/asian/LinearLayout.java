package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LinearLayout extends AppCompatActivity {

    private EditText mNumberOneEditText;
    private EditText mNumberTwoEditText;
    private TextView mResultTextView;
    private Button mButtonPlus;
    private Button mButtonSub;
    private Button mButtonMul;
    private Button mButtonDiv;
    private float mNumberOne;
    private float mNumberTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout);
        initView();
        handleClick();
    }

    private void initView() {
        mNumberOneEditText = findViewById(R.id.edtNumberOne);
        mNumberTwoEditText = findViewById(R.id.edtNumberTwo);
        mButtonPlus = findViewById(R.id.btnPlus);
        mButtonSub = findViewById(R.id.btnSub);
        mButtonMul = findViewById(R.id.btnMul);
        mButtonDiv = findViewById(R.id.btnDiv);
        mResultTextView = findViewById(R.id.tvResult);
    }

    @SuppressLint("SetTextI18n")
    private void handleClick() {
        mNumberOne = Float.parseFloat(mNumberOneEditText.getText().toString());
        mNumberTwo = Float.parseFloat(mNumberTwoEditText.getText().toString());
        mButtonPlus.setOnClickListener(view -> mResultTextView.setText(Float.toString(mNumberOne + mNumberTwo)));
        mButtonSub.setOnClickListener(view -> mResultTextView.setText(Float.toString(mNumberOne - mNumberTwo)));
        mButtonMul.setOnClickListener(view -> mResultTextView.setText(Float.toString(mNumberOne * mNumberTwo)));
        mButtonDiv.setOnClickListener(view -> {
            if (!(mNumberTwo == 0)) {
                mResultTextView.setText(Float.toString(mNumberOne / mNumberTwo));
            } else {
                Toast.makeText(this, "Number can't divide 0", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
