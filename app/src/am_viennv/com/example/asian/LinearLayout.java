package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LinearLayout extends AppCompatActivity implements View.OnClickListener {

    private EditText mNumberOneEditText;
    private EditText mNumberTwoEditText;
    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout);

        Button btnPlus = findViewById(R.id.btn_plus);
        Button btnSub = findViewById(R.id.btn_sub);
        Button btnMul = findViewById(R.id.btn_mul);
        Button btnDiv = findViewById(R.id.btn_div);

        mNumberOneEditText = findViewById(R.id.edt_num1);
        mNumberTwoEditText = findViewById(R.id.edt_num2);

        mResultTextView = findViewById(R.id.tv_result);
        btnPlus.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMul.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
    }


    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View view) {
        String strNumber1 = mNumberOneEditText.getText().toString();
        String strNumber2 = mNumberTwoEditText.getText().toString();
        float num1;
        float num2;
        if (!strNumber1.equals("") && !strNumber2.equals("")) {
            if (!validateNumber(strNumber1) || !validateNumber(strNumber2)) {
                Toast.makeText(view.getContext(), "Please Enter Number", Toast.LENGTH_SHORT).show();
                return;
            }
            num1 = Float.parseFloat(strNumber1);
            num2 = Float.parseFloat(strNumber2);
            switch (view.getId()) {
                case R.id.btn_plus: {
                    mResultTextView.setText(Float.toString(num1 + num2));
                    break;
                }
                case R.id.btn_sub: {
                    mResultTextView.setText(Float.toString(num1 - num2));
                    break;
                }
                case R.id.btn_mul: {
                    mResultTextView.setText(Float.toString(num1 * num2));
                    break;
                }
                case R.id.btn_div: {
                    if (Float.parseFloat(strNumber2) == 0) {
                        Toast.makeText(view.getContext(), "Number can't divide 0", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    mResultTextView.setText(Float.toString(num1 / num2));
                    break;
                }
            }
        } else
            Toast.makeText(view.getContext(), "Please enter number !", Toast.LENGTH_SHORT).show();
    }

    private boolean validateNumber(String number) {
        return number.matches(".*[0-9].*");
    }
}