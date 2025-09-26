package com.rentalhomes.ui.buyer.createtemplate

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.requestModel.AddItemsInTemplateRequest
import com.rentalhomes.data.network.model.requestModel.AddPropertyScanRequest
import com.rentalhomes.data.network.model.requestModel.CreateCustomTemplateRequest
import com.rentalhomes.data.network.model.responseModel.AddItemsInTemplateResponse
import com.rentalhomes.data.network.model.responseModel.CommonResponse
import com.rentalhomes.data.network.model.responseModel.GetTier3
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.GlobalFields

class CreateTemplateVM(application: Application) :
    BaseViewModel<CreateTemplateNavigator>(application) {
    var userId: Int = 0
    var accessToken: String? = ""
    var tier3Data = MutableLiveData<GetTier3>()

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

    fun onSetAsDefaultClicked() {
        navigator.onSetAsDefaultClicked()
    }

    fun addItemsInTemplate(categoryId: Int, templateItems: ArrayList<String>) {
        val request = AddItemsInTemplateRequest(userId, categoryId, templateItems)
        val requestCall = APIHandler.apiServices?.addItemsInTemplate(
            request,
            "${Keys.BEARER} $accessToken"
        )

        requestCall?.let { call ->
            navigator.showProgress()

            APIHandler().CommonAPI(
                context,
                call,
                rcbAddItemsInTemplate,
                Keys.API_ADD_ITEMS_IN_TEMPLATE
            )
        }
    }

    private val rcbAddItemsInTemplate: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val templateResponse: AddItemsInTemplateResponse =
                `object` as AddItemsInTemplateResponse
            navigator.hideProgress()
            if (templateResponse.status == GlobalFields.STATUS_SUCCESS) {
                navigator.addTemplatesToCategory(templateResponse)
                templateResponse.message?.let { navigator.setMessageComingFromServer(it) }
            } else if (templateResponse.status == GlobalFields.STATUS_FAIL || templateResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                templateResponse.message?.let { navigator.setMessageComingFromServer(it) }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    fun getTier3Data(propertyId: Int) {
        val request = AddPropertyScanRequest(propertyId, userId)
        val requestCall = APIHandler.apiServices?.getCustomTemplateList(
            request,
            "${Keys.BEARER} $accessToken"
        )

        requestCall?.let { call ->
            navigator.showProgress()

            APIHandler().CommonAPI(
                context,
                call,
                rcbTier3Data,
                Keys.API_GET_CUSTOM_TEMPLATE_LIST
            )
        }
    }

    private val rcbTier3Data: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val getTier3: GetTier3 = `object` as GetTier3
            if (getTier3.status == GlobalFields.STATUS_SUCCESS) {
                navigator.hideProgress()
                tier3Data.value = getTier3
            } else if (getTier3.status == GlobalFields.STATUS_FAIL || getTier3.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                getTier3.message?.let { navigator.setMessageComingFromServer(it) }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    fun createCustomTemplate(
        propertyId: Int,
        customTempData: ArrayList<CreateCustomTemplateRequest.Data>
    ) {
        val request = CreateCustomTemplateRequest(userId, propertyId, customTempData)
        val requestCall = APIHandler.apiServices?.createCustomTemplates(
            request,
            "${Keys.BEARER} $accessToken"
        )

        requestCall?.let { call ->
            navigator.showProgress()
            APIHandler().CommonAPI(
                context,
                call,
                rcbCreateCustomTemplate,
                Keys.API_CREATE_CUSTOM_TEMPLATE
            )
        }
    }

    private val rcbCreateCustomTemplate: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
                navigator.onCustomTemplateSuccess()
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }
}