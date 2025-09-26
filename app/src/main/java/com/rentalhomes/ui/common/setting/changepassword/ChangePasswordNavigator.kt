package com.rentalhomes.ui.common.setting.changepassword

interface ChangePasswordNavigator {
    fun onClickChangeEmail()
    fun onClickChangePassword()
    fun onEmailClick()
    fun onPasswordClick()
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)

    fun onEmailResponse()
    fun onPasswordResponse()
}