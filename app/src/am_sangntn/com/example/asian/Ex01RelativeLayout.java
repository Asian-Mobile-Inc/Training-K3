package com.example.asian;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.O_MR1)
public class Ex01RelativeLayout extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;

    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,20}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relativelayout);

        editTextEmail = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> checkValidation());
    }

    public void checkValidation() {
        if (!checkEmail(editTextEmail)) {
            editTextEmail.setError("Username must be a Email and >=8 characters!");
        }
        if (!checkPassword(editTextPassword)) {
            editTextPassword.setError("Password must contain a number, a Upper character, a special character and >= 8 characters!");
        }
    }

    boolean checkEmail(EditText editText) {
        CharSequence email = editText.getText().toString();
        return (email.length() > 8 && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean checkPassword(EditText editText) {
        return (isValidPassword(editText.getText().toString()));
    }

    public static boolean isValidPassword(String s) {
        Pattern password_pattern = Pattern.compile(PASSWORD_PATTERN);
        return password_pattern.matcher(s).matches();
    }
}