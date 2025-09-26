package com.rentalhomes.ui.agent.homescreen

import com.rentalhomes.data.network.model.responseModel.GetAgentPropertyListResponse

interface AgentHomeNavigator {
    fun onListTypeSelected(flag: Int)
    fun onListedItemClick(data: GetAgentPropertyListResponse.Data)
    fun onProfileClicked()
    fun onNotificationClicked()
    fun onMessageClick()
    fun onAddNonListedClicked()
    fun showProgress()
    fun hideProgress()
    fun hideSwipeLoading()
    fun setMessageComingFromServer(it: String)
}