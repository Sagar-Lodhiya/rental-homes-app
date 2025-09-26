package com.rentalhomes.ui.common.signin

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
import com.rentalhomes.data.network.Keys.AGENT
import com.rentalhomes.data.network.Keys.BUYER
import com.rentalhomes.data.network.Keys.DEVICE_TYPE
import com.rentalhomes.data.network.Keys.DEVICE_TYPE_VALUE
import com.rentalhomes.data.network.ResponseCallback
import com.rentalhomes.data.network.model.User
import com.rentalhomes.data.network.model.requestModel.LoginRequest
import com.rentalhomes.data.network.model.responseModel.AuthResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.base.BaseViewModel
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.Constant
import com.rentalhomes.utils.Constant.TOKEN
import com.rentalhomes.utils.Constant.USERS
import com.rentalhomes.utils.GlobalFields

class SignInVM(application: Application) :
    BaseViewModel<SignInNavigator>(application) {

    lateinit var apiType: String
    var deviceToken: String? = ""
    var mldLogin: MutableLiveData<LoginRequest> = MutableLiveData<LoginRequest>()
    var mldLoginResponse: MutableLiveData<AuthResponse> = MutableLiveData<AuthResponse>()

    var mldFirebaseSignIn: MutableLiveData<LoginRequest> = MutableLiveData<LoginRequest>()
    val mAuth: FirebaseAuth
    var firebaseUser: FirebaseUser? = null
    val db: FirebaseFirestore

    companion object {
        private val TAG = SignInVM::class.simpleName
    }

    init {
        mldLogin.value = LoginRequest("", "", DEVICE_TYPE_VALUE, "")
//        deviceToken = RentalHomesApp.appSessionManager.getDeviceToken(context)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    fun loginCall() {
        val request = LoginRequest(
            mldLogin.value?.email.toString(),
            mldLogin.value?.password.toString(),
            DEVICE_TYPE_VALUE,
            RentalHomesApp.appSessionManager.getDeviceToken(context)
        )

        val requestCall = APIHandler.apiServices?.login(request)
        apiType = Keys.API_LOGIN
        navigator.showProgress()
        val apiRequest = APIHandler()
        if (requestCall != null) {
            apiRequest.CommonAPI(context, requestCall, responseCallback, apiType)
        }
    }

    val responseCallback: ResponseCallback = object : ResponseCallback {
        override fun onSuccess(`object`: Any?, name: String?) {
            val mResponse: AuthResponse = `object` as AuthResponse
//            navigator.hideProgress()
            if (mResponse.status == GlobalFields.STATUS_SUCCESS) {
                //Store details in sharedPreferences
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
                    AppSessionManager.PREF_KEY_AGENCY_NAME,
                    mResponse.data.agencyName
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
                    AppSessionManager.PREF_KEY_EMAIL,
                    mResponse.data.email
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
                    AppSessionManager.PREF_KEY_SUBURB,
                    mResponse.data.suburb
                )
                RentalHomesApp.appSessionManager.setString(
                    context,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_PASSWORD,
                    mldLogin.value?.password.toString()
                )

                //Set session
                RentalHomesApp.appSessionManager.setSession(context, true)

//                if (mResponse.data.userType == AGENT) {
//                    navigator.goToAgent()
//                } else if (mResponse.data.userType == BUYER) {
//                    navigator.goToBuyer()
//                }

//                navigator.onSignInCompleted()

            } else if (mResponse.status == GlobalFields.STATUS_FAIL || mResponse.status == GlobalFields.STATUS_ERROR_MESSAGE) {
                navigator.hideProgress()
                mResponse.message.let { navigator.setMessageComingFromServer(it) }
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

    fun doLoginAuthUserWithEmailAndPassword(task: Task<AuthResult>) {
        Log.e("SIGN_IN", "ddd===>login")
        try {
            if (!task.isSuccessful) {
                navigator.hideProgress()
                Toast.makeText(context, context.getString(R.string.auth_failed), Toast.LENGTH_LONG)
                    .show()
            } else {
//                navigator.hideProgress()
                firebaseUser = task.result!!.user
//                Toast.makeText(context, "Auth Successful", Toast.LENGTH_LONG)
//                    .show()
                updateUserData(firebaseUser!!.uid)
            }
        } catch (e: Exception) {
            navigator.hideProgress()
            Log.e("SIGN_IN", "ddd===>" + e.message)
            e.printStackTrace()
        }

    }

    private fun updateUserData(Id: String) {

        try {
            val user = db.collection(USERS).document(Id)

            val userDoc: MutableMap<String, Any> = HashMap()
            userDoc[TOKEN] = RentalHomesApp.appSessionManager.getDeviceToken(context)
            userDoc[DEVICE_TYPE] = DEVICE_TYPE_VALUE.toString()
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
                                    token = userDocument.get(TOKEN) as String,
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
                                navigator.hideProgress()

                                //Navigate to Agent or Buyer
                                if (RentalHomesApp.appSessionManager.getInt(
                                        context,
                                        AppSessionManager.PREF_NAME,
                                        AppSessionManager.PREF_KEY_USER_TYPE
                                    ) == AGENT
                                ) {
                                    navigator.goToAgent()
                                } else if (RentalHomesApp.appSessionManager.getInt(
                                        context,
                                        AppSessionManager.PREF_NAME,
                                        AppSessionManager.PREF_KEY_USER_TYPE
                                    ) == BUYER
                                ) {
                                    navigator.goToBuyer()
                                }
                            }

                        } else {
                            navigator.hideProgress()
                            task.exception?.printStackTrace()
                        }
                    }
                }).addOnFailureListener(OnFailureListener { e ->
                    navigator.hideProgress()
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

}
