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
    private Button mBtnShowAll, mBtnDeleteAll, mBtnAdd;

    private List<User> mListUser;

    private RecyclerView mRcvUser;

    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdtAge = findViewById(R.id.edtUserAge);
        mEdtName = findViewById(R.id.edtUserName);
        mBtnAdd = findViewById(R.id.btnAdd);
        mBtnDeleteAll = findViewById(R.id.btnDeleteAll);
        mBtnShowAll = findViewById(R.id.btnShowAll);
        mRcvUser = findViewById(R.id.rcvListUsers);

        mUserAdapter = new UserAdapter();
        mUserAdapter.setData(mListUser);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRcvUser.setLayoutManager(linearLayoutManager);
        mRcvUser.setAdapter(mUserAdapter);

        mBtnShowAll.setOnClickListener(view -> {
            try {
                mUserAdapter.setData(UserDatabase.getInstance(this).userDao().getListUser());
            } catch (Exception e) {
                Log.d("ddd", e.toString());
            }
        });

        mBtnAdd.setOnClickListener(view -> {
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
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}