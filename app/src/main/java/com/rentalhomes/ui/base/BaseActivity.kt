package com.rentalhomes.ui.base

import android.app.Activity
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.R
import com.rentalhomes.data.network.Keys
import com.rentalhomes.ui.common.signin.SignInActivity
import com.rentalhomes.utils.*
import com.rentalhomes.utils.fcm.MyFCMService
import timber.log.Timber
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>?> : AppCompatActivity(),
    ConnectivityReceiver.ConnectivityReceiverListener {
    private lateinit var viewDataBinding: T
    private var mViewModel: V? = null
    private lateinit var loggedOutUserReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()

    }

    abstract fun setHeader()

    abstract val bindingVariable: Int

    @get:LayoutRes

    abstract val layoutId: Int

    abstract val viewModel: V

    abstract fun setObservers()

    fun showProgressDialog() {
        if (NetworkUtils.isInternetAvailable(this)) {
            CommonUtils.showProgressDialog(this)
        }
    }

    fun hideProgressDialog() {
        CommonUtils.hideProgressDialog()
    }

    fun getViewDataBinding(): T {
        return viewDataBinding
    }

    fun showInternetAlert(isConnected: Boolean) {
        if (!isConnected) {
            AlertDialogUtils.showInternetAlert(this)
        }
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding.setVariable(bindingVariable, mViewModel)
        viewDataBinding.executePendingBindings()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            AlertDialogUtils.showInternetAlert(this)
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onPause() {
        super.onPause()
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(loggedOutUserReceiver)
    }

    private fun initializeUserLogoutBroadcast() {
        loggedOutUserReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let { intent ->
                    if (intent.action == MyFCMService.USER_LOGGED_OUT_NOTIFICATION) {
                        val bundle = intent.extras!!
                        val message = bundle.getString(Keys.MESSAGE)
                        Timber.d("Message" + "$message")
                        AlertDialogUtils.showAlert(
                            this@BaseActivity,
                            message
                        ) { dialog: DialogInterface, which: Int ->
                            dialog.dismiss()
                            clearDataAndLogout()
                        }
                    }
                }
            }

        }
    }

    fun clearDataAndLogout() {
        RentalHomesApp.appSessionManager.setSession(this@BaseActivity, false)
        RentalHomesApp.appSessionManager.clearPreference(this)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        sendBroadcast(it)
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    var toolTip: View? = null

    fun getTooltipTop(context: Activity, view: View?, text: String?): View? {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        if (toolTip != null) {
            toolTip = null
        }
        toolTip = ViewTooltip
            .on(context, view)
            .autoHide(false, 3000)
            .corner(20)
            .textSize(1, 11f)
            .textColor(ContextCompat.getColor(context, R.color.white))
            .color(ContextCompat.getColor(context, R.color.color_green))
            .textTypeFace(ResourcesCompat.getFont(context, R.font.product_sans_regular))
//            .color(paint)
            .position(ViewTooltip.Position.BOTTOM)
            .text(text + "\n")
            .arrowWidth(20)
            .arrowHeight(40)
            .shadowColor(ContextCompat.getColor(context, R.color.color_green))
            //.padding(50, 50, 50, 50)
            .margin(10, 20, 10, 10)
            .setTextGravity(Gravity.CENTER)
            .distanceWithView(0)
            .clickToHide(true)
            .show()

        return toolTip
    }

    fun hideToolTip() {
        if (toolTip != null) {
            toolTip!!.visibility = GONE
        }
    }

    open fun numberFormat(value: Int): String? {
        val longVal: Long = value.toLong()
        val formatter: DecimalFormat =
            NumberFormat.getInstance(Locale.US) as DecimalFormat
        formatter.applyPattern("#,###,###,###")
        return formatter.format(longVal)
    }
}