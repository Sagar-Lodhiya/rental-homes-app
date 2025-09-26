package com.rentalhomes.ui.common.setting

interface SettingNavigator {
    fun onSettingItemClick(position: Int)
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
    fun onLogoutComplete()
}