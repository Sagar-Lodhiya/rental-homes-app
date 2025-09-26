package com.rentalhomes.ui.common.qrcode

import android.app.Application
import android.content.DialogInterface
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.requestModel.AddPropertyScanRequest
import com.rentalhomes.data.network.model.requestModel.UserAddByQRRequest
import com.rentalhomes.data.network.model.responseModel.CommonResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalFields

class ScanVM(application: Application) : BaseViewModel<ScanNavigator>(application) {
    lateinit var apiType: String
    var userId: Int? = 0
    var accessToken: String? = ""

    init {
        userId = RentalHomesApp.appSessionManager.getInt(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_USER_ID
        )
        accessToken = RentalHomesApp.appSessionManager.getString(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_ACCESS_TOKEN
        )
    }

    fun addPropertyCall(propertyId: Int) {
        val request = AddPropertyScanRequest(
            propertyId, userId
        )

        val requestCall = APIHandler.apiServices?.addPropertyScan(
            request,
            "${Keys.BEARER} ${accessToken.toString()}"
        )
        apiType = Keys.API_PROPERTY_ADDED_QR_SCAN
        navigator.showProgress()
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, responseCallback, apiType)
        }
    }

    private val responseCallback: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
//                navigator.setMessageComingFromServer(mResponse.message)
                navigator.onAddPropertyComplete()
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                navigator.onAddPropertyComplete()
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                AlertDialogUtils.showAlert(
                    context,
                    mResponse.message
                ) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    fun userAddByQRCall(otherUserId: Int) {
        val request = UserAddByQRRequest(
            userId, otherUserId
        )

        val requestCall = APIHandler.apiServices?.userAddByQRScan(
            request,
            "${Keys.BEARER} ${accessToken.toString()}"
        )
        apiType = Keys.API_USER_ADDED_BY_QR_SCAN
        navigator.showProgress()
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, userAddByQRCallback, apiType)
        }
    }

    private val userAddByQRCallback: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                navigator.setMessageComingFromServer(mResponse.message)
                navigator.onAddPropertyComplete()
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                navigator.onAddPropertyComplete()
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                AlertDialogUtils.showAlert(
                    context,
                    mResponse.message
                ) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }
}