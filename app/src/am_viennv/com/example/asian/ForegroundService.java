package com.example.asian;

import static com.example.asian.MainActivity.mTextViewConnected;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.Objects;

public class ForegroundService extends Service {
    private final IBinder mBinder = new MyBinder();
    private static final String CHANNEL_ID = "2";
    private FusedLocationProviderClient mClient;
    private boolean mIsNetworkConnected = false;
    private LocationCallback mLocationCallback;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mClient = LocationServices.getFusedLocationProviderClient(this);
        buildNotification();
        updateLocationTracking();
        registerNetworkChangeReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChangeReceiver();
    }

    private void registerNetworkChangeReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangeReceiver, filter);
    }

    private void unregisterNetworkChangeReceiver() {
        unregisterReceiver(mNetworkChangeReceiver);
    }

    private final BroadcastReceiver mNetworkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                boolean isConnected = NetworkChangeReceiver.mIsConnected;
                if (mIsNetworkConnected != isConnected) {
                    mIsNetworkConnected = isConnected;
                    updateLocationTracking();
                }
            }
        }
    };

    private void buildNotification() {
        String stop = "stop";
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Location tracking is working")
                .setOngoing(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(false);
            channel.setDescription("Location tracking is working");
            channel.setSound(null, null);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
        startForeground(1, builder.build());
    }

    @SuppressLint("SetTextI18n")
    private void stopLocationUpdates() {
        if (mLocationCallback != null) {
            mTextViewConnected.setText("No Connect");
            mClient.removeLocationUpdates(mLocationCallback);
        }
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 20000)
                .setWaitForAccurateLocation(false)
                .build();

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            if (NetworkChangeReceiver.mIsConnected) {
                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        String location = "Latitude : " + Objects.requireNonNull(locationResult.getLastLocation()).getLatitude() +
                                "\nLongitude : " + locationResult.getLastLocation().getLongitude();
                        MainActivity.upDateUI(locationResult.getLastLocation());
                        Log.d("Location", location);
                    }
                };
                mClient.requestLocationUpdates(request, mLocationCallback, null);
            } else {
                stopLocationUpdates();
            }
        } else {
            stopSelf();
        }
    }

    private void updateLocationTracking() {
        if (mIsNetworkConnected) {
            requestLocationUpdates();
        } else {
            stopLocationUpdates();
        }
    }

    public static class MyBinder extends Binder {
    }
}
