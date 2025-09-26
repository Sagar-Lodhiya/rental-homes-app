package com.rentalhomes.ui.common.setting.privacypolicy

import android.app.Application
import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.Keys.API_TERMS_PRIVACY
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.requestModel.TermsPrivacyRequest
import com.rentalhomes.data.network.model.responseModel.TermsPrivacyResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalFields
import com.rentalhomes.utils.GlobalFields.PRIVACY_POLICY

class PrivacyPolicyVM (application: Application) :
    BaseViewModel<PrivacyPolicyNavigator>(application) {

    var userId: Int? = null
    var accessToken: String? = ""
    lateinit var apiType: String

    var mldPrivacyPolicy: MutableLiveData<TermsPrivacyResponse> = MutableLiveData<TermsPrivacyResponse>()

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

    fun getPrivacyPolicy(){
        val request = TermsPrivacyRequest(
            userId, PRIVACY_POLICY/*,accessToken*/
        )
        val requestCall = APIHandler.apiServices?.getTermsPrivacy(request,"${Keys.BEARER} ${accessToken.toString()}")
        apiType = API_TERMS_PRIVACY
        navigator.showProgress()
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, responseCallback, apiType)
        }
    }

    private val responseCallback: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: TermsPrivacyResponse = `object` as TermsPrivacyResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                mldPrivacyPolicy.value = mResponse
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                mResponse.message?.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
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