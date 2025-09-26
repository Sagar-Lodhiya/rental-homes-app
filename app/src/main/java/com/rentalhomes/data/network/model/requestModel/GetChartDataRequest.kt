package com.rentalhomes.data.network.model.requestModel

data class GetChartDataRequest(
    val userId: Int,
    val propertyId: Int
)