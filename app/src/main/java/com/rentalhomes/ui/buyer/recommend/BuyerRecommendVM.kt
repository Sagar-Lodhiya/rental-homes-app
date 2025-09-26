package com.rentalhomes.ui.buyer.recommend

import android.app.Application
import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.requestModel.CommonRequest
import com.rentalhomes.data.network.model.responseModel.GetRecommendListBuyerResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalFields

class BuyerRecommendVM(application: Application) :
    BaseViewModel<BuyerRecommendNavigator>(application) {

    var mldRecommendList = MutableLiveData<ArrayList<GetRecommendListBuyerResponse.Data>>()
    var userId: Int? = null
    var userType: Int? = null
    var accessToken: String? = ""
    lateinit var apiType: String

    var isLoading = false
    var isMoreItemAvailable = true
    var pageIndex = 0
    var isSwipeLoading = false

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

    fun getPropertyListCall() {
        val request = CommonRequest(
            userId,
            userType
        )
        val requestCall = APIHandler.apiServices?.getRecommendListBuyer(
            request,
            "${Keys.BEARER} ${accessToken.toString()}"
        )
        apiType = Keys.API_GET_RECOMMEND_BUYER
        isLoading = true
        if (/*pageIndex == 0 && */!isSwipeLoading) {
            navigator.showProgress()
        }
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, responseCallback, apiType)
        }
    }

    private val responseCallback: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: GetRecommendListBuyerResponse = `object` as GetRecommendListBuyerResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                if (isSwipeLoading) {
                    isSwipeLoading = false
                    mldRecommendList.value = ArrayList()
                    navigator.hideSwipeLoading()
                }

                mldRecommendList.value = mResponse.data
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                if (isSwipeLoading) {
                    isSwipeLoading = false
                    navigator.hideSwipeLoading()
                }
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
                Log.e("RecommendBuyer : == ", mResponse.message.toString())
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                if (isSwipeLoading) {
                    isSwipeLoading = false
                    navigator.hideSwipeLoading()
                }
                AlertDialogUtils.showAlert(
                    context,
                    mResponse.message
                ) { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
            }
            isLoading = false
        }

        override fun onFail(`object`: Any?) {
            isLoading = false
            navigator.hideProgress()
            if (isSwipeLoading) {
                isSwipeLoading = false
                navigator.hideSwipeLoading()
            }
            navigator.setMessageComingFromServer((`object` as String?)!!)
            Log.e("RecommendBuyer : Fail == ", (`object` as String?)!!)
        }
    }
}