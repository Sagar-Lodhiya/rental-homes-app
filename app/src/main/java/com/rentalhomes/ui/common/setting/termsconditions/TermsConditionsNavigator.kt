package com.rentalhomes.ui.common.setting.termsconditions

interface TermsConditionsNavigator {
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
}