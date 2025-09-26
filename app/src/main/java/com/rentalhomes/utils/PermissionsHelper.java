package com.rentalhomes.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

public class PermissionsHelper {
    /**
     * -------------How to use-----------------
     * PermissionsHelper.permissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
     * Manifest.permission.CAMERA}).checkPermissions(TestActivity.this, new PermissionsHelper.OnPermissionResult() {
     *
     * @Override public void onGranted() { Perform whatever you want to do here }
     * @Override public void notGranted() { Perform whatever you want to do here } });
     * <p> <p> <p> Add this line of code in the base activity in onRequestPermissionsResult method
     * @Override public void onRequestPermissionsResult(int requestCode, String[] permissions
     * , int[] grantResults) { if (requestCode == PermissionsHelper.REQUEST_CODE_ASK_PERMISSIONS) PermissionsHelper.setGrantResult(grantResults); }
     */


    public static final int REQUEST_CODE_ASK_PERMISSIONS = 0x100;
    /**
     * variables used
     */
    private static String[] permissionsList;
    /**
     * The constant callback.
     */
    private static OnPermissionResult callback;

    /**
     * constructor
     *
     * @param permissionsList the permissions list
     */
    public PermissionsHelper(final String... permissionsList) {
        PermissionsHelper.permissionsList = permissionsList;
//        Log.v("SIZE", permissionsList.length + "");
    }

    /**
     * getter of permissionsList
     *
     * @return permissionsList List of Permissions
     */
    public static String[] getPermissionsList() {
        return permissionsList;
    }

    /**
     * setter of permissionsList
     *
     * @param permissionsList , sets List of Permissions
     */
    public static void setPermissionsList(final String[] permissionsList) {
        PermissionsHelper.permissionsList = permissionsList;
    }

    /**
     * getter of callback
     *
     * @return callback , OnPermissionResult instance
     */
    public static OnPermissionResult getCallback() {
        return callback;
    }

    /**
     * setter of callback
     *
     * @param callback , OnPermissionResult instance
     */
    public static void setCallback(final OnPermissionResult callback) {
        PermissionsHelper.callback = callback;
    }

    /**
     * Permissions permissions helper.
     *
     * @param permissionsArray the permissions list
     * @return the permissions helper
     */
    public static PermissionsHelper permissions(final String... permissionsArray) {
        return new PermissionsHelper(permissionsArray);
    }

    /**
     * Sets grant result.
     *
     * @param grantResults the grant results
     */
    public static void setGrantResult(final int[] grantResults) {
        boolean notGrantedBool = false;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                notGrantedBool = true;
                break;
            }
        }

        if (!notGrantedBool) {
            if (callback != null) {
                callback.onGranted();
            }
        } else {
            if (callback != null) {
                callback.notGranted();
            }
        }
    }

    /**
     * Check permissions.
     *
     * @param activity the activity
     * @param callbackParam the callback
     */
    public void checkPermissions(final Activity activity, final OnPermissionResult callbackParam) {

        callback = callbackParam;
        boolean notGrantedBool = false;
        for (int i = 0; i < permissionsList.length; i++) {
            if (ActivityCompat.checkSelfPermission(activity, permissionsList[i]) != PackageManager.PERMISSION_GRANTED) {
                notGrantedBool = true;
                break;
            }
        }

        if (notGrantedBool) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(permissionsList,
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        } else {
            callback.onGranted();
        }

    }

    /**
     * The interface On permission result.
     */
    public interface OnPermissionResult {

        /**
         * On granted.
         */
        void onGranted();

        /**
         * Not granted.
         */
        void notGranted();
    }

}
