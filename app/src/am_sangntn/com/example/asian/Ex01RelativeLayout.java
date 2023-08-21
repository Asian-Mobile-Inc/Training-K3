package com.example.asian;

import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.O_MR1)
public class Ex01RelativeLayout extends AppCompatActivity {

    private EditText mEdtUsername, mEdtPassword;

    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,20}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relativelayout);

        mEdtUsername = (EditText) findViewById(R.id.edtUsername);
        mEdtPassword = (EditText) findViewById(R.id.edtPassword);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> checkValidation());
    }

    public void checkValidation() {
        if (!checkEmail(mEdtUsername)) {
            mEdtUsername.setError("Username must be a Email and >=8 characters!");
        }
        if (!checkPassword(mEdtPassword)) {
            mEdtPassword.setError("Password must contain a number, a Upper character, a special character and >= 8 characters!");
        }
    }

    boolean checkEmail(EditText mEdtUsername) {
        CharSequence email = mEdtUsername.getText().toString();
        return (email.length() > 8 && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean checkPassword(EditText mEdtPassword) {
        return (isValidPassword(mEdtPassword.getText().toString()));
    }

    public static boolean isValidPassword(String s) {
        Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        return passwordPattern.matcher(s).matches();
    }
}
