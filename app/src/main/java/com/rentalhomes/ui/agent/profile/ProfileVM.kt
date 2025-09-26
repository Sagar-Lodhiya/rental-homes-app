package com.rentalhomes.ui.agent.profile

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.model.responseModel.GetProfileResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel

class ProfileVM(application: Application) : BaseViewModel<ProfileNavigator>(application) {

    var mldProfileDetails: MutableLiveData<GetProfileResponse.Data> =
        MutableLiveData<GetProfileResponse.Data>()

    var accessToken: String? = null

    init {
        accessToken = RentalHomesApp.appSessionManager.getString(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_ACCESS_TOKEN
        )
    }

    fun onEditProfilePressed() {
        navigator.onEditProfileClicked()
    }
}