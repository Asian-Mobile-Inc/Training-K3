package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LinearLayout extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNumber1;
    private EditText edtNumber2;
    private Button btnPlus;
    private Button btnSub;
    private Button btnMul;
    private Button btnDiv;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout);

        btnPlus = findViewById(R.id.btn_plus);
        btnSub  = findViewById(R.id.btn_sub);
        btnMul = findViewById(R.id.btn_mul);
        btnDiv  = findViewById(R.id.btn_div);

        edtNumber1 = findViewById(R.id.edt_num1);
        edtNumber2 = findViewById(R.id.edt_num2);

        tvResult = findViewById(R.id.tv_result);
        btnPlus.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMul.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        String strNumber1 = edtNumber1.getText().toString();
        String strNumber2 = edtNumber2.getText().toString();
        float number_1;
        float number_2;
        if(!strNumber1.equals("")&&!strNumber2.equals(""))
        {
            if (!validateNumber(strNumber1) || !validateNumber(strNumber2)) {
                Toast.makeText(view.getContext(), "Please Enter Number", Toast.LENGTH_SHORT).show();
                return;
            }
            number_1 = Float.parseFloat(strNumber1);
            number_2 = Float.parseFloat(strNumber2);
            if(Float.parseFloat(strNumber2)==0){
                Toast.makeText(view.getContext(), "Number can't divide 0", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (view.getId()){
                case R.id.btn_plus:
                {
                    tvResult.setText(Float.toString(number_1+number_2));
                    break;
                }
                case R.id.btn_sub:
                {
                    tvResult.setText(Float.toString(number_1-number_2));
                    break;
                }
                case R.id.btn_mul:
                {
                    tvResult.setText(Float.toString(number_1*number_2));
                    break;
                }
                case R.id.btn_div:
                {
                    tvResult.setText(Float.toString(number_1/number_2));
                    break;
                }
            }
        } else
            Toast.makeText(view.getContext(), "Please enter number !", Toast.LENGTH_SHORT).show();
    }

    private boolean validateNumber(String number_1){
        if (number_1.matches(".*[a-zA-Z].*")) {
            return false;
        }
        return true;
    }
}