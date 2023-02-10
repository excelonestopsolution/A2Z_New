package com.di_md.a2z.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.content.Context.TELEPHONY_SERVICE;

public class AppUitls {

    public static final long REQUEST_TIME_OUT = 0L;

    public static final boolean isTestMode = false;

    public static String strRetailerNews="";
    public static String strDistributorNews="";
    public static String strBankDownList="";

    private static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs





    public static boolean stringHasOnlyNumber(String string) {

        boolean numeric;
        numeric = string.matches("-?\\d+(\\.\\d+)?");
        return numeric;
    }

    public static Date getTimeFuture2Miuntes() {

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        return new Date(t + (2 * ONE_MINUTE_IN_MILLIS));
    }
    public static Date getTimeFuture24Hours() {

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        return new Date(t + (1440 * ONE_MINUTE_IN_MILLIS));
    }

    public static Date getTime() {

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        return new Date(t);
    }


    public static Map<String, String> sortByComparator(Map<String, String> unsortMap, final boolean order) {

        List<Map.Entry<String, String>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, (o1, o2) -> {
            if (order) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());

            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, String> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static String currentDate(){
        Date cDate = new Date();
        return new SimpleDateFormat("dd-MM-yyyy").format(cDate);
    }

    public static String getStringImage(Bitmap bmp) {
        if(bmp!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();

            return Base64.encodeToString(imageBytes, Base64.DEFAULT);

        }
        return "";
    }


    public static Bitmap getResizedBitmap(Bitmap image) {
        Bitmap bitmap = Bitmap.createScaledBitmap(image, 720, 960, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return bitmap;
    }

/*    public static Bitmap getResizedBitmap(Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos);

        return bitmap;

    }*/
private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(TELEPHONY_SERVICE);
        assert telephonyManager != null;
        return telephonyManager.getDeviceId();
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}

