package com.example.asian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.beans.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserAdapter mUserAdapter;
    private Button mButtonAdd;
    private Button mButtonDeleteAll;
    private Button mButtonShowAll;
    private EditText mEditTextUserName;
    private EditText mEditTextId;
    private DBHelper mDbHelper;

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
    }

    private void handleClick() {
        mButtonAdd.setOnClickListener(view -> {
            String nameUser = mEditTextUserName.getText().toString();
            String ageAsStr = mEditTextId.getText().toString();
            if (!nameUser.equals("") && !ageAsStr.equals("")) {
                Integer newId = mUserAdapter.findIdNewUser();
                Integer age = Integer.parseInt(ageAsStr);
                Boolean checkInsertData = mDbHelper.insertUserData(newId, nameUser, age);
                User user = new User(newId, nameUser, age);
                if (checkInsertData) {
                    Toast.makeText(this, "New User Inserted !", Toast.LENGTH_SHORT).show();
                    mUserAdapter.addUser(user);
                    clearAllEditText();
                } else {
                    Toast.makeText(this, "New User Not Inserted !", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please Enter Infomation !", Toast.LENGTH_SHORT).show();
            }
        });
        mButtonDeleteAll.setOnClickListener(view -> showDialog());
        mButtonShowAll.setOnClickListener(view -> {
            String nameUser = mEditTextUserName.getText().toString();
            mUserAdapter.searchUserByName(nameUser);
        });

    }

    private void showDialog() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    mUserAdapter.deleteAll();
                    int rowsDeleted = mDbHelper.deleteAll();
                    if (rowsDeleted > 0) {
                        Toast.makeText(MainActivity.this, "Delete Success !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "No User Exists !", Toast.LENGTH_SHORT).show();
                    }
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
        mDbHelper = new DBHelper(this);
        RecyclerView mRecyclerViewUsers = findViewById(R.id.rcvUsers);
        mRecyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter = new UserAdapter(this, getList());
        mRecyclerViewUsers.setAdapter(mUserAdapter);
    }

    public List<User> getList() {
        List<User> lstUsers = new ArrayList<>();
        Cursor cursor = mDbHelper.getAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No User Exists !", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                lstUsers.add(new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2))));
            }
        }
        return lstUsers;
    }

    private void clearAllEditText() {
        mEditTextUserName.setText("");
        mEditTextId.setText("");
    }
}
