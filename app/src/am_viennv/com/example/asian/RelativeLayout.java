package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RelativeLayout extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPass;
    private Button btnLogin;

    private static final String MESSAGE_WARMNING_EMAIL = "Email invalid ! Please enter correct email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_layout);

        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageValidatePass = validatePass(edtPass.getText().toString());
                if(!validateEmail(edtEmail.getText().toString())){
                    makeText(view.getContext(),MESSAGE_WARMNING_EMAIL);
                }
                else if(messageValidatePass!=null){
                    makeText(view.getContext(),messageValidatePass);
                }
                else {
                    makeText(view.getContext(),"Login Success !");
                    switchActivities(MainActivity.class);
                }
            }
        });
    }

    public static  void makeText(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void switchActivities(Class tClass) {
        Intent switchActivityIntent = new Intent(this, tClass);
        startActivity(switchActivityIntent);
    }

    private boolean validateEmail(String email){
        // Phần trước email có thể chứa chữ thường, hoa, chữ số hoặc 1 số kí tự đặt biệt + @gmail.com
        if(email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")){
            return true;
        }
        return false;
    }

    private String validatePass(String pass){
        // Chua ít nhất 1 chữ số, 1 chữ cái, 1 kí tự đặt biệt và <=8 kí tự
        if(pass.length()<8){
            return "Length Pass must >=8 Character";
        }
        else if (!pass.matches(".*\\d.*")){
            return "Password must contain at least 1 number";
        }
        else if(!pass.matches(".*[a-zA-Z].*")){
            return "Password must contain at least 1 normal character";
        }
        else if(!pass.matches(".*[\\W_].*")){
            return "Password must contain at least 1 special chars";
        }
        return null;
    }
}
