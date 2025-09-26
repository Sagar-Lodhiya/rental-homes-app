package com.rentalhomes.ui.buyer.propertydetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.Keys.API_SET_TIER_2
import com.rentalhomes.data.network.Keys.API_SET_TIER_3
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.requestModel.*
import com.rentalhomes.data.network.model.responseModel.CommonResponse
import com.rentalhomes.data.network.model.responseModel.GetTier1Details
import com.rentalhomes.data.network.model.responseModel.GetTier2
import com.rentalhomes.data.network.model.responseModel.GetTier3
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.GlobalFields

class BuyerPropertyDetailVM(application: Application) :
    BaseViewModel<BuyerPropertyDetailNavigator>(application) {
    var userId: Int? = 0
    var accessToken: String? = ""
    var tier3Data = MutableLiveData<GetTier3>()
    var tier1Details = MutableLiveData<GetTier1Details.Data>()
    var tier2Details = MutableLiveData<GetTier2.Data>()
    var mldTemplateType = MutableLiveData<Int>()
    var propertyId = MutableLiveData<Int>()
    var likeCount = MutableLiveData<Int>()
    var likePercent = MutableLiveData<Int>()
    var disLikeCount = MutableLiveData<Int>()
    var disLikePercent = MutableLiveData<Int>()

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

    //Back button click
    fun onBackPress() {
        navigator.onBackPress()
    }

    //Tier tabs clicks
    fun onPropertyDetailClicked() {
        navigator.onPropertyDetailClicked()
    }

    fun onQuestionClicked() {
        navigator.onQuestionClicked()
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

    fun onCompareClick() {
        navigator.onCompareClick()
    }

    fun onQRClick() {
        navigator.onQRClick()
    }

    //  Get Tier 1 API Call
    fun getTier1Details(propertyId: Int) {
        val request = AddPropertyScanRequest(propertyId = propertyId, userId = userId)

        val requestCall = APIHandler.apiServices?.getTier1Details(
            request,
            "${Keys.BEARER} $accessToken"
        )
        navigator.showProgress()

        requestCall?.let { call ->
            APIHandler().CommonAPI(
                context,
                call,
                rcbTier1Data,
                Keys.API_GET_TIER_1_DETAILS
            )
        }
    }

    // Get Tier 1 Response
    private val rcbTier1Data: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val getTier1Details: GetTier1Details = `object` as GetTier1Details
            navigator.hideProgress()
            if (getTier1Details.status == GlobalFields.STATUS_SUCCESS) {
                tier1Details.value = getTier1Details.data
            } else if (getTier1Details.status == GlobalFields.STATUS_FAIL || getTier1Details.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                getTier1Details.message?.let { navigator.setMessageComingFromServer(it) }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    //  Get Tier 2 API Call
    fun getTier2Details(propertyId: Int) {
        val request = AddPropertyScanRequest(propertyId = propertyId, userId = userId)

        val requestCall = APIHandler.apiServices?.getTier2Details(
            request,
            "${Keys.BEARER} $accessToken"
        )
        navigator.showProgress()

        requestCall?.let { call ->
            APIHandler().CommonAPI(
                context,
                call,
                rcbTier2Data,
                Keys.API_GET_TIER_2
            )
        }
    }

    // Get Tier 2 Response
    private val rcbTier2Data: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val getTier2Details: GetTier2 = `object` as GetTier2
            navigator.hideProgress()
            if (getTier2Details.status == GlobalFields.STATUS_SUCCESS) {
                tier2Details.value = getTier2Details.data
            } else if (getTier2Details.status == GlobalFields.STATUS_FAIL || getTier2Details.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                getTier2Details.message?.let { navigator.setMessageComingFromServer(it) }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    //  Set Tier 2 API Call
    fun setTier2Details(setTier2Request: SetTier2Request) {
        val request = setTier2Request.copy(userId = userId!!)

        val requestCall = APIHandler.apiServices?.setTier2(
            request,
            "${Keys.BEARER} $accessToken"
        )
        navigator.showProgress()

        requestCall?.let { call ->
            APIHandler().CommonAPI(
                context,
                call,
                rcbSetTier2Data,
                API_SET_TIER_2
            )
        }
    }

    //  Set Tier 2 Response
    private val rcbSetTier2Data: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    //    Get Tier 3 Data
    fun getTier3Data(propertyId: Int) {
        val dummyTier3 = GetTier3(
            message = "Tier 3 data fetched successfully",
            status = 200,
            defaultTemplateType = 1,
            isCustomTemplateCreated = 1,
            likeCount = 120,
            likePercent = 75,
            disLikeCount = 40,
            disLikePercent = 25,
            data = arrayListOf(
                GetTier3.Data(
                    categoryId = 101,
                    category = "Amenities",
                    categoryList = arrayListOf(
                        GetTier3.Category(
                            featureId = 1,
                            featureName = "Swimming Pool",
                            like = 1,
                            visible = 1
                        ),
                        GetTier3.Category(
                            featureId = 2,
                            featureName = "Gymnasium",
                            like = 0,
                            visible = 1
                        ),
                        GetTier3.Category(
                            featureId = 3,
                            featureName = "Garden Area",
                            like = 1,
                            visible = 1
                        )
                    ),
                    expanded = true
                ),
                GetTier3.Data(
                    categoryId = 102,
                    category = "Safety Features",
                    categoryList = arrayListOf(
                        GetTier3.Category(
                            featureId = 4,
                            featureName = "CCTV Surveillance",
                            like = 1,
                            visible = 1
                        ),
                        GetTier3.Category(
                            featureId = 5,
                            featureName = "Fire Safety Equipment",
                            like = 1,
                            visible = 1
                        ),
                        GetTier3.Category(
                            featureId = 6,
                            featureName = "24x7 Security Guard",
                            like = 0,
                            visible = 1
                        )
                    ),
                    expanded = false
                ),
                GetTier3.Data(
                    categoryId = 103,
                    category = "Nearby Facilities",
                    categoryList = arrayListOf(
                        GetTier3.Category(
                            featureId = 7,
                            featureName = "School within 1km",
                            like = 1,
                            visible = 1
                        ),
                        GetTier3.Category(
                            featureId = 8,
                            featureName = "Hospital nearby",
                            like = 1,
                            visible = 1
                        ),
                        GetTier3.Category(
                            featureId = 9,
                            featureName = "Shopping Mall",
                            like = 0,
                            visible = 1
                        )
                    ),
                    expanded = true
                )
            )
        )
tier3Data.value = dummyTier3

    }

    //    Tier 3 Response
    private val rcbTier3Data: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val getTier3: GetTier3 = `object` as GetTier3
            navigator.hideProgress()
            if (getTier3.status == GlobalFields.STATUS_SUCCESS) {
                tier3Data.value = getTier3
            } else if (getTier3.status == GlobalFields.STATUS_FAIL || getTier3.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                getTier3.message?.let { navigator.setMessageComingFromServer(it) }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    //    Set Tier 3 Data
    fun setTier3Data(setTier3DataList: ArrayList<SetTier3DataRequest.Data>) {
        val request = SetTier3DataRequest(
            userId!!,
            propertyId.value!!,
            likeCount.value!!,
            likePercent.value!!,
            disLikeCount.value!!,
            disLikePercent.value!!,
            mldTemplateType.value!!,
            setTier3DataList
        )
        val requestCall = APIHandler.apiServices?.setTier3Data(
            request,
            "${Keys.BEARER} $accessToken"
        )

        requestCall?.let { call ->
            navigator.showProgress()

            APIHandler().CommonAPI(
                context,
                call,
                rcbSetTier3Data,
                API_SET_TIER_3
            )
        }
    }


    //    Set tier 3 response
    private val rcbSetTier3Data: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                getTier3Data(propertyId.value!!)
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    //    Set Tier 1 Data
    fun setTier1Details(
        que1: Int,
        que2: Int,
        que3: Int,
        que3Feed: String,
        que4: String,
        myNotes: String
    ) {
        val request = SetTier1Request(
            userId!!,
            propertyId.value!!,
            que1,
            que2,
            que3,
            que3Feed,
            que4,
            myNotes
        )
        val requestCall = APIHandler.apiServices?.setTier1Details(
            request,
            "${Keys.BEARER} $accessToken"
        )

        requestCall?.let { call ->
            navigator.showProgress()

            APIHandler().CommonAPI(
                context,
                call,
                rcbSetTier1Details,
                Keys.API_SET_TIER_1_DETAILS
            )
        }
    }

    //    Set Tier 1 Response
    private val rcbSetTier1Details: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    //    Set Tier 1 Ratings
    fun setTier1Rating(
        location: Int,
        streetAppeal: Int,
        internalLayout: Int,
        externalLayout: Int,
        qualityOfBuilding: Int,
        averageScore: Int,
        marketValue: Int,
        myValuation: Int
    ) {
        val request = SetTier1RatingRequest(
            userId!!,
            propertyId.value!!,
            location,
            streetAppeal,
            internalLayout,
            externalLayout,
            qualityOfBuilding,
            averageScore,
            marketValue,
            myValuation
        )
        val requestCall = APIHandler.apiServices?.setTier1Ratings(
            request,
            "${Keys.BEARER} $accessToken"
        )

        requestCall?.let { call ->
            navigator.showProgress()

            APIHandler().CommonAPI(
                context,
                call,
                rcbSetTier1Ratings,
                Keys.API_SET_TIER_1_RATINGS
            )
        }
    }

    //    Tier 1 Ratings Response
    private val rcbSetTier1Ratings: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

//    Tier-1 tab clicks
//    fun onDetailsClick(){
//        navigator.onDetailsClick()
//    }
//
//    fun onScoringClick(){
//        navigator.onScoringClick()
//    }
}