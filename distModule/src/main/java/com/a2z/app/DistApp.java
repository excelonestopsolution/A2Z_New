package com.a2z.app;

import android.app.Application;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.a2z.app.util.APIs;
import com.a2z.app.util.RootTools;
import com.a2z.di.R;

import static com.a2z.app.util.AppConstants.BASE_URL;

public class DistApp extends Application {

    public static boolean isRooted;

    public static String currentAppVersion;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        isRooted = RootTools.isDeviceRooted();
        new APIs(BASE_URL, "userId", "token", "password", "ASKJHAU123SHYEWR"
                , getApplicationContext()
                .getResources()
                .getString(R.string.open));
        currentAppVersion = getCurrentAppVersion();


    }

    private String getCurrentAppVersion() {
        String currentVersion = "0";
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersion;
    }
}
