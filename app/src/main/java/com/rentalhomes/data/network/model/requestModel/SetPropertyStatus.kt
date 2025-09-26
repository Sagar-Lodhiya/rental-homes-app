package com.rentalhomes.data.network.model.requestModel

data class SetPropertyStatus(
    var userId: Int = 0,
    var propertyId: Int = 0,
    var status: Int = 0,
)
