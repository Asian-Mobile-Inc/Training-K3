package com.example.asian;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mEdtName, mEdtAge;

    private List<User> mListUser;

    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdtAge = findViewById(R.id.edtUserAge);
        mEdtName = findViewById(R.id.edtUserName);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnDeleteAll = findViewById(R.id.btnDeleteAll);
        Button btnShowAll = findViewById(R.id.btnShowAll);
        RecyclerView rcvUser = findViewById(R.id.rcvListUsers);

        mUserAdapter = new UserAdapter();
        mUserAdapter.setData(mListUser);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvUser.setLayoutManager(linearLayoutManager);
        rcvUser.setAdapter(mUserAdapter);

        btnShowAll.setOnClickListener(view -> {
            mListUser = UserDatabase.getInstance(this).userDao().getListUser();
            mUserAdapter.setData(mListUser);
        });

        btnAdd.setOnClickListener(view -> {
            if (mEdtName.getText().toString().equals("") || (mEdtAge.getText().toString()).equals("")) {
                showToast("Name or Age do not Empty !");
            } else {
                try {
                    int age = Integer.parseInt(mEdtAge.getText().toString());
                    String name = mEdtName.getText().toString();
                    UserDatabase.getInstance(this).userDao().insertUser(new User(name, age));
                    showToast("Add User successfully !");
                } catch (Exception e) {
                    Log.d("ddd", e.toString());
                }
            }
        });

        btnDeleteAll.setOnClickListener(view -> {
            UserDatabase.getInstance(this).userDao().deleteAll();

            mListUser = UserDatabase.getInstance(this).userDao().getListUser();
            mUserAdapter.setData(mListUser);
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}