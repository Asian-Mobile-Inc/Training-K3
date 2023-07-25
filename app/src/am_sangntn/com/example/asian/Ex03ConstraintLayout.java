
package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Ex03ConstraintLayout extends AppCompatActivity {

    EditText editTextName, getEditTextCMND, editTextInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraintlayout);

        editTextName = findViewById(R.id.editTextName);
        getEditTextCMND = findViewById(R.id.editTextCMND);
        editTextInfor = findViewById(R.id.editTextInfor);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(view -> check());

    }

    void check() {
        if (checkEmpty(editTextInfor)) {
            editTextInfor.setError("Information do not empty");
        }
        if (checkEmpty(editTextName)) {
            editTextName.setError("Name do not empty");
        }
        if (checkEmpty(getEditTextCMND)) {
            getEditTextCMND.setError("CMND do not empty");
        }
        if (checkLength(editTextInfor)) {
            editTextInfor.setError("Information must be >= 100 characters");
        }
    }

    boolean checkEmpty(EditText editText) {
        return (editText.getText().toString().matches(""));
    }

    boolean checkLength(EditText editText) {
        return (editText.getText().toString().length()<=100);
    }
}