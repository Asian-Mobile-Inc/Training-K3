package com.example.asian;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NetworkService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent mainActivityIntent = new Intent(this, LocationReceiver.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivityIntent);
        return START_STICKY;
    }
}