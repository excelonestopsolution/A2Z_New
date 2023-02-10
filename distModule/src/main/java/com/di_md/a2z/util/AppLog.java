package com.di_md.a2z.util;

import android.util.Log;

import com.di_md.a2z.BuildConfig;

public class AppLog {
    private static boolean isDebug= BuildConfig.DEBUG;

    public static void d(String message){
        if(isDebug){
            Log.d("AppLog",message);
        }
    }
    public static void d(String TAG,String message){
        if(isDebug){
            Log.d(TAG,message);
        }
    }

    public static void e(String message){
        if(isDebug){
            Log.d("AppLog",message);
        }
    }
    public static void e(String TAG,String message){
        if(isDebug){
            Log.d(TAG,message);
        }
    }
}
