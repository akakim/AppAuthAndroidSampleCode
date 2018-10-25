package com.google.codelabs.appauth;

import android.util.Log;

/**
 * @author KIM
 * @version 0.0.1
 * @date 2018-10-25
 * @since 0.0.1
 */
public class Logger {

    private static String TAG = "Suck";

    public static void CustomLog(String message ){
        Log.d(TAG, message );
    }
}
