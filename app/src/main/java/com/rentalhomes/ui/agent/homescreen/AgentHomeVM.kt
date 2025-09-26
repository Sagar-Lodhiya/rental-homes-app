package com.rentalhomes.ui.agent.homescreen

import android.app.Application
import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.responseModel.GetAgentPropertyListResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalFields

class AgentHomeVM(application: Application) : BaseViewModel<AgentHomeNavigator>(application) {
    var userId: Int? = null
    var userType: Int? = null
    var accessToken: String? = ""
    lateinit var apiType: String

    var isLoading = false
    var isMoreItemAvailable = true
    var pageIndex = 0
    var isSwipeLoading = false
    val dummyAgentPropertyListResponse = GetAgentPropertyListResponse(
        message = "Agent property list fetched successfully",
        status = 200,
        data = arrayListOf(
            GetAgentPropertyListResponse.Data(
                propertyId = 201,
                propertyImage = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c", // Modern house
                propertyAddress = "456 Green Valley Road",
                propertyCity = "Ahmedabad",
                propertyCreatedDate = "2025-09-15",
                propertyQRImage = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=property201", // QR Code
                description = "Spacious 4BHK independent house with private parking.",
                landSize = 3200,
                bed = 4,
                bath = 3,
                car = 2,
                propertyStatus = "Available",
                latitude = "23.0225",
                longitude = "72.5714",
                listType = 1
            ),
            GetAgentPropertyListResponse.Data(
                propertyId = 202,
                propertyImage = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914", // Apartment building
                propertyAddress = "12 Riverfront Apartments, Block B",
                propertyCity = "Surat",
                propertyCreatedDate = "2025-09-10",
                propertyQRImage = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=property202",
                description = "Modern 2BHK apartment with river view and balcony.",
                landSize = 1100,
                bed = 2,
                bath = 2,
                car = 1,
                propertyStatus = "Sold",
                latitude = "21.1702",
                longitude = "72.8311",
                listType = 1
            ),
            GetAgentPropertyListResponse.Data(
                propertyId = 203,
                propertyImage = "https://images.unsplash.com/photo-1600585154526-990dced4db0d", // Small cozy house
                propertyAddress = "78 Lake View Society",
                propertyCity = "Vadodara",
                propertyCreatedDate = "2025-09-05",
                propertyQRImage = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=property203",
                description = "Affordable 1BHK house, perfect for small family or rental investment.",
                landSize = 850,
                bed = 1,
                bath = 1,
                car = 0,
                propertyStatus = "Available",
                latitude = "22.3072",
                longitude = "73.1812",
                listType = 2
            )
        )
    )


    var mldAgentPropertyList = MutableLiveData<ArrayList<GetAgentPropertyListResponse.Data>>()

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

    fun getAgentHomeList() {
        mldAgentPropertyList.value = dummyAgentPropertyListResponse.data
    }

    private val responseCallback: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: GetAgentPropertyListResponse = `object` as GetAgentPropertyListResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {

                if (isSwipeLoading) {
                    isSwipeLoading = false
                    mldAgentPropertyList.value = ArrayList()
                    navigator.hideSwipeLoading()
                }

                mldAgentPropertyList.value = mResponse.data
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


    fun onListTypeSelected(flag: Int) {
        navigator.onListTypeSelected(flag)
    }

    fun onProfilePressed() {
        navigator.onProfileClicked()
    }

    fun onNotificationPressed() {
        navigator.onNotificationClicked()
    }

    fun onMessageClicked() {
        navigator.onMessageClick()
    }

    fun onAddNonListedPressed() {
        navigator.onAddNonListedClicked()
    }
}