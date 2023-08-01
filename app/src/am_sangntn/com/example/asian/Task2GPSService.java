package com.example.asian;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class Task2GPSService extends Service {

    private static final String TAG = "GPSService";
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Log.d(TAG, "Latitude: " + latitude + ", Longitude: " + longitude);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "GPS off");
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener);
        } else {
            Log.d(TAG, "Quyền truy cập GPS bị từ chối.");
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
