package com.rentalhomes.ui.buyer.editprofile

import android.app.Application
import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.Keys.BEARER
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.responseModel.AuthResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalFields
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class BuyerEditProfileVM(application: Application) :
    BaseViewModel<BuyerEditProfileNavigator>(application) {

    lateinit var apiType: String

    var accessToken: String? = null

    var profilePic = MutableLiveData<File>()
    var agencyName = MutableLiveData<String>("")
    var firstName = MutableLiveData<String>("")
    var lastName = MutableLiveData<String>("")
    var mobile = MutableLiveData<String>("")
//    var postCode = MutableLiveData<String>("")
    var suburb = MutableLiveData<String>("")
    var userType = MutableLiveData<String>("")
    var userId = MutableLiveData<String>("")
    var isProfileRemove = MutableLiveData<Int>(0)

    init {
        userId.value = RentalHomesApp.appSessionManager.getInt(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_USER_ID
        ).toString()
        userType.value = RentalHomesApp.appSessionManager.getInt(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_USER_TYPE
        ).toString()
        accessToken = RentalHomesApp.appSessionManager.getString(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_ACCESS_TOKEN
        )
    }

    fun onSavePressed() {
        val request = setProfileRequest()
        val requestCall =
            APIHandler.apiServices?.setBuyerProfile(request.build(),  "$BEARER ${accessToken.toString()}")
        apiType = Keys.API_SET_PROFILE
        navigator.showProgress()
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, responseCallback, apiType)
        }
    }

    private val responseCallback: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: AuthResponse = `object` as AuthResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                //Store details in sharedPreferences
                RentalHomesApp.appSessionManager.setInt(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_USER_ID,
                    mResponse.data.userId
                )
                RentalHomesApp.appSessionManager.setInt(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_USER_TYPE,
                    mResponse.data.userType
                )
                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_ACCESS_TOKEN,
                    mResponse.data.accessToken
                )
                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_PROFILE_IMAGE,
                    mResponse.data.profilePic
                )
                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_FIRST_NAME,
                    mResponse.data.firstName
                )
                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_LAST_NAME,
                    mResponse.data.lastName
                )
                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_EMAIL,
                    mResponse.data.email
                )
                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_MOBILE,
                    mResponse.data.mobile
                )
                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_QR_CODE,
                    mResponse.data.qrCode
                )
                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_POST_CODE,
                    mResponse.data.postCode
                )
                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_SUBURB,
                    mResponse.data.suburb
                )

                navigator.onEditProfileComplete(mResponse.message)
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

    private fun setProfileRequest(): MultipartBody.Builder {
        val request = MultipartBody.Builder().setType(MultipartBody.FORM)

        if (profilePic.value == null) {
            request.addFormDataPart(Keys.PROFILE_PIC, "")
        } else {
            profilePic.value?.let { file ->
                if (file.length() > 0) {
                    val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
                    request.addFormDataPart(
                        Keys.PROFILE_PIC,
                        file.name,
                        requestBody
                    )
                }
            }
        }

        request.addFormDataPart(Keys.AGENCY_NAME, agencyName.value!!.toString().trim())
        request.addFormDataPart(Keys.FIRST_NAME, firstName.value!!.toString().trim())
        request.addFormDataPart(Keys.LAST_NAME, lastName.value!!.toString().trim())
        request.addFormDataPart(Keys.MOBILE, mobile.value!!.toString().trim())
//        request.addFormDataPart(Keys.POST_CODE, postCode.value!!.toString().trim())
        request.addFormDataPart(Keys.SUBURB, suburb.value!!.toString().trim())
        request.addFormDataPart(Keys.USER_TYPE, userType.value!!.toString().trim())
        request.addFormDataPart(Keys.USER_ID, userId.value!!.toString().trim())
        request.addFormDataPart(Keys.IS_PROFILE_REMOVE, isProfileRemove.value!!.toString().trim())

        return request
    }


    fun onUpdateClicked() {
        navigator.onUpdateClicked()
    }

    fun onRemoveClicked() {
        navigator.onRemoveClicked()
    }
}