package com.example.asian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.adapter.UserAdapter;
import com.example.asian.beans.User;
import com.example.asian.database.UserDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserAdapter mUserAdapter;
    private Button mButtonAdd;
    private Button mButtonDeleteAll;
    private Button mButtonShowAll;
    private EditText mEditTextUserName;
    private EditText mEditTextId;
    private List<User> mListUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRecyclerView();
        handleClick();
    }

    private void initView() {
        mButtonAdd = findViewById(R.id.btnAdd);
        mButtonDeleteAll = findViewById(R.id.btnDeleteAll);
        mButtonShowAll = findViewById(R.id.btnShowAll);
        mEditTextId = findViewById(R.id.edtId);
        mEditTextUserName = findViewById(R.id.edtUserName);
        mListUser = new ArrayList<>();
    }

    private void handleClick() {
        mButtonAdd.setOnClickListener(view -> {
            String nameUser = mEditTextUserName.getText().toString();
            String ageAsStr = mEditTextId.getText().toString();
            if (!nameUser.equals("") && !ageAsStr.equals("")) {
                Integer age = Integer.parseInt(ageAsStr);
                User user = new User(createNewUser(), nameUser, age);
                Toast.makeText(this, "New User Inserted !", Toast.LENGTH_SHORT).show();
                UserDatabase.getInstance(this).userDAO().insertUser(user);
                loadData();
                clearAllEditText();
            } else {
                Toast.makeText(this, "Please Enter Infomation !", Toast.LENGTH_SHORT).show();
            }
        });
        mButtonDeleteAll.setOnClickListener(view -> showDialog());
        mButtonShowAll.setOnClickListener(view -> {
            String nameUser = mEditTextUserName.getText().toString();
            if (nameUser.equals("")) {
                loadData();
            } else {
                List<User> usersIsFound = UserDatabase.getInstance(this).userDAO().findUserByName(nameUser);
                mUserAdapter.setData(this, usersIsFound);
            }
        });

    }

    private void clearAllEditText() {
        mEditTextId.setText("");
        mEditTextUserName.setText("");
    }

    private void showDialog() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    UserDatabase.getInstance(this).userDAO().deleteAll();
                    loadData();
                    Toast.makeText(MainActivity.this, "Delete Success !", Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure Delete All Data ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void initRecyclerView() {
        RecyclerView mRecyclerViewUsers = findViewById(R.id.rcvUsers);
        mRecyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter = new UserAdapter(this::clickDeleteUser);
        loadData();
        mRecyclerViewUsers.setAdapter(mUserAdapter);
    }

    private void clickDeleteUser(User user) {
        UserDatabase.getInstance(this).userDAO().delete(user);
        loadData();
    }

    private void loadData() {
        mListUser = UserDatabase.getInstance(this).userDAO().getAll();
        mUserAdapter.setData(this, mListUser);
    }

    public Integer createNewUser() {
        int newId = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mListUser.sort(Comparator.comparing(User::getMIdUser));
        }
        for (int i = 0; i < mListUser.size(); i++) {
            if (newId != mListUser.get(i).getMIdUser()) {
                return newId;
            }
            newId++;
        }
        return newId;
    }
}
