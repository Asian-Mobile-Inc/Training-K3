package com.example.asian;

import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String SQL_QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS User(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR(100), Age INTEGER)";

    private static final String SQL_QUERY_DELETE_ALL = "DELETE FROM User";

    private static final String SQL_QUERY_GET_LIST_USER = "SELECT * FROM User";

    private static final String SQL_QUERY_ADD_USER = "INSERT INTO User (Name, Age) VALUES ('";

    private UserHelper mUserHelper;

    private EditText mEdtUserName, mEdtUserAge;

    private UserAdapter mUserAdapter;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdtUserAge = findViewById(R.id.edtUserAge);
        mEdtUserName = findViewById(R.id.edtUserName);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnDeleteAll = findViewById(R.id.btnDeleteAll);
        Button btnShowAll = findViewById(R.id.btnShowAll);

        //DATABASE
        mUserHelper = new UserHelper(this, "User.database", null, 1);
        mUserHelper.queryData(SQL_QUERY_CREATE_TABLE);

        //ADD
        btnAdd.setOnClickListener(view -> {
            if (mEdtUserAge.getText().toString().isEmpty() || mEdtUserName.getText().toString().isEmpty()) {
                showToast("please enter full information of User");
                return;
            }
            String nameUser = mEdtUserName.getText().toString();
            String age = mEdtUserAge.getText().toString();

            Cursor listUser = mUserHelper.getData(SQL_QUERY_GET_LIST_USER);
            while (listUser.moveToNext()) {
                String nameUserTemp = listUser.getString(1);
                String ageTemp = listUser.getString(2);
                if ((nameUser.equals(nameUserTemp)) && (age.equals(ageTemp))) {
                    showToast("User already exists in the database.");
                    return;
                }
            }
            try {
                String sqlAdd = SQL_QUERY_ADD_USER + nameUser + "', " + age + ")";
                mUserHelper.queryData(sqlAdd);
                showAll();
            } catch (Exception e) {
                Log.d("ddd", e.toString());
            }
        });

        //SHOW ALL
        btnShowAll.setOnClickListener(view -> showAll());

        //DELETE ALL
        btnDeleteAll.setOnClickListener(view -> {
            mUserHelper.queryData(SQL_QUERY_DELETE_ALL);
            showAll();
        });

        TextView tvTitleUserId = findViewById(R.id.tvTitleUserId);
        TextView tvTitleUserName = findViewById(R.id.tvTitleUserName);
        TextView tvTitleUserAge = findViewById(R.id.tvTitleUserAge);

        GradientDrawable border = new GradientDrawable();
        border.setStroke(2, getResources().getColor(R.color.black));
        tvTitleUserId.setBackground(border);
        tvTitleUserName.setBackground(border);
        tvTitleUserAge.setBackground(border);
        LinearLayout llRcvLayout = findViewById(R.id.llRcvLayout);
        llRcvLayout.setBackground(border);

        RecyclerView rcvUser = findViewById(R.id.rcvListUsers);
        mUserAdapter = new UserAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvUser.setLayoutManager(linearLayoutManager);
        rcvUser.setAdapter(mUserAdapter);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showAll() {
        Cursor listUser = mUserHelper.getData(SQL_QUERY_GET_LIST_USER);
        List<User> list = new ArrayList<>();
        while (listUser.moveToNext()) {
            int id = listUser.getInt(0);
            String nameUser = listUser.getString(1);
            int age = listUser.getInt(2);
            User user = new User(id, nameUser, age);
            list.add(user);
        }
        mUserAdapter.setData(list);
    }
}