package com.example.asian.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class Utils {

    private Utils() {

    }

    public static void showToast(final String message, Context context) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(
                context, message,
                Toast.LENGTH_SHORT).show()
        );
    }

}
