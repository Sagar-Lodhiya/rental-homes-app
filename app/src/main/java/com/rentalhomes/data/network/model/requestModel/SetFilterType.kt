package com.rentalhomes.data.network.model.requestModel

data class SetFilterType(
    val userId: Int,
    val buyerId: Int,
    val propertyId: Int,
    val filterType: Int,
){

}
