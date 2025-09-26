package com.rentalhomes.ui.buyer.home

import com.rentalhomes.data.network.model.responseModel.GetPropertyListBuyerResponse

interface BuyerHomeNavigator {
    fun onNotificationClick()
    fun onFilterClick()
    fun onMessageClick()
    fun onScanQRClick()
    fun onItemClick(data:GetPropertyListBuyerResponse.Data)
    fun onCompareClick()
    fun onRecommendClick()
    fun onNonListedClick()
    fun onProfileClick()
    fun onMapViewClick()
    fun onTickCountZero()
    fun onCompareNowClick()
    fun onPropertyInfoClick()
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
    fun hideSwipeLoading()
    fun onDeletePropertyClick(listItem: GetPropertyListBuyerResponse.Data)
    fun onDeletePropertySuccess()
    fun onEditPropertyClick(listItem: GetPropertyListBuyerResponse.Data)
}