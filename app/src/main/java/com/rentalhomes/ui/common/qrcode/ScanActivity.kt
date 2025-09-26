package com.rentalhomes.ui.common.qrcode

import android.Manifest
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.zxing.Result
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.data.network.Keys
import com.rentalhomes.databinding.ActivityScanBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.io.UnsupportedEncodingException

class ScanActivity : BaseActivity<ActivityScanBinding, ScanVM>(), ZXingScannerView.ResultHandler,
    ScanNavigator {
    private lateinit var binding: ActivityScanBinding
    private lateinit var scanVM: ScanVM
    private lateinit var snackBar: View
    private var mScannerView: ZXingScannerView? = null
    var layout: LinearLayout? = null
    var scanView: FrameLayout? = null
    private lateinit var isFrom: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        requestCameraPermission()
        mScannerView = ZXingScannerView(this)
        binding.contentFrame.addView(mScannerView)
        binding.contentFrame.visibility = View.VISIBLE
        binding.layoutScan.visibility = View.GONE
    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this@ScanActivity
            it.scanVM = scanVM
        }
        scanVM.attachContext(this)
        scanVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
        isFrom = intent.getStringExtra(Keys.FROM_ACTIVITY).toString()
    }

    override val bindingVariable: Int
        get() = BR.scanVM
    override val layoutId: Int
        get() = R.layout.activity_scan
    override val viewModel: ScanVM
        get() {
            scanVM = ViewModelProvider(this).get(ScanVM::class.java)
            return scanVM
        }

    override fun setHeader() {
    }

    override fun setObservers() {
    }

    override fun onResume() {
        super.onResume()
        try {
            mScannerView!!.setResultHandler(this)
            mScannerView!!.startCamera()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            mScannerView!!.stopCamera()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun showQRAlertDialog() {
        AlertDialogUtils.showConfirmAlertWithButtonName(
            this,
            "Invalid QR code.",
            { dialog, _ ->
                dialog.dismiss()
                mScannerView!!.setResultHandler(this)
                mScannerView!!.startCamera()
            },
            "Retry",
            "Cancel",
            { _, _ ->
                setResult(RESULT_CANCELED)
                finish()
            })

    }

    private fun requestCameraPermission() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report != null) {
                        if (report.areAllPermissionsGranted()) {

                        } else {
                            finish()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            })
            .check()
    }

    override fun onBackPressed() {
        try {
            mScannerView!!.stopCamera()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        setResult(RESULT_CANCELED)
        finish()
    }

    override fun handleResult(result: Result?) {
        if (result != null) {
            // Get Result from QR code here
            Log.e("ScanActivity", "scan_data : ${result.toString().substring(5)}")
//            Toast.makeText(this, "Successfully Scanned", Toast.LENGTH_SHORT).show()


            try {
                if (isFrom == Keys.BUYER_HOME_ACTIVITY) {
                    val data: ByteArray = Base64.decode(result.toString(), Base64.DEFAULT)
//                    Log.e("ScanActivity", "result : $data")
                    val text = String(data, Charsets.UTF_8)
//                    Log.e("ScanActivity", "result : $text")

                    if (text.isNotEmpty()) {
                        val gson = Gson()
                        val scannedQRModel: ScannedQRModel =
                            gson.fromJson(text, ScannedQRModel::class.java)
                        scanVM.addPropertyCall(scannedQRModel.propertyId!!)
                    } else {
                        showQRAlertDialog()
                    }
                } else if (isFrom == Keys.COMPARE_ACTIVITY) {
                    val data: ByteArray =
                        Base64.decode(result.toString().substring(5), Base64.DEFAULT)
//                    Log.e("ScanActivity", "result : $data")
                    val text = String(data, Charsets.UTF_8)
//                    Log.e("ScanActivity", "result : $text")

                    if (text.isNotEmpty()) {
                        val gson = Gson()
                        val userAddByQRModel: UserAddByQRModel =
                            gson.fromJson(text, UserAddByQRModel::class.java)
                        UserAddByQRModel
                        scanVM.userAddByQRCall(userAddByQRModel.userId!!)
                    } else {
                        showQRAlertDialog()
                    }
                }
            } catch (e: UnsupportedEncodingException) {
//                Log.e("ScanActivity", "catch1 : ${e.message}")
                e.printStackTrace()
                showQRAlertDialog()
                return
            } catch (e: Exception) {
//                Log.e("ScanActivity", "catch : ${e.message}")
                e.printStackTrace()
                showQRAlertDialog()
                return
            }

//            onBackPressed()
        } else {
            showQRAlertDialog()
        }
    }

    override fun onAddPropertyComplete() {
//        Handler(Looper.getMainLooper()).postDelayed({
//            finish()
//        }, 2000)
        finish()
    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun setMessageComingFromServer(it: String) {
        AlertDialogUtils.showSnakeBar(it, snackBar, this)
    }

}