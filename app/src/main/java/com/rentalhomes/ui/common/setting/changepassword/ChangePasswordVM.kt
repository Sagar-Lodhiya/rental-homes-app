package com.rentalhomes.ui.common.setting.changepassword

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.model.requestModel.ChangeEmailRequest
import com.rentalhomes.data.network.model.requestModel.ChangePasswordRequest
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel

class ChangePasswordVM(application: Application) :
    BaseViewModel<ChangePasswordNavigator>(application) {

    var userId: Int? = null
    var userType: Int? = null
    var accessToken: String? = null
    lateinit var apiType: String

    var mldChangeEmail: MutableLiveData<ChangeEmailRequest> = MutableLiveData<ChangeEmailRequest>()
    var mldChangePassword: MutableLiveData<ChangePasswordRequest> = MutableLiveData<ChangePasswordRequest>()


    init {
        mldChangeEmail.value = ChangeEmailRequest(0, 0, "", "","")
        mldChangePassword.value = ChangePasswordRequest(0,0,"","")

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



    fun onChangeEmailPressed() {
        navigator.onClickChangeEmail()
    }

    fun onChangePasswordPressed() {
        navigator.onClickChangePassword()
    }

    fun onEmailClick() {
        navigator.onEmailClick()
    }

    fun onPasswordClick() {
        navigator.onPasswordClick()
    }
}