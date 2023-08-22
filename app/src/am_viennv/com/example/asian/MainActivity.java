package com.example.asian;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private int mCountSwitch;
    private FragmentManager mFragmentManager;
    private Button mBtnFragmentOne;
    private Button mBtnFragmentTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        handleOnclick();
    }

    private void handleOnclick() {
        mBtnFragmentOne.setOnClickListener(view -> {
            checkClearBackStack();
            handleChangeFragment(
                    FragmentOne.newInstance("#338837"),
                    "Fragment One");
        });
        mBtnFragmentTwo.setOnClickListener(view -> {
            checkClearBackStack();
            handleChangeFragment(
                    FragmentTwo.newInstance("#671063"),
                    "Fragment Two");
        });
    }

    private void initView() {
        mCountSwitch = 0;
        mBtnFragmentOne = findViewById(R.id.btnFragmentOne);
        mBtnFragmentTwo = findViewById(R.id.btnFragmentTwo);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this::handleBackStackChanged);
    }

    private void handleChangeFragment(Fragment fragment, String nameBackStack) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerFragment, fragment);
        fragmentTransaction.addToBackStack(nameBackStack);
        fragmentTransaction.commit();
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
