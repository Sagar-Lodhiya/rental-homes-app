package com.rentalhomes.ui.common.setting.contactus

interface ContactUsNavigator {
    fun isValid(): Boolean

    fun onSettingItemClick(position: Int, contactEmail: String)
}