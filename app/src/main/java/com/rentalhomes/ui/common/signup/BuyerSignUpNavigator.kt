package com.rentalhomes.ui.common.signup

interface BuyerSignUpNavigator {
    fun goToHome()
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
    fun createAuthUserWithEmailAndPassword()
    fun signInWithFirebase()
    fun onGetOTPClick()
    fun onGetOTPComplete()
    fun onOtpVerificationComplete()
}