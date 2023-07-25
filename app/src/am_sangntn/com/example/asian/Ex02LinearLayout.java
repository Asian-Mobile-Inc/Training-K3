package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Ex02LinearLayout extends AppCompatActivity {

    private EditText editText01, editText02;
    private TextView textViewOutput;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linearlayout);

        Button btnMul, btnDiv, btnPlus, btnSub;

        editText01 = (EditText) findViewById(R.id.editText01);
        editText02 = (EditText) findViewById(R.id.editText02);
        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMul = (Button) findViewById(R.id.btnMul);
        btnDiv = (Button) findViewById(R.id.btnDiv);
        textViewOutput = (TextView) findViewById(R.id.textViewOutput);

        btnPlus.setOnClickListener(view -> plus());

        btnSub.setOnClickListener(view -> sub());

        btnMul.setOnClickListener(view -> mul());

        btnDiv.setOnClickListener(view -> div());
    }

    @SuppressLint("SetTextI18n")
    public void plus() {
        if (checkEmpty(editText01, editText02)) {
            float number1 = Float.parseFloat(editText01.getText().toString());
            float number2 = Float.parseFloat(editText02.getText().toString());
            textViewOutput.setText(Float.toString(number1 + number2));
        }
    }

    @SuppressLint("SetTextI18n")
    public void sub() {
        if (checkEmpty(editText01, editText02)) {
            float number1 = Float.parseFloat(editText01.getText().toString());
            float number2 = Float.parseFloat(editText02.getText().toString());
            textViewOutput.setText(Float.toString(number1 - number2));
        }
    }

    @SuppressLint("SetTextI18n")
    public void mul() {
        if (checkEmpty(editText01, editText02)) {
            float number1 = Float.parseFloat(editText01.getText().toString());
            float number2 = Float.parseFloat(editText02.getText().toString());
            textViewOutput.setText(Float.toString(number1 * number2));
        }
    }

    @SuppressLint("SetTextI18n")
    public void div() {
        if (checkEmpty(editText01, editText02)) {
            if (!checkZero(editText02)) {
                float number1 = Float.parseFloat(editText01.getText().toString());
                float number2 = Float.parseFloat(editText02.getText().toString());
                textViewOutput.setText(Float.toString(number1 / number2));
            } else {
                editText02.setError("Number can't divide 0");
            }
        }
    }

    boolean checkEmpty(EditText editText01, EditText editText02) {
        boolean check = true;
        if (editText01.getText().toString().matches("")) {
            editText01.setError("Input do not empty");
            check = false;
        }
        if (editText02.getText().toString().matches("")) {
            editText02.setError("Input do not empty");
            check = false;
        }
        return check;
    }

    boolean checkZero(EditText editText) {
        return (Float.parseFloat(editText.getText().toString())==0);
    }

}
