package com.example.asian;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import static com.example.asian.CaseAction.CASE_ADD;
import static com.example.asian.CaseAction.CASE_DEL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.asian.adapter.ViewPagerAdapter;
import com.example.asian.view.FragmentTab1;
import com.example.asian.view.FragmentTab2;
import com.example.asian.view.FragmentTab3;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPagerAdapter.addFragment(new FragmentTab1(createData("1")), "Tab-1");
        mViewPagerAdapter.addFragment(new FragmentTab2(createData("2")), "Tab-2");
        mViewPagerAdapter.addFragment(new FragmentTab3(createData("3")), "Tab-3");
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        handleClick();
    }

    private void handleClick() {
        mFloatingActionButton.setOnClickListener(view -> {
            CustomDialog customDialog = new CustomDialog(MainActivity.this);
            customDialog.show();
        });
    }

    private void handleAction(String value, CaseAction caseAction) {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof FragmentTab1) {
            if (caseAction == CASE_ADD) {
                ((FragmentTab1) currentFragment).addDataToRecyclerView(value);
            } else {
                ((FragmentTab1) currentFragment).delDataToRecyclerView(value);
            }
        } else if (currentFragment instanceof FragmentTab2) {
            if (caseAction == CASE_ADD) {
                ((FragmentTab2) currentFragment).addDataToRecyclerView(value);
            } else {
                ((FragmentTab2) currentFragment).delDataToRecyclerView(value);
            }
        } else if (currentFragment instanceof FragmentTab3) {
            if (caseAction == CASE_ADD) {
                ((FragmentTab3) currentFragment).addDataToRecyclerView(value);
            } else {
                ((FragmentTab3) currentFragment).delDataToRecyclerView(value);
            }
        }
    }

    private Fragment getCurrentFragment() {
        int currentItem = mViewPager.getCurrentItem();
        return mViewPagerAdapter.getItem(currentItem);
    }

    private void initView() {
        mFloatingActionButton = findViewById(R.id.flbutton);
        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tb_layout);
    }

    public ArrayList<String> createData(String name) {
        ArrayList<String> stringList = new ArrayList<>();
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            stringList.add("Item " + name + ch);
        }
        return stringList;
    }

    public void showTextDialog(String title, String hintText, CaseAction caseAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        final EditText edtInfoItem = new EditText(this);
        edtInfoItem.setHint(hintText);
        builder.setView(edtInfoItem);
        builder.setPositiveButton("Save", (dialog, which) -> {
            if (caseAction == CASE_DEL) {
                handleAction(edtInfoItem.getText().toString(), CASE_DEL);
            } else if (caseAction == CASE_ADD) {
                handleAction(edtInfoItem.getText().toString(), CASE_ADD);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public class CustomDialog extends Dialog {

        public Button mButtonAdd;
        public Button mButtonDel;

        public CustomDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog);

            initView();
            handleClick();
        }

        private void initView() {
            mButtonAdd = findViewById(R.id.btn_add);
            mButtonDel = findViewById(R.id.btn_del);
        }

        private void handleClick() {
            mButtonAdd.setOnClickListener(view -> showTextDialog("Add Item", "Enter data", CASE_ADD));
            mButtonDel.setOnClickListener(view -> showTextDialog("Delete Item", "Enter position delete", CASE_DEL));
        }

    }

}