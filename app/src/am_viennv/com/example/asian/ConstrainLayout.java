package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConstrainLayout extends AppCompatActivity {

    private Button mButtonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constrain_layout);
        initView();
        handleClick();
    }

    private void initView() {
        mButtonSend = findViewById(R.id.btnSend);
    }

    private void handleClick() {
        mButtonSend.setOnClickListener(view -> {
            String userName = ((EditText) findViewById(R.id.edtUserName)).getText().toString();
            String cardInfo = ((EditText) findViewById(R.id.edtCard)).getText().toString();
            String moreInfo = ((EditText) findViewById(R.id.edtMoreInfo)).getText().toString();
            if (
                    validateInfo(view.getContext(), userName, "UserName") &&
                            validateInfo(view.getContext(), cardInfo, "CardInfo") &&
                            validateInfo(view.getContext(), moreInfo, "MoreInfo")
            ) {
                makeText(view.getContext(), "Login Success !");
                Intent switchActivityIntent = new Intent(view.getContext(), MainActivity.class);
                startActivity(switchActivityIntent);
            }
        });
    }

    private boolean validateInfo(Context context, String text, String nameMessage) {
        if (text.equals("")) {
            makeText(context, "Please enter " + nameMessage);
            return false;
        }
        if (nameMessage.equals("UserName")) {
            if (text.length() < 3) {
                makeText(context, "Length UserName must >=3 character");
                return false;
            }
        } else if (nameMessage.equals("CardInfo")) {
            if (text.length() != 9) {
                makeText(context, "Length Card information must = 9 character");
                return false;
            }
        } else {
            if (text.length() < 100) {
                makeText(context, "Length more information must >= 100 character");
                return false;
            }
        }
        return true;
    }

    public static void makeText(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
