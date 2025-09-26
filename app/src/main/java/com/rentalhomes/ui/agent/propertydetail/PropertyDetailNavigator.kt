package com.rentalhomes.ui.agent.propertydetail

interface PropertyDetailNavigator {
    fun onRecommendClick()

    fun onBackClick()

    fun onStatisticsClick()

    fun onQRImageClick()

    fun showProgress()

    fun hideProgress()

    fun setMessageComingFromServer(message: String?)

    fun onResponse(message: String?)

    fun setPropertyResponse(message: String?)
}