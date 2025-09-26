package com.rentalhomes.ui.common.signup

import android.app.Application
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.R
import com.rentalhomes.data.network.APIHandler
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.User
import com.rentalhomes.data.network.model.requestModel.GetOTPRequest
import com.rentalhomes.data.network.model.requestModel.OtpVerificationRequest
import com.rentalhomes.data.network.model.requestModel.SignUpRequest
import com.rentalhomes.data.network.model.requestModel.VerifyPromoRequest
import com.rentalhomes.data.network.model.responseModel.AuthResponse
import com.rentalhomes.data.network.model.responseModel.CommonResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.Constant
import com.rentalhomes.utils.GlobalFields
import java.text.SimpleDateFormat
import java.util.*

class SignUpVM(application: Application) : BaseViewModel<SignUpNavigator>(application) {

    lateinit var apiType: String
    lateinit var code: String
    var promoCode = MutableLiveData<String>("")
    var userType: Int? = 0
    var deviceToken: String? = null
    var mldSignup: MutableLiveData<SignUpRequest> = MutableLiveData<SignUpRequest>()
    val mAuth: FirebaseAuth
    var firebaseUser: FirebaseUser? = null
    val db: FirebaseFirestore
    var todayDate:String? = null

    companion object {
        private val TAG = SignUpVM::class.simpleName
    }

    init {
        promoCode.value = ""
        mldSignup.value = SignUpRequest(
            "", "", "", "", "", "",
            "", "", 0, Keys.DEVICE_TYPE_VALUE, deviceToken
        )
        deviceToken = "abcd123"
        userType = RentalHomesApp.appSessionManager.getInt(
            application.applicationContext,
            AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_USER_TYPE
        )
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        todayDate = df.format(c)
//        Log.e("TAG", "SignUP: ${todayDate} ")

        //Firebase
//        MLDFirebaseSignup.value = LoginRequest("", "", Constant.DEVICE_TYPE_VALUE, "")
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    fun signUpCall() {
        val request = SignUpRequest(
            mldSignup.value?.firstName.toString(),
            mldSignup.value?.lastName.toString(),
            mldSignup.value?.mobile.toString(),
            mldSignup.value?.email.toString(),
            mldSignup.value?.password.toString(),
            mldSignup.value?.agencyName.toString(),
            mldSignup.value?.promoCode.toString(),
            mldSignup.value?.suburb.toString(),
            userType,
            Keys.DEVICE_TYPE_VALUE,
            RentalHomesApp.appSessionManager.getDeviceToken(context)
        )

        val requestCall = APIHandler.apiServices?.signUp(request)
        apiType = Keys.API_SIGNUP
        navigator.showProgress()
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, signUpResponseCallback, apiType)
        }
    }

    val signUpResponseCallback: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: AuthResponse = `object` as AuthResponse
//            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {

                RentalHomesApp.appSessionManager.setInt(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_USER_TYPE,
                    mResponse.data.userType
                )

                RentalHomesApp.appSessionManager.setInt(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_USER_ID,
                    mResponse.data.userId
                )

                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_ACCESS_TOKEN,
                    mResponse.data.accessToken
                )

                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_PROFILE_IMAGE,
                    mResponse.data.profilePic
                )

                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_PROFILE_THUMB,
                    mResponse.data.profilePicThumb
                )

                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_FIRST_NAME,
                    mResponse.data.firstName
                )

                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_LAST_NAME,
                    mResponse.data.lastName
                )

                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_MOBILE,
                    mResponse.data.mobile
                )

                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_EMAIL,
                    mResponse.data.email
                )

                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_QR_CODE,
                    mResponse.data.qrCode
                )

                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_POST_CODE,
                    mResponse.data.postCode
                )

                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_AGENCY_NAME,
                    mResponse.data.agencyName
                )

                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_PROMO_CODE,
                    mResponse.data.promoCode
                )

                RentalHomesApp.appSessionManager.setSession(context, true) //Set session

