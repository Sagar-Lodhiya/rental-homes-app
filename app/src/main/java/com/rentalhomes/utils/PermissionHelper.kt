package com.rentalhomes.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.R
import com.rentalhomes.data.pref.AppSessionManager


object PermissionHelper {

    private val TAG = PermissionHelper::class.simpleName

    fun askUserForPermission(
        context: Activity,
        permissionArray: Array<String>,
        requestCode: Int,
        permissionSharedPrefKey: String,
        requestMessage: String
    ) {

        if (shouldRequestPermissionsAtRuntime()) {
            if (checkShowRequestPermissionRationale(context, permissionArray)) {
                ActivityCompat.requestPermissions(context, permissionArray, requestCode)
            } else {
                var userAskedPermissionBefore: Boolean? =
                    RentalHomesApp.appSessionManager.getBoolean(
                        context,
                        AppSessionManager.PREF_PERMISSIONS,
                        permissionSharedPrefKey
                    )

                if (userAskedPermissionBefore == null) {
                    userAskedPermissionBefore = false
                }

                if (userAskedPermissionBefore) {

                    //If User was asked permission before and denied
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

                    alertDialogBuilder.setTitle(context.getString(R.string.permission_needed))
                    alertDialogBuilder.setMessage(requestMessage)
                    alertDialogBuilder.setPositiveButton(
                        context.getString(R.string.open_setting)
                    ) { dialogInterface, i ->
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri: Uri = Uri.fromParts(
                            "package", context.packageName,
                            null
                        )
                        intent.data = uri
                        context.startActivity(intent)
                    }
                    alertDialogBuilder.setNegativeButton(
                        context.getString(R.string.cancel)
                    ) { dialogInterface, i -> Log.d(TAG, "onClick: Cancelling") }

                    val dialog = alertDialogBuilder.create()
                    dialog.show()


                } else {
                    //If user is asked permission for first time
                    ActivityCompat.requestPermissions(
                        context, permissionArray,
                        requestCode
                    )
                    RentalHomesApp.appSessionManager.setBoolean(
                        context,
                        AppSessionManager.PREF_PERMISSIONS,
                        permissionSharedPrefKey,
                        true
                    )
                }
            }
        }

    }

    fun shouldRequestPermissionsAtRuntime(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun arePermissionsGranted(context: Context, permissionArray: Array<String>): Boolean {
        return permissionArray.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

//    private fun requestPermissions() {
//        if(shouldRequestPermissionsAtRuntime()){
//            requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE)
//        }
//    }

    fun checkShowRequestPermissionRationale(
        context: Activity,
        permissionArray: Array<String>
    ): Boolean {
        var isShow = false
        for (s in permissionArray) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    s
                )
            ) {
                isShow = true
            }
        }
        return isShow
    }
}