package com.rentalhomes.ui.common.setting.feedback

interface FeedbackNavigator {
    fun isValid(): Boolean
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
    fun onResponse()
}