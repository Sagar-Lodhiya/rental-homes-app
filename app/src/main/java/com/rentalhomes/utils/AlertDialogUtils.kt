package com.rentalhomes.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.rentalhomes.R

object AlertDialogUtils {
    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showSnakeBar(message: String?, view: View?, context: Context?) {
        val snackbar = Snackbar.make(view!!, message!!, Snackbar.LENGTH_LONG)
        val v = snackbar.view
//        Change the background color according to need

    }

    fun showAlert(context: Context, msg: String?) {
        AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name))
            .setMessage(msg).setCancelable(false)
            .setPositiveButton("OK") { dialog: DialogInterface?, which: Int -> }.show()
    }

    fun showAlertSignup(
        context: Context,
        msg: String?,
        onYesClick: DialogInterface.OnClickListener?
    ) {
        AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name))
            .setMessage(msg).setCancelable(false)
            .setNeutralButton("OK", onYesClick).show()
    }

    fun CustomAlert(
        context: Context?,
        title: String?,
        message: String?,
        Positive_text: String?,
        Negative_text: String?,
        PositiveListener: DialogInterface.OnClickListener?,
        NegativeListener: DialogInterface.OnClickListener?
    ) {
        val builder = AlertDialog.Builder(context).setTitle(title).setMessage(message)
            .setNeutralButton(Positive_text, PositiveListener)
            .setNeutralButton(Negative_text, NegativeListener)
        val dialog = builder.create()
        dialog.show()
    }

    fun showConfirmAlert(
        context: Context,
        msg: String?,
        onYesClick: DialogInterface.OnClickListener?
    ) {
        AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name))
            .setMessage(msg).setCancelable(false)
            .setNegativeButton("No", null)
            .setPositiveButton("Yes", onYesClick).show()
    }

    fun showConfirmAlertWithNO(
        context: Context,
        msg: String?,
        onYesClick: DialogInterface.OnClickListener?,
        onNoClick: DialogInterface.OnClickListener?
    ) {
        AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name))
            .setMessage(msg).setCancelable(false)
            .setNegativeButton("No", onNoClick)
            .setPositiveButton("Yes", onYesClick).show()
    }

    fun showConfirmAlertWithButtonName(
        context: Context,
        msg: String?,
        onYesClick: DialogInterface.OnClickListener?,
        strPositiveButton: String?,
        strNegaviteButton: String?,
        onNoClick: DialogInterface.OnClickListener?
    ) {
        AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name))
            .setMessage(msg).setCancelable(false)
            .setNegativeButton(strNegaviteButton, onNoClick)
            .setPositiveButton(strPositiveButton, onYesClick).show()
    }

    fun showLogoutAlert(
        context: Context,
        msg: String?,
        onYesClick: DialogInterface.OnClickListener?
    ) {
        AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name))
            .setMessage(msg).setCancelable(false)
            .setNegativeButton("No", null)
            .setPositiveButton("Yes", onYesClick).show()
    }

    fun showDeleteAlert(
        context: Context,
        msg: String?,
        onYesClick: DialogInterface.OnClickListener?
    ) {
        AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name))
            .setMessage(msg).setCancelable(false)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete", onYesClick).show()
    }

    fun showAlert(context: Context?, msg: String?, onYesClick: (DialogInterface, Int) -> Unit) {
        try {
            if (context == null) {
                return
            }
            AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name))
                .setMessage(msg).setCancelable(false)
                .setPositiveButton("OK", onYesClick).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showInternetAlert(context: Context?) {
        try {
            if (context == null) {
                return
            }
            AlertDialog.Builder(context).setIcon(0).setTitle(GlobalFields.KEY_INTERNET_ALERT_TITLE)
                .setMessage(GlobalFields.KEY_INTERNET_ALERT_MESSAGE)
                .setCancelable(false).setPositiveButton("OK", null).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showUploadImage(
        context: Context,
        msg: String?,
        camera: (Any, Any) -> Unit,
        gallery: (Any, Any) -> Unit
    ) {
        AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name))
            .setMessage(msg)
            .setCancelable(true)
            .setNegativeButton("Gallery", gallery)
            .setPositiveButton("Camera", camera).show();
    }

    fun showQRImagePopup(
        context: Context,
        qrImage: String?
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_qrcode)

        val qrCodeImage: AppCompatImageView = dialog.findViewById(R.id.ivQRCode)

        Glide.with(context).load(qrImage)
            .placeholder(R.drawable.ic_qr).transform(
                FitCenter(), RoundedCorners(25)
            ).into(qrCodeImage)

        dialog.findViewById<View>(R.id.ivClose).setOnClickListener { dialog.dismiss() }

        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.parseColor("#99000000")))
        dialog.show()
    }
}