//                navigator.goToHome()
                navigator.signInWithFirebase()
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                mResponse.message?.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                AlertDialogUtils.showAlert(
                    context,
                    mResponse.message
                ) { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    /*Get OTP API call*/
    fun getOTPAPICall(email: String, deviceType: Int) {
        val request = GetOTPRequest(email, deviceType)

        val requestCall = APIHandler.apiServices?.sendOTPMail(request)
        apiType = Keys.API_SEND_OTP_MAIL
        navigator.showProgress()
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, rcbGetOTP, apiType)
        }
    }

    /*Get OTP Response*/
    private val rcbGetOTP: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                mResponse.message?.let { navigator.setMessageComingFromServer(it) }
                navigator.onGetOTPComplete()
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                mResponse.message?.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                AlertDialogUtils.showAlert(
                    context,
                    mResponse.message
                ) { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    /*OTP Verification API call*/
    fun otpVerificationAPICall(email: String, deviceType: Int, otp: Int) {
        val request = OtpVerificationRequest(email, deviceType, otp)

        val requestCall = APIHandler.apiServices?.otpVerification(request)
        apiType = Keys.API_OTP_VERIFICATION
        navigator.showProgress()
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, rcbOTPVerification, apiType)
        }
    }

    /*OTP Verification Response*/
    private val rcbOTPVerification: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: CommonResponse = `object` as CommonResponse
            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
//                mResponse.message?.let { navigator.setMessageComingFromServer(it) }
                navigator.onOtpVerificationComplete()
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                mResponse.message?.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                AlertDialogUtils.showAlert(
                    context,
                    mResponse.message
                ) { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    fun verifyPromoCodeCall() {
        val request = VerifyPromoRequest(
            promoCode.value!!,
            todayDate
        )

        val requestCall = APIHandler.apiServices?.verifyPromoCode(request)
        apiType = Keys.API_VERIFY_PROMO_CODE
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
                navigator.onVerifiedPromoCode()
                mResponse.message?.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                mResponse.message?.let { navigator.setMessageComingFromServer(it) }
            } else if (mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                AlertDialogUtils.showAlert(
                    context,
                    mResponse.message
                ) { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
            }
        }

        override fun onFail(`object`: Any?) {
            navigator.hideProgress()
            navigator.setMessageComingFromServer((`object` as String?)!!)
        }
    }

    /*Firebase Authentication*/
    fun doLoginAuthUserWithEmailAndPassword(task: Task<AuthResult>) {
        Log.e(TAG, "ddd===>login")
        try {
            if (!task.isSuccessful) {
                Toast.makeText(context, context.getString(R.string.auth_failed), Toast.LENGTH_LONG)
                    .show()
            } else {
                firebaseUser = task.result!!.user
//                Toast.makeText(context, "Auth Successful", Toast.LENGTH_LONG)
//                    .show()
                updateUserData(firebaseUser!!.uid)
            }
        } catch (e: Exception) {
            navigator.hideProgress()
            Log.e(TAG, "ddd===>" + e.message)
            e.printStackTrace()
        }

    }

    private fun updateUserData(Id: String) {

        try {
            val user = db.collection(Constant.USERS).document(Id)

            val userDoc: MutableMap<String, Any> = HashMap()
            userDoc[Constant.TOKEN] = RentalHomesApp.appSessionManager.getDeviceToken(context)
            userDoc[Keys.DEVICE_TYPE] = Keys.DEVICE_TYPE_VALUE.toString()
            user.update(userDoc)
                .addOnSuccessListener(OnSuccessListener<Void?> {
                    user.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            var userDocument = task.result
                            userDocument?.let {
                                var dbUser: User = User(
                                    deviceType = userDocument.get(Constant.DEVICE_TYPE) as String?,
                                    email = userDocument.get(Constant.EMAIL) as String,
                                    id = userDocument.get(Constant.ID) as String,
                                    name = userDocument.get(Constant.NAME) as String,
                                    token = userDocument.get(Constant.TOKEN) as String,
//                                    currentPassword = mldLogin.value!!.password
                                )
//                                Log.d(TAG, "deviceType" + dbUser.deviceType)
//                                Log.d(TAG, "email" + dbUser.email)
//                                Log.d(TAG, "id" + dbUser.id)
//                                Log.d(TAG, "name" + dbUser.name)
//                                Log.d(TAG, "profilePicture" + dbUser.profilePicture)
//                                Log.d(TAG, "currentPassword" + dbUser.currentPassword)
                                RentalHomesApp.appSessionManager.saveUser(context, dbUser)
                                navigator.hideProgress()
//                                navigator.hideProgress()

                                //Navigate to Agent Home
                                navigator.goToHome()
                            }

                        } else {
                            navigator.hideProgress()
                            task.exception?.printStackTrace()
                        }
                    }
                }).addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "FAILED " + e.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                })

        } catch (e: Exception) {
            navigator.hideProgress()
            e.printStackTrace()
        }

    }

    fun onGetOTPClick() {
        navigator.onGetOTPClick()
    }
}