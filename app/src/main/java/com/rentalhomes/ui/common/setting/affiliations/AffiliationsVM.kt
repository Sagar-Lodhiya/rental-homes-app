package com.rentalhomes.ui.common.setting.affiliations

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.model.responseModel.GetAffiliationsResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel

class AffiliationsVM(application: Application) : BaseViewModel<AffiliationsNavigator>(application) {

    var userId: Int? = null
    var userType: Int? = null
    var accessToken: String? = ""
    lateinit var apiType: String
    val dummyAffiliationsResponse = GetAffiliationsResponse(
        message = "Affiliations fetched successfully",
        status = 200,
        affiliations = arrayListOf(
            GetAffiliationsResponse.Affiliations(
                image = "https://logo.clearbit.com/housing.com",
                link  = "https://housing.com"
            ),
            GetAffiliationsResponse.Affiliations(
                image = "https://logo.clearbit.com/www.99acres.com",
                link  = "https://www.99acres.com"
            ),
            GetAffiliationsResponse.Affiliations(
                image = "https://logo.clearbit.com/www.magicbricks.com",
                link  = "https://www.magicbricks.com"
            ),
            GetAffiliationsResponse.Affiliations(
                image = "https://logo.clearbit.com/www.nobroker.in",
                link  = "https://www.nobroker.in"
            ),
            GetAffiliationsResponse.Affiliations(
                image = "https://logo.clearbit.com/realtor.com",
                link  = "https://www.realtor.com"
            )
        )
    )



    var mldAffiliations: MutableLiveData<GetAffiliationsResponse> =
        MutableLiveData<GetAffiliationsResponse>()

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

    fun getAffiliationsCall() {
        mldAffiliations.value = dummyAffiliationsResponse
    }

}