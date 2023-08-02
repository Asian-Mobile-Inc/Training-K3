package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private int mCountSwitch;
    private FragmentManager mFragmentManager;

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCountSwitch = 0;
        Button btnFragmentOne = findViewById(R.id.btnFragmentOne);
        Button btnFragmentTwo = findViewById(R.id.btnFragmentTwo);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this::handleBackStackChanged);
        btnFragmentOne.setOnClickListener(view -> {
            checkClearBackStack();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerFragment, FragmentOne.newInstance("#338837"));
            fragmentTransaction.addToBackStack("Fragment One");
            fragmentTransaction.commit();
        });
        btnFragmentTwo.setOnClickListener(view -> {
            checkClearBackStack();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerFragment, FragmentTwo.newInstance("#671063"));
            fragmentTransaction.addToBackStack("Fragment Two");
            fragmentTransaction.commit();
        });
    }

    private void handleBackStackChanged() {
        int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            FragmentManager.BackStackEntry backEntry = mFragmentManager.getBackStackEntryAt(backStackEntryCount - 1);
            String fragmentName = backEntry.getName();
            setTitle(fragmentName);
        }
    }

    private void checkClearBackStack() {
        mCountSwitch++;
        if (mCountSwitch > 2) {
            mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            mCountSwitch = 0;
        }
    }
}
