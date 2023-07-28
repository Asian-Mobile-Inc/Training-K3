package com.example.asian;

import static pub.devrel.easypermissions.EasyPermissions.requestPermissions;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class GPSService extends Service implements EasyPermissions.PermissionCallbacks {

    private static final int RC_LOCATION_PERMISSION = 123;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Xử lý thông tin vị trí tại đây
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String message = "Latitude: " + latitude + "\nLongitude: " + longitude;
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        // Kiểm tra quyền truy cập GPS
        if (hasLocationPermission()) {
            // Yêu cầu cập nhật vị trí từ GPS
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            // Nếu quyền chưa được cấp, yêu cầu quyền truy cập GPS
            requestLocationPermission();
        }
    }

    @Override
    public void onDestroy() {
        // Hủy cập nhật vị trí khi Service bị hủy
        locationManager.removeUpdates(locationListener);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean hasLocationPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @AfterPermissionGranted(RC_LOCATION_PERMISSION)
    private void requestLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.somePermissionPermanentlyDenied(this, permissions)) {
            // Nếu quyền bị từ chối vĩnh viễn, hiển thị dialog để hướng dẫn người dùng mở cài đặt
            new AppSettingsDialog.Builder(this).build().show();
            AppSettingsDialog.Builder builder = new AppSettingsDialog.Builder(this);
            builder.build().show();
        } else {
            // Yêu cầu quyền truy cập GPS
            requestPermissions(this, "Ứng dụng cần truy cập GPS để cung cấp dịch vụ.", RC_LOCATION_PERMISSION, permissions);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == RC_LOCATION_PERMISSION) {
            // Nếu quyền được cấp, yêu cầu cập nhật vị trí từ GPS
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == RC_LOCATION_PERMISSION) {
            // Xử lý khi quyền bị từ chối
            Toast.makeText(this, "Ứng dụng không thể cung cấp dịch vụ vì quyền truy cập GPS bị từ chối.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
}
