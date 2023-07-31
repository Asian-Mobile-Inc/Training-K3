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

public class MainServiceHandleGPS extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_servicehandle);

        // Ánh xạ các Button từ layout
        Button startServiceButton = findViewById(R.id.btnStart);
        Button stopServiceButton = findViewById(R.id.btnStop);

        // Bắt sự kiện khi nhấn nút Start Service
        startServiceButton.setOnClickListener(v -> {
            // Khởi động Service khi nhấn nút Start Service
            checkLocationPermission();
        });

        // Bắt sự kiện khi nhấn nút Stop Service
        stopServiceButton.setOnClickListener(v -> {
            // Dừng Service khi nhấn nút Stop Service
            Intent serviceIntent = new Intent(MainServiceHandleGPS.this, GPSService.class);
            stopService(serviceIntent);
        });
    }

    private void checkLocationPermission() {
        // Kiểm tra xem quyền truy cập GPS đã được cấp hay chưa
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu quyền chưa được cấp, yêu cầu người dùng cấp quyền
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            Intent serviceIntent = new Intent(MainServiceHandleGPS.this, GPSService.class);
            startService(serviceIntent);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền, bạn có thể tiếp tục xử lý GPS hoặc bắt đầu Service tại đây
                Intent serviceIntent = new Intent(MainServiceHandleGPS.this, GPSService.class);
                startService(serviceIntent);
            } else {
                // Người dùng đã từ chối cấp quyền, bạn có thể thông báo hoặc xử lý tương ứng
            }
        }
    }
}
