package com.rentalhomes.ui.agent.propertydetail

import android.app.Application
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.requestModel.AddIgnorePropertyRequest
import com.rentalhomes.data.network.model.requestModel.SetPropertyStatus
import com.rentalhomes.data.network.model.responseModel.CommonResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.GlobalFields

class PropertyDetailVM(application: Application) :
    BaseViewModel<PropertyDetailNavigator>(application) {
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

    fun addIgnorePropertyCall(agentId: Int, propertyId: Int, status: Int) {
        val request = AddIgnorePropertyRequest(userId!!, agentId, propertyId, status)

        val requestCall = APIHandler.apiServices?.addIgnoreProperty(
            request,
            "${Keys.BEARER} $accessToken"
        )
        navigator.showProgress()

        requestCall?.let { call ->
            APIHandler().CommonAPI(
                context,
                call,
                rcbAddIgnorePropertyData,
                Keys.API_ADD_IGNORE_PROPERTY
            )
        }
    }

    private val rcbAddIgnorePropertyData: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val addIgnoreProperty: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (addIgnoreProperty.status == GlobalFields.STATUS_SUCCESS) {
//                addIgnoreProperty.message.let { navigator.setMessageComingFromServer(it) }
                navigator.onResponse(addIgnoreProperty.message)
            } else if (addIgnoreProperty.status == GlobalFields.STATUS_FAIL || addIgnoreProperty.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                addIgnoreProperty.message.let { navigator.setMessageComingFromServer(it) }
                navigator.onResponse(addIgnoreProperty.message)
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.onResponse((`object` as String?)!!)
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    fun onRecommendClick() {
        navigator.onRecommendClick()
    }

    fun onBackClick() {
        navigator.onBackClick()
    }

    fun onStatisticsClick() {
        navigator.onStatisticsClick()
    }

    fun onQRImageClick() {
        navigator.onQRImageClick()
    }

    fun setPropertyStatus(propertyId: Int, status: Int) {
        val request = SetPropertyStatus(userId!!, propertyId, status)

        val requestCall = APIHandler.apiServices?.setPropertyStatus(
            request,
            "${Keys.BEARER} $accessToken"
        )
        navigator.showProgress()

        requestCall?.let { call ->
            APIHandler().CommonAPI(
                context,
                call,
                rcbPropertyResponse,
                Keys.API_SET_PROPERTY_STATUS
            )
        }
    }

    private val rcbPropertyResponse: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                navigator.setPropertyResponse(mResponse.message)
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
                navigator.onResponse(mResponse.message)
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.onResponse((`object` as String?)!!)
            navigator.setMessageComingFromServer(`object`!!)
        }
    }
}