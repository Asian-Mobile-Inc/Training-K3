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
    private Button btnPlus;
    private Button btnSub;
    private Button btnMul;
    private Button btnDiv;

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
        btnPlus = findViewById(R.id.btn_plus);
        btnSub = findViewById(R.id.btn_sub);
        btnMul = findViewById(R.id.btn_mul);
        btnDiv = findViewById(R.id.btn_div);
        mResultTextView = findViewById(R.id.tv_result);
    }

    @SuppressLint("SetTextI18n")
    private void handleClick() {
        String strNumber1 = mNumberOneEditText.getText().toString();
        String strNumber2 = mNumberTwoEditText.getText().toString();
        float num1;
        float num2;
        if (!strNumber1.equals("") && !strNumber2.equals("")) {
            if (!strNumber1.matches(".*[0-9].*") || !strNumber2.matches(".*[0-9].*")) {
                Toast.makeText(this, "Please Enter Number", Toast.LENGTH_SHORT).show();
                return;
            }
            num1 = Float.parseFloat(strNumber1);
            num2 = Float.parseFloat(strNumber2);
            btnPlus.setOnClickListener(view -> mResultTextView.setText(Float.toString(num1 + num2)));
            btnSub.setOnClickListener(view -> mResultTextView.setText(Float.toString(num1 - num2)));
            btnMul.setOnClickListener(view -> mResultTextView.setText(Float.toString(num1 * num2)));
            btnDiv.setOnClickListener(view -> {
                if (Float.parseFloat(strNumber2) == 0) {
                    Toast.makeText(view.getContext(), "Number can't divide 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                mResultTextView.setText(Float.toString(num1 / num2));
            });
        } else
            Toast.makeText(this, "Please enter number !", Toast.LENGTH_SHORT).show();
    }

}