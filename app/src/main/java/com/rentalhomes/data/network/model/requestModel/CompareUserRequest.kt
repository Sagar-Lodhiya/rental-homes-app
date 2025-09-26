package com.rentalhomes.data.network.model.requestModel

class CompareUserRequest(
    val userId:Int,
    val otherUserId:Int,
    val propertyId:Int
) {
}