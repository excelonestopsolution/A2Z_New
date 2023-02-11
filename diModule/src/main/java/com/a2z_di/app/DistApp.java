package com.a2z_di.app;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.a2z_di.app.util.APIs;
import com.a2z_di.app.util.RootTools;

import static com.a2z_di.app.util.AppConstants.BASE_URL;

public class DistApp {

    public static boolean isRooted;

    public static String currentAppVersion;

    public void init(Context context) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        isRooted = RootTools.isDeviceRooted();
        new APIs(BASE_URL, "userId", "token", "password", "ASKJHAU123SHYEWR"
                , context
                .getResources()
                .getString(R.string.open));
        currentAppVersion = getCurrentAppVersion(context);

    }

    private String getCurrentAppVersion(Context context) {
        String currentVersion = "0";
        try {
            currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersion;
    }
}
