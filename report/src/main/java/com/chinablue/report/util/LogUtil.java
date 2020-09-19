package com.chinablue.report.util;

import android.util.Log;

public class LogUtil {

    private static boolean isLog = false;

    public LogUtil(boolean isLog) {
        this.isLog = isLog;
    }

    public static void i(String msg) {

        if (isLog)
            Log.i("reportManager", msg);
    }
}
