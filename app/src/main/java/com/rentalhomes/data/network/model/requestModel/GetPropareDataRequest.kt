package com.rentalhomes.data.network.model.requestModel

data class GetPropareDataRequest(
    val userId: Int,
    val propertyId: Int,
    val date: String? = "",
) {

}
