package com.example.asian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.adapters.UserAdapter;
import com.example.asian.beans.User;
import com.example.asian.database.DBHelper;

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
    private DBHelper mDbHelper;
    private List<User> mUserList;

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
            handleAddUser();
        });
        mButtonDeleteAll.setOnClickListener(view -> showDialog());
        mButtonShowAll.setOnClickListener(view -> {
            handleFindUsers();
        });
    }

    private void handleFindUsers() {
        String nameUser = mEditTextUserName.getText().toString();
        resetData(mDbHelper.findAllData(nameUser));
    }

    private void handleAddUser() {
        String nameUser = mEditTextUserName.getText().toString();
        String ageAsStr = mEditTextId.getText().toString();
        if (!nameUser.equals("") && !ageAsStr.equals("")) {
            Integer newId = findIdNewUser();
            Integer age = Integer.parseInt(ageAsStr);
            Boolean checkInsertData = mDbHelper.insertUserData(newId, nameUser, age);
            if (checkInsertData) {
                Toast.makeText(this, "New User Inserted !", Toast.LENGTH_SHORT).show();
                mDbHelper.insertUserData(newId, nameUser, age);
                resetData(mDbHelper.getAllData());
                clearAllEditText();
            } else {
                Toast.makeText(this, "New User Not Inserted !", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please Enter Infomation !", Toast.LENGTH_SHORT).show();
        }
    }

    public Integer findIdNewUser() {
        int newId = 0;
        getData(mDbHelper.getAllData());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mUserList.sort(Comparator.comparing(User::getmIdUser));
        }
        for (int i = 0; i < mUserList.size(); i++) {
            if (newId != mUserList.get(i).getmIdUser()) {
                return newId;
            }
            newId++;
        }
        return newId;
    }

    private void showDialog() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    int rowsDeleted = mDbHelper.deleteAll();
                    resetData(mDbHelper.getAllData());
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
        builder.setMessage("Are you sure Delete All Data ?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    private void resetData(Cursor cursor) {
        getData(cursor);
        mUserAdapter.setData(mUserList);
    }

    private void initRecyclerView() {
        mDbHelper = new DBHelper(this);
        RecyclerView mRecyclerViewUsers = findViewById(R.id.rcvUsers);
        mRecyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter = new UserAdapter(this, this::clickDeleteUser);
        mRecyclerViewUsers.setAdapter(mUserAdapter);
        getData(mDbHelper.getAllData());
        mUserAdapter.setData(mUserList);
    }

    private void clickDeleteUser(User user) {
        mDbHelper.deleteOneUser(user.getmIdUser());
        getData(mDbHelper.getAllData());
        mUserAdapter.setData(mUserList);
    }

    public void getData(Cursor cursor) {
        mUserList = new ArrayList<>();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No User Exists !", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                mUserList.add(new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2))));
            }
        }
    }

    private void clearAllEditText() {
        mEditTextUserName.setText("");
        mEditTextId.setText("");
    }
}
