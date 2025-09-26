package com.rentalhomes.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.GsonBuilder
import com.rentalhomes.BuildConfig
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.Keys.API_ADD_IGNORE_PROPERTY
import com.rentalhomes.data.network.Keys.API_ADD_ITEMS_IN_TEMPLATE
import com.rentalhomes.data.network.Keys.API_CHANGE_EMAIL
import com.rentalhomes.data.network.Keys.API_CHANGE_PASSWORD
import com.rentalhomes.data.network.Keys.API_COMPARE_PROPERTIES
import com.rentalhomes.data.network.Keys.API_COMPARE_WITH_USER
import com.rentalhomes.data.network.Keys.API_CREATE_CUSTOM_TEMPLATE
import com.rentalhomes.data.network.Keys.API_DELETE_PROPERTY
import com.rentalhomes.data.network.Keys.API_FORGET_PASSWORD
import com.rentalhomes.data.network.Keys.API_GET_CUSTOM_TEMPLATE_LIST
import com.rentalhomes.data.network.Keys.API_GET_NON_LISTED
import com.rentalhomes.data.network.Keys.API_GET_PROFILE_DATA
import com.rentalhomes.data.network.Keys.API_GET_PROPARE_DATA
import com.rentalhomes.data.network.Keys.API_GET_PROPERTY_AGENT
import com.rentalhomes.data.network.Keys.API_GET_PROPERTY_BUYER
import com.rentalhomes.data.network.Keys.API_GET_PROPERTY_DETAILS
import com.rentalhomes.data.network.Keys.API_GET_RECOMMEND_AGENT
import com.rentalhomes.data.network.Keys.API_GET_RECOMMEND_BUYER
import com.rentalhomes.data.network.Keys.API_GET_STATISTICS_LIST
import com.rentalhomes.data.network.Keys.API_GET_TIER_1_DETAILS
import com.rentalhomes.data.network.Keys.API_GET_TIER_2
import com.rentalhomes.data.network.Keys.API_GET_TIER_3
import com.rentalhomes.data.network.Keys.API_LIKE_PROSPECTS
import com.rentalhomes.data.network.Keys.API_LINK_UNLINK_USER
import com.rentalhomes.data.network.Keys.API_LINK_USER_LIST
import com.rentalhomes.data.network.Keys.API_LOGIN
import com.rentalhomes.data.network.Keys.API_PROPERTY_ADDED_QR_SCAN
import com.rentalhomes.data.network.Keys.API_RATING_DATA
import com.rentalhomes.data.network.Keys.API_SEND_FEEDBACK
import com.rentalhomes.data.network.Keys.API_SET_FILTER_TYPE
import com.rentalhomes.data.network.Keys.API_SET_PROFILE
import com.rentalhomes.data.network.Keys.API_SET_PROPERTY_STATUS
import com.rentalhomes.data.network.Keys.API_SET_TIER_1_DETAILS
import com.rentalhomes.data.network.Keys.API_SET_TIER_1_RATINGS
import com.rentalhomes.data.network.Keys.API_SET_TIER_2
import com.rentalhomes.data.network.Keys.API_SET_TIER_3
import com.rentalhomes.data.network.Keys.API_SIGNUP
import com.rentalhomes.data.network.Keys.API_TERMS_PRIVACY
import com.rentalhomes.data.network.logginginterceptor.Level
import com.rentalhomes.data.network.logginginterceptor.LoggingInterceptor
import com.rentalhomes.data.network.model.responseModel.*
import com.rentalhomes.ui.common.signin.SignInActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.CommonUtils
import com.rentalhomes.utils.GlobalFields
import com.rentalhomes.utils.GlobalFields.STATUS_DELETE_USER
import com.rentalhomes.utils.GlobalFields.STATUS_INACTIVE_USER
import com.rentalhomes.utils.GlobalFields.STATUS_TOKEN_EXPIRE
import com.rentalhomes.utils.NetworkUtils.isInternetAvailable
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class APIHandler {
    fun <T> CommonAPI(
        context: Context?,
        requestCall: Call<T>,
        callback: ResponseCallback,
        name: String
    ) {
        if (isInternetAvailable(context!!)) {
            try {
                requestCall.enqueue(object : Callback<T?> {
                    override fun onResponse(call: Call<T?>, response: Response<T?>) {
                        try {
                            if (response.body() != null)
                                checkData(name, response, context)
                            if (response.code() == 200 && response.body() != null) {
                                Timber.e("responses%s", response.body().toString())
                                callback.onSuccess(response.body(), name)
                            } else if (response.errorBody() != null) {
                                val jsonObject = JSONObject(response.errorBody()!!.string())
                                val message = jsonObject.getString(Keys.MESSAGE)
                                callback.onFail(message)
                            }
                        } catch (ex: Exception) {
                            Timber.e("Exception%s", ex.localizedMessage)
                            ex.printStackTrace()
                            callback.onFail(ex.localizedMessage)
                        }
                    }

                    @SuppressLint("LogNot Timber")
                    override fun onFailure(call: Call<T?>, t: Throwable) {
                        Timber.e("response_error%s", t.localizedMessage)
                        Log.d("onFailure::", "${t.message}")
                        t.printStackTrace()
                        if (t is SocketTimeoutException || t is UnknownHostException || t is ConnectException) {
                            callback.onFail(GlobalFields.KEY_INTERNET_ALERT_MESSAGE)
                        } else {
                            callback.onFail(t.message)
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            CommonUtils.hideProgressDialog()
            AlertDialogUtils.showInternetAlert(context)
        }
    }

    private fun <T> checkData(strAPI: String, response: Response<T?>, context: Context?) {
        when (strAPI) {
            API_SEND_FEEDBACK, API_CHANGE_EMAIL, API_CHANGE_PASSWORD, API_FORGET_PASSWORD, API_PROPERTY_ADDED_QR_SCAN,
            API_LIKE_PROSPECTS, API_CREATE_CUSTOM_TEMPLATE, API_SET_TIER_3, API_SET_FILTER_TYPE, API_SET_TIER_1_DETAILS,
            API_SET_TIER_1_RATINGS, API_SET_TIER_2, API_ADD_IGNORE_PROPERTY, API_DELETE_PROPERTY, API_LINK_UNLINK_USER,
            API_SET_PROPERTY_STATUS, API_GET_PROPERTY_DETAILS -> {
                val commonResponse = response.body() as CommonResponse
                checkStatus(commonResponse.status, commonResponse.message, context!!)
            }
            API_LOGIN, API_SIGNUP -> {
                val authResponse = response.body() as AuthResponse
                checkStatus(authResponse.status, authResponse.message, context!!)
            }
            API_GET_PROFILE_DATA -> {
                val getProfile = response.body() as GetProfileResponse
                checkStatus(getProfile.status, getProfile.message.toString(), context!!)
            }
            API_SET_PROFILE -> {
                val setProfile = response.body() as AuthResponse
                checkStatus(setProfile.status, setProfile.message, context!!)
            }

            API_TERMS_PRIVACY -> {
                val getTermsPrivacy = response.body() as TermsPrivacyResponse
                checkStatus(getTermsPrivacy.status, getTermsPrivacy.message.toString(), context!!)
            }

            API_GET_NON_LISTED -> {
                val getNonListedProperty = response.body() as GetNonListedResponse
                checkStatus(
                    getNonListedProperty.status,
                    getNonListedProperty.message,
                    context!!
                )
            }

            API_GET_PROPERTY_BUYER -> {
                val getPropertyListBuyer = response.body() as GetPropertyListBuyerResponse
                checkStatus(
                    getPropertyListBuyer.status,
                    getPropertyListBuyer.message,
                    context!!
                )
            }

            API_GET_PROPERTY_AGENT -> {
                val getPropertyListAgent = response.body() as GetAgentPropertyListResponse
                checkStatus(
                    getPropertyListAgent.status,
                    getPropertyListAgent.message,
                    context!!
                )
            }
            API_GET_RECOMMEND_BUYER -> {
                val getRecommendListBuyer = response.body() as GetRecommendListBuyerResponse
                checkStatus(
                    getRecommendListBuyer.status,
                    getRecommendListBuyer.message,
                    context!!
                )
            }
            API_GET_RECOMMEND_AGENT -> {
                val getRecommendedAgent = response.body() as GetRecommendedAgentResponse
                checkStatus(
                    getRecommendedAgent.status,
                    getRecommendedAgent.message,
                    context!!
                )
            }
            API_GET_TIER_3 -> {
                val getTier3Response = response.body() as GetTier3
                checkStatus(getTier3Response.status, getTier3Response.message.toString(), context!!)
            }

            API_GET_CUSTOM_TEMPLATE_LIST -> {
                val getCustomTemplate = response.body() as GetTier3
                checkStatus(
                    getCustomTemplate.status,
                    getCustomTemplate.message.toString(),
                    context!!
                )
            }

            API_ADD_ITEMS_IN_TEMPLATE -> {
                val templateResponse = response.body() as AddItemsInTemplateResponse
                checkStatus(templateResponse.status, templateResponse.message.toString(), context!!)
            }
            API_GET_STATISTICS_LIST -> {
                val statisticsResponse = response.body() as StatisticsResponse
                checkStatus(
                    statisticsResponse.status,
                    statisticsResponse.message.toString(),
                    context!!
                )
            }
            API_GET_PROPARE_DATA -> {
                val mResponse = response.body() as GetPropareResponse
                checkStatus(mResponse.status, mResponse.message.toString(), context!!)
            }
            API_GET_TIER_1_DETAILS -> {
                val mResponse = response.body() as GetTier1Details
                checkStatus(mResponse.status, mResponse.message.toString(), context!!)
            }
            API_GET_TIER_2 -> {
                val mResponse = response.body() as GetTier2
                checkStatus(mResponse.status, mResponse.message.toString(), context!!)
            }

            API_LINK_USER_LIST -> {
                val mResponse = response.body() as LinkUserListResponse
                checkStatus(mResponse.status, mResponse.message.toString(), context!!)
            }

            API_COMPARE_WITH_USER -> {
                val mResponse = response.body() as CompareUserResponse
                checkStatus(mResponse.status, mResponse.message.toString(), context!!)
            }

            API_COMPARE_PROPERTIES -> {
                val mResponse = response.body() as CompareNowResponse
                checkStatus(mResponse.status, mResponse.message.toString(), context!!)
            }
            API_RATING_DATA -> {
                val mResponse = response.body() as RatingDataResponse
                checkStatus(mResponse.status, mResponse.message.toString(), context!!)
            }
        }
    }

    private fun checkStatus(status: Int, strMsg: String, context: Context) {
//        ----------Status Details----------
//         Delete User = 2
//         Inactive User = 3
//         Access Token Expired = 4

        when (status) {
            STATUS_DELETE_USER, STATUS_INACTIVE_USER, STATUS_TOKEN_EXPIRE -> {
                clearDataAndLogout(strMsg, context)
//                AlertDialogUtils.showAlert(context, strMsg) { dialog: DialogInterface, which: Int ->
//                    dialog.dismiss()
//                    clearDataAndLogout(strMsg, context)
//                }
            }
        }
    }

    private fun clearDataAndLogout(msg: String?, context: Context?) {
        RentalHomesApp.appSessionManager.setSession(context!!, false)
        RentalHomesApp.appSessionManager.clearPreference(context)
        val intent = Intent(context.applicationContext, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.applicationContext.startActivity(intent)
    }

    companion object {
        private val HTTP_TIMEOUT = TimeUnit.SECONDS.toMillis(60)

        private var apiInterface: APIInterface? = null
        private val retrofitInstance: Retrofit
            get() {
                val client = OkHttpClient.Builder()
                client.connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                client.writeTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                client.readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                client.addInterceptor(
                    LoggingInterceptor.Builder()
                        .loggable(BuildConfig.DEBUG)
                        .setLevel(Level.BASIC)
                        .log(Log.ERROR)
                        .request("Request")
                        .response("Response")
//                        .addHeader("Version", BuildConfig.VERSION_NAME)
                        .build()
                )
                val okHttpClient = client.build()
                okHttpClient.retryOnConnectionFailure()
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                return Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build()
            }

        val apiServices: APIInterface?
            get() {
                if (apiInterface == null) {
                    apiInterface = retrofitInstance.create(APIInterface::class.java)
                }
                return apiInterface
            }
    }
}