package com.rentalhomes.data.network.model.requestModel

data class StatisticsListRequest(
    val userId: Int,
    val propertyId: Int,
    val filterType: Int,
){}