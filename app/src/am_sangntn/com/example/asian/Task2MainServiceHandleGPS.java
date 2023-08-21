package com.example.asian;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Task2MainServiceHandleGPS extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_servicehandle);

        Button startServiceButton = findViewById(R.id.btnStart);
        Button stopServiceButton = findViewById(R.id.btnStop);

        startServiceButton.setOnClickListener(v -> checkLocationPermission());

        stopServiceButton.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(Task2MainServiceHandleGPS.this, Task2GPSService.class);
            stopService(serviceIntent);
        });
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            Intent serviceIntent = new Intent(Task2MainServiceHandleGPS.this, Task2GPSService.class);
            startService(serviceIntent);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent serviceIntent = new Intent(Task2MainServiceHandleGPS.this, Task2GPSService.class);
                startService(serviceIntent);
            }
        }
    }
}
