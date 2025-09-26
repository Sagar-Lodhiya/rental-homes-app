package com.rentalhomes.ui.common.setting.affiliations

interface AffiliationsNavigator {
    fun onAffiliationsItemClick(position: Int, link: String)
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
}