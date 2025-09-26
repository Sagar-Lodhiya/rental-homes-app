package com.rentalhomes.ui.buyer.profile

import android.app.Application
import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.requestModel.GetProfileRequest
import com.rentalhomes.data.network.model.responseModel.GetProfileResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalFields

class BuyerProfileVM(application: Application) : BaseViewModel<BuyerProfileNavigator>(application) {

    lateinit var apiType: String

    var mldProfileDetails: MutableLiveData<GetProfileResponse.Data> =
        MutableLiveData<GetProfileResponse.Data>()

    var userId: Int? = null
    var userType: Int? = null
    var accessToken: String? = null

    init {
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

    fun getProfileCall(otherUserId: Int, userType: Int) {
        val request = GetProfileRequest(
            otherUserId, userType
        )

        val requestCall =
            APIHandler.apiServices?.getProfile(request, "${Keys.BEARER} ${accessToken.toString()}")
        apiType = Keys.API_GET_PROFILE_DATA
        navigator.showProgress()
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, responseCallback, apiType)
        }
    }

    private val responseCallback: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: GetProfileResponse = `object` as GetProfileResponse
            navigator.hideProgress()
            when (mResponse.status) {
                GlobalFields.STATUS_SUCCESS -> {
                    mldProfileDetails.value = mResponse.data
                }
                GlobalFields.STATUS_FAIL/*, GlobalFields.STATUS_ERROR_MESSAGE*/ -> {
                    mResponse.message?.let { navigator.setMessageComingFromServer(it) }
                }
                GlobalFields.STATUS_ERROR_MESSAGE -> {
                    AlertDialogUtils.showAlert(
                        context,
                        mResponse.message
                    ) { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    fun onEditProfileClick() {
        navigator.onEditProfileClick()
    }
}