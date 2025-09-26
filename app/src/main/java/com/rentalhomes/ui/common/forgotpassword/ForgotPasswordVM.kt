package com.rentalhomes.ui.common.forgotpassword

import android.app.Application
import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.requestModel.ForgotPasswordRequest
import com.rentalhomes.data.network.model.responseModel.CommonResponse
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalFields

class ForgotPasswordVM(application: Application) : BaseViewModel<ForgotPasswordNavigator>(application) {
    lateinit var apiType: String
    var mldForgotPassword: MutableLiveData<ForgotPasswordRequest> = MutableLiveData<ForgotPasswordRequest>()

    init {
        mldForgotPassword.value = ForgotPasswordRequest("")
    }

    fun forgotPasswordCall(){
        val request = ForgotPasswordRequest(
            mldForgotPassword.value?.email.toString()
        )

        val requestCall = APIHandler.apiServices?.forgotPassword(request)
        apiType = Keys.API_FORGET_PASSWORD
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
                navigator.setMessageComingFromServer(mResponse.message)
                navigator.onResponse()
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                navigator.onResponse()
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                navigator.onResponse()
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