package com.rentalhomes.ui.common.setting.privacypolicy

interface PrivacyPolicyNavigator {
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
}