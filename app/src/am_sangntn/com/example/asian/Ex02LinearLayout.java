package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Ex02LinearLayout extends AppCompatActivity {

    private EditText mEdtFirstNumber, mEdtSecondNumber;
    private TextView mTvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linearlayout);

        Button btnMul, btnDiv, btnPlus, btnSub;

        mEdtFirstNumber = (EditText) findViewById(R.id.edtFirstNumber);
        mEdtSecondNumber = (EditText) findViewById(R.id.edtSecondNumber);
        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMul = (Button) findViewById(R.id.btnMul);
        btnDiv = (Button) findViewById(R.id.btnDiv);
        mTvResult = (TextView) findViewById(R.id.tvResult);

        btnPlus.setOnClickListener(view -> plus());

        btnSub.setOnClickListener(view -> sub());

        btnMul.setOnClickListener(view -> mul());

        btnDiv.setOnClickListener(view -> div());
    }

    @SuppressLint("SetTextI18n")
    public void plus() {
        if (checkEmpty(mEdtFirstNumber, mEdtSecondNumber)) {
            float firstNumber = Float.parseFloat(mEdtFirstNumber.getText().toString());
            float SecondNumber = Float.parseFloat(mEdtSecondNumber.getText().toString());
            mTvResult.setText(Float.toString(firstNumber + SecondNumber));
        }
    }

    @SuppressLint("SetTextI18n")
    public void sub() {
        if (checkEmpty(mEdtFirstNumber, mEdtSecondNumber)) {
            float firstNumber = Float.parseFloat(mEdtFirstNumber.getText().toString());
            float SecondNumber = Float.parseFloat(mEdtSecondNumber.getText().toString());
            mTvResult.setText(Float.toString(firstNumber - SecondNumber));
        }
    }

    @SuppressLint("SetTextI18n")
    public void mul() {
        if (checkEmpty(mEdtFirstNumber, mEdtSecondNumber)) {
            float firstNumber = Float.parseFloat(mEdtFirstNumber.getText().toString());
            float SecondNumber = Float.parseFloat(mEdtSecondNumber.getText().toString());
            mTvResult.setText(Float.toString(firstNumber * SecondNumber));
        }
    }

    @SuppressLint("SetTextI18n")
    public void div() {
        if (checkEmpty(mEdtFirstNumber, mEdtSecondNumber)) {
            if (!checkZero(mEdtSecondNumber)) {
                float firstNumber = Float.parseFloat(mEdtFirstNumber.getText().toString());
                float SecondNumber = Float.parseFloat(mEdtSecondNumber.getText().toString());
                mTvResult.setText(Float.toString(firstNumber / SecondNumber));
            } else {
                mEdtSecondNumber.setError("Number can't divide 0");
            }
        }
    }

    boolean checkEmpty(EditText mEdtFirstNumber, EditText mEdtSecondNumber) {
        boolean check = true;
        if (mEdtFirstNumber.getText().toString().matches("")) {
            mEdtFirstNumber.setError("Input do not empty");
            check = false;
        }
        if (mEdtSecondNumber.getText().toString().matches("")) {
            mEdtSecondNumber.setError("Input do not empty");
            check = false;
        }
        return check;
    }

    boolean checkZero(EditText editText) {
        return (Float.parseFloat(editText.getText().toString()) == 0);
    }

}
