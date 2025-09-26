package com.rentalhomes.ui.agent.recommend

import android.app.Application
import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.Keys.PROSPECT
import com.rentalhomes.data.network.Keys.RECOMMENDED
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.requestModel.GetRecommendAgentRequest
import com.rentalhomes.data.network.model.requestModel.LikeProspectsRequest
import com.rentalhomes.data.network.model.responseModel.CommonResponse
import com.rentalhomes.data.network.model.responseModel.GetRecommendedAgentResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalFields

class RecommendVM(application: Application) :
    BaseViewModel<RecommendNavigator>(application) {
    var mldRecommendList = MutableLiveData<ArrayList<GetRecommendedAgentResponse.Data>>()
    var mldProspectsList = MutableLiveData<ArrayList<GetRecommendedAgentResponse.Data>>()
    var userId: Int? = null
    var userType: Int? = null
    var accessToken: String? = ""
    lateinit var apiType: String

    val dummyRecommendedAgentResponse = GetRecommendedAgentResponse(
        message = "Recommended agents fetched successfully",
        status = 200,
        data = arrayListOf(
            GetRecommendedAgentResponse.Data(
                userId = 101,
                buyerName = "John Smith",
                buyerImage = "https://images.unsplash.com/photo-1603415526960-f7e0328c63b1?w=400", // Unsplash portrait
                mobile = "+1 555 123 4567",
                date = "2025-09-25"
            ),
            GetRecommendedAgentResponse.Data(
                userId = 102,
                buyerName = "Priya Sharma",
                buyerImage = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400",
                mobile = "+91 98765 43210",
                date = "2025-09-23"
            ),
            GetRecommendedAgentResponse.Data(
                userId = 103,
                buyerName = "Carlos Rivera",
                buyerImage = "https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=400",
                mobile = "+52 555 765 4321",
                date = "2025-09-20"
            ),
            GetRecommendedAgentResponse.Data(
                userId = 104,
                buyerName = "Sophia Williams",
                buyerImage = "https://images.unsplash.com/photo-1544725176-7c40e5a2c9f9?w=400",
                mobile = "+44 7700 900123",
                date = "2025-09-18"
            )
        )
    )


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

    fun getRecommendList(propertyId: Int) {
        mldRecommendList.value = dummyRecommendedAgentResponse.data
    }


    fun getProspectsList(propertyId: Int) {
        mldProspectsList.value = dummyRecommendedAgentResponse.data
    }


    fun likeProspectsCall(buyerId: Int, propertyId: Int) {

    }

    fun onRecommendClick() {
        navigator.onRecommendClick()
    }

    fun onProspectsClick() {
        navigator.onProspectsClick()
    }
}