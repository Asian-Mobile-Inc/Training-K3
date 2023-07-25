package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnFragmentOne;
    private Button btnFragmentTwo;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private int countSwitch =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFragmentOne = findViewById(R.id.btn_fragment_one);
        btnFragmentTwo = findViewById(R.id.btn_fragment_two);

        fragmentManager = getSupportFragmentManager();
        Log.d("0000000000 Start : ",Integer.toString(fragmentManager.getBackStackEntryCount()) + " "+ Integer.toString(countSwitch));

        btnFragmentOne.setOnClickListener(this);
        btnFragmentTwo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fragment_one:
            {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, FragmentOne.newInstance("#338837"));
                fragmentTransaction.addToBackStack("Fragment One");
                countSwitch++;
                break;
            }
            case R.id.btn_fragment_two:{
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container_fragment, FragmentTwo.newInstance("#671063"));
                fragmentTransaction.addToBackStack("Fragment Two");
                countSwitch++;
                break;
            }
        }
        if(countSwitch>2){
            Log.d("0000000000 : ",Integer.toString(fragmentManager.getBackStackEntryCount()) + " "+ Integer.toString(countSwitch));
            for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                fragmentManager.popBackStack();
            }
            countSwitch=0;
            Log.d("0000000000 : ",Integer.toString(fragmentManager.getBackStackEntryCount()) + " "+ Integer.toString(countSwitch));
        }
        fragmentTransaction.commit();
    }
}