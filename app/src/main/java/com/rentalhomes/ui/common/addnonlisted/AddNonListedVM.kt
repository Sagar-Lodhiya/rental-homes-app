package com.rentalhomes.ui.common.addnonlisted

import android.app.Application
import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.responseModel.CommonResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalFields
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddNonListedVM(application: Application) : BaseViewModel<AddNonListedNavigator>(application) {
    lateinit var apiType: String
    var accessToken: String? = null
    var userType = MutableLiveData<String>("")
    var userId = MutableLiveData<String>("")

    var mldPropertyImage = MutableLiveData<File>()
    var mldAddress = MutableLiveData<String>("")
    var mldPropertyCity = MutableLiveData<String>("")
    var mldLatitude = MutableLiveData<String>("")
    var mldSuburb = MutableLiveData<String>("")
    var mldLongitude = MutableLiveData<String>("")
    var mldDescription = MutableLiveData<String>("")
    var mldBedCount = MutableLiveData<Int>()
    var mldBathCount = MutableLiveData<Int>()
    var mldCarCount = MutableLiveData<Int>()
    var mldLandSize = MutableLiveData<Int>()
    var mldFilterType = MutableLiveData<Int>()
    var mldPropertyId = MutableLiveData<Int>(0)

    init {
        userId.value = RentalHomesApp.appSessionManager.getInt(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_USER_ID
        ).toString()
        userType.value = RentalHomesApp.appSessionManager.getInt(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_USER_TYPE
        ).toString()
        accessToken = RentalHomesApp.appSessionManager.getString(
            application.applicationContext, AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_ACCESS_TOKEN
        )
    }

    fun onSavePressed() {
        navigator.onSaveClicked()
    }

    fun addNonListedCall(){
        val request = addNonListedRequest()
        val requestCall =
            APIHandler.apiServices?.addNonListedProperty(
                request.build(),
                "${Keys.BEARER} ${accessToken.toString()}"
            )
        apiType = Keys.API_ADD_NON_LISTED
        navigator.showProgress()
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, responseCallback, apiType)
        }
    }

    private val responseCallback: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                navigator.setMessageComingFromServer(mResponse.message)
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                AlertDialogUtils.showAlert(
                    context,
                    mResponse.message
                ) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    fun editNonListedCall(){
        val request = editNonListedRequest()
        val requestCall =
            APIHandler.apiServices?.editNonListed(
                request.build(),
                "${Keys.BEARER} ${accessToken.toString()}"
            )
        apiType = Keys.API_EDIT_NON_LISTED
        navigator.showProgress()
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, rcpEditNonListed, apiType)
        }
    }

    private val rcpEditNonListed: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                navigator.setMessageComingFromServer(mResponse.message)
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                AlertDialogUtils.showAlert(
                    context,
                    mResponse.message
                ) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    private fun addNonListedRequest(): MultipartBody.Builder {
        val request = MultipartBody.Builder().setType(MultipartBody.FORM)

        if (mldPropertyImage.value == null) {
            request.addFormDataPart(Keys.PROPERTY_IMAGE, "")
        } else {
            mldPropertyImage.value?.let { file ->
                if (file.length() > 0) {
                    val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
                    request.addFormDataPart(
                        Keys.PROPERTY_IMAGE,
                        file.name,
                        requestBody
                    )
                }
            }
        }

        request.addFormDataPart(Keys.ADDRESS, mldAddress.value!!.toString().trim())
        request.addFormDataPart(Keys.PROPERTY_CITY_TEXT, mldPropertyCity.value!!.toString().trim())
        request.addFormDataPart(Keys.LATITUDE, "")
        request.addFormDataPart(Keys.LONGITUDE, "")
        request.addFormDataPart(Keys.PROPERTY_CITY_TEXT, mldSuburb.value!!.toString().trim())
        request.addFormDataPart(Keys.USER_TYPE, userType.value!!.toString().trim())
        request.addFormDataPart(Keys.USER_ID, userId.value!!.toString().trim())
        request.addFormDataPart(Keys.BED_COUNT, mldBedCount.value!!.toString().trim())
        request.addFormDataPart(Keys.BATH_COUNT, mldBathCount.value!!.toString().trim())
        request.addFormDataPart(Keys.CAR_COUNT, mldCarCount.value!!.toString().trim())
        request.addFormDataPart(Keys.DESCRIPTION, mldDescription.value!!.toString().trim())
        request.addFormDataPart(Keys.LAND_SIZE, mldLandSize.value!!.toString().trim())
        request.addFormDataPart(Keys.FILTER_TYPE, "1")

        return request
    }

    private fun editNonListedRequest(): MultipartBody.Builder {
        val request = MultipartBody.Builder().setType(MultipartBody.FORM)

        if (mldPropertyImage.value == null) {
            request.addFormDataPart(Keys.PROPERTY_IMAGE, "")
        } else {
            mldPropertyImage.value?.let { file ->
                if (file.length() > 0) {
                    val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
                    request.addFormDataPart(
                        Keys.PROPERTY_IMAGE,
                        file.name,
                        requestBody
                    )
                }
            }
        }

        request.addFormDataPart(Keys.PROPERTY_ID, mldPropertyId.value!!.toString())
        request.addFormDataPart(Keys.ADDRESS, mldAddress.value!!.toString().trim())
        request.addFormDataPart(Keys.PROPERTY_CITY_TEXT, mldPropertyCity.value!!.toString().trim())
        request.addFormDataPart(Keys.LATITUDE, "")
        request.addFormDataPart(Keys.LONGITUDE, "")
        request.addFormDataPart(Keys.PROPERTY_CITY_TEXT, mldSuburb.value!!.toString().trim())
        request.addFormDataPart(Keys.USER_TYPE, userType.value!!.toString().trim())
        request.addFormDataPart(Keys.USER_ID, userId.value!!.toString().trim())
        request.addFormDataPart(Keys.BED_COUNT, mldBedCount.value!!.toString().trim())
        request.addFormDataPart(Keys.BATH_COUNT, mldBathCount.value!!.toString().trim())
        request.addFormDataPart(Keys.CAR_COUNT, mldCarCount.value!!.toString().trim())
        request.addFormDataPart(Keys.DESCRIPTION, mldDescription.value!!.toString().trim())
        request.addFormDataPart(Keys.LAND_SIZE, mldLandSize.value!!.toString().trim())
        request.addFormDataPart(Keys.FILTER_TYPE, "1")

        return request
    }

    fun onGalleryPressed() {
        navigator.onGalleryClicked()
    }

    fun onCameraPressed() {
        navigator.onCameraClicked()
    }

    fun onPropertyPhotoPressed() {
        navigator.onPropertyPhotoClicked()
    }

    fun onMinusBedPressed() {
        navigator.onMinusBedClicked()
    }

    fun onPlusBedPressed() {
        navigator.onPlusBedClicked()
    }

    fun onMinusBathPressed() {
        navigator.onMinusBathClicked()
    }

    fun onPlusBathPressed() {
        navigator.onPlusBathClicked()
    }

    fun onMinusCarPressed() {
        navigator.onMinusCarClicked()
    }

    fun onPlusCarPressed() {
        navigator.onPlusCarClicked()
    }
}