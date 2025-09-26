package com.rentalhomes.ui.common.signup

interface SignUpNavigator {
    fun isValidPromoCode(): Boolean
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
    fun onVerifiedPromoCode()
    fun goToHome()
    fun signInWithFirebase()
    fun onGetOTPClick()
    fun onGetOTPComplete()
    fun onOtpVerificationComplete()
}