package com.rentalhomes.ui.agent.recommend

interface ProspectNavigator {
    fun onLikeClick(buyerId: Int, position: Int)

    fun onUserClick(otherUserId: Int)
}