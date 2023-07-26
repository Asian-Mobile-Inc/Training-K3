package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private int mCountSwitch;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCountSwitch = 0;
        Button btnFragmentOne = findViewById(R.id.btn_fragment_one);
        Button btnFragmentTwo = findViewById(R.id.btn_fragment_two);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this::handleBackStackChanged);
        btnFragmentOne.setOnClickListener(view -> {
            checkClearBackStack();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, FragmentOne.newInstance("#338837"));
            fragmentTransaction.addToBackStack("Fragment One");
            fragmentTransaction.commit();
        });
        btnFragmentTwo.setOnClickListener(view -> {
            checkClearBackStack();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, FragmentTwo.newInstance("#671063"));
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
