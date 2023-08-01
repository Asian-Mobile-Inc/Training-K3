package com.example.asian;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_broadcast_receiver);
        Intent serviceIntent = new Intent(this, NetworkService.class);
        startService(serviceIntent);
    }
}