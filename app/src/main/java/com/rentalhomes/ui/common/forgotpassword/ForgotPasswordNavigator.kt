package com.rentalhomes.ui.common.forgotpassword

interface ForgotPasswordNavigator {
    fun showProgress()

    fun hideProgress()

    fun isValid(): Boolean

    fun setMessageComingFromServer(it: String)

    fun onResponse()
}