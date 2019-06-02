package com.jarvissoft.begardobebar.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class PermissionUtil {

    public static void requestPermission(Activity activity, int requestId, String... permission) {
        ActivityCompat.requestPermissions(activity, permission, requestId);
    }

    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults, String... permission) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
