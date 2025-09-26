package com.rentalhomes.ui.buyer.recommend

import com.rentalhomes.data.network.model.responseModel.GetRecommendListBuyerResponse

interface BuyerRecommendNavigator {
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
    fun onItemClick(data: GetRecommendListBuyerResponse.Data)

    fun hideSwipeLoading()
}