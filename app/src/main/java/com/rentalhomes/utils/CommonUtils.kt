package com.rentalhomes.utils

import android.app.Dialog
import android.content.Context
import com.rentalhomes.R

object CommonUtils {
    var dialog: Dialog? = null

    @JvmStatic
    fun showProgressDialog(context: Context?) {
        if (dialog != null && dialog?.isShowing == true) {
//            dialog = null
            dialog?.cancel()
            dialog = null
        }
        dialog = Dialog(context!!, R.style.ProgressDialogStyle)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.progress_dialog)
        dialog?.show()
    }

    @JvmStatic
    fun showProgressDialogsplash(context: Context?) {
        if (dialog != null && dialog?.isShowing == true) {
            dialog = null
        }
        dialog = Dialog(context!!)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.progress_dialog)
        dialog?.show()
    }

    @JvmStatic
    fun hideProgressDialog() {
        if (dialog != null && dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

    /*@JvmStatic
    fun showLoadingDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage(context.resources.getString(R.string.please_wait))
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        return progressDialog
    }*/
}