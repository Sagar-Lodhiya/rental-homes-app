package com.rentalhomes.ui.agent.buyerFeedback

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.requestModel.RatingDataRequest
import com.rentalhomes.data.network.model.responseModel.RatingDataResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.GlobalFields

class BuyerFeedbackVM(application: Application) :
    BaseViewModel<BuyerFeedbackNavigator>(application) {
    var ratingData = MutableLiveData<RatingDataResponse.Data>()
    var userId: Int = 0
    var accessToken: String? = ""

    init {
        userId = RentalHomesApp.appSessionManager.getInt(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_USER_ID
        )!!
        accessToken = RentalHomesApp.appSessionManager.getString(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_ACCESS_TOKEN
        )
    }

    //Tier tabs clicks
    fun onFeedbackClicked() {
        navigator.onFeedbackClicked()
    }

    fun onTier1Clicked() {
        navigator.onTier1Clicked()
    }

    fun onTier2Clicked() {
        navigator.onTier2Clicked()
    }

    fun onTier3Clicked() {
        navigator.onTier3Clicked()
    }

    //  Get Rating Data API Call
    fun getRatingData(buyerId: Int, propertyId: Int) {
        val request = RatingDataRequest(buyerId, propertyId)

        val requestCall = APIHandler.apiServices?.getRatingData(
            request,
            "${Keys.BEARER} $accessToken"
        )
        navigator.showProgress()

        requestCall?.let { call ->
            APIHandler().CommonAPI(
                context,
                call,
                rcbRatingData,
                Keys.API_RATING_DATA
            )
        }
    }

    //    Rating Data Response
    private val rcbRatingData: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val rData: RatingDataResponse = `object` as RatingDataResponse
            navigator.hideProgress()
            if (rData.status == GlobalFields.STATUS_SUCCESS) {
                ratingData.value = rData.data
            } else if (rData.status == GlobalFields.STATUS_FAIL || rData.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                rData.message?.let { navigator.setMessageComingFromServer(it) }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }
}