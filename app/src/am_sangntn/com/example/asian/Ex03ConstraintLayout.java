
package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Ex03ConstraintLayout extends AppCompatActivity {

    EditText mEdtName, mEdtCMND, medtEnterInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraintlayout);

        mEdtName = findViewById(R.id.edtName);
        mEdtCMND = findViewById(R.id.edtCMND);
        medtEnterInfor = findViewById(R.id.edtEnterInfor);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(view -> check());

    }

    void check() {
        if (checkEmpty(medtEnterInfor)) {
            medtEnterInfor.setError("Information do not empty");
        }
        if (checkEmpty(mEdtName)) {
            mEdtName.setError("Name do not empty");
        }
        if (checkEmpty(mEdtCMND)) {
            mEdtCMND.setError("CMND do not empty");
        }
        if (checkLength(medtEnterInfor)) {
            medtEnterInfor.setError("Information must be >= 100 characters");
        }
    }

    boolean checkEmpty(EditText editText) {
        return (editText.getText().toString().matches(""));
    }
    
    boolean checkLength(EditText editText) {
        return (editText.getText().toString().length() <= 100);
    }
}