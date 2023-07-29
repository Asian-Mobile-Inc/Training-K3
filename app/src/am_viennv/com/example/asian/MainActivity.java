package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static TextView mTextViewLatitude;
    @SuppressLint("StaticFieldLeak")
    private static TextView mTextViewLongTitude;
    @SuppressLint("StaticFieldLeak")
    public static TextView mTextViewConnected;
    private Button mButtonGetLocate;
    public static NetworkChangeReceiver mNetworkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        handleOnClick();
        initNetwork();
    }

    private void initNetwork() {
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangeReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký BroadcastReceiver khi Activity bị hủy
        unregisterReceiver(mNetworkChangeReceiver);
    }

    private void handleOnClick() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mButtonGetLocate.setOnClickListener(view -> {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                requestPermission();
            } else {
                requestAppPermissions();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public static void upDateUI(Location location) {
        mTextViewLatitude.setText(String.valueOf(location.getLatitude()));
        mTextViewLongTitude.setText(String.valueOf(location.getLongitude()));
        if (NetworkChangeReceiver.mIsConnected) {
            mTextViewConnected.setText("Connected");
        } else {
            mTextViewConnected.setText("No Connect");
        }
    }

    private void initView() {
        mTextViewLatitude = findViewById(R.id.tv_latitude);
        mTextViewLongTitude = findViewById(R.id.tv_longtitude);
        mButtonGetLocate = findViewById(R.id.btn_get_location);
        mTextViewConnected = findViewById(R.id.tv_connected);
    }

    private void requestAppPermissions() {
        Dexter.withActivity(MainActivity.this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            startService(new Intent(MainActivity.this, ForegroundService.class));
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 10);
    }

}
