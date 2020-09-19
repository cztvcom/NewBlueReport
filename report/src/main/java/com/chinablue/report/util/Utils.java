package com.chinablue.report.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import java.util.UUID;

public final class Utils {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init( final Context context) {
        Utils.context = context.getApplicationContext();
    }

    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("should be initialized in application");
    }

    public static String getDeviceID(Context context) {
        String deviceId = null;

        if (context != null)
            deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = UUID.randomUUID().toString();
        }
        return deviceId;
    }

}
