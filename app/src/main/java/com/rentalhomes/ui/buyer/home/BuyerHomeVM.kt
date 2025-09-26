package com.rentalhomes.ui.buyer.home

import android.app.Application
import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.requestModel.DeletePropertyRequest
import com.rentalhomes.data.network.model.requestModel.GetPropertyListBuyerRequest
import com.rentalhomes.data.network.model.requestModel.LoginRequest
import com.rentalhomes.data.network.model.responseModel.CommonResponse
import com.rentalhomes.data.network.model.responseModel.GetPropertyListBuyerResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.Constant
import com.rentalhomes.utils.GlobalFields

class BuyerHomeVM(application: Application) :
    BaseViewModel<BuyerHomeNavigator>(application) {

    var userId: Int? = null
    var userType: Int? = null
    var accessToken: String? = ""
    lateinit var apiType: String
    var mldPropertyList = MutableLiveData<ArrayList<GetPropertyListBuyerResponse.Data>>()

    var isLoading = false
    var isMoreItemAvailable = true
    var pageIndex = 0
    var isSwipeLoading = false

    var MLDFirebaseSignIn: MutableLiveData<LoginRequest> = MutableLiveData<LoginRequest>()


    companion object {
        private val TAG = BuyerHomeVM::class.simpleName
    }

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

        MLDFirebaseSignIn.value = LoginRequest("", "", Constant.DEVICE_TYPE_VALUE, "")
    }

    fun getHomePropertyListCall(filterType: Int) {
        val request = GetPropertyListBuyerRequest(
            userId,
            filterType
        )

        isLoading = true
        if (/*pageIndex == 0 && */!isSwipeLoading) {
            navigator.showProgress()
        }

        val requestCall = APIHandler.apiServices?.getPropertyListBuyer(
            request,
            "${Keys.BEARER} ${accessToken.toString()}"
        )
        apiType = Keys.API_GET_PROPERTY_BUYER
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, responseCallback, apiType)
        }
    }

    private val responseCallback: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: GetPropertyListBuyerResponse = `object` as GetPropertyListBuyerResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                if (isSwipeLoading) {
                    isSwipeLoading = false
                    mldPropertyList.value = ArrayList()
                    navigator.hideSwipeLoading()
                }
                mldPropertyList.value = mResponse.data
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                if (isSwipeLoading) {
                    isSwipeLoading = false
                    navigator.hideSwipeLoading()
                }
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
                Log.e("HomeBuyerList : == ", mResponse.message)
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
            Log.e("HomeBuyerList : Fail == ", `object`!!)
        }
    }

    fun deletePropertyCall(propertyId: Int) {
        val request = DeletePropertyRequest(
            userId!!,
            propertyId,
            2
        )

        val requestCall = APIHandler.apiServices?.deleteProperty(
            request,
            "${Keys.BEARER} ${accessToken.toString()}"
        )
        apiType = Keys.API_DELETE_PROPERTY
        navigator.showProgress()
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, rcbDeleteProperty, apiType)
        }
    }

    private val rcbDeleteProperty: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
                navigator.onDeletePropertySuccess()
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
                Log.e("DeleteProperty : == ", mResponse.message.toString())
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
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
            navigator.setMessageComingFromServer((`object` as String?)!!)
            Log.e("DeleteProperty : Fail == ", (`object` as String?)!!)
        }
    }

    fun onNotificationClick() {
        navigator.onNotificationClick()
    }

    fun onFilterClick() {
        navigator.onFilterClick()
    }

    fun onMessageClick() {
        navigator.onMessageClick()
    }

    fun onMapViewClick() {
        navigator.onMapViewClick()
    }

    fun onScanQRClick() {
        navigator.onScanQRClick()
    }

    fun onCompareClick() {
        navigator.onCompareClick()
    }

    fun onRecommendClick() {
        navigator.onRecommendClick()
    }

    fun onNonListed() {
        navigator.onNonListedClick()
    }

    fun onProfileClick() {
        navigator.onProfileClick()
    }

    fun onCompareNowClick() {
        navigator.onCompareNowClick()
    }

    fun onPropertyInfoClick() {
        navigator.onPropertyInfoClick()
    }
}