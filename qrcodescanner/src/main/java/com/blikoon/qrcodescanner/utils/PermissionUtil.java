package com.blikoon.qrcodescanner.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class PermissionUtil {

    public static void requestPermission(AppCompatActivity activity, int requestId, String... permission) {
        ActivityCompat.requestPermissions(activity, permission, requestId);
    }

    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults, String... permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            boolean result = true;
            for(int i = 0; i < grantPermissions.length; i++){
                if(permission[i].equals(grantPermissions[i])){
                    result &= (grantResults[i] == PackageManager.PERMISSION_GRANTED);
                }
            }
            return result;
        }
        return false;
    }

    public static boolean checkPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean result = true;
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    result &= false;
                }
            }
            return result;
        }
        return true;
    }
}
