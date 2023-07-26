package com.example.asian;


import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainFragment extends AppCompatActivity {
    Button btnAdd, btnReplace;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_argument_main);
        btnAdd = findViewById(R.id.btn_add);
        btnReplace = findViewById(R.id.btn_replace);
        btnAdd.setOnClickListener(view -> onClickFragment1());
        btnReplace.setOnClickListener(view -> onClickFragment2());
        fragmentManager = getSupportFragmentManager();
    }

    private void onClickFragment1() {
        Bundle bundle = new Bundle();
        Fragment1 fragment1 = new Fragment1();
        bundle.putString("color", "#1F9A25");
        fragment1.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment1);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void onClickFragment2() {
        Bundle bundle = new Bundle();
        Fragment2 fragment2 = new Fragment2();
        bundle.putString("color", "#350831");
        System.out.println(bundle.getString("color"));
        fragment2.setArguments(bundle);
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        fragmentTransaction1.replace(R.id.container, fragment2);
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
    }
}
