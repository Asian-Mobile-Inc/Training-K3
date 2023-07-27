package com.example.asian;


import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainFragment extends AppCompatActivity {
    Button mBtnFragmentOne, mBtnFragmentTwo;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_argument_main);
        mBtnFragmentOne = findViewById(R.id.btnFragmentOne);
        mBtnFragmentTwo = findViewById(R.id.btnFragmentTwo);
        mBtnFragmentOne.setOnClickListener(view -> onClickFragment1());
        mBtnFragmentTwo.setOnClickListener(view -> onClickFragment2());
        mFragmentManager = getSupportFragmentManager();
    }

    private void onClickFragment1() {
        Bundle bundle = new Bundle();
        Fragment1 fragment1 = new Fragment1();
        bundle.putString("color", "#1F9A25");
        fragment1.setArguments(bundle);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragment1);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void onClickFragment2() {
        Bundle bundle = new Bundle();
        Fragment2 fragment2 = new Fragment2();
        bundle.putString("color", "#350831");
        fragment2.setArguments(bundle);
        FragmentTransaction fragmentTransaction1 = mFragmentManager.beginTransaction();
        fragmentTransaction1.replace(R.id.flContainer, fragment2);
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
    }
}
