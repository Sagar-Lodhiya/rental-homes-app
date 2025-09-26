package com.rentalhomes.ui.common.setting.feedback

import android.app.Application
import android.content.DialogInterface
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.BuildConfig
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.Keys.API_SEND_FEEDBACK
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.requestModel.SendFeedbackRequest
import com.rentalhomes.data.network.model.responseModel.CommonResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalFields

class FeedbackVM(application: Application) :
    BaseViewModel<FeedbackNavigator>(application) {

    var userId: Int? = null
    var userType: Int? = null
    var accessToken: String? = null
    lateinit var apiType: String

    private val versionCode = BuildConfig.VERSION_NAME
    private val androidVersion = Build.VERSION.RELEASE
    private val modelName = Build.MANUFACTURER + " " + Build.MODEL

    var mldFeedback: MutableLiveData<String> = MutableLiveData<String>()

    init {
        mldFeedback.value = ""
        userId = RentalHomesApp.appSessionManager.getInt(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_USER_ID
        )
        userType = RentalHomesApp.appSessionManager.getInt(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_USER_TYPE
        )
        accessToken = RentalHomesApp.appSessionManager.getString(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_ACCESS_TOKEN
        )
    }

    fun sendFeedbackCall() {
        val request = SendFeedbackRequest(
            userId,
            userType,
            mldFeedback.value.toString(),
            versionCode.toString(),
            androidVersion.toString(),
            modelName
        )

        val requestCall = APIHandler.apiServices?.sendFeedback(
            request,
            "${Keys.BEARER} ${accessToken.toString()}"
        )
        apiType = API_SEND_FEEDBACK
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
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
                navigator.onResponse()
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                navigator.onResponse()
                AlertDialogUtils.showAlert(
                    context,
                    mResponse.message
                ) { dialog: DialogInterface, which: Int ->
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