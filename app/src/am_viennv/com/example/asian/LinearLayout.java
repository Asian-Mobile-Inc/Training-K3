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
        mNumberOneEditText = findViewById(R.id.edt_num1);
        mNumberTwoEditText = findViewById(R.id.edt_num2);
        mButtonPlus = findViewById(R.id.btn_plus);
        mButtonSub = findViewById(R.id.btn_sub);
        mButtonMul = findViewById(R.id.btn_mul);
        mButtonDiv = findViewById(R.id.btn_div);
        mResultTextView = findViewById(R.id.tv_result);
    }

    @SuppressLint("SetTextI18n")
    private void handleClick() {
        mButtonPlus.setOnClickListener(view -> {
            if (checkNumber()) {
                mResultTextView.setText(Float.toString(mNumberOne + mNumberTwo));
            }
        });
        mButtonSub.setOnClickListener(view -> {
            if (checkNumber()) {
                mResultTextView.setText(Float.toString(mNumberOne - mNumberTwo));
            }
        });
        mButtonMul.setOnClickListener(view -> {
            if (checkNumber()) {
                mResultTextView.setText(Float.toString(mNumberOne * mNumberTwo));
            }
        });
        mButtonDiv.setOnClickListener(view -> {
            if (checkNumber() && !mNumberTwoEditText.getText().toString().equals("0")) {
                mResultTextView.setText(Float.toString(mNumberOne / mNumberTwo));
            } else {
                Toast.makeText(this, "Number can't divide 0", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkNumber() {
        String strNumber1 = mNumberOneEditText.getText().toString();
        String strNumber2 = mNumberTwoEditText.getText().toString();
        if (strNumber1.matches(".*[0-9].*") && strNumber2.matches(".*[0-9].*")) {
            mNumberOne = Float.parseFloat(strNumber1);
            mNumberTwo = Float.parseFloat(strNumber2);
            return true;
        } else {
            Toast.makeText(this, "Please Enter Number", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}