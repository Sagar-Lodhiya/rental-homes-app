package com.rentalhomes.ui.agent.recommend

interface RecommendNavigator {
    fun onRecommendClick()

    fun onProspectsClick()

    fun showProgress()

    fun hideProgress()

    fun setMessageComingFromServer(it: String)

    fun likeSuccess(it: String)

    fun onItemClick(otherUserId: Int)

}